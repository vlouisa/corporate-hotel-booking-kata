package dev.louisa.api.acceptance;

import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.hotel.HotelApi;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.shared.domain.ApiService;


public class ScenarioAction extends Scenario {
    private final ApiService[] apiServices;

    private ScenarioAction(ApiService[] apiServices) {
        this.apiServices = apiServices;
        assign(apiServices);
    }

    public static ScenarioAction createAction(ApiService... apiServices) {
        return new ScenarioAction(apiServices);
    }

    public ScenarioAction addEmployee(String employeeId, String companyId) {
        validate(companyApi, CompanyApi.class);
        companyApi.get().addEmployee(employeeId, companyId);
        return this;
    }

    public ScenarioAction addCompanyPolicy(String companyId, RoomType... roomTypes) {
        validate(policyApi, PolicyApi.class);
        policyApi.get().setCompanyPolicy(companyId, roomTypes);
        return this;
    }

    public ScenarioAction addEmployeePolicy(String employeeId, RoomType... roomTypes) {
        validate(policyApi, PolicyApi.class);
        policyApi.get().setEmployeePolicy(employeeId, roomTypes);
        return this;
    }

    public ScenarioAction addHotel(String hotelId, String hotelName) {
        validate(hotelApi, HotelApi.class);
        hotelApi.get().addHotel(hotelId, hotelName);
        return this;
    }

    public ScenarioAction setRoomType(String hotelId, RoomType roomType, int quantity) {
        validate(hotelApi, HotelApi.class);
        hotelApi.get().setRoomType(hotelId, roomType, quantity);
        return this;
    }

    public ScenarioAsserter then() {
        return ScenarioAsserter.createAssert(apiServices);
    }
}
