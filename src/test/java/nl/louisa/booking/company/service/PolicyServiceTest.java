package nl.louisa.booking.company.service;

import nl.louisa.booking.company.domain.CompanyPolicy;
import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.EmployeePolicy;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {
    @Mock
    private Repository<Employee> employeeRepository;
    @Mock
    private Repository<Policy> policyRepository;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyService = new PolicyService(policyRepository, employeeRepository);
    }

    @Test
    void should_save_an_employee_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        policyService.setEmployeePolicy("TS", SINGLE, DOUBLE, EXECUTIVE);

        Policy employeePolicy = new EmployeePolicy("TS", asList(SINGLE, DOUBLE, EXECUTIVE));
        verify(policyRepository).upsert(employeePolicy);
    }

    @Test
    void should_throw_exception_when_employee_does_not_exist_when_setting_employee_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(null);

        assertThatThrownBy(() -> policyService.setEmployeePolicy("TS", SINGLE, DOUBLE, EXECUTIVE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Technical error while setting policy");

    }

    @Test
    void should_save_a_company_policy() {
        policyService.setCompanyPolicy("Stark Industries", SINGLE, DOUBLE, EXECUTIVE);

        Policy companyPolicy = new CompanyPolicy("Stark Industries", asList(SINGLE, DOUBLE, EXECUTIVE));
        verify(policyRepository).upsert(companyPolicy);
    }

    @Test
    void should_allow_booking_based_on_employee_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        when(policyRepository.findBy("EP-TS")).thenReturn(
                new EmployeePolicy("TS", asList(SINGLE, EXECUTIVE))
        );

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isTrue();
    }

    @Test
    void should_deny_booking_based_on_employee_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        when(policyRepository.findBy("EP-TS")).thenReturn(
                new EmployeePolicy("TS", asList(SINGLE, DOUBLE))
        );

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isFalse();
    }

    @Test
    void should_allow_booking_based_on_company_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        when(policyRepository.findBy("EP-TS")).thenReturn(null);
        when(policyRepository.findBy("CP-Stark Industries")).thenReturn(
                new CompanyPolicy("Stark Industries", asList(SINGLE, EXECUTIVE))
        );

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isTrue();
    }

    @Test
    void should_deny_booking_based_on_company_policy() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        when(policyRepository.findBy("EP-TS")).thenReturn(null);
        when(policyRepository.findBy("CP-Stark Industries")).thenReturn(
                new CompanyPolicy("Stark Industries", asList(SINGLE, DOUBLE))
        );

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isFalse();
    }

    @Test
    void should_allow_booking_when_no_policy_found() {
        when(employeeRepository.findBy("TS")).thenReturn(new Employee("TS", "Stark Industries"));
        when(policyRepository.findBy("EP-TS")).thenReturn(null);
        when(policyRepository.findBy("CP-Stark Industries")).thenReturn(null);

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isTrue();
    }

    @Test
    void should_deny_booking_when_employee_is_not_found() {
        when(employeeRepository.findBy("TS")).thenReturn(null);

        assertThat(policyService.isBookingAllowed("TS", EXECUTIVE)).isFalse();
    }
}