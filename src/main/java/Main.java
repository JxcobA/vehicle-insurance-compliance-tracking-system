import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.model.InsurancePolicy;
import com.insurance.model.Vehicle;
import com.insurance.setup.Databaseinitialiser;
import com.insurance.dao.VehicleDAO;

import java.time.LocalDate;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {

        Databaseinitialiser.initialiseDatabase();

        Databaseinitialiser.initialiseDatabase();
        VehicleDAO dao = new VehicleDAO();
        InsurancePolicyDAO policyDAO = new InsurancePolicyDAO();


        dao.insertVehicle("RGF3 YNX","Toyota","Aygo");

        Vehicle JacobsCar = dao.getVehicleByReg("RGF3 YNX");

        JacobsCar.PrintVehicleInfo(JacobsCar);

        // dao.deleteByReg("RGF3 YNX");

        System.out.println("Vehicle end");


        // Insurance stuff:
        // Create:
        System.out.println("Insurance start");
        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusYears(1);

        InsurancePolicy policy1 = new InsurancePolicy( "RGF3 YNX", issueDate, expiryDate, "COMPREHENSIVE", true);

        policyDAO.createPolicy(policy1);


    }



}

