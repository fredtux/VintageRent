package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private String tableName = "INCHIRIERE";

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
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public RentModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("RentModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Rent";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Inchiriere.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "INCHIRIERE";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>();

        while(rs.next()){
            InnerRentModel model = new InnerRentModel();
            model.DURATA_IN_ZILE = rs.getInt("DURATAINZILE");
            try {
                model.DATA_INCHIRIERE = rs.getDate("DATAINCHIRIERE");
            } catch (Exception e) {
                LocalDateTime localDateTime = LocalDateTime.parse(rs.getString("DATAINCHIRIERE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.DATA_INCHIRIERE = Date.valueOf(localDateTime.toLocalDate());
            }
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
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.getAllTableData(this.tableName);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy);

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public ModelList<InnerRentModel> getData(String whereClause, String orderBy, String limit, String offset)  throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.executeQuery("SELECT * FROM " + this.tableName + " WHERE " + whereClause + " ORDER BY " + orderBy + " OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY");

        this.transferToModelList(rs);

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerRentModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        db.update("UPDATE " + this.tableName + " SET DURATAINZILE = " + oneRow.get(0).DURATA_IN_ZILE + ", ESTERETURNAT = " + (oneRow.get(0).ESTE_RETURNAT ? "'1'" : "'0'") + ", PENALIZARE = " + oneRow.get(0).PENALIZARE + ", DATAINCHIRIERE = DATE'" + oneRow.get(0).DATA_INCHIRIERE + "', IDCAMERA = " + oneRow.get(0).IDCAMERA + ", IDCLIENT = " + oneRow.get(0).IDCLIENT + ", IDOBIECTIV = " + oneRow.get(0).IDOBIECTIV + ", IDANGAJAT = " + oneRow.get(0).IDANGAJAT + " WHERE IDANGAJAT = " + oneRow.get(0).IDANGAJAT + " AND IDCAMERA = " + oneRow.get(0).IDCAMERA + " AND IDCLIENT = " + oneRow.get(0).IDCLIENT + " AND IDOBIECTIV = " + oneRow.get(0).IDOBIECTIV);
    }

    @Override
    public void throwIntoCsv() throws Exception {
        if(this.databaseType == DatabaseConnection.DatabaseType.CSV){
            throw new Exception("Cannot throw into CSV from CSV.");
        }

        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        ResultSet rs = db.getAllTableData(this.tableName);

        DatabaseConnection csv = null;
        try {
            csv = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.CSV);
        } catch (Exception e) {
            csv = new CsvConnection();
        }

        // Get headers from rs into String[]
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] headers = new String[columnCount];
        for(int i = 0; i < columnCount; i++){
            headers[i] = rsmd.getColumnName(i + 1);
        }

        // Get data from rs into List<String[]>
        List<String[]> data = new ArrayList<>();
        while(rs.next()){
            String[] row = new String[columnCount];
            for(int i = 0; i < columnCount; i++){
                row[i] = rs.getString(i + 1);
            }
            data.add(row);
        }

        csv.createAndInsert(this.tableName + ".csv", headers, data);
    }
}
