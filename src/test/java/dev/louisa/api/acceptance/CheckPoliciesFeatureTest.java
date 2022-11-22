package dev.louisa.api.acceptance;

import dev.louisa.api.company.repository.EmployeeRepository;
import dev.louisa.api.policy.repository.PolicyRepository;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.service.PolicySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.louisa.api.acceptance.PolicyAsserter.*;
import static dev.louisa.api.acceptance.ScenarioBuilder.*;
import static dev.louisa.api.policy.domain.RoomType.*;

public class CheckPoliciesFeatureTest {
    private static final String IBM = "IBM";
    private static final String TESLA = "TESLA";
    private static final String MICROSOFT = "MICROSOFT";
    
    private static final String KEES = "KEES";
    private static final String JAN = "JAN";
    private static final String SASKIA = "SASKIA";
    private static final String IRIS = "IRIS";

    private CompanyApi companyApi;
    private PolicyApi policyApi;

    @BeforeEach
    void setUp() {
        final PolicyRepository policyRepository = new PolicyRepository();
        companyApi = new CompanyApi(new EmployeeRepository());
        policyApi = new PolicyApi(policyRepository, new PolicySelector(companyApi, policyRepository));
    }

    @Test
    void checkOnlyEmployeePolicyScenario() {
        scenario(policyApi, companyApi)
                .addEmployee(SASKIA, TESLA)
                .addEmployeePolicy(SASKIA, SINGLE, DOUBLE);
 
        using(policyApi)
                .assertThat(SASKIA)
                .isAllowedToBook(SINGLE, DOUBLE)
                .isNotAllowedToBook(TRIPLE)
                .assertAll();
    }

    @Test
    void checkEmployeeAndCompanyPolicyScenario() {
        scenario(policyApi, companyApi)
                .addEmployee(IRIS, MICROSOFT)
                .addCompanyPolicy(MICROSOFT, SINGLE)
                .addEmployeePolicy(IRIS, DOUBLE, EXECUTIVE);

        using(policyApi)
                .assertThat(IRIS)
                .isAllowedToBook(DOUBLE, EXECUTIVE)
                .isNotAllowedToBook(SINGLE, QUEEN)
                .assertAll();
    }

    @Test
    void checkOnlyCompanyPolicyScenario() {
        scenario(policyApi, companyApi)
                .addEmployee(JAN, IBM)
                .addCompanyPolicy(IBM, SINGLE, DOUBLE);
        
        using(policyApi)
                .assertThat(JAN)
                .isAllowedToBook(SINGLE, DOUBLE)
                .isNotAllowedToBook(KING)
                .assertAll();
    }
    
    @Test
    void checkNoPoliciesScenario() {
        scenario(policyApi, companyApi)
            .addEmployee(KEES, TESLA);

        using(policyApi)
                .assertThat(KEES)
                .isAllowedToBook(EXECUTIVE)
                .assertAll();
    }
}
