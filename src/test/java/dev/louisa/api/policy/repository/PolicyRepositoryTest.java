package dev.louisa.api.policy.repository;

import dev.louisa.api.policy.domain.CompanyPolicy;
import dev.louisa.api.policy.domain.EmployeePolicy;
import dev.louisa.api.policy.domain.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static dev.louisa.api.policy.domain.PolicyType.*;
import static dev.louisa.api.policy.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class PolicyRepositoryTest {
    private static final CompanyPolicy RET_COMPANY_POLICY = new CompanyPolicy("RET", asList(SINGLE, DOUBLE, TRIPLE));
    private static final CompanyPolicy KLM_COMPANY_POLICY = new CompanyPolicy("KLM", asList(SINGLE, DOUBLE, QUEEN));
    private static final CompanyPolicy ANOTHER_KLM_COMPANY_POLICY = new CompanyPolicy("KLM", asList(SINGLE, DOUBLE));
    
    private static final EmployeePolicy EMPLOYEE_POLICY_OF_JOOST = new EmployeePolicy("JOOST", asList(SINGLE, DOUBLE));
    private static final EmployeePolicy EMPLOYEE_POLICY_OF_JAN = new EmployeePolicy("JAN", asList(SINGLE, DOUBLE, QUEEN));
   
    private PolicyRepository policyRepository;

    @BeforeEach
    void setUp() {
        policyRepository = new PolicyRepository();
    }

    @Test
    void should_save_a_new_company_policy() {
        policyRepository.save(KLM_COMPANY_POLICY);

        final Optional<Policy> policy = policyRepository.findPolicyBy("KLM", COMPANY_POLICY);
        assertThat(policy).isEqualTo(Optional.of(KLM_COMPANY_POLICY));
    }

    @Test
    void should_save_a_new_employee_policy() {
        policyRepository.save(EMPLOYEE_POLICY_OF_JOOST);

        final Optional<Policy> policy = policyRepository.findPolicyBy("JOOST", EMPLOYEE_POLICY);
        assertThat(policy).isEqualTo(Optional.of(EMPLOYEE_POLICY_OF_JOOST));
    }

    @Test
    void should_update_an_existing_policy() {
        policyRepository.save(KLM_COMPANY_POLICY);
        policyRepository.save(ANOTHER_KLM_COMPANY_POLICY);

        final List<Policy> policies = policyRepository.findAllBy(COMPANY_POLICY);
        assertThat(policies).containsOnly(ANOTHER_KLM_COMPANY_POLICY);
    }

    @Test
    void should_find_all_policies_of_a_certain_type() {
        policyRepository.save(EMPLOYEE_POLICY_OF_JOOST);
        policyRepository.save(EMPLOYEE_POLICY_OF_JAN);
        policyRepository.save(RET_COMPANY_POLICY);
        
        final List<Policy> policies = policyRepository.findAllBy(EMPLOYEE_POLICY);
        
        assertThat(policies).contains(
                EMPLOYEE_POLICY_OF_JOOST,
                EMPLOYEE_POLICY_OF_JAN
        );
    }
}