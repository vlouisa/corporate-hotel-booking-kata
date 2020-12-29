package nl.louisa.booking.hotel.builder;

import nl.louisa.booking.hotel.domain.Hotel;
import org.junit.jupiter.api.Test;

import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static nl.louisa.booking.hotel.domain.RoomType.SUITE;
import static org.assertj.core.api.Assertions.assertThat;

class HotelBuilderTest {

    @Test
    void should_clone_an_existing_hotel() {
        Hotel hotel = new Hotel("GRH", "Golden Rose Hotel");
        hotel.saveRoomDetails(SINGLE, 4);
        hotel.saveRoomDetails(DOUBLE, 6);


        Hotel newHotel = HotelBuilder.aHotel()
                            .basedOn(hotel)
                            .build();

        assertThat(newHotel.getHotelId()).isEqualTo("GRH");
        assertThat(newHotel.getHotelName()).isEqualTo("Golden Rose Hotel");
        assertThat(newHotel.getRoomTypes().get(SINGLE)).isEqualTo(4);
        assertThat(newHotel.getRoomTypes().get(DOUBLE)).isEqualTo(6);
    }

    @Test
    void should_update_existing_rooms() {
        Hotel hotel = new Hotel("GRH", "Golden Rose Hotel");
        hotel.saveRoomDetails(SINGLE, 4);
        hotel.saveRoomDetails(DOUBLE, 6);


        Hotel newHotel = HotelBuilder.aHotel()
                            .basedOn(hotel)
                            .saveRoom(SINGLE, 5)
                            .saveRoom(DOUBLE, 7)
                            .build();

        assertThat(newHotel.getHotelId()).isEqualTo("GRH");
        assertThat(newHotel.getHotelName()).isEqualTo("Golden Rose Hotel");
        assertThat(newHotel.getRoomTypes().get(SINGLE)).isEqualTo(5);
        assertThat(newHotel.getRoomTypes().get(DOUBLE)).isEqualTo(7);
        assertThat(newHotel.getRoomTypes().size()).isEqualTo(2);
    }

    @Test
    void should_add_non_existing_rooms() {
        Hotel hotel = new Hotel("GRH", "Golden Rose Hotel");
        hotel.saveRoomDetails(SINGLE, 4);
        hotel.saveRoomDetails(DOUBLE, 6);


        Hotel newHotel = HotelBuilder.aHotel()
                            .basedOn(hotel)
                            .saveRoom(EXECUTIVE, 2)
                            .saveRoom(SUITE, 1)
                            .build();

        assertThat(newHotel.getHotelId()).isEqualTo("GRH");
        assertThat(newHotel.getHotelName()).isEqualTo("Golden Rose Hotel");
        assertThat(newHotel.getRoomTypes().get(SINGLE)).isEqualTo(4);
        assertThat(newHotel.getRoomTypes().get(DOUBLE)).isEqualTo(6);
        assertThat(newHotel.getRoomTypes().get(EXECUTIVE)).isEqualTo(2);
        assertThat(newHotel.getRoomTypes().get(SUITE)).isEqualTo(1);
        assertThat(newHotel.getRoomTypes().size()).isEqualTo(4);
    }
}