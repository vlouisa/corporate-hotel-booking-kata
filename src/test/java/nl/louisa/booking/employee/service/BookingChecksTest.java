package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.hotel.domain.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class BookingChecksTest {
    private static final LocalDate MARCH_3RD = LocalDate.of(2020, 3, 3);
    private static final LocalDate MARCH_9TH = LocalDate.of(2020, 3, 9);
    private static final BookingRequest BOOKING_REQUEST = new BookingRequest("PP", "PAH", RoomType.SINGLE, MARCH_3RD, MARCH_9TH);

    @Mock
    private BookingCheck firstCheck;
    @Mock
    private BookingCheck secondCheck;
    @Mock
    private BookingCheck thirdCheck;


    private BookingChecks bookingChecks;

    @BeforeEach
    void setUp() {
        bookingChecks = new BookingChecks(firstCheck, secondCheck, thirdCheck);
    }

    @Test
    void should_execute_all_checks_in_correct_order() {
        bookingChecks.execute(BOOKING_REQUEST);

        InOrder inOrder = inOrder(firstCheck, secondCheck, thirdCheck);
        inOrder.verify(firstCheck).doCheck(BOOKING_REQUEST);
        inOrder.verify(secondCheck).doCheck(BOOKING_REQUEST);
        inOrder.verify(thirdCheck).doCheck(BOOKING_REQUEST);
    }

    @Test
    void should_execute_all_checks_until_exception_is_thrown() {
        doThrow(new IllegalStateException()).when(secondCheck).doCheck(BOOKING_REQUEST);

        assertThatThrownBy(() -> bookingChecks.execute(BOOKING_REQUEST))
                .isInstanceOf(IllegalStateException.class);

        InOrder inOrder = inOrder(firstCheck, secondCheck);
        inOrder.verify(firstCheck).doCheck(BOOKING_REQUEST);
        inOrder.verify(secondCheck).doCheck(BOOKING_REQUEST);

        verifyNoInteractions(thirdCheck);
    }

    @Test
    void should_do_nothing_when_no_checks_defined() {
        bookingChecks = new BookingChecks();
        assertThatNoException().isThrownBy(() -> bookingChecks.execute(BOOKING_REQUEST));
    }
}