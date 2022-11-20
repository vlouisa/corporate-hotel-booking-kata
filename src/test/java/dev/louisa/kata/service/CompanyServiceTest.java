package dev.louisa.kata.service;

import dev.louisa.kata.domain.Employee;
import dev.louisa.kata.exception.CompanyApiException;
import dev.louisa.kata.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(employeeRepository);
    }

    @Test
    void should_store_a_new_employee() {
        companyService.addEmployee("BAS", "TESLA");
        
        verify(employeeRepository).save(new Employee("BAS", "TESLA"));
    }
    
    @Test
    void should_throw_exception_when_employee_already_exists() {
        when(employeeRepository.findByEmployeeId("BAS")).thenReturn(Optional.of(new Employee("BAS","TESLA")));
        
        assertThatThrownBy(() -> companyService.addEmployee("BAS", "TESLA"))
                .isInstanceOf(CompanyApiException.class)
                .hasMessage("Employee already exists");
    }
}