package nl.louisa.booking.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityTest {
    private static final String REGEX_PATTERN = "[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}";
    private Identity identity;

    @BeforeEach
    void setUp() {
        identity = new Identity();
    }

    @Test
    void should_format_uuid() {
        final String id = identity.generate();
        assertThat(id).matches(REGEX_PATTERN);
    }
}