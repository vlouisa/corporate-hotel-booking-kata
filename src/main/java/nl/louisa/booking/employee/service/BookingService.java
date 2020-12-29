package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.hotel.domain.RoomType;

import java.time.LocalDate;

public class BookingService {
    private final BookingChecks bookingChecks;

    public BookingService(BookingChecks bookingChecks){
        this.bookingChecks = bookingChecks;
    }

    public void book(String employeeId, String hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        final BookingRequest bookingRequest = new BookingRequest(employeeId, hotelId, roomType, checkIn, checkOut);
        bookingChecks.execute(bookingRequest);

        throw new UnsupportedOperationException();
    }
}
