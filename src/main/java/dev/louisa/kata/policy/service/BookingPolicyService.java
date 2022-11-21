package dev.louisa.kata.policy.service;

import dev.louisa.kata.policy.domain.CompanyPolicy;
import dev.louisa.kata.policy.domain.EmployeePolicy;
import dev.louisa.kata.policy.domain.Policy;
import dev.louisa.kata.policy.domain.RoomType;
import dev.louisa.kata.policy.repository.PolicyRepository;

import java.util.Optional;

import static java.util.Arrays.asList;

public class BookingPolicyService {

    private final PolicyRepository policyRepository;
    private final PolicySelector policySelector;
    
    public BookingPolicyService(PolicyRepository policyRepository, PolicySelector policySelector) {
        this.policyRepository = policyRepository;
        this.policySelector = policySelector;
    }
    
    public void setCompanyPolicy(String companyId, RoomType... roomTypes) {
        final Policy policy = new CompanyPolicy(companyId, asList(roomTypes));
        policyRepository.save(policy);
    }

    public void setEmployeePolicy(String employeeId, RoomType... roomTypes) {
        final Policy policy = new EmployeePolicy(employeeId, asList(roomTypes));
        policyRepository.save(policy);
    }

    public boolean isBookingAllowed(String employeeId, RoomType roomType) {
        final Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee(employeeId);
        return policy
                .map(value -> value.getRoomTypes().contains(roomType))
                .orElse(true);
    }
}