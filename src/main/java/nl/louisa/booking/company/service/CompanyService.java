package nl.louisa.booking.company.service;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.shared.repository.Repository;

public class CompanyService {
    private final Repository<Employee> employeeRepository;
    private final Repository<Policy> policyRepository;

    public CompanyService(Repository<Employee> employeeRepository, Repository<Policy> policyRepository) {
        this.employeeRepository = employeeRepository;
        this.policyRepository = policyRepository;
    }

    public void addEmployee(String companyId, String employeeId) {
        validatePrimaryKey(employeeId);
        employeeRepository.create(new Employee(employeeId, companyId));
    }

    private void validatePrimaryKey(String employeeId) {
        if(employeeRepository.findBy(employeeId) != null){
            throw new IllegalStateException("Technical error while creating an employee");
        }
    }

    public void deleteEmployee(String employeeId) {
        final Employee employee = employeeRepository.findBy(employeeId);
        employeeRepository.delete(employee);

        final Policy policy = policyRepository.findBy("EP-" + employeeId);
        policyRepository.delete(policy);
    }
}
