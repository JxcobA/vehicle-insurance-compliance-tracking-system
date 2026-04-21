package SetupTests;

import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.dao.VehicleDAO;
import com.insurance.service.ComplianceService;
import com.insurance.setup.Databaseinitialiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseInitialiserTests {

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
    void shouldInitialiseDatabaseWithoutError() {
        assertDoesNotThrow(() -> Databaseinitialiser.initialiseDatabase());
    }

    @Test
    void shouldResetDataWithoutError() {
        assertDoesNotThrow(() -> Databaseinitialiser.resetData());
    }

    @Test
    void vehicleTableShouldBeEmptyAfterReset() {
        Databaseinitialiser.resetData();
        assertTrue(vehicleDAO.getAllVehicles().isEmpty());
    }
}
