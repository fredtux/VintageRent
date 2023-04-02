package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.gui.tables.CameraTypeAdd;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SubdomainModel extends Model implements LinkModelToDatabase<ModelList<SubdomainModel.InnerSubdomainModel>, SubdomainModel.InnerSubdomainModel> {

    public static class InnerSubdomainModel extends AbstractInnerModel implements Comparable<InnerSubdomainModel> {
        public int SubdomainID;
        public String Name;

        @Override
        public int compareTo(InnerSubdomainModel o) {
            return this.SubdomainID - o.SubdomainID;
        }
    }
    private String tableName = "SUBDOMAINS";

    private static SubdomainModel instance = null;
    private ModelList<InnerSubdomainModel> modelList = null;

    public ModelList<InnerSubdomainModel> getModelList() {
        return modelList;
    }

    public static SubdomainModel getInstance() {
        if (instance == null)
            instance = new SubdomainModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Subdomains.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "SUBDOMAINS";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "subdomain";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"SubdomainID", "Name"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerSubdomainModel model : this.modelList.getList()){
            Object[] obj = {model.SubdomainID, model.Name};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public SubdomainModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("SubdomainModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "CameraType";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public SubdomainModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("SubdomainModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "SUBDOMAINS";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Subdomains.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "SUBDOMAINS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "subdomain";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerSubdomainModel model = new InnerSubdomainModel();
            model.SubdomainID = rs.getInt("SUBDOMAINID");
            model.Name = rs.getString("NAME");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<SubdomainModel.InnerSubdomainModel> predicate =  null;

        switch (column) {
            case "SubdomainID":
                predicate = (SubdomainModel.InnerSubdomainModel model) -> {
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
            case "Name":
                predicate = (SubdomainModel.InnerSubdomainModel model) -> {
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
        }

        this.modelList = this.modelList.filter(predicate, value);
    }


    @Override
    public ModelList<InnerSubdomainModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("SUBDOMAINS", "Subdomains.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("SUBDOMAINS", "SUBDOMAINS");
        } else {
            tables = new HashMap<>();
            tables.put("SUBDOMAINS", "subdomain");
        }


        ResultSet formats = db.getAllTableData(tables.get("SUBDOMAINS"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerSubdomainModel model = new InnerSubdomainModel();
            model.SubdomainID = formats.getInt("SUBDOMAINID");
            model.Name = formats.getString("NAME");

            this.modelList.add(model);
        }

        try{
            logger.log("SubdomainModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerSubdomainModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("NAME", "'" + oneRow.get(0).Name + "'");
        Map<String, String> where = new HashMap<>();
        where.put("SUBDOMAINID", oneRow.get(0).SubdomainID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("SubdomainModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerSubdomainModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("SUBDOMAINID", row.get(0).SubdomainID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("SubdomainModel delete data");
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
            logger.log("SubdomainModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerSubdomainModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("SUBDOMAINID", ""));
        values.add(new Pair<>("Name", "'" + row.Name + "'"));

        db.insert(this.tableName, values);
        try{
            logger.log("SubdomainModel insert data");
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
