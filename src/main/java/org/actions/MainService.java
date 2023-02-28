package org.actions;

import org.database.DatabaseConnection;
import org.models.*;

import java.util.*;
import java.sql.Date;

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
        for (RentModel.InnerRentModel rentModel : lrent.getList()) {
            // Count number of rents
            if (rentModel.IDCLIENT == clientID) {
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
        for (CameraModel.InnerCameraModel cameraModel : lcamera.getList()) {
            if (cameraModel.IDCamera == cameraID) {
                return cameraModel.Pret;
            }
        }
        return 0;
    }

    private static double getCameraRentPrice(int cameraID, ModelList<CameraModel.InnerCameraModel> lcamera) {
        for (CameraModel.InnerCameraModel cameraModel : lcamera.getList()) {
            if (cameraModel.IDCamera == cameraID) {
                return cameraModel.PretInchiriere;
            }
        }
        return 0;
    }

    /**
     * CRUDS - kind of pointless tbh
     */
    // Client
    public static void CreateClient(String name, Date birthDate, int type, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ClientModel client = ClientModel.getInstance();
        client.setDatabaseType(databaseType);
        client.getData();

        ClientModel.InnerClientModel newClient = new ClientModel.InnerClientModel();
        newClient.NumeClient = name;
        newClient.DataNasterii = birthDate;
        newClient.IDTip = type;

        client.insertRow(newClient);
    }

    public static void UpdateClient(int id, String name, Date birthDate, int type, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ClientModel client = ClientModel.getInstance();
        client.setDatabaseType(databaseType);
        client.getData();

        ClientModel.InnerClientModel newClient = new ClientModel.InnerClientModel();
        newClient.IDUtilizator = id;
        newClient.NumeClient = name;
        newClient.DataNasterii = birthDate;
        newClient.IDTip = type;

        ModelList<ClientModel.InnerClientModel> lclient = client.getModelList();

        client.updateData(lclient);
    }

    public static void DeleteClient(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ClientModel client = ClientModel.getInstance();
        client.setDatabaseType(databaseType);

        ModelList<ClientModel.InnerClientModel> lclient = new ModelList<>();

        ClientModel.InnerClientModel newClient = new ClientModel.InnerClientModel();
        newClient.IDUtilizator = id;
        lclient.add(newClient);

        client.deleteRow(lclient);
    }

    // Rent
    public static void CreateRent(int clientID, int cameraID, Date startDate, int duration, double penalty, DatabaseConnection.DatabaseType databaseType) throws Exception {
        RentModel rent = RentModel.getInstance();
        rent.setDatabaseType(databaseType);
        rent.getData();

        RentModel.InnerRentModel newRent = new RentModel.InnerRentModel();
        newRent.IDCLIENT = clientID;
        newRent.IDCAMERA = cameraID;
        newRent.DATA_INCHIRIERE = startDate;
        newRent.DURATA_IN_ZILE = duration;
        newRent.PENALIZARE = penalty;

        rent.insertRow(newRent);
    }

    public static void UpdateRent(int id, int clientID, int cameraID, Date startDate, int duration, double penalty, int employeeID, int objectiveID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        RentModel rent = RentModel.getInstance();
        rent.setDatabaseType(databaseType);
        rent.getData();

        RentModel.InnerRentModel newRent = new RentModel.InnerRentModel();
        newRent.IDCLIENT = clientID;
        newRent.IDCAMERA = cameraID;
        newRent.DATA_INCHIRIERE = startDate;
        newRent.DURATA_IN_ZILE = duration;
        newRent.PENALIZARE = penalty;
        newRent.IDANGAJAT = employeeID;
        newRent.IDOBIECTIV = objectiveID;

        ModelList<RentModel.InnerRentModel> lrent = rent.getModelList();

        rent.updateData(lrent);
    }

    public static void DeleteRent(int objectiveID, int clientID, int cameraID, int employeeID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        RentModel rent = RentModel.getInstance();
        rent.setDatabaseType(databaseType);

        ModelList<RentModel.InnerRentModel> lrent = new ModelList<>();

        RentModel.InnerRentModel newRent = new RentModel.InnerRentModel();
        newRent.IDOBIECTIV = objectiveID;
        newRent.IDCLIENT = clientID;
        newRent.IDCAMERA = cameraID;
        newRent.IDANGAJAT = employeeID;

        lrent.add(newRent);

        rent.deleteRow(lrent);
    }

    // Camera
    public static void CreateCamera(String name, double price, double rentPrice, int cameraID, String brand, int year, int formatID, int mountID, int typeID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraModel camera = CameraModel.getInstance();
        camera.setDatabaseType(databaseType);

        CameraModel.InnerCameraModel newCamera = new CameraModel.InnerCameraModel();
        newCamera.IDCamera = cameraID;
        newCamera.ModelCamera = name;
        newCamera.Pret = price;
        newCamera.PretInchiriere = rentPrice;
        newCamera.Marca = brand;
        newCamera.AnFabricatie = year;
        newCamera.IDFormat = formatID;
        newCamera.IDMontura = mountID;
        newCamera.IDTip = typeID;

        camera.insertRow(newCamera);
    }

    public static void UpdateCamera(int id, String name, double price, double rentPrice, int cameraID, String brand, int year, int formatID, int mountID, int typeID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraModel camera = CameraModel.getInstance();
        camera.setDatabaseType(databaseType);
        camera.getData();

        CameraModel.InnerCameraModel newCamera = new CameraModel.InnerCameraModel();
        newCamera.IDCamera = cameraID;
        newCamera.ModelCamera = name;
        newCamera.Pret = price;
        newCamera.PretInchiriere = rentPrice;
        newCamera.Marca = brand;
        newCamera.AnFabricatie = year;
        newCamera.IDFormat = formatID;
        newCamera.IDMontura = mountID;
        newCamera.IDTip = typeID;

        ModelList<CameraModel.InnerCameraModel> lcamera = camera.getModelList();

        camera.updateData(lcamera);
    }

    public static void DeleteCamera(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraModel camera = CameraModel.getInstance();
        camera.setDatabaseType(databaseType);

        ModelList<CameraModel.InnerCameraModel> lcamera = new ModelList<>();

        CameraModel.InnerCameraModel newCamera = new CameraModel.InnerCameraModel();
        newCamera.IDCamera = id;
        lcamera.add(newCamera);

        camera.deleteRow(lcamera);
    }

    // Format
    public static void CreateFormat(String name, int formatID, String width, DatabaseConnection.DatabaseType databaseType) throws Exception {
        FormatModel format = FormatModel.getInstance();
        format.setDatabaseType(databaseType);

        FormatModel.InnerFormatModel newFormat = new FormatModel.InnerFormatModel();
        newFormat.IDFormat = formatID;
        newFormat.Denumire = name;
        newFormat.LatimeFilm = width;

        format.insertRow(newFormat);
    }

    public static void UpdateFormat(int id, String name, int formatID, String width, DatabaseConnection.DatabaseType databaseType) throws Exception {
        FormatModel format = FormatModel.getInstance();
        format.setDatabaseType(databaseType);
        format.getData();

        FormatModel.InnerFormatModel newFormat = new FormatModel.InnerFormatModel();
        newFormat.IDFormat = formatID;
        newFormat.Denumire = name;
        newFormat.LatimeFilm = width;

        ModelList<FormatModel.InnerFormatModel> lformat = format.getModelList();

        format.updateData(lformat);
    }

    public static void DeleteFormat(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        FormatModel format = FormatModel.getInstance();
        format.setDatabaseType(databaseType);

        ModelList<FormatModel.InnerFormatModel> lformat = new ModelList<>();

        FormatModel.InnerFormatModel newFormat = new FormatModel.InnerFormatModel();
        newFormat.IDFormat = id;
        lformat.add(newFormat);

        format.deleteRow(lformat);
    }

    // Montura
    public static void CreateMount(String name, int mountID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        MountModel mount = MountModel.getInstance();
        mount.setDatabaseType(databaseType);

        MountModel.InnerMountModel newMount = new MountModel.InnerMountModel();
        newMount.IDMontura = mountID;
        newMount.Denumire = name;

        mount.insertRow(newMount);
    }

    public static void UpdateMount(int id, String name, int mountID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        MountModel mount = MountModel.getInstance();
        mount.setDatabaseType(databaseType);
        mount.getData();

        MountModel.InnerMountModel newMount = new MountModel.InnerMountModel();
        newMount.IDMontura = mountID;
        newMount.Denumire = name;

        ModelList<MountModel.InnerMountModel> lmount = mount.getModelList();

        mount.updateData(lmount);
    }

    public static void DeleteMount(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        MountModel mount = MountModel.getInstance();
        mount.setDatabaseType(databaseType);

        ModelList<MountModel.InnerMountModel> lmount = new ModelList<>();

        MountModel.InnerMountModel newMount = new MountModel.InnerMountModel();
        newMount.IDMontura = id;
        lmount.add(newMount);

        mount.deleteRow(lmount);
    }

    // Tip
    public static void CreateType(String name, int typeID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraTypeModel type = CameraTypeModel.getInstance();
        type.setDatabaseType(databaseType);

        CameraTypeModel.InnerCameraTypeModel newType = new CameraTypeModel.InnerCameraTypeModel();
        newType.IDTip = typeID;
        newType.Denumire = name;

        type.insertRow(newType);
    }

    public static void UpdateType(int id, String name, int typeID, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraTypeModel type = CameraTypeModel.getInstance();
        type.setDatabaseType(databaseType);
        type.getData();

        CameraTypeModel.InnerCameraTypeModel newType = new CameraTypeModel.InnerCameraTypeModel();
        newType.IDTip = typeID;
        newType.Denumire = name;

        ModelList<CameraTypeModel.InnerCameraTypeModel> ltype = type.getModelList();

        type.updateData(ltype);
    }

    public static void DeleteType(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        CameraTypeModel type = CameraTypeModel.getInstance();
        type.setDatabaseType(databaseType);

        ModelList<CameraTypeModel.InnerCameraTypeModel> ltype = new ModelList<>();

        CameraTypeModel.InnerCameraTypeModel newType = new CameraTypeModel.InnerCameraTypeModel();
        newType.IDTip = id;
        ltype.add(newType);

        type.deleteRow(ltype);
    }

    // Employee
    public static void CreateEmployee(int employeeID, Date birthdate, Date hiredate, int managerID, int salaryID, String address, String phone, String email, DatabaseConnection.DatabaseType databaseType) throws Exception {
        EmployeeModel employee = EmployeeModel.getInstance();
        employee.setDatabaseType(databaseType);

        EmployeeModel.InnerEmployeeModel newEmployee = new EmployeeModel.InnerEmployeeModel();
        newEmployee.IDUtilizator = employeeID;
        newEmployee.DataNasterii = birthdate;
        newEmployee.DataAngajarii = hiredate;
        newEmployee.IDManager = managerID;
        newEmployee.IDSalariu = salaryID;

        employee.insertRow(newEmployee);
    }

    public static void UpdateEmployee(int employeeID, Date birthdate, Date hiredate, int managerID, int salaryID, String address, String phone, String email, DatabaseConnection.DatabaseType databaseType) throws Exception {
        EmployeeModel employee = EmployeeModel.getInstance();
        employee.setDatabaseType(databaseType);
        employee.getData();

        EmployeeModel.InnerEmployeeModel newEmployee = new EmployeeModel.InnerEmployeeModel();
        newEmployee.IDUtilizator = employeeID;
        newEmployee.DataNasterii = birthdate;
        newEmployee.DataAngajarii = hiredate;
        newEmployee.IDManager = managerID;
        newEmployee.IDSalariu = salaryID;

        ModelList<EmployeeModel.InnerEmployeeModel> lemployee = employee.getModelList();

        employee.updateData(lemployee);
    }

    public static void DeleteEmployee(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        EmployeeModel employee = EmployeeModel.getInstance();
        employee.setDatabaseType(databaseType);

        ModelList<EmployeeModel.InnerEmployeeModel> lemployee = new ModelList<>();

        EmployeeModel.InnerEmployeeModel newEmployee = new EmployeeModel.InnerEmployeeModel();
        newEmployee.IDUtilizator = id;
        lemployee.add(newEmployee);

        employee.deleteRow(lemployee);
    }

    // Objective
    public static void CreateObjective(String name, int objectiveID, int mountID, int price, int diameter, int focalDistance, int rentPrice, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ObjectiveModel objective = ObjectiveModel.getInstance();
        objective.setDatabaseType(databaseType);

        ObjectiveModel.InnerObjectiveModel newObjective = new ObjectiveModel.InnerObjectiveModel();
        newObjective.IDObiectiv = objectiveID;
        newObjective.Denumire = name;
        newObjective.IDMontura = mountID;
        newObjective.Pret = price;
        newObjective.Diametru = diameter;
        newObjective.DistantaFocala = focalDistance;
        newObjective.PretInchiriere = rentPrice;

        objective.insertRow(newObjective);
    }

    public static void UpdateObjective(int id, String name, int objectiveID, int mountID, int price, int diameter, int focalDistance, int rentPrice, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ObjectiveModel objective = ObjectiveModel.getInstance();
        objective.setDatabaseType(databaseType);
        objective.getData();

        ObjectiveModel.InnerObjectiveModel newObjective = new ObjectiveModel.InnerObjectiveModel();
        newObjective.IDObiectiv = objectiveID;
        newObjective.Denumire = name;
        newObjective.IDMontura = mountID;
        newObjective.Pret = price;
        newObjective.Diametru = diameter;
        newObjective.DistantaFocala = focalDistance;
        newObjective.PretInchiriere = rentPrice;

        ModelList<ObjectiveModel.InnerObjectiveModel> lobjective = objective.getModelList();

        objective.updateData(lobjective);
    }

    public static void DeleteObjective(int id, DatabaseConnection.DatabaseType databaseType) throws Exception {
        ObjectiveModel objective = ObjectiveModel.getInstance();
        objective.setDatabaseType(databaseType);

        ModelList<ObjectiveModel.InnerObjectiveModel> lobjective = new ModelList<>();

        ObjectiveModel.InnerObjectiveModel newObjective = new ObjectiveModel.InnerObjectiveModel();
        newObjective.IDObiectiv = id;
        lobjective.add(newObjective);

        objective.deleteRow(lobjective);
    }
}
