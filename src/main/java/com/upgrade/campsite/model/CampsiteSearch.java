package com.upgrade.campsite.model;

import java.time.LocalDate;

public class CampsiteSearch {

    private LocalDate fromRange;
    private LocalDate toRange;

    public CampsiteSearch(LocalDate fromRange, LocalDate toRange) {
        this.fromRange = fromRange;
        this.toRange = toRange;
    }

    public LocalDate getFromRange() {
        return fromRange;
    }

    public void setFromRange(LocalDate fromRange) {
        this.fromRange = fromRange;
    }

    public LocalDate getToRange() {
        return toRange;
    }

    public void setToRange(LocalDate toRange) {
        this.toRange = toRange;
    }

}
