package org.database;

import org.database.oracle.OracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;

public abstract class DatabaseConnection {
    protected static DatabaseConnection instance = null;
    protected Connection conn;
    protected String url;
    protected String username;
    protected String password;
    protected String driver;
    protected String database;
    protected String schema;
    protected String port;
    protected String connString;

    public static DatabaseConnection getInstance() throws Exception {
        if(instance == null)
            throw new RuntimeException("No instance of DatabaseConnection has been created");

        return instance;
    }

    public Connection getConn() {
        return conn;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getConnString() {
        return connString;
    }

    public abstract void makeConnectionString();

    public abstract void connect() throws Exception;

    public abstract void disconnect() throws Exception;

    public abstract ResultSet executeQuery(String query) throws Exception;


    public abstract boolean isInitialized() throws Exception;

    public abstract void init() throws Exception;

}
