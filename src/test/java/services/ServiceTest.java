package services;

import com.upgrade.campsite.dal.entities.Reservation;
import com.upgrade.campsite.dal.persistence.ReservationImpl;
import com.upgrade.campsite.dal.persistence.SearchImpl;
import com.upgrade.campsite.model.CampsiteSearch;
import com.upgrade.campsite.model.Response;
import com.upgrade.campsite.services.ReservationService;
import com.upgrade.campsite.services.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ServiceTest {
    @TestConfiguration
    static class ServiceTestContextConfiguration {

        @Bean
        public ReservationService reservationService() {
            return new ReservationService();
        }

        @Bean
        public SearchService searchService() {
            return new SearchService();
        }
    }

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SearchService searchService;

    @MockBean
    private ReservationImpl reservationImpl;

    @MockBean
    private SearchImpl searchImpl;

    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
    Date arrivalDate;
    Date departureDate;

    @Before
    public void setUp() throws ParseException {
        arrivalDate =  format.parse("2019-10-20");
        departureDate = format.parse("2019-10-23");
    }

    @Test
    public void whenReservationDetailsThenReservationShouldBeCreated() {
        Reservation reservation = new Reservation(null, "rav", "kumaran@mail.com", new java.sql.Date(arrivalDate.getDate()), new java.sql.Date(departureDate.getDate()));
        Mockito.when(reservationService.bookReservation(reservation))
                .thenReturn(String.valueOf(123));

        String bookingId = reservationService.bookReservation(reservation);

        assertEquals("123", bookingId);
    }

    @Test
    public void whenModifyReservationDetailsThenReservationShouldBeModified() throws ParseException {

        Response response = new Response(201, "Reservation Modified successfully");
        ResponseEntity responseEntity = new ResponseEntity<Response>(response, HttpStatus.CREATED);
        Reservation reservation = new Reservation(12, "rav", "kumaran@mail.com", new java.sql.Date(arrivalDate.getDate()), new java.sql.Date(departureDate.getDate()));

        Mockito.when(reservationService.modifyReservation(reservation))
                .thenReturn(responseEntity);

        ResponseEntity responseEntity1 = reservationService.modifyReservation(reservation);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void whenDeleteReservationThenReservationShouldBeDeleted() throws ParseException {
        Response deleteresponse = new Response(200, "Reservation Modified successfully");
        ResponseEntity entity = new ResponseEntity<Response>(deleteresponse, HttpStatus.OK);

        Mockito.when(reservationService.deleteReservation(3))
                .thenReturn(entity);

        ResponseEntity responseEntity = reservationService.deleteReservation(3);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void whenSearchReservationThenReservationDateShouldBeReturned() throws ParseException {

        CampsiteSearch campsiteSearch = new CampsiteSearch(LocalDate.now(), LocalDate.now().plusMonths(1));
        Mockito.when(searchService.searchCampsites(campsiteSearch))
                .thenReturn(Arrays.asList("2019-10-29", "2019-10-30"));

        List<String> availabilities = searchService.searchCampsites(campsiteSearch);
        assertTrue(availabilities.contains("2019-10-29"));
    }

}