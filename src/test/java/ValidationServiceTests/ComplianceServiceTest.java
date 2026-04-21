package ValidationServiceTests;

import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.dao.VehicleDAO;
import com.insurance.model.InsurancePolicy;
import com.insurance.model.Trips;
import com.insurance.model.Vehicle;
import com.insurance.model.ViolationRecord;
import com.insurance.service.ComplianceService;
import com.insurance.setup.Databaseinitialiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComplianceServiceTest {

    private InsurancePolicyDAO policyDAO;
    private TripsDAO tripsDAO;
    private ComplianceService complianceService;
    private VehicleDAO vehicleDAO;


    @BeforeEach
    void setUp() {
        vehicleDAO = new VehicleDAO();
        policyDAO = new InsurancePolicyDAO();
        tripsDAO = new TripsDAO();
        complianceService = new ComplianceService();

        vehicleDAO.insertVehicle("TEST1", "Toyota", "Aygo");
    }

    @AfterEach
    void tearDown() {
        Databaseinitialiser.resetData();
    }

    @Test
    void shouldDetectViolation() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2024,1,1), LocalDate.of(2025,1,1), "COMPREHENSIVE", true
        ));

        Trips trip = tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025,3,1,10,0), "Reading");
        assertNotNull(trip, "Trip insert failed"); // tells you if the trip is the problem

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNotNull(result);
    }

    // When nothing in DB should return null
    @Test
    void shouldReturnNullIfNoActivePolicy() {
        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNull(result);
    }

    // Tests if policy exists but has not yet expired, it should return null
    @Test
    void shouldReturnNullIfPolicyNotExpired() throws SQLException {
        // Create a new policy to test
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", // Registration
                LocalDate.of(2026, 1, 1), // Issue date
                LocalDate.of(2027, 1, 1), // Expiry date
                "COMPREHENSIVE", // Policy type
                true // Is policy active
        ));

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNull(result); // Check result matches what is expected: Policy is not expired, so no violation should be recorded
    }

    // Tests that policy has expired, but no trip has started - return null
    @Test
    void shouldReturnNullIfPolicyHasExpiredButNoTripStarts() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", // Registration
                LocalDate.of(2024, 1, 1), // Issue date
                LocalDate.of(2025, 1, 1), // Expiry date
                "COMPREHENSIVE", // Policy type
                true // Is policy active
        ));
        // No trips are provided, so there are no trip starts recorded
        // @After each calls resetData() which clears trip data, and no trips are inserted in @BeforeEach, so there is no trip data recorded in the DB

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNull(result); // Check result matches what is expected: Policy has expired, but no trip starts are recorded
    }


    @Test
    void shouldDetectViolationWhenTripStartsAfterExpiryWithNoEnd() throws SQLException {
        // This test needs trip start and expired policy data
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", // Registration
                LocalDate.of(2024, 1, 1), // Issue date
                LocalDate.of(2025, 1, 1), // Expiry date
                "COMPREHENSIVE", // Policy type
                true // Is policy active
        ));

        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 3, 1, 10, 0), "Reading");

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNotNull(result); // Checks that the violation is detected
        // Checks that the correct data from the DB is tested:
        assertEquals("TEST1", result.getRegNumber());
        assertEquals(LocalDate.of(2025, 1, 1), result.getExpiryDate());

    }


    @Test
    void shouldDetectViolationWhenLastTripStartIsAfterLastTripEndAfterExpiry() throws SQLException {
        // This test needs policy, trip start and end data
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", // Registration
                LocalDate.of(2023, 1, 1), // Issue date
                LocalDate.of(2024, 1, 1), // Expiry date
                "COMPREHENSIVE", // Policy type
                true // Is policy active
        ));

        // Completed trip - Both start and end before expiry
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2023, 4, 4, 10, 0), "Reading");
        tripsDAO.insertTrip("TEST1", "TRIP_END", LocalDateTime.of(2023, 4, 4, 14, 0), "Reading");
        // Trip start after expiry with no trip end
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 3, 1, 12, 0), "Reading");

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNotNull(result); // Checks that the violation is detected
    }

    // Negative test - confirms compliant behaviour isn't flagged incorrectly
    @Test
    void shouldReturnNullWhenLastTripEndedBeforeExpiry() throws SQLException {
        // This test needs policy, trip start and end data
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", // Registration
                LocalDate.of(2024, 1, 1), // Issue date
                LocalDate.of(2025, 1, 1), // Expiry date
                "COMPREHENSIVE", // Policy type
                true // Is policy active
        ));

        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2024, 6, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END", LocalDateTime.of(2024, 6, 1, 12, 0), "Bath");

        ViolationRecord result = complianceService.getViolationRecord("TEST1");
        assertNull(result);
    }

    @Test
    void shouldReturnAllVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
    }

    @Test
    void shouldDeleteVehicleByReg() {
        Vehicle vehicle = vehicleDAO.deleteByReg("TEST1");
        assertNotNull(vehicle);
        assertNull(vehicleDAO.getVehicleByReg("TEST1"));
    }

    @Test
    void shouldReturnNullWhenDeletingNonExistentVehicle() {
        Vehicle vehicle = vehicleDAO.deleteByReg("NONEXISTENT");
        assertNull(vehicle);
    }


    @Test
    void isVehicleViolating_shouldReturnFalseIfNoPolicy() {
        assertFalse(complianceService.isVehicleViolating("TEST1"));
    }

    @Test
    void isVehicleViolating_shouldReturnFalseIfPolicyNotExpired() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), "COMPREHENSIVE", true
        ));
        assertFalse(complianceService.isVehicleViolating("TEST1"));
    }

    @Test
    void isVehicleViolating_shouldReturnFalseIfExpiredButNoTrips() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true
        ));
        assertFalse(complianceService.isVehicleViolating("TEST1"));
    }

    @Test
    void isVehicleViolating_shouldReturnTrueForViolation() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true
        ));
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 3, 1, 10, 0), "Reading");
        assertTrue(complianceService.isVehicleViolating("TEST1"));
    }

    @Test
    void isVehicleViolating_shouldReturnTrueWhenStartAfterEndAfterExpiry() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), "COMPREHENSIVE", true
        ));
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2023, 6, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2023, 6, 1, 12, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2025, 1, 5, 10, 0), "Reading");
        assertTrue(complianceService.isVehicleViolating("TEST1"));
    }

    @Test
    void isVehicleViolating_shouldReturnFalseWhenTripEndedBeforeExpiry() throws SQLException {
        policyDAO.createPolicy(new InsurancePolicy(
                "TEST1", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "COMPREHENSIVE", true
        ));
        tripsDAO.insertTrip("TEST1", "TRIP_START", LocalDateTime.of(2024, 6, 1, 9, 0), "Bath");
        tripsDAO.insertTrip("TEST1", "TRIP_END",   LocalDateTime.of(2024, 6, 1, 12, 0), "Bath");
        assertFalse(complianceService.isVehicleViolating("TEST1"));
    }

}
