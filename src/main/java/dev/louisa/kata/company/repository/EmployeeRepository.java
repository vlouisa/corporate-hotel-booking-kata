package dev.louisa.kata.company.repository;

import dev.louisa.kata.company.domain.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    
    public void save(Employee employee) {
        employees.add(employee);
    }

    public Optional<Employee> findByEmployeeId(String employeeId) {
        return employees.stream()
                .filter(e -> e.getEmployeeId().equals(employeeId))
                .findFirst();
    }
}
