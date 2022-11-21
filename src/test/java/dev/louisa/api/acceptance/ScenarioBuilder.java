package dev.louisa.api.acceptance;

import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.policy.PolicyApi;

public class ScenarioBuilder {

    private final PolicyApi policyApi;
    private final CompanyApi companyApi;

    private ScenarioBuilder(PolicyApi policyApi, CompanyApi companyApi) {
        this.policyApi = policyApi;
        this.companyApi = companyApi;
    }

    public static ScenarioBuilder scenario(PolicyApi policyApi, CompanyApi companyApi) {
        return new ScenarioBuilder(policyApi, companyApi);
    }

    public ScenarioBuilder addEmployee(String employeeId, String companyId) {
        companyApi.addEmployee(employeeId, companyId);
        return this;
    }

    public ScenarioBuilder addCompanyPolicy(String companyId, RoomType ... roomTypes) {
        policyApi.setCompanyPolicy(companyId, roomTypes);
        return this;
    }

    public ScenarioBuilder addEmployeePolicy(String employeeId, RoomType ... roomTypes) {
        policyApi.setEmployeePolicy(employeeId, roomTypes);
        return this;
    }
}
