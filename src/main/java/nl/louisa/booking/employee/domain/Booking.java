package nl.louisa.booking.employee.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.louisa.booking.hotel.domain.RoomType;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Booking {
    private final String bookingId;
    private final String employeeId;
    private final String hotelId;
    private final RoomType roomType;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
}
