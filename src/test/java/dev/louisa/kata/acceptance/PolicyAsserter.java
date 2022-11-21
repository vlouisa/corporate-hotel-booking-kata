package dev.louisa.kata.acceptance;

import dev.louisa.kata.policy.domain.RoomType;
import dev.louisa.kata.policy.service.PolicyService;
import org.assertj.core.api.SoftAssertions;

public class PolicyAsserter {
    private final PolicyService policyService;
    private final SoftAssertions softly = new SoftAssertions();

    private String employeeId;

    private PolicyAsserter(PolicyService policyService) {
        this.policyService = policyService;
    }

    public static PolicyAsserter using(PolicyService policyService) {
        return new PolicyAsserter(policyService);
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
            .assertThat(policyService.isBookingAllowed(employeeId, roomType))
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
            .assertThat(policyService.isBookingAllowed(employeeId, roomType))
            .as("Employee '%s' shouldn't be allowed to book roomType '%s', but is", employeeId, roomType)
            .isFalse();
    }

    public void assertAll() {
        softly.assertAll();
    }
}
