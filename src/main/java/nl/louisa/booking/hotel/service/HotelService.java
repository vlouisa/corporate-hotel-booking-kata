package nl.louisa.booking.hotel.service;

import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;

public class HotelService {
    private final Repository<Hotel> hotelRepository;

    public HotelService(Repository<Hotel> hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void addHotel(String hotelId, String hotelName) {
        hotelRepository.create(new Hotel(hotelId, hotelName));
    }

    public void setRoom(String hotelId, long quantity, RoomType roomType) {
        final Hotel hotel = hotelRepository.findBy(hotelId);
        validate(hotel);

        hotel.saveRoomDetails(roomType, quantity);
        hotelRepository.upsert(hotel);
    }

    private void validate(Hotel hotel) {
        if (hotel == null){
            throw new IllegalStateException("Technical error while updating room details");
        }
    }

}
