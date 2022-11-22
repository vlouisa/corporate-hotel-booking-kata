package dev.louisa.api.policy.repository;

import dev.louisa.api.policy.domain.Policy;
import dev.louisa.api.policy.domain.PolicyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PolicyRepository {
    private final List<Policy> policies = new ArrayList<>();
    
    public void save(Policy policy) {
        findPolicyBy(policy.getId(), policy.hasPolicyType())
                .ifPresent(policies::remove);
        
        policies.add(policy);
    }

    public Optional<Policy> findPolicyBy(String id, PolicyType policyType) {
        return policies.stream()
                .filter(p -> policyType.equals(p.hasPolicyType()))
                .filter(p -> id.equals(p.getId()))
                .findFirst();
    }

    public List<Policy> findAllBy(PolicyType policyType) {
        return policies.stream()
                .filter(p -> policyType.equals(p.hasPolicyType()))
                .collect(Collectors.toList());
    }
}
