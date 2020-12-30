package nl.louisa.booking.features;

import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.company.service.CompanyService;
import nl.louisa.booking.company.service.PolicyService;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyAdminFeaturesTest {
    private CompanyService companyService;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        final Repository<Employee> employeeRepository = new Repository<>();
        final Repository<Policy> policyRepository = new Repository<>();

        companyService = new CompanyService(employeeRepository, policyRepository);
        policyService = new PolicyService(policyRepository, employeeRepository);
    }

    @Test
    void registering_policies() {
        companyService.addEmployee("Central Perk", "Ross");
        companyService.addEmployee("Central Perk", "Joey");
        companyService.addEmployee("Central Perk", "Chandler");
        companyService.addEmployee("Smelly Cat", "Phoebe");

        policyService.setEmployeePolicy("Ross", SINGLE, EXECUTIVE);
        policyService.setEmployeePolicy("Chandler");

        policyService.setCompanyPolicy("Central Perk", SINGLE, DOUBLE);

        //Can book according to employee policy
        assertThat(policyService.isBookingAllowed("Ross", SINGLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Ross", DOUBLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Ross", EXECUTIVE)).isTrue();

        //Can't book any room according to employee policy
        assertThat(policyService.isBookingAllowed("Chandler", SINGLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Chandler", DOUBLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Chandler", EXECUTIVE)).isFalse();

        //Can book according to company policy
        assertThat(policyService.isBookingAllowed("Joey", SINGLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Joey", DOUBLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Joey", EXECUTIVE)).isFalse();

        //Can book any room because no employee or company policy registered
        assertThat(policyService.isBookingAllowed("Phoebe", SINGLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Phoebe", DOUBLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Phoebe", EXECUTIVE)).isTrue();

        //Not registered as an employee
        assertThat(policyService.isBookingAllowed("Monica", SINGLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Monica", DOUBLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Monica", EXECUTIVE)).isFalse();
    }

    @Test
    void updating_the_policies() {
        companyService.addEmployee("Smelly Cat", "Phoebe");
        companyService.addEmployee("Central Perk", "Ross");

        policyService.setCompanyPolicy("Central Perk", SINGLE, DOUBLE);
        policyService.setCompanyPolicy("Central Perk", EXECUTIVE);

        policyService.setEmployeePolicy("Phoebe", EXECUTIVE);
        policyService.setEmployeePolicy("Phoebe", SINGLE);

        assertThat(policyService.isBookingAllowed("Ross", SINGLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Ross", DOUBLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Ross", EXECUTIVE)).isTrue();

        assertThat(policyService.isBookingAllowed("Phoebe", SINGLE)).isTrue();
        assertThat(policyService.isBookingAllowed("Phoebe", DOUBLE)).isFalse();
        assertThat(policyService.isBookingAllowed("Phoebe", EXECUTIVE)).isFalse();
    }
    @Test
    void deleting_employees() {
        companyService.addEmployee("Central Perk", "Ross");
        policyService.setEmployeePolicy("Ross", SINGLE);

        assertThat(policyService.isBookingAllowed("Ross", SINGLE)).isTrue();

        companyService.deleteEmployee("Ross");

        assertThat(policyService.isBookingAllowed("Ross", SINGLE)).isFalse();
    }
}
