package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.BookingRequest;

import java.util.List;

import static java.util.Arrays.asList;

public class BookingChecks {
    private final List<BookingCheck> checks;

    public BookingChecks(BookingCheck ... checks ) {
        this.checks = asList(checks);
    }

    public void execute(BookingRequest bookingRequest) {
        checks.forEach(check -> check.doCheck(bookingRequest));
    }
}
