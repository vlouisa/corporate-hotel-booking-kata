package dev.louisa.kata.acceptance;

import dev.louisa.kata.policy.domain.RoomType;
import dev.louisa.kata.company.repository.EmployeeRepository;
import dev.louisa.kata.policy.repository.PolicyRepository;
import dev.louisa.kata.policy.service.PolicyService;
import dev.louisa.kata.company.service.CompanyService;
import dev.louisa.kata.policy.service.PolicySelector;
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
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        final PolicyRepository policyRepository = new PolicyRepository();
        companyService = new CompanyService(new EmployeeRepository());
        
        policyService = new PolicyService(policyRepository, new PolicySelector(companyService, policyRepository));
    }

    @Test
    void checkPolicies() {
        companyService.addEmployee(KEES, TESLA);
        companyService.addEmployee(JAN, IBM);
        companyService.addEmployee(SASKIA, TESLA);
        companyService.addEmployee(IRIS, MICROSOFT);
        
        policyService.setCompanyPolicy(IBM, RoomType.SINGLE, RoomType.DOUBLE);
        policyService.setCompanyPolicy(MICROSOFT, RoomType.SINGLE);
        
        policyService.setEmployeePolicy(SASKIA, RoomType.SINGLE, RoomType.DOUBLE);
        policyService.setEmployeePolicy(IRIS, RoomType.SINGLE, RoomType.DOUBLE);

        assertTrue(policyService.isBookingAllowed(KEES, RoomType.EXECUTIVE));
        assertTrue(policyService.isBookingAllowed(JAN, RoomType.SINGLE));
        assertFalse(policyService.isBookingAllowed(JAN, RoomType.KING));
        assertTrue(policyService.isBookingAllowed(SASKIA, RoomType.SINGLE));
        assertFalse(policyService.isBookingAllowed(SASKIA, RoomType.TRIPLE));
        assertTrue(policyService.isBookingAllowed(IRIS, RoomType.DOUBLE));
        assertFalse(policyService.isBookingAllowed(IRIS, RoomType.QUEEN));
    }
}
