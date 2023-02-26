package org.models;

import org.database.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;

public class RentModel extends Model implements LinkModelToDatabase<ModelList<RentModel.InnerRentModel>> {
    public static class InnerRentModel{
        public int DURATA_IN_ZILE;
        public boolean ESTE_RETURNAT;
        public double PENALIZARE;
        public Date DATA_INCHIRIERE;
        public int IDCAMERA;
        public int IDCLIENT;
        public int IDOBIECTIV;
        public int IDANGAJAT;
    }
    private final String tableName = "INCHIRIERE";

    private static RentModel instance = null;
    private ModelList<InnerRentModel> modelList = null;

    public ModelList<InnerRentModel> getModelList() {
        return modelList;
    }

    public static RentModel getInstance() {
        if (instance == null)
            instance = new RentModel();

        return instance;
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"DURATA_IN_ZILE", "ESTE_RETURNAT", "PENALIZARE", "DATA_INCHIRIERE", "IDCAMERA", "IDCLIENT", "IDOBIECTIV", "IDANGAJAT"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerRentModel model : this.modelList.getList()){
            Object[] obj = {model.DURATA_IN_ZILE, model.ESTE_RETURNAT, model.PENALIZARE, model.DATA_INCHIRIERE, model.IDCAMERA, model.IDCLIENT, model.IDOBIECTIV, model.IDANGAJAT};
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
        this.modelList = new ModelList<>();

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
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit, String offset)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerRentModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.update("UPDATE " + this.tableName + " SET DURATAINZILE = " + oneRow.get(0).DURATA_IN_ZILE + ", ESTERETURNAT = " + (oneRow.get(0).ESTE_RETURNAT ? "'1'" : "'0'") + ", PENALIZARE = " + oneRow.get(0).PENALIZARE + ", DATAINCHIRIERE = DATE'" + oneRow.get(0).DATA_INCHIRIERE + "', IDCAMERA = " + oneRow.get(0).IDCAMERA + ", IDCLIENT = " + oneRow.get(0).IDCLIENT + ", IDOBIECTIV = " + oneRow.get(0).IDOBIECTIV + ", IDANGAJAT = " + oneRow.get(0).IDANGAJAT + " WHERE IDANGAJAT = " + oneRow.get(0).IDANGAJAT + " AND IDCAMERA = " + oneRow.get(0).IDCAMERA + " AND IDCLIENT = " + oneRow.get(0).IDCLIENT + " AND IDOBIECTIV = " + oneRow.get(0).IDOBIECTIV);
    }
}
