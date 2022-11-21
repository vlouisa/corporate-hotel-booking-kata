package dev.louisa.kata.service;

import dev.louisa.kata.domain.Employee;
import dev.louisa.kata.domain.Policy;
import dev.louisa.kata.repository.PolicyRepository;

import java.util.Optional;

import static dev.louisa.kata.domain.PolicyType.*;

public class PolicySelector {
    private final CompanyService companyService;
    private final PolicyRepository policyRepository;

    public PolicySelector(CompanyService companyService, PolicyRepository policyRepository){
        this.companyService = companyService;
        this.policyRepository = policyRepository;
    }
    
    public Optional<Policy> getApplicablePolicyForEmployee(String employeeId) {
        final Optional<Employee> employee = companyService.fetchEmployee(employeeId);

        final Optional<Policy> employeePolicy = policyRepository.findPolicyBy(employee.get().getEmployeeId(), EMPLOYEE_POLICY);
        if (employeePolicy.isPresent()) {
            return employeePolicy;
        }

        return policyRepository.findPolicyBy(employee.get().getCompanyId(), COMPANY_POLICY);
    }
}
