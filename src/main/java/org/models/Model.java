package org.models;

import org.database.DatabaseConnection;

public abstract class Model {
    protected static Model instance = null;
    protected DatabaseConnection.DatabaseType databaseType = null;
    String name;

    public static class AbstractInnerModel {
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
}
