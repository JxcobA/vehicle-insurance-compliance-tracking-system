package ModelClassTests;

import com.insurance.model.TripValidationRequest;
import com.insurance.model.Trips;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripValidationRequestTests {

    @Test
    void shouldStoreValuesOnConstruction() {
        Trips trips = new Trips(1, "TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0), "Reading");

        TripValidationRequest tvr = new TripValidationRequest(trips, "TRIP_START", LocalDateTime.of(2025, 1, 1, 10, 0));

        assertEquals(trips, tvr.getLastEvent());
        assertEquals("TRIP_START", tvr.getNewEventType());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), tvr.getNewTimestamp());
    }

}
