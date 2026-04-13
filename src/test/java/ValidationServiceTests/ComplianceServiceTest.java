package ValidationServiceTests;

import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.dao.VehicleDAO;
import com.insurance.model.InsurancePolicy;
import com.insurance.model.Trips;
import com.insurance.model.ViolationRecord;
import com.insurance.service.ComplianceService;
import com.insurance.setup.Databaseinitialiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ComplianceServiceTest {

    private InsurancePolicyDAO policyDAO;
    private TripsDAO tripsDAO;
    private ComplianceService complianceService;
    private VehicleDAO vehicleDAO;

    private final String regNumber = "RGF3 YNX";
    private final LocalDate expiry = LocalDate.of(2026, 6, 1);

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


}
