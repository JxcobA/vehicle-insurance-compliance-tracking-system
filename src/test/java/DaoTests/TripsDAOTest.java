package DaoTests;

import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.dao.VehicleDAO;
import com.insurance.model.Trips;
import com.insurance.setup.Databaseinitialiser;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TripsDAOTest {

    private TripsDAO tripsDAO;
    private VehicleDAO vehicleDAO;
    private InsurancePolicyDAO policyDAO;

    @BeforeEach
    void setUp() throws SQLException {
        vehicleDAO = new VehicleDAO();
        tripsDAO = new TripsDAO();
        policyDAO = new InsurancePolicyDAO();
        vehicleDAO.insertVehicle("TEST1", "Toyota", "Aygo");
    }

    @AfterEach
    void tearDown() {
        Databaseinitialiser.resetData();
    }

    @Test
    void getTripsOfVehicle_shouldReturnEmptyListWhenNoTrips() {
        assertTrue(tripsDAO.getTripsOfVehicle("TEST1").isEmpty());
    }

    @Test
    void getTripsOfVehicle_shouldReturnTripsInAscendingOrder() {
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2025, 1, 1, 11, 0), "Bath");
        var trips = tripsDAO.getTripsOfVehicle("TEST1");
        assertEquals(2, trips.size());
        assertTrue(trips.get(0).getEvent_timestamp().isBefore(trips.get(1).getEvent_timestamp()));
    }

    @Test
    void getLastTripEvent_shouldReturnNullWhenNoTrips() {
        assertNull(tripsDAO.getLastTripEvent("TEST1"));
    }

    @Test
    void getLastTripEvent_shouldReturnMostRecentEvent() {
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2025, 1, 1, 11, 0), "Bath");
        Trips last = tripsDAO.getLastTripEvent("TEST1");
        assertNotNull(last);
        assertEquals("TRIP_END", last.getEvent_type());
    }

    @Test
    void getLastTripStart_shouldReturnNullWhenNoTrips() {
        assertNull(tripsDAO.getLastTripStart("TEST1"));
    }

    @Test
    void getLastTripStart_shouldReturnLatestStart() {
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2025, 1, 1, 11, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 2, 1, 9, 0), "Reading");
        Trips last = tripsDAO.getLastTripStart("TEST1");
        assertNotNull(last);
        assertEquals(LocalDateTime.of(2025, 2, 1, 9, 0), last.getEvent_timestamp());
    }

    @Test
    void getLastTripEnd_shouldReturnNullWhenNoTripEnds() {
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        assertNull(tripsDAO.getLastTripEnd("TEST1"));
    }

    @Test
    void getLastTripEnd_shouldReturnLatestEnd() {
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2025, 1, 1, 11, 0), "Bath");
        Trips last = tripsDAO.getLastTripEnd("TEST1");
        assertNotNull(last);
        assertEquals("TRIP_END", last.getEvent_type());
    }

    @Test
    void deleteTripById_shouldReturnTrueWhenDeleted() {
        Trips trip = tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 9, 0), "Bath");
        assertNotNull(trip);
        assertTrue(tripsDAO.deleteTripById(trip.getId()));
    }

    @Test
    void deleteTripById_shouldReturnFalseWhenNotFound() {
        assertFalse(tripsDAO.deleteTripById(99999));
    }
}