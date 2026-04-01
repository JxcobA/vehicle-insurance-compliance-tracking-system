import com.insurance.model.Vehicle;
import com.insurance.setup.Databaseinitialiser;
import com.insurance.dao.VehicleDAO;
public class Main {
    public static void main(String[] args) {
        Databaseinitialiser.initialiseDatabase();
        VehicleDAO dao = new VehicleDAO();


        dao.insertVehicle("RGF3 YNX","Toyota","Aygo");

        Vehicle JacobsCar = dao.getVehicleByReg("RGF3 YNX");

        JacobsCar.PrintVehicleInfo(JacobsCar);

        dao.deleteByReg("RGF3 YNX");

    }



}

