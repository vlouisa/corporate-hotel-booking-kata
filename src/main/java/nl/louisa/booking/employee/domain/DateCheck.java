package nl.louisa.booking.employee.domain;

public class DateCheck implements BookingCheck {
    @Override
    public void doCheck(BookingRequest bookingRequest) {
        if (!bookingRequest.getCheckIn().isBefore(bookingRequest.getCheckOut())){
            throw new IllegalStateException("Booking cancelled: Check-in should be before check-out");
        }

    }
}
