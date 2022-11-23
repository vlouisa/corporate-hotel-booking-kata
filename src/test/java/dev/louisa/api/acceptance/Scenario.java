package dev.louisa.api.acceptance;

import dev.louisa.api.booking.BookingApi;
import dev.louisa.api.company.CompanyApi;
import dev.louisa.api.hotel.HotelApi;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.shared.domain.ApiService;

import java.util.Optional;

public abstract class Scenario {
    protected Optional<PolicyApi> policyApi = Optional.empty();
    protected Optional<CompanyApi> companyApi = Optional.empty();
    protected Optional<HotelApi> hotelApi = Optional.empty();
    protected Optional<BookingApi> bookingApi = Optional.empty();

    protected void assign(ApiService[] apiServices) {
        for (ApiService api : apiServices) {
            if (api instanceof PolicyApi){
                this.policyApi = Optional.of((PolicyApi) api);
            } else if (api instanceof CompanyApi){
                this.companyApi = Optional.of((CompanyApi) api);
            } else if (api instanceof HotelApi){
                this.hotelApi = Optional.of((HotelApi) api);
            } else if (api instanceof BookingApi){
                this.bookingApi = Optional.of((BookingApi) api);
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
