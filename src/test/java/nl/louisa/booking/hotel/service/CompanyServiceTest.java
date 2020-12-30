package nl.louisa.booking.hotel.service;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.EmployeePolicy;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.company.service.CompanyService;
import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock
    private Repository<Employee> employeeRepository;
    @Mock
    private Repository<Policy> policyRepository;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(employeeRepository, policyRepository);
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

    @Test
    void should_delete_employee_and_employee_policy() {
        when(employeeRepository.findBy("Bart")).thenReturn(new Employee("Bart", "IBM"));
        when(policyRepository.findBy("EP-Bart")).thenReturn(new EmployeePolicy("Bart", asList(RoomType.SINGLE, RoomType.DOUBLE)));

        companyService.deleteEmployee("Bart");

        verify(employeeRepository).delete(new Employee("Bart", "IBM"));
        verify(policyRepository).delete(new EmployeePolicy("Bart", asList(RoomType.SINGLE, RoomType.DOUBLE)));
    }
}