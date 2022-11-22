package dev.louisa.api.acceptance;

import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.shared.domain.ApiService;

public class ScenarioBuilder extends Scenario {

    public static ScenarioBuilder scenario() {
        return new ScenarioBuilder();
    }

    public ScenarioBuilder using(ApiService... apiServices) {
        assign(apiServices);
        return this;
    }
    
    public ScenarioBuilder addEmployee(String employeeId, String companyId) {
        validate(companyApi, CompanyApi.class);
        companyApi.get().addEmployee(employeeId, companyId);
        return this;
    }

    public ScenarioBuilder addCompanyPolicy(String companyId, RoomType ... roomTypes) {
        validate(policyApi, PolicyApi.class);
        policyApi.get().setCompanyPolicy(companyId, roomTypes);
        return this;
    }

    public ScenarioBuilder addEmployeePolicy(String employeeId, RoomType ... roomTypes) {
        validate(policyApi, PolicyApi.class);
        policyApi.get().setEmployeePolicy(employeeId, roomTypes);
        return this;
    }

    public ScenarioAsserter then() {
        return ScenarioAsserter.using(policyApi.get());
    }
}
