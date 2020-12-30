package nl.louisa.booking.features;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.company.domain.PolicyCheck;
import nl.louisa.booking.company.service.CompanyService;
import nl.louisa.booking.company.service.PolicyService;
import nl.louisa.booking.employee.domain.Booking;
import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.DateCheck;
import nl.louisa.booking.employee.domain.RoomAvailabilityCheck;
import nl.louisa.booking.employee.domain.RoomTypeCheck;
import nl.louisa.booking.employee.service.BookingChecker;
import nl.louisa.booking.employee.service.BookingService;
import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.hotel.service.HotelService;
import nl.louisa.booking.shared.Identity;
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
import static nl.louisa.booking.hotel.domain.RoomType.SUITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeFeaturesTest {
    private static final LocalDate MAY_29TH = LocalDate.of(2020, 5, 29);
    private static final LocalDate JUNE_1ST = LocalDate.of(2020, 6, 1);
    private static final LocalDate JUNE_2ND = LocalDate.of(2020, 6, 2);
    private static final LocalDate JUNE_3RD = LocalDate.of(2020, 6, 3);
    private static final LocalDate JUNE_4TH = LocalDate.of(2020, 6, 4);
    private static final LocalDate JUNE_6TH = LocalDate.of(2020, 6, 6);

    @Mock
    private Identity identity;


    private PolicyService policyService;
    private CompanyService companyService;
    private HotelService hotelService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        final Repository<Employee> employeeRepository = new Repository<>();
        final Repository<Hotel> hotelRepository = new Repository<>();
        final Repository<Policy> policyRepository = new Repository<>();
        final Repository<Booking> bookingRepository = new Repository<>();

        policyService = new PolicyService(policyRepository, employeeRepository);
        companyService = new CompanyService(employeeRepository);
        hotelService = new HotelService(hotelRepository);

        final BookingCheck dateCheck = new DateCheck();
        final BookingCheck roomTypeCheck = new RoomTypeCheck(hotelRepository);
        final BookingCheck policyCheck = new PolicyCheck(policyService);
        final BookingCheck roomAvailabilityCheck = new RoomAvailabilityCheck(hotelRepository, bookingRepository);
        final BookingChecker bookingChecker = new BookingChecker(dateCheck, roomTypeCheck, policyCheck, roomAvailabilityCheck);

        bookingService = new BookingService(bookingChecker, identity, bookingRepository);
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

    @Test
    void booking_a_hotel_with_wrong_check_in_and_check_out_date() {
        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 5 , DOUBLE);

        companyService.addEmployee("WEN", "BW");

        assertThatThrownBy(() -> bookingService.book("BW", "WAS", DOUBLE, JUNE_6TH, JUNE_1ST))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Check-in should be before check-out");
    }

    @Test
    void booking_a_hotel_when_employee_policy_does_not_allow_booking() {
        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 10 , SINGLE);
        hotelService.setRoom("WAS", 5 , DOUBLE);
        hotelService.setRoom("WAS", 1 , EXECUTIVE);

        companyService.addEmployee("WEN", "BW");
        policyService.setEmployeePolicy("BW", SINGLE, DOUBLE);

        assertThatThrownBy(() -> bookingService.book("BW", "WAS", EXECUTIVE, JUNE_1ST, JUNE_6TH))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Room can't be booked because of policy");
    }

    @Test
    void booking_a_hotel_when_company_policy_does_not_allow_booking() {
        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 10 , SINGLE);
        hotelService.setRoom("WAS", 5 , DOUBLE);
        hotelService.setRoom("WAS", 1 , EXECUTIVE);

        companyService.addEmployee("WEN", "BW");
        policyService.setCompanyPolicy("WEN", SINGLE, DOUBLE);

        assertThatThrownBy(() -> bookingService.book("BW", "WAS", EXECUTIVE, JUNE_1ST, JUNE_6TH))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Room can't be booked because of policy");
    }

    @Test
    void booking_a_hotel_when_no_room_available_during_period() {
        when(identity.generate()).thenReturn(
                "EA624220-4A96-11EB-B378-0242AC130002",
                "87E05630-4AA2-11EB-B378-0242AC130002"
        );

        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 2 , SUITE);

        companyService.addEmployee("WEN", "TS");
        companyService.addEmployee("WEN", "AO");

        bookingService.book("TS", "WAS", SUITE, MAY_29TH, JUNE_3RD);
        bookingService.book("AO", "WAS", SUITE, JUNE_2ND, JUNE_4TH);

        companyService.addEmployee("WEN", "BW");

        assertThatThrownBy(() -> bookingService.book("BW", "WAS", SUITE, JUNE_1ST, JUNE_6TH))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking cancelled: Room isn't available in requested period");
    }

    @Test
    void booking_a_hotel_room_successfully() {
        when(identity.generate()).thenReturn("EA624220-4A96-11EB-B378-0242AC130002");

        hotelService.addHotel("WAS", "Waldorf Astoria");
        hotelService.setRoom("WAS", 2 , SUITE);

        companyService.addEmployee("WEN", "BW");

        final Booking booking = bookingService.book("BW", "WAS", SUITE, JUNE_1ST, JUNE_6TH);

        assertThat(booking.getBookingId()).isEqualTo("EA624220-4A96-11EB-B378-0242AC130002");
        assertThat(booking.getEmployeeId()).isEqualTo("BW");
        assertThat(booking.getHotelId()).isEqualTo("WAS");
        assertThat(booking.getRoomType()).isEqualTo(SUITE);
        assertThat(booking.getCheckIn()).isEqualTo(JUNE_1ST);
        assertThat(booking.getCheckOut()).isEqualTo(JUNE_6TH);
    }
}
