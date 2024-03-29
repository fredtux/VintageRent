package org.database;

import org.database.csv.CsvConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.logger.CsvLogger;
import org.models.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DatabaseConnection {
    protected static ArrayList<DatabaseConnection> instances = new ArrayList<>();

    public enum DatabaseType {
        ORACLE,
        CSV,
        INMEMORY
    }
    protected Connection conn;
    protected String url;
    protected String username;
    protected String password;
    protected String driver;
    protected String database;
    protected String schema;
    protected String port;
    protected String connString;

    protected CsvLogger logger = CsvLogger.getInstance();

    public static DatabaseConnection getInstance(DatabaseType t) throws RuntimeException {
        if(instances == null)
            throw new RuntimeException("No instance of DatabaseConnection has been created");

        for(DatabaseConnection db : instances) {
            if(db instanceof OracleConnection && t == DatabaseType.ORACLE)
                return db;
            else if(db instanceof CsvConnection && t == DatabaseType.CSV)
                return db;
            else if(db instanceof InMemory && t == DatabaseType.INMEMORY)
                return db;
        }

        throw new RuntimeException("No instance of DatabaseConnection of the specified DatabaseType has been created");
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public abstract void makeConnectionString();

    public abstract void connect() throws Exception;

    public abstract void disconnect() throws Exception;

    public abstract ResultSet executeQuery(String query) throws Exception;


    public abstract boolean isInitialized() throws Exception;

    public abstract void init() throws Exception;

    public abstract void update(String tableName, Map<String, String> set, Map<String, String> where) throws Exception;

    public abstract ResultSet getAllTableData(String tableName) throws Exception;

    public abstract void createAndInsert(String tableName, String[] columns, List<String[]> values) throws Exception;

    public abstract void createTable(String tableName, String[] columns, String[] types) throws Exception;

    public abstract void insert(String tableName, List<Pair<String, String>> values) throws Exception;

    public abstract void delete(String tableName, Map<String, String> where) throws Exception;

    public abstract void drop(String tableName) throws Exception;

    public abstract void truncate(String tableName) throws Exception;

    public abstract void init(String[] columns) throws Exception;

    public abstract int getNewId(String tableName, String column) throws Exception;
}
