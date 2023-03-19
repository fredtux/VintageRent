package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTypeModel extends Model implements LinkModelToDatabase<ModelList<ClientTypeModel.InnerClientTypeModel>, ClientTypeModel.InnerClientTypeModel> {
    public static class InnerClientTypeModel extends AbstractInnerModel implements Comparable<InnerClientTypeModel> {
        public int IDTip;;
        public String Denumire;
        public double Discount;

        @Override
        public int compareTo(InnerClientTypeModel o) {
            return this.IDTip - o.IDTip;
        }
    }
    private String tableName = "TIPCLIENT";

    private static ClientTypeModel instance = null;
    private ModelList<InnerClientTypeModel> modelList = null;

    public ModelList<InnerClientTypeModel> getModelList() {
        return modelList;
    }

    public static ClientTypeModel getInstance() {
        if (instance == null)
            instance = new ClientTypeModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "TipClient.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "TIPCLIENT";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "client_type";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDTip", "Denumire", "Discount"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerClientTypeModel model : this.modelList.getList()){
            Object[] obj = {model.IDTip, model.Denumire, model.IDTip, model.Discount};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public ClientTypeModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "TipClient";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ClientTypeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "TipClienti";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "TipClient.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CLIENTI";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "client_type";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerClientTypeModel model = new InnerClientTypeModel();
            model.IDTip = rs.getInt("IDTip");
            model.Denumire = rs.getString("Denumire");
            model.Discount = rs.getDouble("Discount");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerClientTypeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("TIPCLIENT", "TipClient.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("TIPCLIENT", "TIPCLIENT");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("TIPCLIENT", "client_type");
        }


        ResultSet tipClienti = db.getAllTableData(tables.get("TIPCLIENT"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(tipClienti.next()) {
            InnerClientTypeModel model = new InnerClientTypeModel();
            model.IDTip = tipClienti.getInt("IDTip");
            model.Denumire = tipClienti.getString("Denumire");
            model.Discount = tipClienti.getDouble("Discount");

            this.modelList.add(model);
        }

        try{
            logger.log("ClientTypeModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerClientTypeModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("Denumire", "'" + oneRow.get(0).Denumire + "'");
        set.put("Discount", String.valueOf(oneRow.get(0).Discount));

        Map<String, String> where = new HashMap<>();
        where.put("IDTip", String.valueOf(oneRow.get(0).IDTip));

        db.update(this.tableName, set, where);
        try{
            logger.log("ClientTypeModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerClientTypeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDTip", row.get(0).IDTip + "");
        db.delete(this.tableName, where);
        try{
            logger.log("ClientTypeModel delete data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
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
        try{
            logger.log("ClientTypeModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerClientTypeModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDTIP", row.IDTip + ""));
        values.add(new Pair<>("DENUMIRE", "'" + row.Denumire + "'"));
        values.add(new Pair<>("DISCOUNT", row.Discount + ""));


        db.insert(this.tableName, values);

        try{
            logger.log("ClientTypeModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
