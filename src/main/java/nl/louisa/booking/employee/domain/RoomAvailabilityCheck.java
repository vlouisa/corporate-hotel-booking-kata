package nl.louisa.booking.employee.domain;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.hotel.service.HotelService;
import nl.louisa.booking.shared.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public class RoomAvailabilityCheck implements BookingCheck {
    private final HotelService hotelService;
    private final Repository<Booking> bookingRepository;

    public RoomAvailabilityCheck(HotelService hotelService, Repository<Booking> bookingRepository) {
        this.hotelService = hotelService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void doCheck(BookingRequest bookingRequest) {
        Hotel hotel = hotelService.findHotelBy(bookingRequest.getHotelId());
        long roomCapacity = hotel.getRoomTypes().get(bookingRequest.getRoomType());

        List<Booking> bookings = bookingRepository.findWhere(
                booking -> booking.getHotelId().equals(bookingRequest.getHotelId()),
                booking -> booking.getRoomType().equals(bookingRequest.getRoomType())
        );

        long roomsTaken = bookings.stream()
                                .filter( booking -> inPeriod(booking, bookingRequest.getCheckIn(), bookingRequest.getCheckOut()) )
                                .count();

        if ((roomCapacity - roomsTaken) < 1){
            throw new IllegalStateException("Booking cancelled: Room isn't available in requested period");
        }
    }

    private boolean inPeriod(Booking booking, LocalDate checkIn, LocalDate checkOut) {

        if(booking.getCheckOut().isBefore(checkIn) || booking.getCheckOut().isEqual(checkIn) ){
            return false;
        }

        return !booking.getCheckIn().isEqual(checkOut) && !booking.getCheckIn().isAfter(checkOut);
    }
}
