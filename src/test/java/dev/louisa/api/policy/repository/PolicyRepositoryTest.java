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
    
    private PolicyRepository policyRepository;

    @BeforeEach
    void setUp() {
        policyRepository = new PolicyRepository();
    }

    @Test
    void should_save_a_new_company_policy() {
        
        policyRepository.save(new CompanyPolicy("KLM", asList(SINGLE, DOUBLE, QUEEN)));

        final Optional<Policy> policy = policyRepository.findPolicyBy("KLM", COMPANY_POLICY);
        assertThat(policy).isEqualTo(Optional.of(new CompanyPolicy("KLM", asList(SINGLE, DOUBLE, QUEEN))));
    }

    @Test
    void should_save_a_new_employee_policy() {
        
        policyRepository.save(new EmployeePolicy("JOOST", asList(SINGLE, DOUBLE)));

        final Optional<Policy> policy = policyRepository.findPolicyBy("JOOST", EMPLOYEE_POLICY);
        assertThat(policy).isEqualTo(Optional.of(new EmployeePolicy("JOOST", asList(SINGLE, DOUBLE))));
    }

    @Test
    void should_update_an_existing_policy() {

        policyRepository.save(new CompanyPolicy("KLM", asList(SINGLE, DOUBLE, QUEEN)));
        policyRepository.save(new CompanyPolicy("KLM", asList(SINGLE, DOUBLE)));

        final List<Policy> policies = policyRepository.findAllBy(COMPANY_POLICY);
        assertThat(policies).containsOnly(new CompanyPolicy("KLM", asList(SINGLE, DOUBLE)));
    }

    @Test
    void should_find_all_policies_of_a_certain_type() {
        policyRepository.save(new EmployeePolicy("JOOST", asList(SINGLE, DOUBLE)));
        policyRepository.save(new EmployeePolicy("JAN", asList(SINGLE, DOUBLE, QUEEN)));
        policyRepository.save(new CompanyPolicy("RET", asList(SINGLE, DOUBLE, TRIPLE)));
        
        final List<Policy> policies = policyRepository.findAllBy(EMPLOYEE_POLICY);
        
        assertThat(policies).contains(
                new EmployeePolicy("JOOST", asList(SINGLE, DOUBLE)),
                new EmployeePolicy("JAN", asList(SINGLE, DOUBLE, QUEEN))
        );
    }
}