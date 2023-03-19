package org.models;

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
import java.sql.ResultSet;
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
