package com.upgrade.campsite.dal.persistence;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.model.Response;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@EnableAutoConfiguration
public class ReservationImpl {

    private static final Logger LOGGER = Logger.getLogger(ReservationImpl.class.getName());
    private static final String BOOKING_ID_NOT_FOUND = "Booking Id not found";

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
        return sessionFactory;
    }

    public String createCampsiteBooking(Reservation reservation) {
        return createBooking(reservation).toString();
    }

    public ResponseEntity<?> modifyCampsiteBooking(Reservation reservation) {
        ResponseEntity<?> responseEntity = findReservation(reservation);
        if (responseEntity != null) return responseEntity;

        return modifyBooking(reservation);
    }

    public List<Reservation> viewCampsiteBooking(int bookingId) {
        return getReservation(bookingId);
    }

    public ResponseEntity<?> deleteCampsiteBooking(int bookingId) {
        ResponseEntity<?> responseEntity = findReservation(new Reservation(bookingId, null, null, null, null));
        if (responseEntity != null) return responseEntity;

        return deleteBooking(bookingId);
    }

    private List<Reservation> getReservation(int bookingId) {
        Session session = getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<Reservation> reservations = session.createQuery("FROM Reservation where bookingid=:bookingid")
                .setParameter("bookingid",bookingId).list();
        session.close();
        return reservations;
    }

    private ResponseEntity<?> findReservation(Reservation reservation) {
        Response response = new Response();
        List<Reservation> reservations = getReservation(reservation.getBookingId());

        if (reservations.isEmpty()) {
            response.setMessage(BOOKING_ID_NOT_FOUND);
            response.setStatuscode(404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
        return null;
    }

    public Integer createBooking(Reservation reservation) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(reservation);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + reservation.toString());
        return reservation.getBookingId();
    }

    private ResponseEntity<?> deleteBooking(int bookingId) {
        Session session = getSessionFactory().openSession();
        Response response = new Response();
        ResponseEntity responseEntity = null;
        try{
            session.beginTransaction();
            Reservation reservation = (Reservation) session.load(Reservation.class, bookingId);
            session.delete(reservation);
            session.getTransaction().commit();

            responseEntity = generateResponseMessage(response, "Reservation deleted successfully",
                    201, HttpStatus.CREATED);

        } catch (Exception error) {
            responseEntity = new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
            LOGGER.log(Level.WARNING,  "Unexpected error occured, unable to delete "+ String.valueOf(bookingId) , error);
        } finally {
            session.close();
        }
        LOGGER.log(Level.INFO, String.valueOf(bookingId) + "successfully deleted");
        return responseEntity;
    }


    private ResponseEntity<?> modifyBooking(Reservation reservation) throws ObjectNotFoundException {
        boolean updateStatus = false;
        Response response = new Response();
        Session session = getSessionFactory().openSession();
        ResponseEntity responseEntity = null;

        try {
            session.beginTransaction();
            Reservation dbreservation = (Reservation) session.load(Reservation.class, reservation.getBookingId());

            if (dbreservation != null && dbreservation.getBookingId() != null) {
                updateStatus = updateReservationDetails(reservation, updateStatus, dbreservation);
                session.getTransaction().commit();
                responseEntity = generateResponseMessage(response, "Reservation modified successfully",
                        201, HttpStatus.CREATED);
            }
        } catch (Exception error) {
            responseEntity = new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
            LOGGER.log(Level.WARNING,  reservation.getBookingId().toString() + BOOKING_ID_NOT_FOUND, error);
        }
        finally {
            session.close();
        }

        if (!updateStatus) {
            responseEntity = generateResponseMessage(response, "Unable to modify the reservation, please check the request",
                    400, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    private boolean updateReservationDetails(Reservation reservation, boolean updateStatus, Reservation dbreservation) {
        if (reservation.getUsername() != null) {
            dbreservation.setUsername(reservation.getUsername());
            updateStatus = true;
        }
        if (reservation.getEmail() != null) {
            dbreservation.setEmail(reservation.getEmail());
            updateStatus = true;
        }
        if (reservation.getArrivalDate() != null) {
            dbreservation.setArrivalDate(reservation.getArrivalDate());
            updateStatus = true;
        }
        if (reservation.getDepartureDate() != null) {
            dbreservation.setDepartureDate(reservation.getDepartureDate());
            updateStatus = true;
        }
        return updateStatus;
    }


    private ResponseEntity generateResponseMessage(Response response, String s, int i, HttpStatus created) {
        ResponseEntity responseEntity;
        response.setMessage(s);
        response.setStatuscode(i);
        responseEntity = new ResponseEntity<Response>(response, created);
        return responseEntity;
    }
}
