package nl.louisa.booking.employee.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.louisa.booking.hotel.domain.RoomType;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BookingRequest {
    private String employeeId;
    private String hotelId;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
