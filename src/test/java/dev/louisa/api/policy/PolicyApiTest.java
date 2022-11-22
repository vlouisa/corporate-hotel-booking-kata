package dev.louisa.api.policy;

import dev.louisa.api.policy.domain.CompanyPolicy;
import dev.louisa.api.policy.domain.EmployeePolicy;
import dev.louisa.api.policy.repository.PolicyRepository;
import dev.louisa.api.policy.service.PolicySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.louisa.api.policy.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PolicyApiTest {
    private static final CompanyPolicy GOOGLE_COMPANY_POLICY = new CompanyPolicy("GOOGLE", asList(SINGLE, DOUBLE, KING));
    private static final EmployeePolicy EMPLOYEE_POLICY_OF_NICK = new EmployeePolicy("NICK", asList(DOUBLE, KING));
    
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PolicySelector policySelector;
    private PolicyApi policyApi;

    @BeforeEach
    void setUp() {
        policyApi = new PolicyApi(policyRepository, policySelector);
    }

    @Test
    void should_store_a_company_policy() {
        policyApi.setCompanyPolicy("GOOGLE", SINGLE, DOUBLE, KING);
        verify(policyRepository).save(GOOGLE_COMPANY_POLICY);
    }

    @Test
    void should_store_an_employee_policy() {
        policyApi.setEmployeePolicy("NICK", DOUBLE, KING);
        verify(policyRepository).save(EMPLOYEE_POLICY_OF_NICK);
    }
}