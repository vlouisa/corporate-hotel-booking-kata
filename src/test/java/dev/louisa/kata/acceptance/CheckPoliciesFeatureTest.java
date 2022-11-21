package dev.louisa.kata.acceptance;

import dev.louisa.kata.domain.RoomType;
import dev.louisa.kata.repository.EmployeeRepository;
import dev.louisa.kata.repository.PolicyRepository;
import dev.louisa.kata.service.BookingPolicyService;
import dev.louisa.kata.service.CompanyService;
import dev.louisa.kata.service.PolicySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckPoliciesFeatureTest {
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
        final PolicyRepository policyRepository = new PolicyRepository();
        companyService = new CompanyService(new EmployeeRepository());
        
        bookingPolicyService = new BookingPolicyService(policyRepository, new PolicySelector(companyService, policyRepository));
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
