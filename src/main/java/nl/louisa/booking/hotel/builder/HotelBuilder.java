package nl.louisa.booking.hotel.builder;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.hotel.domain.RoomType;

import java.util.HashMap;
import java.util.Map;

public class HotelBuilder {
    private Hotel bluePrintHotel = new Hotel("", "");
    private Map<RoomType, Long> roomTypeMap = new HashMap<>();

    public static HotelBuilder aHotel() {
        return new HotelBuilder();
    }

    public HotelBuilder basedOn(Hotel hotel) {
        this.bluePrintHotel = hotel;
        return this;
    }

    public HotelBuilder saveRoom(RoomType roomType, long quantity) {
        roomTypeMap.put(roomType, quantity);
        return this;
    }

    public Hotel build() {
        Hotel hotel = createClone();
        saveRoomDetails(hotel);

        return hotel;
    }

    private void saveRoomDetails(Hotel hotel) {
        roomTypeMap.forEach(hotel::saveRoomDetails);
    }

    private Hotel createClone() {
        return new Hotel(
                bluePrintHotel.getHotelId(),
                bluePrintHotel.getHotelName(),
                bluePrintHotel.getRoomTypes()
        );
    }

}
