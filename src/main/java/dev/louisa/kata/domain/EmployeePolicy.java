package dev.louisa.kata.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static dev.louisa.kata.domain.PolicyType.*;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class EmployeePolicy implements Policy {
    private final String employeeId;
    private final List<RoomType> roomTypes;

    @Override
    public String getId() {
        return employeeId;
    }

    @Override
    public PolicyType hasPolicyType() {
        return EMPLOYEE_POLICY;
    }
}