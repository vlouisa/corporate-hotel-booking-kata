package dev.louisa.kata.policy.service;

import dev.louisa.kata.policy.domain.CompanyPolicy;
import dev.louisa.kata.policy.domain.EmployeePolicy;
import dev.louisa.kata.policy.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.louisa.kata.policy.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {
    
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private PolicySelector policySelector;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyService = new PolicyService(policyRepository, policySelector);
    }

    @Test
    void should_store_a_company_policy() {
        policyService.setCompanyPolicy("GOOGLE", SINGLE, DOUBLE, KING);
        verify(policyRepository).save(new CompanyPolicy("GOOGLE", asList(SINGLE, DOUBLE, KING)));
    }

    @Test
    void should_store_an_employee_policy() {
        policyService.setEmployeePolicy("NICK", DOUBLE, KING);
        verify(policyRepository).save(new EmployeePolicy("NICK", asList(DOUBLE, KING)));
    }
}