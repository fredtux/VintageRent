package org.database.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.database.DatabaseConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.models.Pair;

public class CsvConnection extends DatabaseConnection {
    private static CsvConnection instance = null;

    private String path = null;
    private String folder = "CSV/";
    private CSVReader reader = null;
    private CSVWriter writer = null;
    private CSVParser parser = null;

    public CsvConnection() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CsvConnection is a singleton class. Use getInstance() instead.");
        else{
            instance = this;
            instances.add(this);
        }
    }

    public static DatabaseConnection getInstance(DatabaseType t) throws RuntimeException {
        if(instance == null || t != DatabaseType.CSV)
            throw new RuntimeException("No instance of DatabaseConnection has been created");

        for(DatabaseConnection db : instances) {
            if(db instanceof CsvConnection && t == DatabaseType.CSV)
                return db;
        }

        throw new RuntimeException("No instance of DatabaseConnection of the specified DatabaseType has been created");

    }

    public CsvConnection(String path) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CsvConnection is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.path = path;
    }

    public CsvConnection setPath(String path) {
        this.path = path;
        return this;
    }

    public void setParser(CSVParser c){
        this.parser = c;
    }
    @Override
    public void makeConnectionString() {
        this.path = this.url;
    }

    @Override
    public void connect() throws Exception {
    }

    @Override
    public void disconnect() throws Exception {
    }

    @Override
    public ResultSet executeQuery(String query) throws Exception {
        return null;
    }

    @Override
    public boolean isInitialized() throws Exception {
        try {
            String filePath = Paths.get(System.getProperty("user.dir") + path).toString();

            Reader r = new BufferedReader(new FileReader(filePath));
            this.reader = new CSVReader(r);
            String line[] = null;
            if ((line = this.reader.readNext()) != null) {
                this.reader.close();
                r.close();
                return true;
            }

            this.reader.close();
            r.close();

            try{
                logger.log("CsvConnection check initialization");
            } catch (Exception ex) {
                System.out.println("Error logging to CSV: " + ex.getMessage());
            }
            return false;
        } catch (Exception ex) {
            try{
                logger.log("CsvConnection check initialization");
            } catch (Exception ex2) {
                System.out.println("Error logging to CSV: " + ex2.getMessage());
            }
            return false;
        }
    }

    @Override
    public void init() throws Exception {
        throw new UnsupportedOperationException("Not supported without parameters");
    }

    public void init(String[] columns) throws Exception {
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + path).toString();

        // Create file at this.path
        File f = new File(filePath);
        f.createNewFile();

        this.writer = new CSVWriter(new FileWriter(filePath));
        this.writer.writeNext(columns);
        this.writer.close();

        try{
            logger.log("CsvConnection file initialized");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public int getNewId(String tableName, String column) throws Exception {
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();
        Reader r = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(r);

        List<String[]> lresult = this.reader.readAll();

        this.reader.close();

        List<String[]> sdata = null;

        // Use the rest of the lines as data
        if(lresult.size() > 1){
            sdata = lresult.subList(1, lresult.size());
        }

        if(sdata == null){
            return 1;
        }

        int max = 0;

        for (String[] strings : sdata) {
            if(Integer.parseInt(strings[0]) > max)
                max = Integer.parseInt(strings[0]);
        }

        return max + 1;

    }

    @Override
    public void update(String tableName, Map<String, String> set, Map<String, String> where) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + this.path).toString();
        Reader r = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(r);

        List<String[]> lresult = this.reader.readAll();

        this.reader.close();

        List<String> headers = null;
        List<String[]> sdata = null;
        List<List<Object>> data = null;

        // Get first line and set headers
        if(lresult.size() > 0){
            headers = Arrays.asList(lresult.get(0));
        }

        // Use the rest of the lines as data
        if(lresult.size() > 1){
            sdata = lresult.subList(1, lresult.size());
        }

        // Transform sdata to data
        if(sdata != null){
            data = new java.util.ArrayList<List<Object>>();
            for (String[] strings : sdata) {
                data.add(Arrays.asList(strings));
            }
        }

        // Update data
        for (List<Object> list : data) {
            for (int i = 0; i < list.size(); i++) {
                for (Map.Entry<String, String> entry : set.entrySet()) {
                    if(headers.get(i).equals(entry.getKey()) && this.hasPrimaryKey(headers, where, list)){
                        String s = entry.getValue();
                        if (s.substring(0, 1).equals("'"))
                            s = s.substring(1, s.length() - 1);
                        if(s.length() > 0 && s.substring(s.length() - 1, s.length()).equals("'"))
                            s = s.substring(0, s.length() - 1);

                        list.set(i, s);
                    }
                }
            }
        }

        // Write data to file
        this.writer = new CSVWriter(new FileWriter(filePath));
        this.writer.writeNext(headers.toArray(new String[headers.size()]));
        for (List<Object> list : data) {
            this.writer.writeNext(list.toArray(new String[list.size()]));
        }
        this.writer.close();
    }

    private boolean hasPrimaryKey(List<String> headers, Map<String, String> where, List<Object> row) {
        int counter = 0;
        for (Map.Entry<String, String> entry : where.entrySet()) {
            for (int i = 0; i < headers.size(); i++) {
                if(headers.get(i).toLowerCase().equals(entry.getKey().toLowerCase()) && row.get(i).equals(entry.getValue())){
                    ++counter;
                    break;
                }
            }
        }

        return counter == where.size();
    }

    @Override
    public ResultSet getAllTableData(String tableName) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();

        Reader r = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(r);

        List<String[]> lresult = this.reader.readAll();

        this.reader.close();

        List<String> headers = null;
        List<String[]> sdata = null;
        List<List<Object>> data = null;

        // Get first line and set headers
        if(lresult.size() > 0){
            headers = java.util.Arrays.asList(lresult.get(0));
        }

        // Use the rest of the lines as data
        if(lresult.size() > 1){
            sdata = lresult.subList(1, lresult.size());
        }

        // Transform sdata to data
        if(sdata != null){
            data = new java.util.ArrayList<List<Object>>();
            for (String[] strings : sdata) {
                data.add(java.util.Arrays.asList(strings));
            }
        }

        try{
            logger.log("CsvConnection got all table data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.getResultSet(headers, data);

    }

    public ResultSet getResultSet(List<String> headers, List<List<Object>> data) throws Exception {

        // validation
        if (headers == null) {
            throw new Exception("null parameters");
        }

        // create a mock result set
        MockResultSet mockResultSet = new MockResultSet("myResultSet");

        // add header
        for (String string : headers) {
            mockResultSet.addColumn(string);
        }

        // add data
        if(data != null) {
            for (List<Object> list : data) {
                mockResultSet.addRow(list);
            }
        }

        return mockResultSet;
    }

    @Override
    public void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception {
        try {
            this.setPath(tableName);
            String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();

            File f = new File(filePath);
            this.writer = new CSVWriter(new FileWriter(f));
            if(columns != null)
                this.writer.writeNext(columns);
            for(String[] s : values){
                for(int i = 0; i < s.length; i++){
                    if(s[i] == null){
                        s[i] = "";
                    }
                }
            }
            this.writer.writeAll(values);
            this.writer.close();
        } catch (NullPointerException e) {
            this.createAndInsertDynamically(tableName, columns, values);
        }

        try{
            logger.log("CsvConnection create and insert");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    public void createAndInsertDynamically(String tableName, String[] columns, List<String[]> values) throws Exception {
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + "mock.csv").toString();

        File mock = new File(filePath);
        File parent = mock.getParentFile();

        File f = new File(parent, tableName);
        f.createNewFile();
        this.writer = new CSVWriter(new FileWriter(f));
        this.writer.writeNext(columns);
        this.writer.writeAll(values);
        this.writer.close();

        try{
            logger.log("CsvConnection created and inserted dynamically");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void createTable(String tableName, String[] columns, String[] types) throws Exception {
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + "mock.csv").toString();

        File mock = new File(filePath);
        File parent = mock.getParentFile();

        File f = new File(parent, tableName);
        f.createNewFile();

        try{
            logger.log("CsvConnection file created");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }



    @Override
    public void insert(String tableName, List<Pair<String, String>> values) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();

        File f = new File(filePath);
        this.writer = new CSVWriter(new FileWriter(f, true));
        String[] row = new String[values.size()];
        int i = 0;
        for (Pair<String, String> pair : values) {
            if(pair.second == ""){
                int newId = this.getNewId(tableName, pair.first);
                pair.second = String.valueOf(newId);
            }

            if(pair.second.substring(0,1).equals("'"))
                pair.second = pair.second.substring(1, pair.second.length());
            if(pair.second.substring(pair.second.length()-1, pair.second.length()).equals("'"))
                pair.second = pair.second.substring(0, pair.second.length()-1);

            row[i] = pair.second;
            ++i;
        }
        this.writer.writeNext(row);
        this.writer.close();

        try {
            logger.log("CsvConnection data inserted");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    public void insertNoLog(String tableName, String[] columns, List<String[]> values) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/Log/Log.csv" ).toString();

        File f = new File(filePath);
        this.writer = new CSVWriter(new FileWriter(f, true));
        this.writer.writeNext(columns);
        this.writer.writeAll(values);
        this.writer.close();
    }

    @Override
    public void delete(String tableName, Map<String, String> where) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();

        Reader r = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(r);

        List<String[]> lresult = this.reader.readAll();

        this.reader.close();

        List<String> headers = null;
        List<String[]> sdata = null;
        List<List<Object>> data = null;

        // Get first line and set headers
        if(lresult.size() > 0){
            headers = Arrays.asList(lresult.get(0));
        }

        // Use the rest of the lines as data
        if(lresult.size() > 1){
            sdata = lresult.subList(1, lresult.size());
        }

        // Transform sdata to data
        if(sdata != null){
            data = new java.util.ArrayList<List<Object>>();
            for (String[] strings : sdata) {
                data.add(Arrays.asList(strings));
            }
        }

        // Delete data
        for (List<Object> list : data) {
            if(this.hasPrimaryKey(headers, where, list)){
                data.remove(list);
                break;
            }
        }

        // Write data to file
        this.writer = new CSVWriter(new FileWriter(filePath));
        this.writer.writeNext(headers.toArray(new String[headers.size()]));
        for (List<Object> list : data) {
            this.writer.writeNext(list.toArray(new String[list.size()]));
        }
        this.writer.close();

        try{
            logger.log("CsvConnection delete");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void drop(String tableName) throws Exception {

    }

    @Override
    public void truncate(String tableName) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/CSV/" + tableName).toString();

        File f = new File(filePath);

        // Keep first line, delete the rest
        Reader r = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(r);
        List<String[]> lresult = this.reader.readAll();
        this.reader.close();
        List<String> headers = null;
        if(lresult.size() > 0){
            headers = Arrays.asList(lresult.get(0));
        }
        this.writer = new CSVWriter(new FileWriter(filePath));
        this.writer.writeNext(headers.toArray(new String[headers.size()]));
        this.writer.close();

        try{
            logger.log("CsvConnection file truncated");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    public CsvConnection setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public Pair<List<String>, List<List<String>>> readAllNoLog(String tableName) throws Exception {
        this.setPath(tableName);
        String filePath = Paths.get(System.getProperty("user.dir") + "/Log/Log.csv").toString();

        Reader reader = new BufferedReader(new FileReader(filePath));
        this.reader = new CSVReader(reader);

        List<String[]> lresult = this.reader.readAll();

        this.reader.close();

        List<String> headers = null;
        List<String[]> sdata = null;
        List<List<String>> data = null;

        // Get first line and set headers
        if(lresult.size() > 0){
            headers = java.util.Arrays.asList(lresult.get(0));
        }

        // Use the rest of the lines as data
        if(lresult.size() > 1){
            sdata = lresult.subList(1, lresult.size());
        }

        // Transform sdata to data
        if(sdata != null){
            data = new java.util.ArrayList<>();
            for (String[] strings : sdata) {
                data.add(java.util.Arrays.asList(strings));
            }
        }

        return new Pair<>(headers, data);
    }
}
