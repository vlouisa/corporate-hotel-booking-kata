package dev.louisa.api.acceptance;

import dev.louisa.api.booking.BookingApi;
import dev.louisa.api.booking.domain.BookingConfirmation;
import dev.louisa.api.policy.PolicyApi;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.shared.domain.ApiService;
import org.assertj.core.api.SoftAssertions;

import java.time.LocalDate;

public class ScenarioAsserter extends Scenario {
    private final SoftAssertions softly = new SoftAssertions();
    private String employeeId;
    private BookingConfirmation bookingConfirmation;

    private ScenarioAsserter(ApiService ... apiServices) {
        assign(apiServices);
    }

    public static ScenarioAsserter createAssert(ApiService ... apiServices) {
        return new ScenarioAsserter(apiServices);
    }

    public ScenarioAsserter assertThat(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public ScenarioAsserter isAllowedToBook(RoomType ...  roomTypes) {
        for (RoomType roomType : roomTypes){
            assertAllowed(roomType);
        }
        return this;
    }

    private void assertAllowed(RoomType roomType) {
        validate(policyApi, PolicyApi.class);
        softly
            .assertThat(policyApi.get().isBookingAllowed(employeeId, roomType))
            .as("Employee '%s' should be allowed to book roomType '%s', but isn't", employeeId, roomType)
            .isTrue();
    }

    public ScenarioAsserter isNotAllowedToBook(RoomType ... roomTypes) {
        for (RoomType roomType : roomTypes){
            assertNotAllowed(roomType);
        }
        return this;
    }

    private void assertNotAllowed(RoomType roomType) {
        validate(policyApi, PolicyApi.class);
        softly
            .assertThat(policyApi.get().isBookingAllowed(employeeId, roomType))
            .as("Employee '%s' shouldn't be allowed to book roomType '%s', but is", employeeId, roomType)
            .isFalse();
    }

    public ScenarioAsserter bookHotelRoom(String employeeId, String hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        validate(bookingApi, BookingApi.class);
        bookingConfirmation = bookingApi.get().book(employeeId, hotelId, roomType, checkIn, checkOut);
        return this;
    }

    public ScenarioAsserter hasABookingConfirmation() {
        softly.assertThat(bookingConfirmation)
                .isEqualTo(new BookingConfirmation(employeeId));
        return this;
    }

    public void execute() {
        softly.assertAll();
    }
}
