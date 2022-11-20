package dev.louisa.kata.service;

import dev.louisa.kata.domain.Employee;
import dev.louisa.kata.exception.CompanyApiException;
import dev.louisa.kata.repository.EmployeeRepository;

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
}
