import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.model.InsurancePolicy;
import com.insurance.model.Trips;
import com.insurance.model.Vehicle;
import com.insurance.model.ViolationRecord;
import com.insurance.service.ComplianceService;
import com.insurance.service.S3UploadService;
import com.insurance.service.SnsNotificationService;
import com.insurance.service.SqsPublisherService;
import com.insurance.setup.Databaseinitialiser;
import com.insurance.dao.VehicleDAO;
import com.insurance.exceptions.TripSequenceException;
import com.insurance.util.CsvReportGenerator;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Vehicle Insurance Compliance System...");

        // Initialise database
        Databaseinitialiser.initialiseDatabase();

        // Create DAO instances
        VehicleDAO vehicleDAO = new VehicleDAO();
        InsurancePolicyDAO policyDAO = new InsurancePolicyDAO();
        TripsDAO tripsDAO = new TripsDAO();

        // Create service instances
        ComplianceService complianceService = new ComplianceService();
        CsvReportGenerator csvGenerator = new CsvReportGenerator();

        S3UploadService s3Service = new S3UploadService();
        SnsNotificationService snsService = new SnsNotificationService();
        SqsPublisherService sqsService = new SqsPublisherService();


        // Reset method - Clears data from DB
        // Databaseinitialiser.resetData();


        // Vehicles Section:
        // Contains CRUD operations
        System.out.println("Vehicle CRUD Operations:");

        // Create:
        try {
            Vehicle inserted = vehicleDAO.insertVehicle("RGF3 YNX", "Toyota", "Aygo");
            if (inserted != null) System.out.println("Inserted: " + inserted);
        } catch (Exception e) {
            System.out.println("Vehicle insert failed: " + e.getMessage());
        }

        // Read:
        try {
            Vehicle JacobsCar = vehicleDAO.getVehicleByReg("RGF3 YNX");
            if (JacobsCar != null) {
                System.out.println(JacobsCar);
            } else {
                System.out.println("Vehicle not found.");
            }
        } catch (Exception e) {
            System.out.println("Vehicle fetch failed: " + e.getMessage());
        }

        // Delete:
        // vehicleDAO.deleteByReg("RGF3 YNX");

        System.out.println("Vehicle CRUD Operations End\n");




        // Insurance Section:
        // Contains CRUD operations
        System.out.println("Insurance CRUD Operations:");

        // Create:
        // Constraints prevents duplicate policies, so this is wrapped in try/catch so Main can be re-run
        try {
            InsurancePolicy policy1 = new InsurancePolicy("RGF3 YNX", LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), "COMPREHENSIVE", true);
            policyDAO.createPolicy(policy1);
            System.out.println("Policy created with ID: " + policy1.getId());
        } catch (Exception e) {
            System.out.println("Policy creation skipped: " + e.getMessage());
        }

        // Read + Update:
        try {
            InsurancePolicy activePolicy = policyDAO.getActivePolicy("RGF3 YNX");
            if (activePolicy != null) {
                System.out.println("Policy expires on: " + activePolicy.getExpiryDate());

                // Update: deactivate the policy
                policyDAO.deactivatePolicy(activePolicy.getId());
                System.out.println("Deactivated policy ID: " + activePolicy.getId());
            } else {
                System.out.println("No active policy found.");
            }
        } catch (Exception e) {
            System.out.println("Policy operation failed: " + e.getMessage());
        }

        System.out.println("Insurance CRUD Operations End\n");



        // Trips Section:
        // Contains CRUD Operations
        System.out.println("Trips CRUD Operations:");

        // Create trip start:
        try {
            Trips lastEvent = tripsDAO.getLastTripEvent("RGF3 YNX");
            if (lastEvent == null || lastEvent.getEvent_type().equals("TRIP_END")) {
                Trips tripStart = tripsDAO.insertTrip("RGF3 YNX", "TRIP_START", LocalDateTime.of(2025, 7, 3, 14, 15), "Reading");
                System.out.println("Recorded: " + tripStart.getEvent_type() + " at " + tripStart.getLocation() + " - " + tripStart.getEvent_timestamp());
            } else {
                System.out.println("Trip start skipped: a trip is already in progress.");
            }
        } catch (TripSequenceException e) {
            System.out.println("Trip start skipped: " + e.getMessage());
        }

        // Create trip end:
        try {
            Trips lastEvent = tripsDAO.getLastTripEvent("RGF3 YNX");
            if (lastEvent != null && lastEvent.getEvent_type().equals("TRIP_START")) {
                Trips tripEnd = tripsDAO.insertTrip("RGF3 YNX", "TRIP_END", LocalDateTime.of(2025, 7, 3, 18, 0), "Reading");
                System.out.println("Recorded: " + tripEnd.getEvent_type() + " at " + tripEnd.getLocation() + " - " + tripEnd.getEvent_timestamp());
            } else {
                System.out.println("Trip end skipped: no active trip to end.");
            }
        } catch (TripSequenceException e) {
            System.out.println("Trip end skipped: " + e.getMessage());
        }

        // Read:
        try {
            List<Trips> trips = tripsDAO.getTripsOfVehicle("RGF3 YNX");
            System.out.println("TRIP:");
            // Loop through the list, calling sout for each trip to display details:
            for (Trips trip : trips) {
                System.out.println(trip.getEvent_type() + " at " + trip.getLocation() + " - " + trip.getEvent_timestamp());
            }
        } catch (Exception e) {
            System.out.println("Trip fetch failed: " + e.getMessage());
        }


        // Delete:
        try {
            boolean deleted = tripsDAO.deleteTripById(1);
            System.out.println("Deleted trip: " + deleted);
        } catch (Exception e) {
            System.out.println("Trip delete failed: " + e.getMessage());
        }

        System.out.println("Trips CRUD Operations Ends\n");




        // Vehicle Violation Section
        // Contains vehicle violation checking logic
        List<ViolationRecord> violations = new ArrayList<>();

        try {
            // Retrieve all vehicles as a list
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();

            System.out.println("Total vehicles found: " + vehicles.size());

            // Check each vehicle for violations
            for (Vehicle vehicle : vehicles) {
                ViolationRecord record =
                        complianceService.getViolationRecord(vehicle.getRegNumber());

                if (record != null) {
                    System.out.println("Violation found for: " + vehicle.getRegNumber());
                    violations.add(record);
                }
            }

            System.out.println("Total violations: " + violations.size());

            // CSV Report Section:
            // Contains CSV report generation
            String csvFilePath = csvGenerator.generateViolationReport(violations);

            // Upload CSV to S3
            String s3Key = s3Service.upload(csvFilePath);

            // Publish summary to SNS
            snsService.sendSummary(vehicles.size(), violations.size(), s3Key);

            // Send individual violation messages to SQS
            for (ViolationRecord violation : violations) {
                sqsService.sendMessage(violation);
            }

            System.out.println("Compliance job completed successfully.");

        } catch (Exception e) {
            System.out.println("Error running compliance job: " + e.getMessage());
            e.printStackTrace();
        }


    }
}