package nl.louisa.booking.hotel.service;

import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nl.louisa.booking.hotel.builder.HotelBuilder.aHotel;
import static nl.louisa.booking.hotel.domain.RoomType.DOUBLE;
import static nl.louisa.booking.hotel.domain.RoomType.EXECUTIVE;
import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    private static final Hotel RADISSON_BLU_HOTEL = new Hotel("RAD", "Radisson Blu");
    @Mock
    private Repository<Hotel> hotelRepository;

    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(hotelRepository);
    }

    @Test
    void should_save_hotel_in_repository() {
        hotelService.addHotel("RAD", "Radisson Blu");

        verify(hotelRepository).create(new Hotel("RAD", "Radisson Blu"));
    }

    @Test
    void should_update_hotel_with_added_hotel_room_details() {
        when(hotelRepository.findBy("RAD")).thenReturn(
                        aHotel()
                            .basedOn(new Hotel("RAD", "Radison Blu Hotel"))
                            .saveRoom(SINGLE, 5)
                            .saveRoom(DOUBLE, 10)
                            .build());

        hotelService.setRoom("RAD", 33, EXECUTIVE);

        verify(hotelRepository).upsert(
                        aHotel()
                            .basedOn(new Hotel("RAD", "Radison Blu Hotel"))
                            .saveRoom(SINGLE, 5)
                            .saveRoom(DOUBLE, 10)
                            .saveRoom(EXECUTIVE, 33)
                            .build()
        );
    }

    @Test
    void should_update_hotel_with_updated_room_details() {
        when(hotelRepository.findBy("RAD")).thenReturn(
                        aHotel()
                            .basedOn(new Hotel("RAD", "Radison Blu Hotel"))
                            .saveRoom(SINGLE, 5)
                            .saveRoom(DOUBLE, 10)
                            .build());

        hotelService.setRoom("RAD", 95, DOUBLE);

        verify(hotelRepository).upsert(
                        aHotel()
                            .basedOn(new Hotel("RAD", "Radison Blu Hotel"))
                            .saveRoom(SINGLE, 5)
                            .saveRoom(DOUBLE, 95)
                            .build()
        );
    }

    @Test
    void should_throw_an_exception_when_hotel_not_found_when_updating_room_info() {
        when(hotelRepository.findBy("RAD")).thenReturn(null);

        assertThatThrownBy(() -> hotelService.setRoom("RAD", 10, SINGLE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Technical error while updating room details");

    }

    @Test
    void should_find_hotel_by_id() {
        when(hotelRepository.findBy("RTZ")).thenReturn(
                aHotel()
                        .basedOn(new Hotel("RTZ", "Ritz Carlton"))
                        .saveRoom(SINGLE, 2)
                        .saveRoom(DOUBLE, 3)
                        .build());


        assertThat(hotelService.findHotelBy("RTZ")).isEqualTo(
                aHotel()
                        .basedOn(new Hotel("RTZ", "Ritz Carlton"))
                        .saveRoom(SINGLE, 2)
                        .saveRoom(DOUBLE, 3)
                        .build()
        );
    }
}