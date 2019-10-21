package com.upgrade.campsite.model;

public class BookingConfirmation {

    private String bookingId;

    public BookingConfirmation(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

}
