package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingCheck;
import nl.louisa.booking.employee.domain.BookingRequest;

import java.util.List;

import static java.util.Arrays.asList;

public class BookingChecker {
    private final List<BookingCheck> checks;

    public BookingChecker(BookingCheck ... checks ) {
        this.checks = asList(checks);
    }

    public void validate(BookingRequest bookingRequest) {
        checks.forEach(check -> check.doCheck(bookingRequest));
    }
}
