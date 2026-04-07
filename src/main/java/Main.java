import com.insurance.dao.VehicleDAO;
import com.insurance.model.Vehicle;
import com.insurance.model.ViolationRecord;
import com.insurance.service.ComplianceService;
import com.insurance.util.CsvReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        VehicleDAO vehicleDAO = new VehicleDAO();
        ComplianceService complianceService = new ComplianceService();
        CsvReportGenerator csv = new CsvReportGenerator();

        List<ViolationRecord> violations = new ArrayList<>();

        for (Vehicle v : vehicleDAO.getAllVehicles()) {
            ViolationRecord record = complianceService.getViolationRecord(v.getRegNumber());

            if (record != null) {
                violations.add(record);
            }
        }

        csv.generateViolationReport(violations);
    }
}