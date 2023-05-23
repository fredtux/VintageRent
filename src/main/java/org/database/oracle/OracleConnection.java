package org.database.oracle;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.database.DatabaseConnection;
import org.models.Pair;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class OracleConnection extends DatabaseConnection {
    private static OracleConnection instance = null;
    private static final String DB_INIT_FILE = "/Migrations/init.sql";
    public OracleConnection(String url, String username, String password, String driver, String database, String schema, String port) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
        else {
            instance = this;
            instances.add(this);
        }

        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
        this.database = database;
        this.schema = schema;
        this.port = port;

        makeConnectionString();

        try{
            logger.log("New instance of OracleConnection created");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    public static DatabaseConnection getInstance(DatabaseType t) throws RuntimeException {
        if(instance == null || t != DatabaseType.ORACLE)
            throw new RuntimeException("No instance of DatabaseConnection has been created");

        for(DatabaseConnection db : instances) {
            if(db instanceof OracleConnection)
                return db;
        }

        throw new RuntimeException("No instance of DatabaseConnection of the specified DatabaseType has been created");
    }

    @Override
    public void makeConnectionString() {
        this.connString = "jdbc:oracle:thin:" + "@" + this.url + ":" + this.port + ":" + this.database;
    }

    @Override
    public void connect() throws Exception {
        try {
            Class.forName(this.driver);
            this.conn = DriverManager.getConnection(this.connString, this.username, this.password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error connecting to Oracle Database: " + ex.getMessage());
            throw new ClassNotFoundException("Error connecting to Oracle Database: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Error connecting to Oracle Database: " + ex.getMessage());
            throw new SQLException("Error connecting to Oracle Database: " + ex.getMessage());
        }

        try{
            logger.log("Connected to Oracle Database");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void disconnect() throws Exception {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.out.println("Error disconnecting from Oracle Database: " + ex.getMessage());
            throw new SQLException("Error disconnecting from Oracle Database: " + ex.getMessage());
        }

        try{
            logger.log("Disconnected to Oracle Database");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public ResultSet executeQuery(String query) throws Exception {
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            ResultSet res = stmt.executeQuery(query);
            try{
                logger.log("OracleConnection query executed");
            } catch (Exception ex) {
                System.out.println("Error logging to CSV: " + ex.getMessage());
            }

            return res;
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage() + ":" + ex.getErrorCode());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }
    }

    @Override
    public boolean isInitialized() throws Exception {
        try{
            logger.log("OracleConnection checking initialization");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
        try {
            ResultSet rs = executeQuery("SELECT COUNT(*) AS CNT FROM user_tables");
            while(rs.next()){
                int count = rs.getInt("CNT");
                return count > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error checking if database is initialized: " + ex.getMessage());
            throw new SQLException("Error checking if database is initialized: " + ex.getMessage());
        }

        return false;
    }

    @Override
    public void init() throws Exception {
        try {
            Reader initScript = readFile(DB_INIT_FILE);
            ScriptRunner sr = new ScriptRunner(this.conn);
            sr.setLogWriter(null);
            sr.runScript(initScript);
            initScript.close();

            try{
                logger.log("OracleConnection database initialized");
            } catch (Exception ex) {
                System.out.println("Error logging to CSV: " + ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("Error reading init script: " + ex.getMessage());
            throw new IOException("Error reading init script: " + ex.getMessage());
        }
    }

    @Override
    public void update(String tableName, Map<String, String> set, Map<String, String> where) throws Exception {
        Statement stmt = null;
        try{
            String query = "UPDATE " + tableName + " SET ";
            for(Map.Entry<String, String> entry : set.entrySet()){
                query += entry.getKey() + " = " + entry.getValue() + ", ";
            }
            query = query.substring(0, query.length() - 2);
            query += " WHERE ";
            for(Map.Entry<String, String> entry : where.entrySet()){
                query += entry.getKey() + " = " + entry.getValue() + " AND ";
            }
            query = query.substring(0, query.length() - 5);

            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);

            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }

        try{
            logger.log("OracleConnection executed update");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public ResultSet getAllTableData(String tableName) throws Exception {
        return this.executeQuery("SELECT * FROM " + tableName);
    }

    @Override
    public void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception {

    }

    @Override
    public void createTable(String tableName, String[] columns, String[] types) throws Exception {

    }

    @Override
    public void insert(String tableName, List<Pair<String, String>> values) throws Exception {
        Statement stmt = null;
        try{
            String query = "INSERT INTO " + tableName + " (";
            for(Pair<String, String> entry : values){
                query += entry.first + ", ";
            }
            query = query.substring(0, query.length() - 2);
            query += ") VALUES (";
            for(Pair<String, String> entry : values){
                if(entry.second == ""){
                    int max = this.getNewId(tableName, entry.first);
                    entry.second = String.valueOf(max);
                }

                query += entry.second + ", ";
            }
            query = query.substring(0, query.length() - 2);
            query += ")";

            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }

        try{
            logger.log("OracleConnection executed insert");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void delete(String tableName, Map<String, String> where) throws Exception {
        Statement stmt = null;
        try{
            String query = "DELETE FROM " + tableName + " WHERE ";
            for(Map.Entry<String, String> entry : where.entrySet()){
                query += entry.getKey() + " = " + entry.getValue() + " AND ";
            }
            query = query.substring(0, query.length() - 5);

            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }

        try{
            logger.log("OracleConnection executed delete");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void drop(String tableName) throws Exception {

    }

    @Override
    public void truncate(String tableName) throws Exception {
        Statement stmt = null;
        try{
            String query = "TRUNCATE TABLE " + tableName ;

            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }

        try{
            logger.log("OracleConnection executed truncate");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void init(String[] columns) throws Exception {

    }

    @Override
    public int getNewId(String tableName, String column) throws Exception {
        Statement stmt = null;
        try{
            String query = "SELECT MAX(" + column + ") AS MAX_ID FROM " + tableName;

            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int maxId = 0;
            while(rs.next()){
                maxId = rs.getInt("MAX_ID");
            }

            stmt.close();
            return maxId + 1;
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }
    }

    private Reader readFile(String resourcePath) throws IOException{
        InputStream in = getClass().getResourceAsStream(resourcePath);
        InputStreamReader isr = new InputStreamReader(in);
        return new BufferedReader(isr);
    }


}
