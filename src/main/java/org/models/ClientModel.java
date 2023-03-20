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

public class ClientModel extends Model implements LinkModelToDatabase<ModelList<ClientModel.InnerClientModel>, ClientModel.InnerClientModel> {
    public static class InnerClientModel extends AbstractInnerModel implements Comparable<InnerClientModel> {
        public int UserID;
        public Date BirthDate;
        public int TypeID;
        public String NameTip;
        public double DiscountTip;
        public String SurnameClient;

        @Override
        public int compareTo(InnerClientModel o) {
            return this.UserID - o.UserID;
        }
    }
    private String tableName = "CLIENTS";

    private static ClientModel instance = null;
    private ModelList<InnerClientModel> modelList = null;

    public ModelList<InnerClientModel> getModelList() {
        return modelList;
    }

    public static ClientModel getInstance() {
        if (instance == null)
            instance = new ClientModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Clients.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CLIENTS";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "client";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"UserID", "BirthDate", "TypeID", "NameTip", "DiscountTip", "SurnameClient"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerClientModel model : this.modelList.getList()){
            Object[] obj = {model.UserID, model.BirthDate, model.TypeID, model.NameTip, model.DiscountTip, model.SurnameClient};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public ClientModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Clients";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ClientModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Clients";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Clients.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CLIENTS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "client";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerClientModel model = new InnerClientModel();
            model.UserID = rs.getInt("UserID");
            model.BirthDate = rs.getDate("BirthDate");
            model.TypeID = rs.getInt("TypeID");
            model.SurnameClient = rs.getString("SurnameClient");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerClientModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CLIENTS", "Clients.csv");
            tables.put("CLIENTTYPEI", "ClientType.csv");
            tables.put("USERS", "Users.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("CLIENTS", "CLIENTS");
            tables.put("CLIENTTYPEI", "CLIENTTYPE");
            tables.put("USERS", "USERS");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("CLIENTS", "client");
            tables.put("CLIENTTYPEI", "client_type");
            tables.put("USERS", "user");
        }


        ResultSet clienti = db.getAllTableData(tables.get("CLIENTS"));
        ResultSet tipClients = db.getAllTableData(tables.get("CLIENTTYPEI"));
        ResultSet utilizatori = db.getAllTableData(tables.get("USERS"));

        Map<Integer, String> typeMap = new HashMap<>();
        Map<Integer, Double> discountMap = new HashMap<>();
        while(tipClients.next()){
            typeMap.put(tipClients.getInt("TYPEID"), tipClients.getString("NAME"));
            discountMap.put(tipClients.getInt("TYPEID"), tipClients.getDouble("DISCOUNT"));
        }
        Map<Integer, String> userMap = new HashMap<>();
        while(utilizatori.next()){
            userMap.put(utilizatori.getInt("USERID"), utilizatori.getString("SURNAME") + " " + utilizatori.getString("FIRSTNAME"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(clienti.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            InnerClientModel model = new InnerClientModel();
            model.UserID = clienti.getInt("UserID");
            try {
                model.BirthDate = new Date(sdf.parse(clienti.getString("BirthDate")).getTime());
            } catch (Exception ex) {
                model.BirthDate = null;
            }
            model.TypeID = clienti.getInt("TypeID");
            model.NameTip = typeMap.get(clienti.getInt("TypeID"));
            try {
                model.DiscountTip = discountMap.get(clienti.getInt("TypeID"));
            } catch (Exception ex) {
                model.DiscountTip = 0.0;
            }
            model.SurnameClient = userMap.get(clienti.getInt("UserID"));

            this.modelList.add(model);
        }

        try{
            logger.log("ClientModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerClientModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("UserID", "'" + oneRow.get(0).UserID + "'");
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("BirthDate", "TO_DATE('" + oneRow.get(0).BirthDate + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("BirthDate", oneRow.get(0).BirthDate + "");
        }

        Map<String, String> where = new HashMap<>();
        where.put("UserID", String.valueOf(oneRow.get(0).UserID));

        db.update(this.tableName, set, where);
        try{
            logger.log("ClientModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerClientModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("UserID", row.get(0).UserID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("ClientModel delete data");
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
            logger.log("ClientModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerClientModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("USERID", row.UserID + ""));
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("BIRTHDATE", "TO_DATE('" + sdf.format(row.BirthDate) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("BIRTHDATE", sdf.format(row.BirthDate) + ""));
        }
        values.add(new Pair<>("TYPEID", row.TypeID + ""));


        db.insert(this.tableName, values);

        try{
            logger.log("ClientModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
