package dev.louisa.kata.service;

import dev.louisa.kata.domain.CompanyPolicy;
import dev.louisa.kata.domain.EmployeePolicy;
import dev.louisa.kata.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.louisa.kata.domain.RoomType.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingPolicyServiceTest {
    
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private CompanyService companyService;
    private BookingPolicyService bookingPolicyService;

    @BeforeEach
    void setUp() {
        bookingPolicyService = new BookingPolicyService(policyRepository, companyService);
    }

    @Test
    void should_store_a_company_policy() {
        bookingPolicyService.setCompanyPolicy("GOOGLE", SINGLE, DOUBLE, KING);
        verify(policyRepository).save(new CompanyPolicy("GOOGLE", asList(SINGLE, DOUBLE, KING)));
    }

    @Test
    void should_store_an_employee_policy() {
        bookingPolicyService.setEmployeePolicy("NICK", DOUBLE, KING);
        verify(policyRepository).save(new EmployeePolicy("NICK", asList(DOUBLE, KING)));
    }
}