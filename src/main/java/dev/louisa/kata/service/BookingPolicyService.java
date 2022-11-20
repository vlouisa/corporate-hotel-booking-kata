package dev.louisa.kata.service;

import dev.louisa.kata.domain.RoomType;

public class BookingPolicyService {

    public void setCompanyPolicy(String companyId, RoomType... roomTypes) {
    }

    public void setEmployeePolicy(String employeeId, RoomType... roomTypes) {
    }

    public boolean isBookingAllowed(String employeeId, RoomType roomType) {
        return false;
    }
}