package com.upgrade.campsite.services;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.dal.persistence.SearchImpl;
import com.upgrade.campsite.model.CampsiteSearch;
import com.upgrade.campsite.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SearchService {

    private static final Logger LOGGER = Logger.getLogger(SearchService.class.getName());

    @Autowired
    SearchImpl searchImpl;

    public List<String> searchCampsites(CampsiteSearch campsiteSearch) throws ParseException {
        return searchImpl.searchAvailableCampsites(campsiteSearch);
    }

    public ResponseEntity<?> findAvailablity(Reservation reservation) throws ParseException {
        LocalDate fromRange = reservation.getArrivalDate().toLocalDate();
        LocalDate toRange = reservation.getDepartureDate().toLocalDate();
        List<String> availableDates = searchCampsites(new CampsiteSearch(fromRange, toRange));
        Period period = Period.between(reservation.getArrivalDate().toLocalDate(), reservation.getDepartureDate().toLocalDate());
        int reservedDays = period.getDays();
        for (int i = 0; i <= reservedDays ; i++) {
            LocalDate localDate = fromRange.plusDays(i);
            if (!availableDates.contains(localDate.toString())){
                Response response = new Response(400, "Date Range is unavailable");
                return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED );
            }
        }
        return null;
    }


}
