package nl.louisa.booking.company.service;

import nl.louisa.booking.company.domain.CompanyPolicy;
import nl.louisa.booking.company.domain.Employee;
import nl.louisa.booking.company.domain.EmployeePolicy;
import nl.louisa.booking.company.domain.Policy;
import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.shared.repository.Repository;

import static java.util.Arrays.asList;

public class PolicyService {
    private static final String EMPLOYEE_POLICY_ID_PREFIX = "EP-";
    private static final String COMPANY_POLICY_ID_PREFIX = "CP-";

    private final Repository<Policy> policyRepository;
    private final Repository<Employee> employeeRepository;

    public PolicyService(Repository<Policy> policyRepository, Repository<Employee> employeeRepository) {
        this.policyRepository = policyRepository;
        this.employeeRepository = employeeRepository;
    }

    public void setEmployeePolicy(String employeeId, RoomType ... roomTypesAllowed) {
        validate(employeeId);
        policyRepository.upsert(new EmployeePolicy(employeeId, asList(roomTypesAllowed)));
    }

    private void validate(String employeeId) {
        final Employee employee = employeeRepository.findBy(employeeId);
        if (employee == null){
            throw new IllegalStateException("Technical error while setting policy");
        }
    }

    public void setCompanyPolicy(String companyId, RoomType ... roomTypesAllowed) {
        policyRepository.upsert(new CompanyPolicy(companyId, asList(roomTypesAllowed)));
    }

    public boolean isBookingAllowed(String employeeId, RoomType roomType) {
        final Employee employee = employeeRepository.findBy(employeeId);
        if (employee == null){
            return false;
        }

        final Policy employeePolicy = policyRepository.findBy(EMPLOYEE_POLICY_ID_PREFIX + employeeId);
        if(employeePolicy != null){
            return employeePolicy.getRoomTypesAllowed().contains(roomType);
        }

        final Policy companyPolicy = policyRepository.findBy(COMPANY_POLICY_ID_PREFIX + employee.getCompanyId());
        if (companyPolicy != null){
            return companyPolicy.getRoomTypesAllowed().contains(roomType);
        }

        return true;
    }
}
