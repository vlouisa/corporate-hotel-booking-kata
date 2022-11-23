package dev.louisa.api.booking;

import dev.louisa.api.booking.domain.BookingConfirmation;
import dev.louisa.api.policy.domain.RoomType;
import dev.louisa.api.shared.domain.ApiService;

import java.time.LocalDate;

public class BookingApi implements ApiService {
    public BookingConfirmation book(String employeeId, String hotelId, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        return null;
    }
}
