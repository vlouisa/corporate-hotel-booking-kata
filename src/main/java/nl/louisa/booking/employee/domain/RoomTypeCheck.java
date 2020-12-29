package nl.louisa.booking.employee.domain;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.shared.repository.Repository;

public class RoomTypeCheck implements BookingCheck {
    private final Repository<Hotel> hotelRepository;

    public RoomTypeCheck(Repository<Hotel> hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void doCheck(BookingRequest bookingRequest) {
        final Hotel hotel = hotelRepository.findBy(bookingRequest.getHotelId());
        if(requestedRoomTypeNotAvailable(bookingRequest.getRoomType(), hotel)){
            throw new IllegalStateException("Booking cancelled: Hotel does not provide requested room");
        }
    }

    private boolean requestedRoomTypeNotAvailable(RoomType requestedRoomType, Hotel hotel) {
        return hotel == null || !hotel.getRoomTypes().containsKey(requestedRoomType);
    }
}
