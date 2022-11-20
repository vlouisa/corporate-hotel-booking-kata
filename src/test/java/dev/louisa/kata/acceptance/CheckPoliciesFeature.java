package dev.louisa.kata.acceptance;

import dev.louisa.kata.domain.RoomType;
import dev.louisa.kata.service.BookingPolicyService;
import dev.louisa.kata.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckPoliciesFeature {
    private static final String IBM = "IBM";
    private static final String TESLA = "TESLA";
    private static final String MICROSOFT = "MICROSOFT";
    
    private static final String KEES = "KEES";
    private static final String JAN = "JAN";
    private static final String SASKIA = "SASKIA";
    private static final String IRIS = "IRIS";

    private CompanyService companyService;
    private BookingPolicyService bookingPolicyService;

    @BeforeEach
    void setUp() {
        bookingPolicyService = new BookingPolicyService();
        companyService = new CompanyService();
    }

    @Test
    void checkPolicies() {
        companyService.addEmployee(KEES, TESLA);
        companyService.addEmployee(JAN, IBM);
        companyService.addEmployee(SASKIA, TESLA);
        companyService.addEmployee(IRIS, MICROSOFT);
        
        bookingPolicyService.setCompanyPolicy(IBM, RoomType.SINGLE, RoomType.DOUBLE);
        bookingPolicyService.setCompanyPolicy(MICROSOFT, RoomType.SINGLE);
        bookingPolicyService.setEmployeePolicy(SASKIA, RoomType.SINGLE, RoomType.DOUBLE);
        bookingPolicyService.setEmployeePolicy(IRIS, RoomType.SINGLE, RoomType.DOUBLE);

        assertTrue(bookingPolicyService.isBookingAllowed(KEES, RoomType.EXECUTIVE));
        assertTrue(bookingPolicyService.isBookingAllowed(JAN, RoomType.SINGLE));
        assertFalse(bookingPolicyService.isBookingAllowed(JAN, RoomType.KING));
        assertTrue(bookingPolicyService.isBookingAllowed(SASKIA, RoomType.SINGLE));
        assertFalse(bookingPolicyService.isBookingAllowed(SASKIA, RoomType.TRIPLE));
        assertTrue(bookingPolicyService.isBookingAllowed(IRIS, RoomType.DOUBLE));
        assertFalse(bookingPolicyService.isBookingAllowed(IRIS, RoomType.QUEEN));
    }
}
