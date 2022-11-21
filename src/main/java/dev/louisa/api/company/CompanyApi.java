package dev.louisa.api.company;

import dev.louisa.api.shared.domain.Employee;
import dev.louisa.api.company.exception.CompanyApiException;
import dev.louisa.api.company.repository.EmployeeRepository;

import java.util.Optional;

public class CompanyApi {

    private final EmployeeRepository employeeRepository;

    public CompanyApi(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }
    
    public void addEmployee(String emplopyeeId, String companyId) {
        validate(emplopyeeId);
        final Employee employee = new Employee(emplopyeeId, companyId);
        employeeRepository.save(employee);
    }

    private void validate(String emplopyeeId) {
        if (employeeRepository.findByEmployeeId(emplopyeeId).isPresent()) {
            throw new CompanyApiException("Employee already exists");
        }
    }

    public Optional<Employee> fetchEmployee(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }
}
