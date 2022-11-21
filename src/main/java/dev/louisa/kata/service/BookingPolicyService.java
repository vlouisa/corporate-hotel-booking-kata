package dev.louisa.kata.service;

import dev.louisa.kata.domain.*;
import dev.louisa.kata.repository.PolicyRepository;

import java.util.Optional;

import static java.util.Arrays.asList;

public class BookingPolicyService {

    private final PolicyRepository policyRepository;
    private final CompanyService companyService;

    public BookingPolicyService(PolicyRepository policyRepository, CompanyService companyService) {
        this.policyRepository = policyRepository;
        this.companyService = companyService;
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
        PolicySelector policySelector = new PolicySelector(companyService, policyRepository);
        final Optional<Policy> policy = policySelector.getApplicablePolicyForEmployee(employeeId);
        return policy
                .map(value -> value.getRoomTypes().contains(roomType))
                .orElse(true);
    }
}