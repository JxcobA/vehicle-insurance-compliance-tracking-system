package ModelClassTests;

import com.insurance.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleTests {

    // Test getter methods()
    @Test
    void shouldStoreValuesOnConstruction() {
        // Create Vehicle object using constructor
        Vehicle vehicle = new Vehicle("TEST1", "Toyota", "Aygo");
        // Test all getters
        assertEquals("TEST1", vehicle.getRegNumber());
        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Aygo", vehicle.getModel());
    }

    // Test setMake()
    @Test
    void shouldUpdateMakeUsingSetter() {
        Vehicle vehicle = new Vehicle("TEST1", "Toyota", "Aygo");
        vehicle.setMake("Honda");
        assertEquals("Honda", vehicle.getMake());
    }

    // Test setModel()
    @Test
    void shouldUpdateModelUsingSetter() {
        Vehicle vehicle = new Vehicle("TEST1", "Toyota", "Aygo");
        vehicle.setModel("Civic");
        assertEquals("Civic", vehicle.getModel());
    }

    // Test toString()
    @Test
    void shouldReturnFormattedToString() {
        Vehicle vehicle = new Vehicle("TEST1", "Toyota", "Aygo");
        assertEquals("Vehicle: Toyota Aygo\nRegistration: TEST1", vehicle.toString());
    }

}
