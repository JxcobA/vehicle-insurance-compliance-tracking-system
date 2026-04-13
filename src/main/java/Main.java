import com.insurance.dao.VehicleDAO;
import com.insurance.model.Vehicle;
import com.insurance.model.ViolationRecord;
import com.insurance.service.ComplianceService;
import com.insurance.service.S3UploadService;
import com.insurance.service.SnsNotificationService;
import com.insurance.service.SqsPublisherService;
import com.insurance.setup.Databaseinitialiser;
import com.insurance.util.CsvReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Vehicle Insurance Compliance System...");

        // Step 1: Initialise database
        Databaseinitialiser.initialiseDatabase();

        // Step 2: Create service and DAO instances
        VehicleDAO vehicleDAO = new VehicleDAO();
        ComplianceService complianceService = new ComplianceService();
        CsvReportGenerator csvGenerator = new CsvReportGenerator();

        S3UploadService s3Service = new S3UploadService();
        SnsNotificationService snsService = new SnsNotificationService();
        SqsPublisherService sqsService = new SqsPublisherService();

        List<ViolationRecord> violations = new ArrayList<>();

        try {
            // Step 3: Retrieve all vehicles
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();

            System.out.println("Total vehicles found: " + vehicles.size());

            // Step 4: Check each vehicle for violations
            for (Vehicle vehicle : vehicles) {
                ViolationRecord record =
                        complianceService.getViolationRecord(vehicle.getRegNumber());

                if (record != null) {
                    System.out.println("Violation found for: " + vehicle.getRegNumber());
                    violations.add(record);
                }
            }

            System.out.println("Total violations: " + violations.size());

            // Step 5: Generate CSV report
            String csvFilePath = csvGenerator.generateViolationReport(violations);

            // Step 6: Upload CSV to S3
            String s3Key = s3Service.upload(csvFilePath);

            // Step 7: Publish summary to SNS
            snsService.sendSummary(vehicles.size(), violations.size(), s3Key);

            // Step 8: Send individual violation messages to SQS
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

