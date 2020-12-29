package nl.louisa.booking.company.service;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.shared.repository.Repository;

public class CompanyService {
    private final Repository<Employee> employeeRepository;

    public CompanyService(Repository<Employee> employeeRepository) {
        this.employeeRepository = employeeRepository;
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
}
