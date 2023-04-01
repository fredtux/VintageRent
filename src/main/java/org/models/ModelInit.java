package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.oracle.OracleConnection;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class ModelInit {
    private static String[] modelNames = {"Employee", "Rental", "Administrator_Subdomains", "Administrators", "Address", "Camera","Clients", "Format", "Mount", "Objective", "Salary", "CameraType", "ClientType", "Users", "Subdomains"};

    public static void copyResourceDirectory(Path source, Path destination) throws IOException  {
//        Path source = Paths.get("CSV");
//        Path destination = Paths.get("C:/myproject/resources/CSV"); // specify your desired destination path here

        try {
            Files.walk(source)
                    .forEach(sourcePath -> {
                        try {
                            Path destinationPath = destination.resolve(source.relativize(sourcePath));
                            Files.copy(sourcePath, destinationPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void copyFromJar(URI uri, final Path target) throws IOException {
//        Path myPath = null;
//
//        try {
//            myPath = Paths.get(uri);
//        } catch (FileSystemNotFoundException e) {
//            System.out.println("File system not found: " + e.getMessage() + ":" + uri.toString());
//            throw e;
//        }
//
//        Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                try {
//                    Files.copy(file, target.resolve(file.getFileName()));
//                } catch (Exception e){
//                    System.out.println("Error copying file: " + e.getMessage());
//                }
//                return FileVisitResult.CONTINUE;
//            }
//        });
//    }

    public static void logInit() throws Exception{
        String logPath = Paths.get(System.getProperty("user.dir") + "/Log").toString();
        try {
            Files.createDirectories(Paths.get(logPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileExists(logPath + "/Log.csv")) {
            URL inputUrl = ModelInit.class.getResource("/Log/Log.csv");
            File dest = new File(logPath + "/Log.csv");
            Files.copy(inputUrl.openStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean fileExists(String path){
        File tmp = new File(path);

        return tmp.exists();
    }

    private static void initInMemCameraModel(){
        try{
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            CameraModel.InnerCameraModel data = new CameraModel.InnerCameraModel();
            data.IDCamera = 1;
            data.ModelCamera = "Camera1";
            data.MountID = 1;
            data.FormatID = 1;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand1";
            data.TypeID = 1;

            MainService.insert(cameraModel, data);

            data.IDCamera = 2;
            data.ModelCamera = "Camera2";
            data.MountID = 2;
            data.FormatID = 2;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand2";
            data.TypeID = 2;

            MainService.insert(cameraModel, data);

            data.IDCamera = 3;
            data.ModelCamera = "Camera3";
            data.MountID = 1;
            data.FormatID = 1;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand3";
            data.TypeID = 3;

            MainService.insert(cameraModel, data);

            data.IDCamera = 4;
            data.ModelCamera = "Camera4";
            data.MountID = 1;
            data.FormatID = 3;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand4";
            data.TypeID = 1;

            MainService.insert(cameraModel, data);

            data.IDCamera = 5;
            data.ModelCamera = "Camera5";
            data.MountID = 4;
            data.FormatID = 2;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand5";
            data.TypeID = 4;

            MainService.insert(cameraModel, data);

            data.IDCamera = 6;
            data.ModelCamera = "Camera6";
            data.MountID = 2;
            data.FormatID = 2;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand6";
            data.TypeID = 2;

            MainService.insert(cameraModel, data);

            data.IDCamera = 7;
            data.ModelCamera = "Camera7";
            data.MountID = 3;
            data.FormatID = 3;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand7";
            data.TypeID = 3;

            MainService.insert(cameraModel, data);

            data.IDCamera = 8;
            data.ModelCamera = "Camera8";
            data.MountID = 1;
            data.FormatID = 1;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ManufacturingYear = 1970;
            data.Brand = "Brand8";
            data.TypeID = 1;

            MainService.insert(cameraModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemCameraTypeModel(){
        try{
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            CameraTypeModel.InnerCameraTypeModel data = new CameraTypeModel.InnerCameraTypeModel();
            data.TypeID = 1;
            data.Name = "Camera1";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 2;
            data.Name = "Camera2";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 3;
            data.Name = "Camera3";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 4;
            data.Name = "Camera4";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 5;
            data.Name = "Camera5";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 6;
            data.Name = "Camera6";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 7;
            data.Name = "Camera7";

            MainService.insert(cameraTypeModel, data);

            data.TypeID = 8;
            data.Name = "Camera8";

            MainService.insert(cameraTypeModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemAdministratorModel(){
        try{
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            AdministratorModel.InnerAdministratorModel data = new AdministratorModel.InnerAdministratorModel();
            data.UserID = 1;
            data.isActive = true;
            MainService.insert(administratorModel, data);

            data.UserID = 2;
            data.isActive = true;
            MainService.insert(administratorModel, data);

            data.UserID = 3;
            data.isActive = false;
            MainService.insert(administratorModel, data);

            data.UserID = 4;
            data.isActive = true;
            MainService.insert(administratorModel, data);

            data.UserID = 5;
            data.isActive = false;
            MainService.insert(administratorModel, data);

            data.UserID = 6;
            data.isActive = false;
            MainService.insert(administratorModel, data);

            data.UserID = 7;
            data.isActive = true;
            MainService.insert(administratorModel, data);

            data.UserID = 8;
            data.isActive = true;
            MainService.insert(administratorModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemSAdministratorubdomainModel(){
        try{
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            AdministratorSubdomainModel.InnerAdministratorSubdomainModel data = new AdministratorSubdomainModel.InnerAdministratorSubdomainModel();
            data.IDAdministrator = 1;
            data.SubdomainID = 1;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 2;
            data.SubdomainID = 1;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 3;
            data.SubdomainID = 3;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 4;
            data.SubdomainID = 4;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 5;
            data.SubdomainID = 2;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 6;
            data.SubdomainID = 2;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 7;
            data.SubdomainID = 2;
            MainService.insert(administratorSubdomainModel, data);

            data.IDAdministrator = 7;
            data.SubdomainID = 1;
            MainService.insert(administratorSubdomainModel, data);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemSubdomainModel(){
        try{
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            SubdomainModel.InnerSubdomainModel data = new SubdomainModel.InnerSubdomainModel();
            data.SubdomainID = 1;
            data.Name = "Subdomain1";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 2;
            data.Name = "Subdomain2";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 3;
            data.Name = "Subdomain3";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 4;
            data.Name = "Subdomain4";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 5;
            data.Name = "Subdomain5";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 6;
            data.Name = "Subdomain6";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 7;
            data.Name = "Subdomain7";
            MainService.insert(subdomainModel, data);

            data.SubdomainID = 8;
            data.Name = "Subdomain8";
            MainService.insert(subdomainModel, data);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemClientModel(){
        try{
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            ClientModel.InnerClientModel data = new ClientModel.InnerClientModel();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data.UserID = 1;
            data.SurnameClient = "ClientName1";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 2;
            data.SurnameClient = "ClientName2";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 3;
            data.SurnameClient = "ClientName3";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 4;
            data.SurnameClient = "ClientName4";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 5;
            data.SurnameClient = "ClientName5";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 6;
            data.SurnameClient = "ClientName6";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);
            data.UserID = 7;
            data.SurnameClient = "ClientName7";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.UserID = 8;
            data.SurnameClient = "ClientName8";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemClientTypeModel(){
        try{
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            ClientTypeModel.InnerClientTypeModel data = new ClientTypeModel.InnerClientTypeModel();
            data.TypeID = 1;
            data.Name = "ClientType1";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 2;
            data.Name = "ClientType2";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 3;
            data.Name = "ClientType3";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 4;
            data.Name = "ClientType4";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 5;
            data.Name = "ClientType5";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 6;
            data.Name = "ClientType6";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 7;
            data.Name = "ClientType7";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.TypeID = 8;
            data.Name = "ClientType8";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemEmployeeModel(){
        try{
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            EmployeeModel.InnerEmployeeModel data = new EmployeeModel.InnerEmployeeModel();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data.UserID = 1;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 1;
            data.IDManager = 1;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 2;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 2;
            data.IDManager = 1;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 3;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 2;
            data.IDManager = 1;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 4;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 3;
            data.IDManager = 2;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 5;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 4;
            data.IDManager = 4;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 6;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 2;
            data.IDManager = 2;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 7;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 2;
            data.IDManager = 2;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);

            data.UserID = 8;
            data.BirthDate = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.HireDate = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.SalaryID = 5;
            data.IDManager = 5;
            data.EmployeeName = "Test";

            MainService.insert(employeeModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemFormatModel(){
        try{
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            FormatModel.InnerFormatModel data = new FormatModel.InnerFormatModel();
            data.FormatID = 1;
            data.Name = "Format1";
            data.FilmWidth = "Latime1";

            MainService.insert(formatModel, data);

            data.FormatID = 2;
            data.Name = "Format2";
            data.FilmWidth = "Latime2";

            MainService.insert(formatModel, data);

            data.FormatID = 3;
            data.Name = "Format3";
            data.FilmWidth = "Latime3";

            MainService.insert(formatModel, data);

            data.FormatID = 4;
            data.Name = "Format4";
            data.FilmWidth = "Latime4";

            MainService.insert(formatModel, data);

            data.FormatID = 5;
            data.Name = "Format5";
            data.FilmWidth = "Latime5";

            MainService.insert(formatModel, data);

            data.FormatID = 6;
            data.Name = "Format6";
            data.FilmWidth = "Latime6";

            MainService.insert(formatModel, data);

            data.FormatID = 7;
            data.Name = "Format7";
            data.FilmWidth = "Latime7";

            MainService.insert(formatModel, data);

            data.FormatID = 8;
            data.Name = "Format8";
            data.FilmWidth = "Latime8";

            MainService.insert(formatModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemMountModel(){
        try{
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            MountModel.InnerMountModel data = new MountModel.InnerMountModel();
            data.MountID = 1;
            data.Name = "Mount1";

            MainService.insert(mountModel, data);

            data.MountID = 2;
            data.Name = "Mount2";

            MainService.insert(mountModel, data);

            data.MountID = 3;
            data.Name = "Mount3";

            MainService.insert(mountModel, data);

            data.MountID = 4;
            data.Name = "Mount4";

            MainService.insert(mountModel, data);

            data.MountID = 5;
            data.Name = "Mount5";

            MainService.insert(mountModel, data);

            data.MountID = 6;
            data.Name = "Mount6";

            MainService.insert(mountModel, data);

            data.MountID = 7;
            data.Name = "Mount7";

            MainService.insert(mountModel, data);

            data.MountID = 8;
            data.Name = "Mount8";

            MainService.insert(mountModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemObjectiveModel(){
        try{
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ObjectiveModel.InnerObjectiveModel data = new ObjectiveModel.InnerObjectiveModel();
            data.ObjectiveID = 1;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective1";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 2;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective2";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 3;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective3";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 4;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective4";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 5;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective5";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 6;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective6";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 7;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective7";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            data.ObjectiveID = 8;
            data.RentalPrice = 100;
            data.Price = 100;
            data.Name = "Objective8";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemRentModel(){
        try{
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            RentModel.InnerRentModel data = new RentModel.InnerRentModel();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 1;
            data.OBJECTIVEID = 1;
            data.IS_RETURNED = false;
            data.IDCAMERA = 1;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 2;
            data.IDANGAJAT = 2;
            data.IDCLIENT = 2;
            data.OBJECTIVEID = 2;
            data.IS_RETURNED = false;
            data.IDCAMERA = 2;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 2;
            data.OBJECTIVEID = 2;
            data.IS_RETURNED = false;
            data.IDCAMERA = 2;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 3;
            data.IDCLIENT = 3;
            data.OBJECTIVEID = 3;
            data.IS_RETURNED = false;
            data.IDCAMERA = 3;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 1;
            data.OBJECTIVEID = 1;
            data.IS_RETURNED = false;
            data.IDCAMERA = 1;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 2;
            data.IDCLIENT = 2;
            data.OBJECTIVEID = 2;
            data.IS_RETURNED = false;
            data.IDCAMERA = 2;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 3;
            data.IDCLIENT = 3;
            data.OBJECTIVEID = 3;
            data.IS_RETURNED = false;
            data.IDCAMERA = 3;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 4;
            data.IDCLIENT = 4;
            data.OBJECTIVEID = 4;
            data.IS_RETURNED = false;
            data.IDCAMERA = 4;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemSalaryModel(){
        try{
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            SalaryModel.InnerSalaryModel data = new SalaryModel.InnerSalaryModel();
            data.SalaryID = 0;
            data.Salary = 100;
            data.Bonus = 100;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 200;
            data.Bonus = 200;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 300;
            data.Bonus = 300;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 400;
            data.Bonus = 400;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 500;
            data.Bonus = 500;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 600;
            data.Bonus = 600;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 700;
            data.Bonus = 700;

            MainService.insert(msalaryModel, data);

            data.SalaryID = 0;
            data.Salary = 800;
            data.Bonus = 800;

            MainService.insert(msalaryModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initInMemUserModel(){
        try{
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            UserModel.InnerUserModel data = new UserModel.InnerUserModel();
            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user1";
            data.Firstname = "John1";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name1";

            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user2";
            data.Firstname = "John2";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name2";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user3";
            data.Firstname = "John3";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name3";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user4";
            data.Firstname = "John4";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name4";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user5";
            data.Firstname = "John5";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name5";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user6";
            data.Firstname = "John6";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name6";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user7";
            data.Firstname = "John7";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name7";


            MainService.insert(muserModel, data);

            data.UserID = 0;
            data.Password = "123";
            data.UserName = "user8";
            data.Firstname = "John8";
            data.CNP = "123";
            data.Email = "";
            data.Surname = "Name8";


            MainService.insert(muserModel, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void inmemInit() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.INMEMORY);

        initInMemFormatModel();
        initInMemCameraTypeModel();
        initInMemMountModel();
        initInMemObjectiveModel();
        initInMemCameraModel();
        initInMemClientTypeModel();
        initInMemUserModel();

        initInMemSalaryModel();
        initInMemEmployeeModel();
        initInMemClientModel();

        initInMemRentModel();
        initInMemAdministratorModel();
        initInMemSubdomainModel();
        initInMemSAdministratorubdomainModel();
    }

    public static void csvInit() throws Exception{
        // Make path to Log and CSV
        String csvPath = Paths.get(System.getProperty("user.dir") + "/CSV").toString();

        // Make Log and CSV directories
        try {
            Files.createDirectories(Paths.get(csvPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!fileExists(csvPath + "/mock.csv")) {
            URL inputUrl = ModelInit.class.getResource("/CSV/mock.csv");
            File dest = new File(csvPath + "/mock.csv");
            Files.copy(inputUrl.openStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        for(String modelName : modelNames) {
            try {
                if(!fileExists(csvPath + "/" + modelName + ".csv")) {
                    URL url = ModelInit.class.getResource("/CSV/" + modelName + ".csv");
                    File file = new File(csvPath + "/" + modelName + ".csv");
                    Files.copy(url.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e){
                System.out.println("Error while copying " + modelName + " table: " + e.getMessage());
            }
        }
    }

    public static void init() throws Exception{

        DatabaseConnection db  = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.ORACLE);
        db.connect();


        DatabaseConnection csv = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.CSV);

        // Make path to Log and CSV
        String csvPath = Paths.get(System.getProperty("user.dir") + "/CSV").toString();

        // Make Log and CSV directories
        try {
            Files.createDirectories(Paths.get(csvPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String modelName : modelNames) {
            try{
                csv.setUrl("/CSV/" + modelName + ".csv");
                csv.makeConnectionString();
                if(!csv.isInitialized()) {
                    csv.createTable(modelName + ".csv", null, null);

                    ResultSet rs = db.getAllTableData(modelName);

                    // Get rs headers
                    ArrayList<String> headers = new ArrayList<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        headers.add(rs.getMetaData().getColumnName(i));
                    }

                    // Make headers as String[]
                    String[] headersStrings = new String[headers.size()];
                    for (String header : headers) {
                        headersStrings[headers.indexOf(header)] = header;
                    }

                    // Get rs data
                    ArrayList<ArrayList<String>> data = new ArrayList<>();
                    while (rs.next()) {
                        ArrayList<String> row = new ArrayList<>();
                        for (String header : headers) {
                            row.add(rs.getString(header));
                        }
                        data.add(row);
                    }

                    // Make data as List<String[]>
                    ArrayList<String[]> dataStrings = new ArrayList<>();
                    for (ArrayList<String> row : data) {
                        String[] rowStrings = new String[row.size()];
                        for(int i = 0; i < row.size(); i++)
                            rowStrings[i] = row.get(i);
                        dataStrings.add(rowStrings);
                    }

                    ((CsvConnection)csv).setFolder("CSV/");
                    csv.createAndInsert(modelName + ".csv", headersStrings, dataStrings);
                }
            } catch (Exception e){
                System.out.println("Error while creating " + modelName + " table: " + e.getMessage());
            }
        }

    }
}
