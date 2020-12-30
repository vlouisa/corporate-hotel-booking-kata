package nl.louisa.booking.employee.service;

import nl.louisa.booking.employee.domain.Booking;
import nl.louisa.booking.employee.domain.BookingRequest;
import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.shared.Identity;
import nl.louisa.booking.shared.repository.Repository;

import java.time.LocalDate;

public class BookingService {
    private final BookingChecks bookingChecks;
    private final Identity identity;
    private final Repository<Booking> bookingRepository;

    public BookingService(BookingChecks bookingChecks, Identity identity, Repository<Booking> bookingRepository){
        this.bookingChecks = bookingChecks;
        this.identity = identity;
        this.bookingRepository = bookingRepository;
    }

    public Booking book(String employeeId, String hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        final BookingRequest bookingRequest = new BookingRequest(employeeId, hotelId, roomType, checkIn, checkOut);
        bookingChecks.execute(bookingRequest);

        final Booking booking = new Booking(
                identity.generate(),
                bookingRequest.getEmployeeId(),
                bookingRequest.getHotelId(),
                bookingRequest.getRoomType(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut()
        );

        bookingRepository.create(booking);

        return booking;
    }
}
