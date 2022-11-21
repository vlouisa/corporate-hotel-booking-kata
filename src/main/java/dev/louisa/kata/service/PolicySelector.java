package dev.louisa.kata.service;

import dev.louisa.kata.domain.Employee;
import dev.louisa.kata.domain.Policy;
import dev.louisa.kata.domain.PolicyType;
import dev.louisa.kata.exception.CompanyApiException;
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
        final Optional<Employee> employee = getEmployeeBy(employeeId);
        validate(employee);

        final Optional<Policy> employeePolicy = getPolicyBy(employee.get().getEmployeeId(), EMPLOYEE_POLICY);
        if (employeePolicy.isPresent()) {
            return employeePolicy;
        }

        return getPolicyBy(employee.get().getCompanyId(), COMPANY_POLICY);
    }

    private void validate(Optional<Employee> employee) {
        if (employee.isEmpty()) {
            throw new CompanyApiException("Employee does not exist");
        }
    }

    private Optional<Employee> getEmployeeBy(String employeeId) {
        return companyService.fetchEmployee(employeeId);
    }

    private Optional<Policy> getPolicyBy(String id, PolicyType policyType) {
        return policyRepository.findPolicyBy(id, policyType);
    }
}
