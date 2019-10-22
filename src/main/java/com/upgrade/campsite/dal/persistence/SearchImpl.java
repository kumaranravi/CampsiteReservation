package com.upgrade.campsite.dal.persistence;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.model.CampsiteSearch;
import com.upgrade.campsite.utils.Utility;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

@Component
@EnableAutoConfiguration
public class SearchImpl {
    private static final Logger LOGGER = Logger.getLogger(SearchImpl.class.getName());


    public List<String> searchAvailableCampsites(CampsiteSearch campsiteSearch) throws ParseException {

        LocalDate fromRange = campsiteSearch.getFromRange();
        LocalDate toRange = campsiteSearch.getToRange();

        List<String> availableDates = new ArrayList<String>();
        LocalDate localDate = fromRange;
        //Make a list of dates from the given range
        while (localDate.isBefore(toRange)) {
            availableDates.add(localDate.toString());
            localDate = localDate.plusDays(1);
        }
        availableDates.add(localDate.toString());

        List<Reservation> bookedReservation = getBookedReservations(fromRange, toRange);

        //Remove the booked ones from our given range
        for (Reservation reservation : bookedReservation) {
            Period period = Period.between(reservation.getArrivalDate().toLocalDate(), reservation.getDepartureDate().toLocalDate());
            int reservedDays = period.getDays();
            LocalDate startDate = reservation.getArrivalDate().toLocalDate();
            availableDates.remove(startDate.toString());
            for (int i = 1; i < reservedDays ; i ++ ) {

                startDate = startDate.plusDays(1);
                LocalDate dayInTrip = startDate;
                availableDates.remove(dayInTrip.toString());
            }
        }

        return availableDates;
    }

    private List<Reservation> getBookedReservations(LocalDate fromRange, LocalDate toRange) {
        Session session = Utility.getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<Reservation> reservations = session.createQuery("FROM Reservation where" +
                    "(arrivalDate >= :fromRange and arrivalDate <=:toRange) or " +
                    "(departureDate>= :fromRange and departureDate<= :toRange)")
                    .setParameter("fromRange",java.sql.Date.valueOf(fromRange))
                    .setParameter("toRange",java.sql.Date.valueOf(toRange))
                    .list();
        session.close();
        return reservations;
    }

}
