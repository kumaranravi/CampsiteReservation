package com.upgrade.campsite.dal.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingid")
    private Integer bookingId;

    @Column(name = "username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name = "arrivalDate")
    private Date arrivalDate;


    @Column(name="departuredate")
    private Date departureDate;

    public Reservation(Integer bookingId, String username, String email, Date arrivalDate, Date departureDate) {
        this.bookingId = bookingId;
        this.username = username;
        this.email = email;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Reservation(){}

    public Reservation(String username, String email, Date arrivalDate, Date departureDate) {
        this.username = username;
        this.email = email;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "Reservation: " + this.username + ", " + this.email ;
    }

}