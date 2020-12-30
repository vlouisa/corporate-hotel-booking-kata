package nl.louisa.booking.shared;

import java.util.UUID;

public class Identity {
    public String generate() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
