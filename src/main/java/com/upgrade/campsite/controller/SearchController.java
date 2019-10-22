package com.upgrade.campsite.controller;

import com.upgrade.campsite.model.Availability;
import com.upgrade.campsite.model.CampsiteSearch;
import com.upgrade.campsite.model.Response;
import com.upgrade.campsite.services.SearchService;
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
public class SearchController {


    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/campsitesearch",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchCampsites(@RequestBody(required = false) CampsiteSearch campsiteSearch) throws ParseException {
        Period period = Period.between(campsiteSearch.getFromRange(), campsiteSearch.getToRange());
        if ((period.getMonths() == 1 || period.getMonths() > 1) && period.getDays() >0) {
            Response response = new Response(200, "Input Range is greater than a month");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }

        LocalDate fromRange = campsiteSearch.getFromRange() != null ? campsiteSearch.getFromRange() : LocalDate.now();
        LocalDate toRange = campsiteSearch.getToRange() != null ? campsiteSearch.getToRange() : fromRange.plusMonths(1);
        campsiteSearch.setFromRange(fromRange);
        campsiteSearch.setToRange(toRange);

        List<String> availableDates = searchService.searchCampsites(campsiteSearch);
        if (availableDates.isEmpty()) {
            Response response = new Response(200, "No Available Dates are found");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Availability>(new Availability(availableDates), org.springframework.http.HttpStatus.OK);

    }
}
