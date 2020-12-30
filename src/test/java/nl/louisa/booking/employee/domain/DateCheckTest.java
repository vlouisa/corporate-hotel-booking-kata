package nl.louisa.booking.employee.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class DateCheckTest {
    private static final LocalDate MARCH_3RD = LocalDate.of(2020, 3, 3);
    private static final LocalDate MARCH_4TH = LocalDate.of(2020, 3, 4);
    private BookingRequest bookingRequest;

    private BookingCheck check;

    @BeforeEach
    void setUp() {
        check = new DateCheck();
    }

    @Test
    void should_throw_an_exception_when_check_in_after_check_out() {
        bookingRequest = new BookingRequest("PP", "PAH", SINGLE, MARCH_4TH,  MARCH_3RD);

        assertThatThrownBy(() -> check.doCheck(bookingRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Check-in should be before check-out");
    }

    @Test
    void should_throw_an_exception_when_check_in_on_same_day_as_check_out() {
        bookingRequest = new BookingRequest("PP", "PAH", SINGLE, MARCH_3RD,  MARCH_3RD);

        assertThatThrownBy(() -> check.doCheck(bookingRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Check-in should be before check-out");
    }

    @Test
    void should_not_throw_any_excetion_when_check_in_before_check_out() {
        bookingRequest = new BookingRequest("PP", "PAH", SINGLE, MARCH_3RD,  MARCH_4TH);

        assertThatNoException().isThrownBy(() -> check.doCheck(bookingRequest));
    }

}