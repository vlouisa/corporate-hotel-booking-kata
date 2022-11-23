package dev.louisa.api.acceptance;

import dev.louisa.api.booking.BookingApi;
import dev.louisa.api.hotel.HotelApi;
import dev.louisa.api.shared.domain.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static dev.louisa.api.policy.domain.RoomType.*;

public class BookARoomFeatureTest {
    private static final LocalDate JULY_1ST = LocalDate.of(2022, 7, 1);
    private static final LocalDate JULY_6TH = LocalDate.of(2022, 7, 6);

    private ApiService bookingApi;
    private ApiService hotelApi;


    @BeforeEach
    void setUp() {
        hotelApi = new HotelApi();
        bookingApi = new BookingApi();
    }


    @Test
    void bookARoomWithoutPolicyCheck() {
        ScenarioBuilder.scenario()
                .using(bookingApi, hotelApi)
                .addHotel("HBC", "Hilton Barcelona")
                .setRoomType("HBC", SINGLE, 10)
                .then()
                .bookHotelRoom("KEES", "HBC", SINGLE, JULY_1ST, JULY_6TH)
                .assertThat("KEES")
                .hasABookingConfirmation()
                .execute();
    }
}
