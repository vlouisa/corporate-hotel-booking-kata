package dev.louisa.kata.company.repository;

import dev.louisa.kata.company.domain.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest {
    private static final Optional<Employee> EMPLOYEE_BRUCE = Optional.of(new Employee("BRUCE", "HUDSON"));
    private static final Optional<Object> NO_EMPLOYEE = Optional.empty();
    
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository = new EmployeeRepository();
    }

    @Test
    void should_save_a_new_employee_in_repository() {
        employeeRepository.save(new Employee("BRUCE", "HUDSON"));
        
        Optional<Employee> employee = employeeRepository.findByEmployeeId("BRUCE");
        
        assertThat(employee).isEqualTo(EMPLOYEE_BRUCE);
    }
    
    @Test
    void should_return_empty_optional_for_non_existing_user() {
        Optional<Employee> employee = employeeRepository.findByEmployeeId("BRUCE");
        
        assertThat(employee).isEqualTo(NO_EMPLOYEE);
    }
}