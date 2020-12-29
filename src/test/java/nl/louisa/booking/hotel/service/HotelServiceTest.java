package nl.louisa.booking.hotel.service;

import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.hotel.domain.Hotel;
import nl.louisa.booking.shared.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static nl.louisa.booking.hotel.domain.RoomType.SINGLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    private static final Hotel RADISSON_BLU_HOTEL = new Hotel("RAD", "Radisson Blu", new HashMap<>());
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

        verify(hotelRepository).create(RADISSON_BLU_HOTEL);
    }

    @Test
    void should_create_room_details_of_an_existing_hotel() {
        when(hotelRepository.findBy("RAD")).thenReturn(RADISSON_BLU_HOTEL);

        hotelService.setRoom("RAD", 10, SINGLE);

        verify(hotelRepository).upsert(hotelWith(SINGLE, 10));
    }

    @Test
    void should_update_room_details_of_an_existing_hotel() {
        when(hotelRepository.findBy("RAD")).thenReturn(hotelWith(SINGLE, 5));

        hotelService.setRoom("RAD", 10, SINGLE);

        verify(hotelRepository).upsert(hotelWith(SINGLE, 10));
    }

    @Test
    void should_throw_an_exception_when_hotel_not_found_when_updating_room_info() {
        when(hotelRepository.findBy("RAD")).thenReturn(null);

        assertThatThrownBy(() -> hotelService.setRoom("RAD", 10, SINGLE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Technical error while updating room details");

    }

    private Hotel hotelWith(RoomType single, int quantity) {
        Hotel expectedHotel = new Hotel("RAD", "Radisson Blu");
        expectedHotel.saveRoomDetails(single, quantity);
        return expectedHotel;
    }


}