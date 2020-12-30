package nl.louisa.booking.company.domain;

import nl.louisa.booking.company.service.PolicyService;
import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.BookingRequest;

public class PolicyCheck implements BookingCheck {
    private final PolicyService policyService;

    public PolicyCheck(PolicyService policyService) {
        this.policyService = policyService;
    }

    @Override
    public void doCheck(BookingRequest bookingRequest) {
        if (!policyService.isBookingAllowed(bookingRequest.getEmployeeId(), bookingRequest.getRoomType())){
            throw new IllegalStateException("Booking cancelled: Room can't be booked because of policy");
        }
    }
}
