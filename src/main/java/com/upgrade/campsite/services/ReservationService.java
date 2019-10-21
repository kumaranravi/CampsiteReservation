package com.upgrade.campsite.services;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.dal.persistence.ReservationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationImpl reservationImpl;

    public String bookReservation(Reservation reservation) {
        return reservationImpl.createCampsiteBooking(reservation);
    }

    public List<Reservation> viewReservation(int bookingId) {
        return reservationImpl.viewCampsiteBooking(bookingId);
    }

    public ResponseEntity<?> modifyReservation(Reservation reservation) {
        return reservationImpl.modifyCampsiteBooking(reservation);
    }

    public ResponseEntity<?> deleteReservation(int bookingId) {
        return reservationImpl.deleteCampsiteBooking(bookingId);
    }
}
