package dev.louisa.kata.acceptance;

import dev.louisa.kata.company.service.CompanyService;
import dev.louisa.kata.policy.domain.RoomType;
import dev.louisa.kata.policy.service.PolicyService;

public class ScenarioBuilder {

    private final PolicyService policyService;
    private final CompanyService companyService;

    private ScenarioBuilder(PolicyService policyService, CompanyService companyService) {
        this.policyService = policyService;
        this.companyService = companyService;
    }

    public static ScenarioBuilder scenario(PolicyService policyService, CompanyService companyService) {
        return new ScenarioBuilder(policyService, companyService);
    }

    public ScenarioBuilder addEmployee(String employeeId, String companyId) {
        companyService.addEmployee(employeeId, companyId);
        return this;
    }

    public ScenarioBuilder addCompanyPolicy(String companyId, RoomType ... roomTypes) {
        policyService.setCompanyPolicy(companyId, roomTypes);
        return this;
    }

    public ScenarioBuilder addEmployeePolicy(String employeeId, RoomType ... roomTypes) {
        policyService.setEmployeePolicy(employeeId, roomTypes);
        return this;
    }
}
