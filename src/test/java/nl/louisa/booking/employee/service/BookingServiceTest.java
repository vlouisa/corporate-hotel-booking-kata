package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.hotel.domain.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private static final LocalDate MARCH_3RD = LocalDate.of(2020, 3, 3);
    private static final LocalDate MARCH_9TH = LocalDate.of(2020, 3, 9);
    @Mock
    BookingChecks bookingChecks;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingChecks);
    }

    @Test
    void should_throw_exception_when_check_is_not_ok() {
        doThrow(new IllegalStateException("Check failed")).when(bookingChecks).execute(new BookingRequest("PP", "PAH", RoomType.SINGLE, MARCH_3RD, MARCH_9TH));

        assertThatThrownBy(() -> bookingService.book("PP", "PAH", RoomType.SINGLE, MARCH_3RD, MARCH_9TH))
                .isInstanceOf(IllegalStateException.class);
    }
}