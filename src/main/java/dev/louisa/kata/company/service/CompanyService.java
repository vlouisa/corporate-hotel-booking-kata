package dev.louisa.kata.company.service;

import dev.louisa.kata.shared.domain.Employee;
import dev.louisa.kata.company.exception.CompanyApiException;
import dev.louisa.kata.company.repository.EmployeeRepository;

import java.util.Optional;

public class CompanyService {

    private final EmployeeRepository employeeRepository;

    public CompanyService(EmployeeRepository employeeRepository){
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
