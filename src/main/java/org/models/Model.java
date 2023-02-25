package org.models;

public abstract class Model {
    protected static Model instance = null;
    String name;

    public Model getInstance() throws Exception{
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
}
