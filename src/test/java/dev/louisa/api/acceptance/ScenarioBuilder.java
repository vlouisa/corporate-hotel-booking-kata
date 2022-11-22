package dev.louisa.api.acceptance;

import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.shared.domain.ApiService;

public class ScenarioBuilder {

    private PolicyApi policyApi;
    private CompanyApi companyApi;

    public static ScenarioBuilder scenario() {
        return new ScenarioBuilder();
    }

    public ScenarioBuilder using(ApiService... apiServices) {
        assign(apiServices);
        return this;
    }

    private void assign(ApiService[] apiServices) {
        for (ApiService api : apiServices) {
            if (api instanceof PolicyApi){
                this.policyApi = (PolicyApi) api;
            } else if (api instanceof CompanyApi) {
                this.companyApi = (CompanyApi) api;
            }
        }
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
