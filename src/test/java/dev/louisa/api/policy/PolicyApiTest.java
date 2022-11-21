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
        verify(policyRepository).save(new CompanyPolicy("GOOGLE", asList(SINGLE, DOUBLE, KING)));
    }

    @Test
    void should_store_an_employee_policy() {
        policyApi.setEmployeePolicy("NICK", DOUBLE, KING);
        verify(policyRepository).save(new EmployeePolicy("NICK", asList(DOUBLE, KING)));
    }
}