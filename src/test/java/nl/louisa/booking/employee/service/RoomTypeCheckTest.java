package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.employee.domain.RoomTypeCheck;
import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomTypeCheckTest {
    private static final LocalDate MARCH_3RD = LocalDate.of(2020, 3, 3);
    private static final LocalDate MARCH_9TH = LocalDate.of(2020, 3, 9);
    private static final BookingRequest BOOKING_REQUEST = new BookingRequest("PP", "PAH", SINGLE, MARCH_3RD, MARCH_9TH);

    @Mock
    private Repository<Hotel> hotelRepository;
    private BookingCheck bookingCheck;

    @BeforeEach
    void setUp() {
        bookingCheck = new RoomTypeCheck(hotelRepository);
    }

    @Test
    void should_not_throw_exception_when_room_type_is_provided_by_requested_hotel() {
        Hotel hotel = new Hotel("PAH", "Palm Beach Hotel");
        hotel.saveRoomDetails(SINGLE, 1);
        hotel.saveRoomDetails(DOUBLE, 5);
        when(hotelRepository.findBy("PAH")).thenReturn(hotel);

        assertThatNoException().isThrownBy(() -> bookingCheck.doCheck(BOOKING_REQUEST));
    }

    @Test
    void should_throw_exception_when_room_type_is_not_provided_by_requested_hotel() {
        Hotel hotel = new Hotel("PAH", "Palm Beach Hotel");
        hotel.saveRoomDetails(DOUBLE, 5);
        hotel.saveRoomDetails(EXECUTIVE, 1);

        when(hotelRepository.findBy("PAH")).thenReturn(hotel);


        assertThatThrownBy(() -> bookingCheck.doCheck(BOOKING_REQUEST))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Hotel does not provide requested room");
    }

    @Test
    void should_throw_exception_when_requested_hotel_is_not_found() {
        when(hotelRepository.findBy("PAH")).thenReturn(null);

        assertThatThrownBy(() -> bookingCheck.doCheck(BOOKING_REQUEST))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Hotel does not provide requested room");
    }
}