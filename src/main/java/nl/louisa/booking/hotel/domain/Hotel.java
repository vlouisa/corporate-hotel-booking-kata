package nl.louisa.booking.hotel.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.louisa.booking.shared.repository.Entity;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@ToString
public class Hotel implements Entity {
    private String hotelId;
    private String hotelName;
    private Map<RoomType, Long> roomTypes;

    public Hotel(String hotelId, String hotelName){
        this(hotelId, hotelName, new HashMap<>());
    }

    public void saveRoomDetails(RoomType roomType, long quantity){
        roomTypes.put(roomType, quantity);
    }

    @Override
    public String getId() {
        return getHotelId();
    }
}
