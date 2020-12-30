package nl.louisa.booking.shared.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
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

        assertThat(userRepository.findAll()).contains(BRUCE_WAYNE, TONY_STARK, BRUCE_BANNER);
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

        assertThat(userRepository.findAll()).contains(BILL_WOODRUFF, TONY_STARK);
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void should_create_an_entity_when_does_not_exist_in_repository_on_upsert() {
        initializeRepository(BRUCE_WAYNE);

        userRepository.upsert(TONY_STARK);

        assertThat(userRepository.findAll()).contains(BRUCE_WAYNE, TONY_STARK);
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void should_delete_an_entity() {
        initializeRepository(BRUCE_WAYNE, TONY_STARK);

        userRepository.delete(BRUCE_WAYNE);

        assertThat(userRepository.findAll()).contains(TONY_STARK);
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

    @Test
    void should_return_entities_based_on_criteria() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        final List<User> users = userRepository.findWhere(user -> user.getUserName().startsWith("Bruce"));

        assertThat(users).contains(BRUCE_BANNER, BRUCE_WAYNE);
        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    void should_return_all_entities_given_criteria_is_null() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        final List<User> users = userRepository.findWhere(null);

        assertThat(users).contains(BRUCE_BANNER, BRUCE_WAYNE, TONY_STARK);
        assertThat(users.size()).isEqualTo(3);

    }

    @Test
    void should_return_all_entities_given_single_criteria_is_null() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        final List<User> users = userRepository.findWhere((Predicate<User>) null);

        assertThat(users).contains(BRUCE_BANNER, BRUCE_WAYNE, TONY_STARK);
        assertThat(users.size()).isEqualTo(3);

    }

    @Test
    void should_return_all_entities_given_multiple_null_criteria() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        final List<User> users = userRepository.findWhere(null, null);

        assertThat(users).contains(BRUCE_BANNER, BRUCE_WAYNE, TONY_STARK);
        assertThat(users.size()).isEqualTo(3);

    }

    @Test
    void should_return_entities_based_on_multiple_criteria() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        final List<User> users = userRepository.findWhere(
                user -> user.getUserName().startsWith("Bruce"),
                user -> user.getUserName().contains("Wayne")
        );

        assertThat(users).contains(BRUCE_WAYNE);
        assertThat(users.size()).isEqualTo(1);

    }

    @Test
    void should_count_entities_by_a_given_criteria() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        assertThat(userRepository.countWhere(
                user -> user.getUserName().startsWith("Bruce")
        )).isEqualTo(2);
    }

    @Test
    void should_count_entities_by_multiple_given_criteria() {
        initializeRepository(BRUCE_WAYNE, BRUCE_BANNER, TONY_STARK);

        assertThat(userRepository.countWhere(
                user -> user.getUserName().startsWith("Bruce"),
                user -> user.getUserName().contains("Wayne")
        )).isEqualTo(1);
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