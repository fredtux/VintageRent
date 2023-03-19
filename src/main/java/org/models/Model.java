package org.models;

import org.database.DatabaseConnection;
import org.logger.CsvLogger;

import javax.swing.table.TableModel;

public abstract class Model {
    protected static Model instance = null;
    protected DatabaseConnection.DatabaseType databaseType = null;
    String name;
    protected CsvLogger logger = CsvLogger.getInstance();

    public static abstract class AbstractInnerModel {
    }

    public static Model getInstance() throws Exception{
        if(instance == null)
            throw new RuntimeException("No instance of Model has been created");

        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void setDatabaseType(DatabaseConnection.DatabaseType databaseType);

    public abstract ModelList getModelList();

    public abstract TableModel getTableModel();
}
