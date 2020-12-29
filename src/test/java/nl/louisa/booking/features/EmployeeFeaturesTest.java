package nl.louisa.booking.features;

import nl.louisa.booking.employee.service.BookingService;
import nl.louisa.booking.hotel.service.CompanyService;
import nl.louisa.booking.hotel.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SUITE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmployeeFeaturesTest {

    private static final LocalDate JUNE_1ST = LocalDate.of(2020, 6, 1);
    private static final LocalDate JUNE_6TH = LocalDate.of(2020, 6, 6);
    private CompanyService companyService;
    private HotelService hotelService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService();
        hotelService = new HotelService();
        bookingService = new BookingService();
    }

    @Test
    void booking_a_room_type_not_provided_by_hotel() {
        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 5 , EXECUTIVE);
        hotelService.setRoom("WAS", 4 , SUITE);

        companyService.addEmployee("WEN", "BW");

        assertThatThrownBy(() -> bookingService.book("BW", "WAS", DOUBLE, JUNE_1ST, JUNE_6TH))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Hotel does not provide requested room");
    }
}
