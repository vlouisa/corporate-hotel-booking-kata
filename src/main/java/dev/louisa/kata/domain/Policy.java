package dev.louisa.kata.domain;

import java.util.List;

public interface Policy {
    
    String getId();
    List<RoomType> getRoomTypes();
    PolicyType hasPolicyType();
}
