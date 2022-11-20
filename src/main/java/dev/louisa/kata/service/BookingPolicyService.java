package dev.louisa.kata.service;

import dev.louisa.kata.domain.*;
import dev.louisa.kata.repository.PolicyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.louisa.kata.domain.PolicyType.*;
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

        final List<Policy> policies = new ArrayList<>();

        if (policyRepository.findPolicyBy(employeeId, EMPLOYEE_POLICY).isPresent()) {
            policies.add(policyRepository.findPolicyBy(employeeId, EMPLOYEE_POLICY).get());
        }

        final Optional<Employee> employee = companyService.fetchEmployee(employeeId);
        if(employee.isPresent()) {
            if (policyRepository.findPolicyBy(employee.get().getCompanyId(), COMPANY_POLICY).isPresent()) {
                policies.add(policyRepository.findPolicyBy(employee.get().getCompanyId(), COMPANY_POLICY).get());
            }
        }
        
        if (policies.isEmpty()) {
            return true;
        }
        
        final Policy effectivePolicy = policies.get(0);
        return effectivePolicy.getRoomTypes().contains(roomType);
    }
}