package dev.louisa.api.policy.service;

import dev.louisa.api.policy.domain.CompanyPolicy;
import dev.louisa.api.shared.domain.Employee;
import dev.louisa.api.policy.domain.EmployeePolicy;
import dev.louisa.api.policy.domain.Policy;
import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.exception.PolicyApiException;
import dev.louisa.api.policy.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.louisa.api.policy.domain.PolicyType.*;
import static dev.louisa.api.policy.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicySelectorTest {
    @Mock
    private CompanyApi companyApi;
    @Mock
    private PolicyRepository policyRepository;
    
    private PolicySelector policySelector;

    @BeforeEach
    void setUp() {
        policySelector = new PolicySelector(companyApi, policyRepository);
    }

    @Test
    void should_return_no_policy_when_no_company_or_employee_policy_exist() {
        when(companyApi.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.empty());
        when(policyRepository.findPolicyBy("JETBRAINS", COMPANY_POLICY)).thenReturn(Optional.empty());
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.empty());
    }

    @Test
    void should_return_employee_policy_when_only_employee_policy_exist() {
        when(companyApi.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.of(new EmployeePolicy("MIKE", asList(DOUBLE, SINGLE))));
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.of(new EmployeePolicy("MIKE", asList(DOUBLE, SINGLE))));
    }

    @Test
    void should_return_company_policy_when_only_company_policy_exist() {
        when(companyApi.fetchEmployee("MIKE")).thenReturn(Optional.of(new Employee("MIKE", "JETBRAINS")));
        when(policyRepository.findPolicyBy("MIKE", EMPLOYEE_POLICY)).thenReturn(Optional.empty());
        when(policyRepository.findPolicyBy("JETBRAINS", COMPANY_POLICY)).thenReturn(Optional.of(new CompanyPolicy("JETBRAINS", asList(QUEEN, KING))));
        
        Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee("MIKE");
        
        assertThat(policy).isEqualTo(Optional.of(new CompanyPolicy("JETBRAINS", asList(QUEEN, KING))));
    }
    
    @Test
    void should_throw_exception_when_employee_not_found() {
        when(companyApi.fetchEmployee("MIKE")).thenReturn(Optional.empty());
        
        
        assertThatThrownBy(() -> policySelector.getApplicablePolicyForEmployee("MIKE"))
                .isInstanceOf(PolicyApiException.class)
                .hasMessage("Employee does not exist");
        
    }
}
