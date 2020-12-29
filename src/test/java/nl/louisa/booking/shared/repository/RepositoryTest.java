package nl.louisa.booking.shared.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RepositoryTest {
    private static final User TONY_STARK = new User("TS", "Tony Stark");
    private static final User BRUCE_WAYNE = new User("BW", "Bruce Wayne");
    private static final User BRUCE_BANNER = new User("BB", "Bruce Banner");
    private static final User BILL_WOODRUFF = new User("BW", "Bill Woodruff");
    private static final User NO_USER = new User(null, null);

    private Repository<User> userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void should_create_an_entry_in_the_repository() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        userRepository.create(BRUCE_BANNER);

        assertThat(userRepository.findAll()).containsAll(asList(BRUCE_WAYNE, TONY_STARK, BRUCE_BANNER));
    }


    @Test
    void should_throw_an_exception_when_entity_already_exists_on_create() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        assertThatThrownBy(() -> userRepository.create(BRUCE_WAYNE))
                .isInstanceOf(RepositoryException.class)
                .hasMessage("entity 'User' already exists [id=BW]");
    }

    @Test
    void should_update_an_existing_entity_on_upsert() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        userRepository.upsert(BILL_WOODRUFF);

        assertThat(userRepository.findAll()).containsAll(asList(BILL_WOODRUFF, TONY_STARK));
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void should_create_an_entity_when_does_not_exist_in_repository_on_upsert() {
        initializeRepository(BRUCE_WAYNE);

        userRepository.upsert(TONY_STARK);

        assertThat(userRepository.findAll()).containsAll(asList(BRUCE_WAYNE, TONY_STARK));
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void should_delete_an_entity() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        userRepository.delete(BRUCE_WAYNE);

        assertThat(userRepository.findAll()).containsAll(singletonList(TONY_STARK));
        assertThat(userRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    void should_find_an_entity_by_id() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        assertThat(userRepository.findBy("TS")).isEqualTo(TONY_STARK);
    }

    @Test
    void should_return_null_entity_when_find_by_id_cannot_find_entity() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);
        userRepository.setNullValue(NO_USER);

        assertThat(userRepository.findBy("VL")).isEqualTo(NO_USER);
    }

    private void initializeRepository(User ... users) {
        userRepository = new Repository<>(asList(users));
    }


    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @ToString
    public static class User implements Entity {
        private final String userId;
        private final String userName;

        @Override
        public String getId() {
            return userId;
        }
    }
}