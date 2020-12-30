package nl.louisa.booking.company.domain;

import nl.louisa.booking.hotel.domain.RoomType;
import nl.louisa.booking.shared.repository.Entity;

import java.util.List;

public interface Policy extends Entity {
    List<RoomType> getRoomTypesAllowed();
}
