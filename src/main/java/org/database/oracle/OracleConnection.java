package org.database.oracle;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.database.DatabaseConnection;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleConnection extends DatabaseConnection {
    private static final String DB_INIT_FILE = "Migrations/init.sql";
    public OracleConnection(String url, String username, String password, String driver, String database, String schema, String port) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("OracleConnection is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
        this.database = database;
        this.schema = schema;
        this.port = port;

        makeConnectionString();
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
    }

    @Override
    public void disconnect() throws Exception {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.out.println("Error disconnecting from Oracle Database: " + ex.getMessage());
            throw new SQLException("Error disconnecting from Oracle Database: " + ex.getMessage());
        }
    }

    @Override
    public ResultSet executeQuery(String query) throws Exception {
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            ResultSet res = stmt.executeQuery(query);
            return res;
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }

    }

    @Override
    public boolean isInitialized() throws Exception {
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
        } catch (IOException ex) {
            System.out.println("Error reading init script: " + ex.getMessage());
            throw new IOException("Error reading init script: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Error initializing database: " + ex.getMessage());
            throw new SQLException("Error initializing database: " + ex.getMessage());
        }
    }

    @Override
    public void update(String query) throws Exception {
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error executing query: " + ex.getMessage());
            throw new SQLException("Error executing query: " + ex.getMessage());
        }
    }

    private Reader readFile(String resourcePath) throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        return new BufferedReader(new FileReader(classLoader.getResource(resourcePath).getFile()));
    }


}
