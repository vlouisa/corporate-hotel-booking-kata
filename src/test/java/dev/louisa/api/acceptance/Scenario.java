package dev.louisa.api.acceptance;

import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.shared.domain.ApiService;

import java.util.Optional;

public abstract class Scenario {
    protected Optional<PolicyApi> policyApi = Optional.empty();
    protected Optional<CompanyApi> companyApi = Optional.empty();

    protected void assign(ApiService[] apiServices) {
        for (ApiService api : apiServices) {
            if (api instanceof PolicyApi){
                this.policyApi = Optional.of((PolicyApi) api);
            } else if (api instanceof CompanyApi){
                this.companyApi = Optional.of((CompanyApi) api);
            } 
        }
    }

    protected void validate(Optional<? extends ApiService> api, Class<? extends ApiService> clazz ) {
        if (api.isEmpty()) {
            String message = String.format("Want to invoke '%s' but service isn't registered", clazz.getSimpleName());
            throw new ScenarioException(message);
        }
    }

}
