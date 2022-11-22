package dev.louisa.api.acceptance;

import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.policy.PolicyApi;
import org.assertj.core.api.SoftAssertions;

public class PolicyAsserter {
    private final PolicyApi policyApi;
    private final SoftAssertions softly = new SoftAssertions();

    private String employeeId;

    private PolicyAsserter(PolicyApi policyApi) {
        this.policyApi = policyApi;
    }

    public static PolicyAsserter using(PolicyApi policyApi) {
        return new PolicyAsserter(policyApi);
    }

    public PolicyAsserter assertThat(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public PolicyAsserter isAllowedToBook(RoomType ...  roomTypes) {
        for (RoomType roomType : roomTypes){
            assertAllowed(roomType);
        }
        return this;
    }

    private void assertAllowed(RoomType roomType) {
        softly
            .assertThat(policyApi.isBookingAllowed(employeeId, roomType))
            .as("Employee '%s' should be allowed to book roomType '%s', but isn't", employeeId, roomType)
            .isTrue();
    }

    public PolicyAsserter isNotAllowedToBook(RoomType ... roomTypes) {
        for (RoomType roomType : roomTypes){
            assertNotAllowed(roomType);
        }
        return this;
    }

    private void assertNotAllowed(RoomType roomType) {
        softly
            .assertThat(policyApi.isBookingAllowed(employeeId, roomType))
            .as("Employee '%s' shouldn't be allowed to book roomType '%s', but is", employeeId, roomType)
            .isFalse();
    }

    public void assertAll() {
        softly.assertAll();
    }
}
