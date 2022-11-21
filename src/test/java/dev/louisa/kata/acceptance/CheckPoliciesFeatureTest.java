package dev.louisa.kata.acceptance;

import dev.louisa.kata.company.repository.EmployeeRepository;
import dev.louisa.kata.policy.repository.PolicyRepository;
import dev.louisa.kata.policy.service.PolicyService;
import dev.louisa.kata.company.service.CompanyService;
import dev.louisa.kata.policy.service.PolicySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.louisa.kata.acceptance.PolicyAsserter.*;
import static dev.louisa.kata.acceptance.ScenarioBuilder.*;
import static dev.louisa.kata.policy.domain.RoomType.*;

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
    void checkOnlyEmployeePolicyScenario() {
        scenario(policyService, companyService)
                .addEmployee(SASKIA, TESLA)
                .addEmployeePolicy(SASKIA, SINGLE, DOUBLE);
 
        using(policyService)
                .assertThat(SASKIA)
                .isAllowedToBook(SINGLE, DOUBLE)
                .isNotAllowedToBook(TRIPLE)
                .assertAll();
    }

    @Test
    void checkEmployeeAndCompanyPolicyScenario() {
        scenario(policyService, companyService)
                .addEmployee(IRIS, MICROSOFT)
                .addCompanyPolicy(MICROSOFT, SINGLE)
                .addEmployeePolicy(IRIS, DOUBLE, EXECUTIVE);

        using(policyService)
                .assertThat(IRIS)
                .isAllowedToBook(DOUBLE, EXECUTIVE)
                .isNotAllowedToBook(SINGLE, QUEEN)
                .assertAll();
    }

    @Test
    void checkOnlyCompanyPolicyScenario() {
        scenario(policyService, companyService)
                .addEmployee(JAN, IBM)
                .addCompanyPolicy(IBM, SINGLE, DOUBLE);
        
        using(policyService)
                .assertThat(JAN)
                .isAllowedToBook(SINGLE, DOUBLE)
                .isNotAllowedToBook(KING)
                .assertAll();
    }
    
    @Test
    void checkNoPoliciesScenario() {
        scenario(policyService, companyService)
            .addEmployee(KEES, TESLA);

        using(policyService)
                .assertThat(KEES)
                .isAllowedToBook(EXECUTIVE)
                .assertAll();
    }
}
