package dev.louisa.api.acceptance;

import dev.louisa.api.shared.domain.ApiService;

public class ScenarioBuilder extends Scenario {

    public static ScenarioBuilder scenario() {
        return new ScenarioBuilder();
    }

    public ScenarioAction using(ApiService... apiServices) {
        return ScenarioAction.createAction(apiServices);
    }
}
