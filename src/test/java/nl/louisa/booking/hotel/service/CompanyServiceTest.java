package nl.louisa.booking.hotel.service;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.service.CompanyService;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock
    private Repository<Employee> employeeRepository;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(employeeRepository);
    }

    @Test
    void should_create_an_employee_in_the_repository() {
        companyService.addEmployee("IBM","Bart");

        verify(employeeRepository).create(new Employee("Bart", "IBM"));
    }

    @Test
    void should_throw_exception_when_entity_already_exists_on_create() {
        when(employeeRepository.findBy("Bart")).thenReturn(new Employee("Bart", "McDonalds"));
        assertThatThrownBy(() -> companyService.addEmployee("IBM", "Bart"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Technical error while creating an employee");

    }
}