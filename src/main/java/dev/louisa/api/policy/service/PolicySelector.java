package dev.louisa.api.policy.service;

import dev.louisa.api.shared.domain.Employee;
import dev.louisa.api.policy.domain.Policy;
import dev.louisa.api.policy.domain.PolicyType;
import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.exception.PolicyApiException;
import dev.louisa.api.policy.repository.PolicyRepository;

import java.util.Optional;

import static dev.louisa.api.policy.domain.PolicyType.*;

public class PolicySelector {
    private final CompanyApi companyApi;
    private final PolicyRepository policyRepository;

    public PolicySelector(CompanyApi companyApi, PolicyRepository policyRepository){
        this.companyApi = companyApi;
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
            throw new PolicyApiException("Employee does not exist");
        }
    }

    private Optional<Employee> getEmployeeBy(String employeeId) {
        return companyApi.fetchEmployee(employeeId);
    }

    private Optional<Policy> getPolicyBy(String id, PolicyType policyType) {
        return policyRepository.findPolicyBy(id, policyType);
    }
}
