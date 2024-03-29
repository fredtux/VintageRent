package org.actions;

import org.database.DatabaseConnection;
import org.logger.CsvLogger;
import org.models.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.table.TableModel;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Date;
import javax.mail.*;

public class MainService {
    private static CsvLogger logger = CsvLogger.getInstance();

    public static Map<String, String> formatSales(int formatID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        Map<String, String> result = new HashMap<>();

        CameraModel camera = CameraModel.getInstance();
        RentModel rent = RentModel.getInstance();

        camera.setDatabaseType(databaseType);
        rent.setDatabaseType(databaseType);

        camera.getData();
        rent.getData();

        ModelList<CameraModel.InnerCameraModel> lcamera = camera.getModelList();
        ModelList<RentModel.InnerRentModel> lrent = rent.getModelList();

        int totalRents = 0;
        double maxRentPrice = 0;
        double secondMaxRentPrice = 0;
        double thirdMaxRentPrice = 0;
        int numDistinctClients = 0;

        Set<Integer> clientset = new HashSet<>();
        // Max heap
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
                rentPriceMap.put(rentPrice, rentPrice);
            }
        }

        numDistinctClients = clientset.size();

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

        RentModel rent = RentModel.getInstance();
        CameraModel camera = CameraModel.getInstance();

        rent.setDatabaseType(databaseType);
        camera.setDatabaseType(databaseType);

        rent.getData();
        camera.getData();

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

    public static List<Model.AbstractInnerModel> getForMount(int id, DatabaseConnection.DatabaseType databaseType) throws Exception{
        List<Model.AbstractInnerModel> result = new ArrayList<>();

        ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
        MainService.setDatabaseType(objectiveModel, databaseType);
        ModelList<ObjectiveModel.InnerObjectiveModel> objectives = MainService.getData(objectiveModel);
        for(ObjectiveModel.InnerObjectiveModel objective : objectives.getList()){
            if(objective.MountID == id){
                result.add(objective);
            }
        }

        CameraModel cameraModel = CameraModel.getInstance();
        MainService.setDatabaseType(cameraModel, databaseType);
        ModelList<CameraModel.InnerCameraModel> cameras = MainService.getData(cameraModel);
        for(CameraModel.InnerCameraModel camera : cameras.getList()){
            if(camera.MountID == id){
                result.add(camera);
            }
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
    public static ModelList getData(LinkModelToDatabase model) throws Exception {
        return model.getData();
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

    public static void truncate(LinkModelToDatabase model) throws Exception {
        model.truncate();
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


    public static boolean sendEmail(String to, int id, String report, DatabaseConnection.DatabaseType dbType) throws Exception {
        Map<String, String> result = null;
        if(report == "ClientReport"){
            result = ClientRents(id, dbType);
        } else if(report == "FormatSalesReport"){
            result = formatSales(id, dbType);
        } else if (report == "MountReport"){
            List<Model.AbstractInnerModel> reportResult = getForMount(id, dbType);
            result = new HashMap<>();

            for (Model.AbstractInnerModel item : reportResult) {
                if(item instanceof ObjectiveModel.InnerObjectiveModel)
                    result.put(((ObjectiveModel.InnerObjectiveModel) item).Name, "Objective");
                else if(item instanceof CameraModel.InnerCameraModel)
                    result.put(((CameraModel.InnerCameraModel) item).Brand + " - " + ((CameraModel.InnerCameraModel) item).Brand, "Objective");
            }
        }

        if(result == null)
            return false;

        String subject = report + ": " + id;
        String body = "";
        for (Map.Entry<String, String> entry : result.entrySet()) {
            body += entry.getKey() + ": " + entry.getValue() + "\n";
        }

        // Tutorial: https://www.tutorialspoint.com/java/java_sending_email.htm
        String from = "root@localhost";
        String host = "localhost";
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);

            Session session = Session.getDefaultInstance(properties);


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (Exception ex) {
        }

        return false;

    }
}