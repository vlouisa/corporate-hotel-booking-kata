package dev.louisa.kata.policy.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static dev.louisa.kata.policy.domain.PolicyType.*;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CompanyPolicy implements Policy {
    private final String companyId;
    private final List<RoomType> roomTypes;

    @Override
    public String getId() {
        return companyId;
    }

    @Override
    public PolicyType hasPolicyType() {
        return COMPANY_POLICY;
    }
}
