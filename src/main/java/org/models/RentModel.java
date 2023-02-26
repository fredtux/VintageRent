package org.models;

import org.database.DatabaseConnection;
import org.database.oracle.OracleConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Date;
import java.sql.ResultSet;

public class RentModel extends Model implements LinkModelToDatabase<ModelList> {
    private class InnerRentModel{
        public int DURATA_IN_ZILE;
        public boolean ESTE_RETURNAT;
        public double PENALIZARE;
        public Date DATA_INCHIRIERE;
        public int IDCAMERA;
        public int IDCLIENT;
        public int IDOBIECTIV;
        public int IDANGAJAT;
    }
    private String tableName = "INCHIRIERE";

    private RentModel instance = null;
    private ModelList<InnerRentModel> modelList = null;

    public ModelList<InnerRentModel> getModelList() {
        return modelList;
    }

    public DefaultTableModel getTableModel() {
        String columns[] = {"DURATA_IN_ZILE", "ESTE_RETURNAT", "PENALIZARE", "DATA_INCHIRIERE", "IDCAMERA", "IDCLIENT", "IDOBIECTIV", "IDANGAJAT"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerRentModel model : this.modelList.getList()){
            Object obj[] = {model.DURATA_IN_ZILE, model.ESTE_RETURNAT, model.PENALIZARE, model.DATA_INCHIRIERE, model.IDCAMERA, model.IDCLIENT, model.IDOBIECTIV, model.IDANGAJAT};
            tableModel.addRow(obj);
        }

        return tableModel;
    }

    public RentModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("RentModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Rent";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<InnerRentModel>();

        while(rs.next()){
            InnerRentModel model = new InnerRentModel();
            model.DURATA_IN_ZILE = rs.getInt("DURATAINZILE");
            model.DATA_INCHIRIERE = rs.getDate("DATAINCHIRIERE");
            model.IDANGAJAT = rs.getInt("IDANGAJAT");
            model.IDCAMERA = rs.getInt("IDCAMERA");
            model.IDCLIENT = rs.getInt("IDCLIENT");
            model.IDOBIECTIV = rs.getInt("IDOBIECTIV");
            model.ESTE_RETURNAT = rs.getBoolean("ESTERETURNAT");
            model.PENALIZARE = rs.getDouble("PENALIZARE");
            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerRentModel> getData() throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " LIMIT " + limit);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit, String offset)  throws Exception{
        DatabaseConnection db = OracleConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " LIMIT " + limit + " OFFSET " + offset);

        this.transferToModelList(rs);

        return this.modelList;
    }
}
