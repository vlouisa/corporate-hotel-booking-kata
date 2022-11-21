package dev.louisa.kata.service;

import dev.louisa.kata.domain.*;
import dev.louisa.kata.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.louisa.kata.domain.PolicyType.*;
import static dev.louisa.kata.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicySelectorTest {
    @Mock
    private CompanyService companyService;
    @Mock
    private PolicyRepository policyRepository;
    
    private PolicySelector policySelector;

    @BeforeEach
    void setUp() {
        policySelector = new PolicySelector(companyService, policyRepository);
    }

    @Test
    void should_return_no_policy_when_no_company_or_employee_policy_exist() {
        when(companyService.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.empty());
        when(policyRepository.findPolicyBy("JETBRAINS", COMPANY_POLICY)).thenReturn(Optional.empty());
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.empty());
    }

    @Test
    void should_return_employee_policy_when_only_employee_policy_exist() {
        when(companyService.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.of(new EmployeePolicy("MIKE", asList(DOUBLE, SINGLE))));
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.of(new EmployeePolicy("MIKE", asList(DOUBLE, SINGLE))));
    }

    @Test
    void should_return_company_policy_when_only_company_policy_exist() {
        when(companyService.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.empty());
        when(policyRepository.findPolicyBy("JETBRAINS", COMPANY_POLICY)).thenReturn(Optional.of(new CompanyPolicy("JETBRAINS", asList(QUEEN, KING))));
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.of(new CompanyPolicy("JETBRAINS", asList(QUEEN, KING))));
    }
}
