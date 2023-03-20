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
    private static String[] modelNames = {"Angajati", "Inchiriere", "Administrator_Subdomenii", "Administratori", "Adrese", "Camere","Clienti", "Format", "Montura", "Obiective", "Salariu", "TipCamera", "TipClient", "Utilizatori"};

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
            data.IDMontura = 1;
            data.IDFormat = 1;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca1";
            data.IDTip = 1;

            MainService.insert(cameraModel, data);

            data.IDCamera = 2;
            data.ModelCamera = "Camera2";
            data.IDMontura = 2;
            data.IDFormat = 2;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca2";
            data.IDTip = 2;

            MainService.insert(cameraModel, data);

            data.IDCamera = 3;
            data.ModelCamera = "Camera3";
            data.IDMontura = 1;
            data.IDFormat = 1;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca3";
            data.IDTip = 3;

            MainService.insert(cameraModel, data);

            data.IDCamera = 4;
            data.ModelCamera = "Camera4";
            data.IDMontura = 1;
            data.IDFormat = 3;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca4";
            data.IDTip = 1;

            MainService.insert(cameraModel, data);

            data.IDCamera = 5;
            data.ModelCamera = "Camera5";
            data.IDMontura = 4;
            data.IDFormat = 2;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca5";
            data.IDTip = 4;

            MainService.insert(cameraModel, data);

            data.IDCamera = 6;
            data.ModelCamera = "Camera6";
            data.IDMontura = 2;
            data.IDFormat = 2;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca6";
            data.IDTip = 2;

            MainService.insert(cameraModel, data);

            data.IDCamera = 7;
            data.ModelCamera = "Camera7";
            data.IDMontura = 3;
            data.IDFormat = 3;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca7";
            data.IDTip = 3;

            MainService.insert(cameraModel, data);

            data.IDCamera = 8;
            data.ModelCamera = "Camera8";
            data.IDMontura = 1;
            data.IDFormat = 1;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Marca8";
            data.IDTip = 1;

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
            data.IDTip = 1;
            data.Denumire = "Camera1";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 2;
            data.Denumire = "Camera2";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 3;
            data.Denumire = "Camera3";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 4;
            data.Denumire = "Camera4";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 5;
            data.Denumire = "Camera5";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 6;
            data.Denumire = "Camera6";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 7;
            data.Denumire = "Camera7";

            MainService.insert(cameraTypeModel, data);

            data.IDTip = 8;
            data.Denumire = "Camera8";

            MainService.insert(cameraTypeModel, data);
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
            data.IDUtilizator = 1;
            data.NumeClient = "ClientName1";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 2;
            data.NumeClient = "ClientName2";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 3;
            data.NumeClient = "ClientName3";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 4;
            data.NumeClient = "ClientName4";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 5;
            data.NumeClient = "ClientName5";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 6;
            data.NumeClient = "ClientName6";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);
            data.IDUtilizator = 7;
            data.NumeClient = "ClientName7";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            data.IDUtilizator = 8;
            data.NumeClient = "ClientName8";
            data.DataNasterii = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

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
            data.IDTip = 1;
            data.Denumire = "ClientType1";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 2;
            data.Denumire = "ClientType2";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 3;
            data.Denumire = "ClientType3";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 4;
            data.Denumire = "ClientType4";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 5;
            data.Denumire = "ClientType5";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 6;
            data.Denumire = "ClientType6";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 7;
            data.Denumire = "ClientType7";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            data.IDTip = 8;
            data.Denumire = "ClientType8";
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
            data.IDUtilizator = 1;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 1;
            data.IDManager = 1;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 2;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 2;
            data.IDManager = 1;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 3;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 2;
            data.IDManager = 1;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 4;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 3;
            data.IDManager = 2;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 5;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 4;
            data.IDManager = 4;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 6;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 2;
            data.IDManager = 2;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 7;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 2;
            data.IDManager = 2;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            data.IDUtilizator = 8;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:01", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:01", dtf);
            data.IDSalariu = 5;
            data.IDManager = 5;
            data.NumeAngajat = "Test";

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
            data.IDFormat = 1;
            data.Denumire = "Format1";
            data.LatimeFilm = "Latime1";

            MainService.insert(formatModel, data);

            data.IDFormat = 2;
            data.Denumire = "Format2";
            data.LatimeFilm = "Latime2";

            MainService.insert(formatModel, data);

            data.IDFormat = 3;
            data.Denumire = "Format3";
            data.LatimeFilm = "Latime3";

            MainService.insert(formatModel, data);

            data.IDFormat = 4;
            data.Denumire = "Format4";
            data.LatimeFilm = "Latime4";

            MainService.insert(formatModel, data);

            data.IDFormat = 5;
            data.Denumire = "Format5";
            data.LatimeFilm = "Latime5";

            MainService.insert(formatModel, data);

            data.IDFormat = 6;
            data.Denumire = "Format6";
            data.LatimeFilm = "Latime6";

            MainService.insert(formatModel, data);

            data.IDFormat = 7;
            data.Denumire = "Format7";
            data.LatimeFilm = "Latime7";

            MainService.insert(formatModel, data);

            data.IDFormat = 8;
            data.Denumire = "Format8";
            data.LatimeFilm = "Latime8";

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
            data.IDMontura = 1;
            data.Denumire = "Mount1";

            MainService.insert(mountModel, data);

            data.IDMontura = 2;
            data.Denumire = "Mount2";

            MainService.insert(mountModel, data);

            data.IDMontura = 3;
            data.Denumire = "Mount3";

            MainService.insert(mountModel, data);

            data.IDMontura = 4;
            data.Denumire = "Mount4";

            MainService.insert(mountModel, data);

            data.IDMontura = 5;
            data.Denumire = "Mount5";

            MainService.insert(mountModel, data);

            data.IDMontura = 6;
            data.Denumire = "Mount6";

            MainService.insert(mountModel, data);

            data.IDMontura = 7;
            data.Denumire = "Mount7";

            MainService.insert(mountModel, data);

            data.IDMontura = 8;
            data.Denumire = "Mount8";

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
            data.IDObiectiv = 1;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective1";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 2;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective2";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 3;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective3";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 4;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective4";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 5;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective5";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 6;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective6";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 7;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective7";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            data.IDObiectiv = 8;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.Denumire = "Objective8";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

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
            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 1;
            data.IDOBIECTIV = 1;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 1;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 2;
            data.IDANGAJAT = 2;
            data.IDCLIENT = 2;
            data.IDOBIECTIV = 2;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 2;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 2;
            data.IDOBIECTIV = 2;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 2;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 3;
            data.IDCLIENT = 3;
            data.IDOBIECTIV = 3;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 3;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 1;
            data.IDOBIECTIV = 1;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 1;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 2;
            data.IDCLIENT = 2;
            data.IDOBIECTIV = 2;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 2;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 3;
            data.IDCLIENT = 3;
            data.IDOBIECTIV = 3;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 3;
            data.PENALIZARE = 0;

            MainService.insert(mrentModel, data);

            data.DATA_INCHIRIERE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATA_IN_ZILE = 1;
            data.IDANGAJAT = 4;
            data.IDCLIENT = 4;
            data.IDOBIECTIV = 4;
            data.ESTE_RETURNAT = false;
            data.IDCAMERA = 4;
            data.PENALIZARE = 0;

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
            data.IDSalariu = 0;
            data.Salariu = 100;
            data.Bonus = 100;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 200;
            data.Bonus = 200;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 300;
            data.Bonus = 300;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 400;
            data.Bonus = 400;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 500;
            data.Bonus = 500;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 600;
            data.Bonus = 600;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 700;
            data.Bonus = 700;

            MainService.insert(msalaryModel, data);

            data.IDSalariu = 0;
            data.Salariu = 800;
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
            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user1";
            data.Prenume = "John1";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name1";

            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user2";
            data.Prenume = "John2";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name2";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user3";
            data.Prenume = "John3";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name3";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user4";
            data.Prenume = "John4";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name4";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user5";
            data.Prenume = "John5";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name5";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user6";
            data.Prenume = "John6";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name6";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user7";
            data.Prenume = "John7";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name7";


            MainService.insert(muserModel, data);

            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "user8";
            data.Prenume = "John8";
            data.CNP = "123";
            data.Email = "";
            data.Nume = "Name8";


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
