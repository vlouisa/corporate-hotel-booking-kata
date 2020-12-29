package nl.louisa.booking.company.domain;

import lombok.Data;
import nl.louisa.booking.shared.repository.Entity;

@Data
public class Employee implements Entity {
    private final String employeeId;
    private final String companyId;

    public Employee(String employeeId, String companyId) {
        this.employeeId = employeeId;
        this.companyId = companyId;
    }

    @Override
    public String getId() {
        return getEmployeeId();
    }
}
