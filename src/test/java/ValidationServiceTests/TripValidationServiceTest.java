package ValidationServiceTests;

import com.insurance.exceptions.TripSequenceException;
import com.insurance.model.Trips;
import com.insurance.service.TripValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripValidationServiceTest {

    private TripValidationService validator;
    private final LocalDateTime baseTime = LocalDateTime.of(2026, 1, 15, 9, 0, 0);

    @BeforeEach
    void setUp() {
        validator = new TripValidationService();
    }

    @Test
    void shouldPassForFirstTripStart() {
        assertDoesNotThrow(() ->
                validator.validateTrips(null, "TRIP_START", baseTime));
    }

    @Test
    void shouldThrowWhenEndingWithNoHistory() {
        assertThrows(TripSequenceException.class, () ->
                validator.validateTrips(null, "TRIP_END", baseTime));
    }

    @Test
    void shouldThrowOnConsecutiveTripEnds() {
        Trips lastEvent = new Trips(1, "RGF3 YNX", "TRIP_END", baseTime, "Oxford");

        assertThrows(TripSequenceException.class, () ->
                validator.validateTrips(lastEvent, "TRIP_END", baseTime.plusHours(2)));
    }

    @Test
    void shouldThrowWhenTimestampIsBeforeLastEvent() {
        Trips lastEvent = new Trips(1, "RGF3 YNX", "TRIP_END", baseTime, "Basingstoke");

        assertThrows(TripSequenceException.class, () ->
                validator.validateTrips(lastEvent, "TRIP_END", baseTime.minusHours(1)));
    }


    @Test
    void shouldPassForValidStartAfterEnd() {
        Trips lastEvent = new Trips(1, "RGF3 YNX", "TRIP_END", baseTime, "Southampton");

        assertDoesNotThrow(() -> validator.validateTrips(lastEvent, "TRIP_START", baseTime.plusHours(1)));
    }

}
