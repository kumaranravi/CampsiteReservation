package com.upgrade.campsite.model;

import java.util.List;

public class Availability {

    private List<String> availableDates;

    public Availability(List<String> availableDates) {
        this.availableDates = availableDates;
    }
    public List<String> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<String> availableDates) {
        this.availableDates = availableDates;
    }

}
