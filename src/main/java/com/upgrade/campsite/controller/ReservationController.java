package com.upgrade.campsite.controller;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.model.BookingConfirmation;
import com.upgrade.campsite.model.Response;
import com.upgrade.campsite.services.ReservationService;
import com.upgrade.campsite.services.SearchService;
import com.upgrade.campsite.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.logging.Logger;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class ReservationController {
    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SearchService searchService;


    @RequestMapping(value = "/reservations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bookReservations(@RequestBody Reservation reservation) throws ParseException {

        // Validate the reservation days are lesser or equal to 3, and reservation are less than a month in advance.
        LocalDate arrivalDate = reservation.getArrivalDate().toLocalDate();
        Period maximumReservationDays = Period.between(reservation.getArrivalDate().toLocalDate(), reservation.getDepartureDate().toLocalDate());
        Period advanceBooking = Period.between(arrivalDate, arrivalDate.plusMonths(1));
        if (maximumReservationDays.getDays() > 3 || ((advanceBooking.getMonths() == 1 || advanceBooking.getMonths() > 1) && advanceBooking.getDays() >0)) {
            Response response = new Response(200, "Reservation cannot be made for more than 3days");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }

        if (!Utility.validateEmailId(reservation.getEmail())) {
            Response response = new Response(400, "Invalid email Address");
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity responseEntity = searchService.findAvailablity(reservation);
        if (responseEntity != null) {
            return responseEntity;
        }

        String bookingId = reservationService.bookReservation(reservation);
        if (bookingId != null) {
            BookingConfirmation bookingConfirmation = new BookingConfirmation(bookingId);
            return new ResponseEntity<BookingConfirmation>(bookingConfirmation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<BookingConfirmation>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/reservations/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyReservations(@RequestBody Reservation reservation) throws ParseException {

        if (reservation.getBookingId() == null) {
            Response response = new Response(400, "Booking Id not provided");
            return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED );
        }

        if (!Utility.validateEmailId(reservation.getEmail())) {
            Response response = new Response(400, "Invalid email Address");
            return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED );
        }

        // Validate the reservation days are lesser or equal to 3, and reservation are less than a month in advance.
        LocalDate arrivalDate = reservation.getArrivalDate().toLocalDate();
        Period maximumReservationDays = Period.between(reservation.getArrivalDate().toLocalDate(), reservation.getDepartureDate().toLocalDate());
        Period advanceBooking = Period.between(arrivalDate, arrivalDate.plusMonths(1));
        if (maximumReservationDays.getDays() > 3 || ((advanceBooking.getMonths() == 1 || advanceBooking.getMonths() > 1) && advanceBooking.getDays() >0)) {
            Response response = new Response(200, "Reservation cannot be made for more than 3days");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }

        ResponseEntity responseEntity = searchService.findAvailablity(reservation);
        if (responseEntity != null) {
            return responseEntity;
        }

        return reservationService.modifyReservation(reservation);
    }

    @RequestMapping(value = "/reservations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> viewReservations(@PathVariable("id") int bookingId) {

        List<Reservation> reservations =reservationService.viewReservation(bookingId);

        if (reservations.isEmpty()) {
            Response response = new Response(404, "Unable to find the booking ID");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Reservation>(reservations.get(0), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/reservations/{id}/delete",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteReservations(@PathVariable("id") int bookingId) {
        return  reservationService.deleteReservation(bookingId);
    }

}
