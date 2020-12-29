package nl.louisa.booking.features;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.service.BookingChecks;
import nl.louisa.booking.employee.service.BookingService;
import nl.louisa.booking.company.service.CompanyService;
import nl.louisa.booking.employee.domain.RoomTypeCheck;
import nl.louisa.booking.hotel.service.HotelService;
import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;
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
        final Repository<Employee> employeeRepository = new Repository<>();
        final Repository<Hotel> hotelRepository = new Repository<>();

        final BookingCheck roomTypeCheck = new RoomTypeCheck(hotelRepository);
        final BookingChecks bookingChecks = new BookingChecks(roomTypeCheck);

        companyService = new CompanyService(employeeRepository);
        hotelService = new HotelService(hotelRepository);
        bookingService = new BookingService(bookingChecks);
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
