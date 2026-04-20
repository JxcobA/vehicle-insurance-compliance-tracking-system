package ModelClassTests;

import com.insurance.model.ViolationRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViolationRecordTests {

    @Test
    void shouldStoreValuesOnConstruction() {
        // Figure out what reason parameter is below
        ViolationRecord vr = new ViolationRecord("TEST1", LocalDate.of(2027, 1, 1), LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 12, 0), "Vehicle used after insurance expiry date");

        assertEquals("TEST1", vr.getRegNumber());
        assertEquals(LocalDate.of(2027, 1, 1), vr.getExpiryDate());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), vr.getLastTripStart());
        assertEquals(LocalDateTime.of(2026, 1, 1, 12, 0), vr.getLastTripEnd());
        assertEquals("Vehicle used after insurance expiry date", vr.getReason());
    }



}
