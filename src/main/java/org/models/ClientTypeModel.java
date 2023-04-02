package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ClientTypeModel extends Model implements LinkModelToDatabase<ModelList<ClientTypeModel.InnerClientTypeModel>, ClientTypeModel.InnerClientTypeModel> {
    public static class InnerClientTypeModel extends AbstractInnerModel implements Comparable<InnerClientTypeModel> {
        public int TypeID;;
        public String Name;
        public double Discount;

        @Override
        public int compareTo(InnerClientTypeModel o) {
            return this.TypeID - o.TypeID;
        }
    }
    private String tableName = "CLIENTTYPE";

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
            this.tableName = "ClientType.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CLIENTTYPE";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "client_type";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"TypeID", "Name", "Discount"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerClientTypeModel model : this.modelList.getList()){
            Object[] obj = {model.TypeID, model.Name, model.Discount};
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

        this.name = "ClientType";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ClientTypeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "ClientTypei";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "ClientType.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CLIENTS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "client_type";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerClientTypeModel model = new InnerClientTypeModel();
            model.TypeID = rs.getInt("TypeID");
            model.Name = rs.getString("Name");
            model.Discount = rs.getDouble("Discount");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<ClientTypeModel.InnerClientTypeModel> predicate =  null;

        switch (column) {
            case "Name":
                predicate = (ClientTypeModel.InnerClientTypeModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(model);
                        if(comparator == "==")
                            return fieldValue.equals(value);
                        else if(comparator == "!=")
                            return !fieldValue.equals(value);
                        else if(comparator == "<")
                            return fieldValue.compareTo(value) < 0;
                        else if(comparator == ">")
                            return fieldValue.compareTo(value) > 0;
                        else if(comparator == "<=")
                            return fieldValue.compareTo(value) <= 0;
                        else if(comparator == ">=")
                            return fieldValue.compareTo(value) >= 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                break;
            case "TypeID":
                predicate = (ClientTypeModel.InnerClientTypeModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        int fieldValue = (int) field.get(model);
                        if(comparator == "==")
                            return fieldValue == Integer.parseInt(value);
                        else if(comparator == "!=")
                            return fieldValue != Integer.parseInt(value);
                        else if(comparator == "<")
                            return fieldValue < Integer.parseInt(value);
                        else if(comparator == ">")
                            return fieldValue > Integer.parseInt(value);
                        else if(comparator == "<=")
                            return fieldValue <= Integer.parseInt(value);
                        else if(comparator == ">=")
                            return fieldValue >= Integer.parseInt(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                break;
            case "Discount":
                predicate = (ClientTypeModel.InnerClientTypeModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        double fieldValue = (double) field.get(model);
                        if(comparator == "==")
                            return fieldValue == Double.parseDouble(value);
                        else if(comparator == "!=")
                            return fieldValue != Double.parseDouble(value);
                        else if(comparator == "<")
                            return fieldValue < Double.parseDouble(value);
                        else if(comparator == ">")
                            return fieldValue > Double.parseDouble(value);
                        else if(comparator == "<=")
                            return fieldValue <= Double.parseDouble(value);
                        else if(comparator == ">=")
                            return fieldValue >= Double.parseDouble(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                break;
        }

        this.modelList = this.modelList.filter(predicate, value);
    }

    @Override
    public ModelList<InnerClientTypeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CLIENTTYPE", "ClientType.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("CLIENTTYPE", "CLIENTTYPE");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("CLIENTTYPE", "client_type");
        }


        ResultSet tipClients = db.getAllTableData(tables.get("CLIENTTYPE"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(tipClients.next()) {
            InnerClientTypeModel model = new InnerClientTypeModel();
            model.TypeID = tipClients.getInt("TypeID");
            model.Name = tipClients.getString("Name");
            model.Discount = tipClients.getDouble("Discount");

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
        set.put("Name", "'" + oneRow.get(0).Name + "'");
        set.put("Discount", String.valueOf(oneRow.get(0).Discount));

        Map<String, String> where = new HashMap<>();
        where.put("TypeID", String.valueOf(oneRow.get(0).TypeID));

        db.update(this.tableName, set, where);
        try{
            logger.log("ClientTypeModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerClientTypeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("TypeID", row.get(0).TypeID + "");
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
        values.add(new Pair<>("TYPEID", ""));
        values.add(new Pair<>("NAME", "'" + row.Name + "'"));
        values.add(new Pair<>("DISCOUNT", row.Discount + ""));


        db.insert(this.tableName, values);

        try{
            logger.log("ClientTypeModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
    @Override
    public void truncate() throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        this.setDatabaseType(databaseType);
        db.truncate(this.tableName);
        this.getData();
    }
}
