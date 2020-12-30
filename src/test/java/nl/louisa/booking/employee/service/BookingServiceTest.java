package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.Booking;
import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.shared.Identity;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private static final LocalDate MARCH_3RD = LocalDate.of(2020, 3, 3);
    private static final LocalDate MARCH_9TH = LocalDate.of(2020, 3, 9);
    @Mock
    Repository<Booking> bookingRepository;

    @Mock
    private BookingChecks bookingChecks;
    @Mock
    private Identity identity;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingChecks, identity, bookingRepository);
    }

    @Test
    void should_throw_exception_when_check_is_not_ok() {
        doThrow(new IllegalStateException("Check failed")).when(bookingChecks).execute(new BookingRequest("PP", "PAH", SINGLE, MARCH_3RD, MARCH_9TH));

        assertThatThrownBy(() -> bookingService.book("PP", "PAH", SINGLE, MARCH_3RD, MARCH_9TH))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_return_booking_confirmation() {
        when(identity.generate()).thenReturn("5BAE5222-4A9A-11EB-B378-0242AC130002");

        Booking booking = bookingService.book("PP", "PAH", SINGLE, MARCH_3RD, MARCH_9TH);

        assertThat(booking.getBookingId()).isEqualTo("5BAE5222-4A9A-11EB-B378-0242AC130002");
        assertThat(booking.getEmployeeId()).isEqualTo("PP");
        assertThat(booking.getHotelId()).isEqualTo("PAH");
        assertThat(booking.getRoomType()).isEqualTo(SINGLE);
        assertThat(booking.getCheckIn()).isEqualTo(MARCH_3RD);
        assertThat(booking.getCheckOut()).isEqualTo(MARCH_9TH);
    }

    @Test
    void should_create_a_booking_in_the_repository() {
        when(identity.generate()).thenReturn("5BAE5222-4A9A-11EB-B378-0242AC130002");

        Booking booking = bookingService.book("PP", "PAH", SINGLE, MARCH_3RD, MARCH_9TH);

        verify(bookingRepository).create(booking);
    }
}