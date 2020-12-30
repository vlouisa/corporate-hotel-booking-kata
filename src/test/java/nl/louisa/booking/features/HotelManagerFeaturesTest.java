package nl.louisa.booking.features;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.hotel.service.HotelService;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.louisa.booking.hotel.builder.HotelBuilder.aHotel;
import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static nl.louisa.booking.hotel.domain.RoomType.SUITE;
import static org.assertj.core.api.Assertions.assertThat;

public class HotelManagerFeaturesTest {
    private HotelService hotelService;


    @BeforeEach
    void setUp() {
        final Repository<Hotel> hotelRepository = new Repository<>();
        hotelService = new HotelService(hotelRepository);
    }

    @Test
    void registering_hotels() {
        hotelService.addHotel("SAV", "The Savoy");
        hotelService.setRoom("SAV", 40, SINGLE);
        hotelService.setRoom("SAV", 35, DOUBLE);
        hotelService.setRoom("SAV", 31, DOUBLE);
        hotelService.setRoom("SAV", 50, SINGLE);
        hotelService.setRoom("SAV", 20, EXECUTIVE);
        hotelService.setRoom("SAV", 4, SUITE);

        assertThat(hotelService.findHotelBy("SAV")).isEqualTo(
                aHotel()
                    .basedOn(new Hotel("SAV", "The Savoy"))
                    .saveRoom(SINGLE, 50)
                    .saveRoom(DOUBLE, 31)
                    .saveRoom(EXECUTIVE, 20)
                    .saveRoom(SUITE, 4)
                    .build()
        );
    }
}
