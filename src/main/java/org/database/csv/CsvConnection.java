package org.database.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.database.DatabaseConnection;

import java.io.*;
import java.sql.ResultSet;
import java.util.List;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.database.oracle.OracleConnection;

public class CsvConnection extends DatabaseConnection {
    private static CsvConnection instance = null;

    private String path = null;
    private CSVReader reader = null;
    private CSVWriter writer = null;
    private CSVParser parser = null;

    public CsvConnection() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
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
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
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
    }

    @Override
    public void connect() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        this.reader = new CSVReader(new FileReader(classLoader.getResource(this.path).getFile()));
    }

    @Override
    public void disconnect() throws Exception {
        this.reader.close();
    }

    @Override
    public ResultSet executeQuery(String query) throws Exception {
        return null;
    }

    @Override
    public boolean isInitialized() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Reader r = new BufferedReader(new FileReader(classLoader.getResource(path).getFile()));
        this.reader = new CSVReader(r);
        String line[] = null;
        if((line = this.reader.readNext()) != null){
            this.reader.close();
            r.close();
            return true;
        }

        this.reader.close();
        r.close();
        return false;
    }

    @Override
    public void init() throws Exception {
        throw new UnsupportedOperationException("Not supported without parameters");
    }

    public void init(String[] columns) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        // Create file at this.path
        File f = new File(classLoader.getResource(path).getFile());
        f.createNewFile();

        this.writer = new CSVWriter(new FileWriter(classLoader.getResource(path).getFile()));
        this.writer.writeNext(columns);
        this.writer.close();
    }

    @Override
    public void update(String query) throws Exception {
        throw new UnsupportedOperationException("Not supported without primary key");
    }

    @Override
    public ResultSet getAllTableData(String tableName) throws Exception {
        this.setPath(tableName);
        ClassLoader classLoader = getClass().getClassLoader();
        Reader r = new BufferedReader(new FileReader(classLoader.getResource("/CSV/" + this.path).getFile()));
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

        return this.getResultSet(headers, data);

    }

    public ResultSet getResultSet(List<String> headers, List<List<Object>> data) throws Exception {

        // validation
        if (headers == null || data == null) {
            throw new Exception("null parameters");
        }

        if (headers.size() != data.size()) {
            throw new Exception("parameters size are not equals");
        }


        // create a mock result set
        MockResultSet mockResultSet = new MockResultSet("myResultSet");

        // add header
        for (String string : headers) {
            mockResultSet.addColumn(string);
        }

        // add data
        for (List<Object> list : data) {
            mockResultSet.addRow(list);
        }

        return mockResultSet;
    }

    @Override
    public void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception {
        try {
            this.setPath(tableName);
            ClassLoader classLoader = getClass().getClassLoader();
            File f = new File(classLoader.getResource("CSV/" + this.path).getFile());
            this.writer = new CSVWriter(new FileWriter(f));
            this.writer.writeNext(columns);
            this.writer.writeAll(values);
            this.writer.close();
        } catch (NullPointerException e) {
            this.createAndInsertDynamically(tableName, columns, values);
        }
    }

    public void createAndInsertDynamically(String tableName, String[] columns, List<String[]> values) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File mock = new File(this.getClass().getResource("/CSV/mock.csv").getFile());
        File parent = mock.getParentFile();

        System.out.println(parent.getPath());
        File f = new File(parent, tableName);
        f.createNewFile();
        this.writer = new CSVWriter(new FileWriter(f));
        this.writer.writeNext(columns);
        this.writer.writeAll(values);
        this.writer.close();
    }

    @Override
    public void createTable(String tableName, String[] columns, String[] types) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File mock = new File(this.getClass().getResource("/CSV/mock.csv").getFile());
        File parent = mock.getParentFile();

        System.out.println(parent.getPath());
        File f = new File(parent, tableName);
        f.createNewFile();
    }

    @Override
    public void insert(String tableName, String[] columns, List<String[]> values) throws Exception {
        this.setPath(tableName);
        ClassLoader classLoader = getClass().getClassLoader();
        File f = new File(classLoader.getResource("CSV/" + this.path).getFile());
        this.writer = new CSVWriter(new FileWriter(f, true));
        this.writer.writeNext(columns);
        this.writer.writeAll(values);
        this.writer.close();
    }

    @Override
    public void delete(String tableName, String[] columns, List<String[]> values) throws Exception {

    }

    @Override
    public void drop(String tableName) throws Exception {

    }

    @Override
    public void truncate(String tableName) throws Exception {
        this.setPath(tableName);
        ClassLoader classLoader = getClass().getClassLoader();
        File f = new File(classLoader.getResource("CSV/" + this.path).getFile());
        this.writer = new CSVWriter(new FileWriter(f));
        this.writer.writeNext(null); // write nothing
        this.writer.close();
    }
}
