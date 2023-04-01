package org.actions;

import org.database.DatabaseConnection;
import org.logger.CsvLogger;
import org.models.*;

import javax.swing.table.TableModel;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;

public class MainService {
    private static CsvLogger logger = CsvLogger.getInstance();

    public static Map<String, String> formatSales(int formatID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        Map<String, String> result = new HashMap<>();

//        FormatModel format = FormatModel.getInstance();
        CameraModel camera = CameraModel.getInstance();
        RentModel rent = RentModel.getInstance();

//        format.setDatabaseType(databaseType);
        camera.setDatabaseType(databaseType);
        rent.setDatabaseType(databaseType);

//        format.getData();
        camera.getData();
        rent.getData();

//        ModelList<FormatModel.InnerFormatModel> lformat = format.getModelList();
        ModelList<CameraModel.InnerCameraModel> lcamera = camera.getModelList();
        ModelList<RentModel.InnerRentModel> lrent = rent.getModelList();

        int totalRents = 0;
        double maxRentPrice = 0;
        double secondMaxRentPrice = 0;
        double thirdMaxRentPrice = 0;
        int numDistinctClients = 0;

        Set<Integer> clientset = new HashSet<>();
        // Max heap
//        PriorityQueue<Double> rentPriceQueue = new PriorityQueue<>(Collections.reverseOrder());
        TreeMap<Double, Double> rentPriceMap = new TreeMap<>();

        List<Integer> cameraIDs = new ArrayList<>();
        for (CameraModel.InnerCameraModel cameraModel : lcamera.getList()) {
            if (cameraModel.FormatID == formatID)
                cameraIDs.add(cameraModel.IDCamera);
        }

        for (RentModel.InnerRentModel rentModel : lrent.getList()) {
            if (rentModel.IDCAMERA == formatID) {
                ++totalRents;
                clientset.add(rentModel.IDCLIENT);
                double rentPrice = rentModel.DURATION_IN_DAYS * getCameraRentPrice(rentModel.IDCAMERA, lcamera);
//                rentPriceQueue.add(rentPrice);
                rentPriceMap.put(rentPrice, rentPrice);
            }
        }

        numDistinctClients = clientset.size();
//        maxRentPrice = rentPriceQueue.peek() == null ? 0 : rentPriceQueue.poll();
//        secondMaxRentPrice = rentPriceQueue.peek() == null ? 0 : rentPriceQueue.poll();
//        thirdMaxRentPrice = rentPriceQueue.peek() == null ? 0 : rentPriceQueue.poll();
        maxRentPrice = rentPriceMap.lastEntry() == null ? 0 : rentPriceMap.lastEntry().getValue();
        secondMaxRentPrice = rentPriceMap.lowerEntry(maxRentPrice) == null ? 0 : rentPriceMap.lowerEntry(maxRentPrice).getValue();
        thirdMaxRentPrice = rentPriceMap.lowerEntry(secondMaxRentPrice) == null ? 0 : rentPriceMap.lowerEntry(secondMaxRentPrice).getValue();

        result.put("No. Rents", String.valueOf(totalRents));
        result.put("1st price", String.valueOf(maxRentPrice));
        result.put("2nd price", String.valueOf(secondMaxRentPrice));
        result.put("3rd price", String.valueOf(thirdMaxRentPrice));
        result.put("Distinct Clients", String.valueOf(numDistinctClients));

        try {
            logger.log("Format Sales report generated");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return result;
    }

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
        for (RentModel.InnerRentModel rentModel : lrent.getList()) {
            // Count number of rents
            if (rentModel.IDCLIENT == clientID) {
                rents++;

                // Add all cameras
                cameraset.add(rentModel.IDCAMERA);

                // Get total rent
                totalRent += rentModel.DURATION_IN_DAYS * getCameraRentPrice(rentModel.IDCAMERA, lcamera);

                // Get total penalty
                totalPenalty += rentModel.PENALTYFEE;

                // Get average camera price
                totalPrice += getCameraPrice(rentModel.IDCAMERA, lcamera);
            }
        }

        result.put("No. Rents", String.valueOf(rents));
        result.put("No. Cameras", String.valueOf(cameraset.size()));
        result.put("Total Rent", String.valueOf(totalRent));
        result.put("Total Penalty", String.valueOf(totalPenalty));
        result.put("Avg. Camera Price", String.valueOf(totalPrice / cameraset.size()));

        try {
            logger.log("Client Rents report generated");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return result;
    }

    private static double getCameraPrice(int cameraID, ModelList<CameraModel.InnerCameraModel> lcamera) {
        for (CameraModel.InnerCameraModel cameraModel : lcamera.getList()) {
            if (cameraModel.IDCamera == cameraID) {
                return cameraModel.Price;
            }
        }
        return 0;
    }

    private static double getCameraRentPrice(int cameraID, ModelList<CameraModel.InnerCameraModel> lcamera) {
        for (CameraModel.InnerCameraModel cameraModel : lcamera.getList()) {
            if (cameraModel.IDCamera == cameraID) {
                return cameraModel.RentalPrice;
            }
        }
        return 0;
    }

    /**
     * CRUDS
     */
    public static void getData(LinkModelToDatabase model) throws Exception {
        model.getData();
    }

    public static ModelList getModelList(Model model) throws Exception {
        return model.getModelList();
    }

    public static void setDatabaseType(Model model, DatabaseConnection.DatabaseType dbType) throws Exception {
        model.setDatabaseType(dbType);
    }

    public static TableModel getTableModel(Model model) throws Exception {
        return model.getTableModel();
    }

    public static void insert(LinkModelToDatabase model, Model.AbstractInnerModel innerModel) throws Exception {
        model.insertRow(innerModel);
    }

    public static void delete(LinkModelToDatabase model, ModelList innerModelList) throws Exception {
        model.deleteRow(innerModelList);
    }

    public static void update(LinkModelToDatabase model, ModelList innerModelList) throws Exception {
        model.updateData(innerModelList);
    }

    public static List<String> getAttributes(Class model) throws Exception {
        List<String> attributes = new ArrayList<>();
        for (Field field : model.getDeclaredFields()) {
            attributes.add(field.getName());
        }
        return attributes;
    }

    public static void getFilteredData(LinkModelToDatabase model, String comparator, String value, String column) throws Exception {
        model.getFilteredData(comparator, value, column);
    }
}