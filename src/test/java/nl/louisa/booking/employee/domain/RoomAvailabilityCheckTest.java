package nl.louisa.booking.employee.domain;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;
import nl.louisa.booking.util.LocalDateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.util.Arrays.asList;
import static nl.louisa.booking.hotel.builder.HotelBuilder.aHotel;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomAvailabilityCheckTest {
    private static final LocalDate FEBRUARY_1ST = LocalDate.of(2020,2,1);
    private static final LocalDate FEBRUARY_3RD = LocalDate.of(2020,2,3);

    @Mock
    private Repository<Hotel> hotelRepository;
    @Mock
    private Repository<Booking> bookingRepository;

    private BookingCheck check;

    @BeforeEach
    void setUp() {
        when(hotelRepository.findBy("PAH")).thenReturn(
                aHotel()
                    .basedOn(new Hotel("PAH", "Palm Hotel"))
                    .saveRoom(SINGLE, 1)
                    .build()
        );

        check = new RoomAvailabilityCheck(hotelRepository, bookingRepository);
    }

    @Test
    void should_allow_booking_when_no_bookings_are_found() {
        when(bookingRepository.findWhere(any())).thenReturn(
                new ArrayList<>()
        );

        assertThatNoException().isThrownBy(() -> check.doCheck(bookingRequest(FEBRUARY_1ST, FEBRUARY_3RD)));
    }

    @ParameterizedTest
    @CsvSource({
            "31-01-2020, 01-02-2020",
            "03-02-2020, 04-02-2020",
    })
    void should_allow_booking(@ConvertWith(LocalDateConverter.class) LocalDate bookingCheckIn, @ConvertWith(LocalDateConverter.class) LocalDate bookingCheckOut) {
        bookingRepositoryReturnsBooking(bookingCheckIn, bookingCheckOut);

        assertThatNoException().isThrownBy(() -> check.doCheck(bookingRequest(FEBRUARY_1ST, FEBRUARY_3RD)));
    }

   @ParameterizedTest
    @CsvSource({
            "31-01-2020, 02-02-2020",
            "31-01-2020, 03-02-2020",
            "31-01-2020, 04-02-2020",
            "01-02-2020, 02-02-2020",
            "01-02-2020, 03-02-2020",
            "01-02-2020, 04-02-2020",
            "02-02-2020, 03-02-2020",
            "02-02-2020, 04-02-2020",
    })
    void should_deny_booking(@ConvertWith(LocalDateConverter.class) LocalDate bookingCheckIn, @ConvertWith(LocalDateConverter.class) LocalDate bookingCheckOut) {
        bookingRepositoryReturnsBooking(bookingCheckIn, bookingCheckOut);

        assertThatThrownBy(() -> check.doCheck(bookingRequest(FEBRUARY_1ST, FEBRUARY_3RD)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Room isn't available in requested period");
    }

    private void bookingRepositoryReturnsBooking(@ConvertWith(LocalDateConverter.class) LocalDate bookingCheckIn, @ConvertWith(LocalDateConverter.class) LocalDate bookingCheckOut) {
        when(bookingRepository.findWhere(any())).thenReturn(
                asList(
                        new Booking("xxx", "AB", "PAH", SINGLE, bookingCheckIn, bookingCheckOut)
                )
        );
    }

    private BookingRequest bookingRequest(LocalDate checkIn, LocalDate checkOut) {
        return new BookingRequest("PP", "PAH", SINGLE, checkIn, checkOut );
    }
}