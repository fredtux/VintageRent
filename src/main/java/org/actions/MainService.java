package org.actions;

import org.database.DatabaseConnection;
import org.models.CameraModel;
import org.models.ClientModel;
import org.models.ModelList;
import org.models.RentModel;

import java.util.*;

public class MainService {

    public static Map<String, String> ClientRents(int clientID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        Map<String, String> result = new HashMap<>();

//        ClientModel client = ClientModel.getInstance();
        RentModel rent = RentModel.getInstance();
        CameraModel camera = CameraModel.getInstance();

//        client.setDatabaseType(databaseType);
        rent.setDatabaseType(databaseType);
        camera.setDatabaseType(databaseType);

//        client.getData();
        rent.getData();
        camera.getData();

//        ModelList<ClientModel.InnerClientModel> lclient = client.getModelList();
        ModelList<RentModel.InnerRentModel> lrent = rent.getModelList();
        ModelList<CameraModel.InnerCameraModel> lcamera = camera.getModelList();

        int rents = 0;
        double totalRent = 0;
        double totalPenalty = 0;
        double totalPrice = 0;
        Set<Integer> cameraset = new HashSet<>();
        for(RentModel.InnerRentModel rentModel : lrent.getList()){
            // Count number of rents
            if(rentModel.IDCLIENT == clientID) {
                rents++;

                // Add all cameras
                cameraset.add(rentModel.IDCAMERA);

                // Get total rent
                totalRent += rentModel.DURATA_IN_ZILE * getCameraRentPrice(rentModel.IDCAMERA, lcamera);

                // Get total penalty
                totalPenalty += rentModel.PENALIZARE;

                // Get average camera price
                totalPrice += getCameraPrice(rentModel.IDCAMERA, lcamera);
            }
        }

        result.put("No. Rents", String.valueOf(rents));
        result.put("No. Cameras", String.valueOf(cameraset.size()));
        result.put("Total Rent", String.valueOf(totalRent));
        result.put("Total Penalty", String.valueOf(totalPenalty));
        result.put("Avg. Camera Price", String.valueOf(totalPrice / cameraset.size()));

        return result;
    }

    private static double getCameraPrice(int cameraID, ModelList<CameraModel.InnerCameraModel> lcamera) {
        for(CameraModel.InnerCameraModel cameraModel : lcamera.getList()){
            if(cameraModel.IDCamera == cameraID){
                return cameraModel.Pret;
            }
        }
        return 0;
    }

    private static double getCameraRentPrice(int cameraID, ModelList<CameraModel.InnerCameraModel> lcamera) {
        for(CameraModel.InnerCameraModel cameraModel : lcamera.getList()){
            if(cameraModel.IDCamera == cameraID){
                return cameraModel.PretInchiriere;
            }
        }
        return 0;
    }
}
