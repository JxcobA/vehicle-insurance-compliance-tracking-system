package ModelClassTests;

import com.insurance.model.Trips;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripsTests {

    @Test
    void shouldStoreValuesOnConstruction() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");

        assertEquals(1, trips.getId());
        assertEquals("TEST1", trips.getRegNumber());
        assertEquals("TRIP_START", trips.getEvent_type());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), trips.getEvent_timestamp());
        assertEquals("Reading", trips.getLocation());
    }

    @Test
    void shouldUpdateRegNumberUsingSetter() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");
        trips.setRegNumber("TEST2");
        assertEquals("TEST2", trips.getRegNumber());
    }

    @Test
    void shouldUpdateEventTypeUsingSetter() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");
        trips.setEvent_type("TRIP_END");
        assertEquals("TRIP_END", trips.getEvent_type());
    }

    @Test
    void shouldUpdateEventTimestampUsingSetter() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");
        trips.setEvent_timestamp(LocalDateTime.of(2025, 1, 1, 12, 30));
        assertEquals(LocalDateTime.of(2025, 1, 1, 12, 30), trips.getEvent_timestamp());
    }

    @Test
    void shouldUpdateTripLocation() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");
        trips.setLocation("Coventry");
        assertEquals("Coventry", trips.getLocation());
    }


}
