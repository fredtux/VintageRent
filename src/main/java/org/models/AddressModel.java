package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AddressModel extends Model implements LinkModelToDatabase<ModelList<AddressModel.InnerAddressModel>, AddressModel.InnerAddressModel> {


    public static class InnerAddressModel extends AbstractInnerModel implements Comparable<InnerAddressModel> {
        public int AddressID;
        public String Street;
        public String City;
        public String County;
        public String PostalCode;
        public int IDClient;
        public String ClientName;

        @Override
        public int compareTo(InnerAddressModel o) {
            return this.AddressID - o.AddressID;
        }


    }
    private String tableName = "ADDRESS";

    private static AddressModel instance = null;
    private ModelList<InnerAddressModel> modelList = null;

    public ModelList<InnerAddressModel> getModelList() {
        return modelList;
    }

    public static AddressModel getInstance() {
        if (instance == null)
            instance = new AddressModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Address.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "ADDRESS";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "address";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"AddressID", "Street", "City", "County", "PostalCode", "IDClient", "ClientName"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column < 5;
            }
        };
        for(InnerAddressModel model : this.modelList.getList()){
            Object[] obj = {model.AddressID, model.Street, model.City, model.County, model.PostalCode, model.IDClient, model.ClientName};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public AddressModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AddressModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Address";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public AddressModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AddressModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Address";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Address.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "ADDRESS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "address";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerAddressModel model = new InnerAddressModel();
            model.AddressID = rs.getInt("AddressID");
            model.Street = rs.getString("Street");
            model.City = rs.getString("City");
            model.County = rs.getString("County");
            model.PostalCode = rs.getString("PostalCode");
            model.IDClient = rs.getInt("IDClient");
            model.ClientName = rs.getString("ClientName");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<InnerAddressModel> predicate =  null;

        switch (column) {
            case "Street":
            case "City":
            case "County":
            case "PostalCode":
            case "ClientName":
                predicate = (InnerAddressModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(model);
                        if (comparator == "==")
                            return fieldValue.equals(value);
                        else if (comparator == "!=")
                            return !fieldValue.equals(value);
                        else if (comparator == "<")
                            return fieldValue.compareTo(value) < 0;
                        else if (comparator == ">")
                            return fieldValue.compareTo(value) > 0;
                        else if (comparator == "<=")
                            return fieldValue.compareTo(value) <= 0;
                        else if (comparator == ">=")
                            return fieldValue.compareTo(value) >= 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                break;
            case "AddressID":
            case "IDClient":
                predicate = (InnerAddressModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        int fieldValue = (int) field.get(model);
                        if (comparator == "==")
                            return fieldValue == Integer.parseInt(value);
                        else if (comparator == "!=")
                            return fieldValue != Integer.parseInt(value);
                        else if (comparator == "<")
                            return fieldValue < Integer.parseInt(value);
                        else if (comparator == ">")
                            return fieldValue > Integer.parseInt(value);
                        else if (comparator == "<=")
                            return fieldValue <= Integer.parseInt(value);
                        else if (comparator == ">=")
                            return fieldValue >= Integer.parseInt(value);
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
    public ModelList<InnerAddressModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("ADDRESS", "Address.csv");
            tables.put("CLIENTS", "Clients.csv");
            tables.put("USERS", "Users.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("ADDRESS", "ADDRESS");
            tables.put("CLIENTS", "CLIENTS");
            tables.put("USERS", "USERS");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("ADDRESS", "address");
            tables.put("CLIENTS", "client");
            tables.put("USERS", "user");
        }


        ResultSet address = db.getAllTableData(tables.get("ADDRESS"));
        ResultSet client = db.getAllTableData(tables.get("CLIENTS"));
        ResultSet user = db.getAllTableData(tables.get("USERS"));

        // Make a HashMap of users with USERID as key
        Map<Integer, String> userMap = new HashMap<>();
        while(user.next()) {
            userMap.put(user.getInt("USERID"), user.getString("SURNAME") + " " + user.getString("FIRSTNAME"));
        }

        // Make a HashMap of clients with CLIENTID as key
        Map<Integer, String> clientMap = new HashMap<>();
        while(client.next()) {
            clientMap.put(client.getInt("USERID"), userMap.get(client.getInt("USERID")));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(address.next()) {
            InnerAddressModel model = new InnerAddressModel();
            model.AddressID = address.getInt("AddressID");
            model.Street = address.getString("Street");
            model.City = address.getString("City");
            model.County = address.getString("County");
            model.PostalCode = address.getString("PostalCode");
            model.IDClient = address.getInt("IDClient");
            model.ClientName = clientMap.get(model.IDClient);

            this.modelList.add(model);
        }

        try{
            logger.log("AddressModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerAddressModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("ADDRESSID", "'" + oneRow.get(0).AddressID + "'");
        set.put("STREET", "'" + oneRow.get(0).Street + "'");
        set.put("CITY", "'" + oneRow.get(0).City + "'");
        set.put("COUNTY", "'" + oneRow.get(0).County + "'");
        set.put("POSTALCODE", "'" + oneRow.get(0).PostalCode + "'");


        Map<String, String> where = new HashMap<>();
        where.put("ADDRESSID", String.valueOf(oneRow.get(0).AddressID));
        db.update(this.tableName, set, where);
        try{
            logger.log("AddressModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

    }

    @Override
    public void deleteRow(ModelList<InnerAddressModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("ADDRESSID", row.get(0).AddressID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("AddressModel delete data");
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
            logger.log("AddressModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerAddressModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("ADDRESSID", ""));
        values.add(new Pair<>("STREET", "'" + row.Street + "'"));
        values.add(new Pair<>("CITY", "'" + row.City + "'"));
        values.add(new Pair<>("COUNTY", "'" + row.County + "'"));
        values.add(new Pair<>("POSTALCODE", "'" + row.PostalCode + "'"));
        values.add(new Pair<>("IDCLIENT", row.IDClient + ""));


        db.insert(this.tableName, values);
        try{
            logger.log("AddressModel insert data");
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
