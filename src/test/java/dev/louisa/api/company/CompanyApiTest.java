package dev.louisa.api.company;

import dev.louisa.api.shared.domain.Employee;
import dev.louisa.api.company.exception.CompanyApiException;
import dev.louisa.api.company.repository.EmployeeRepository;
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
class CompanyApiTest {
    private static final Employee EMPLOYEE_BAS = new Employee("BAS", "TESLA");

    @Mock
    private EmployeeRepository employeeRepository;
    private CompanyApi companyApi;

    @BeforeEach
    void setUp() {
        companyApi = new CompanyApi(employeeRepository);
    }

    @Test
    void should_store_a_new_employee() {
        companyApi.addEmployee("BAS", "TESLA");
        
        verify(employeeRepository).save(EMPLOYEE_BAS);
    }
    
    @Test
    void should_throw_exception_when_employee_already_exists() {
        when(employeeRepository.findByEmployeeId("BAS")).thenReturn(Optional.of(EMPLOYEE_BAS));
        
        assertThatThrownBy(() -> companyApi.addEmployee("BAS", "TESLA"))
                .isInstanceOf(CompanyApiException.class)
                .hasMessage("Employee already exists");
    }
    
    @Test
    void should_fetch_employee_by_id() {
        
        companyApi.fetchEmployee("DIRK");
        
        verify(employeeRepository).findByEmployeeId("DIRK");
    }
}