package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.oracle.OracleConnection;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

public class ModelInit {
    private static String[] modelNames = {"Angajati", "Inchiriere", "Administrator_Subdomenii", "Administratori", "Adrese", "Camere","Clienti", "Format", "Montura", "Obiective", "Salariu", "TipCamera", "TipClient", "Utilizatori"};

    public static void copyFromJar(URI uri, final Path target) throws IOException {
//        URI uri = new URI(source);
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/CSV/");
        } else {
            myPath = Paths.get(uri);
        }
        Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(file.getFileName()));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void logInit() throws Exception{
        String logPath = Paths.get(System.getProperty("user.dir") + "/Log").toString();
        try {
            Files.createDirectories(Paths.get(logPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        URI logUri = ModelInit.class.getResource("/Log").toURI();
        try {
            copyFromJar(logUri, Paths.get(System.getProperty("user.dir") + "/Log"));
        } catch (Exception e){
            System.out.println("Log already exists");
        }

    }

    public static void init() throws Exception{
        // Make path to Log and CSV
        String csvPath = Paths.get(System.getProperty("user.dir") + "/CSV").toString();

        // Make Log and CSV directories
        try {
            Files.createDirectories(Paths.get(csvPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get Log and CSV directories URI from resources

        URI csvUri = ModelInit.class.getResource("/CSV").toURI();



        try{
            copyFromJar(csvUri, Paths.get(System.getProperty("user.dir") + "/CSV"));
        } catch (Exception e){
            System.out.println("CSV already exists");
        }


        DatabaseConnection db = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.ORACLE);
        db.connect();
        DatabaseConnection csv = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.CSV);

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
