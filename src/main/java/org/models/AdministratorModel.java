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

public class AdministratorModel extends Model implements LinkModelToDatabase<ModelList<AdministratorModel.InnerAdministratorModel>, AdministratorModel.InnerAdministratorModel> {

    public static class InnerAdministratorModel extends AbstractInnerModel implements Comparable<InnerAdministratorModel> {
        public int UserID;
        public boolean isActive;

        @Override
        public int compareTo(InnerAdministratorModel o) {
            return this.UserID - o.UserID;
        }
    }
    private String tableName = "ADMINISTRATORS";

    private static AdministratorModel instance = null;
    private ModelList<InnerAdministratorModel> modelList = null;

    public ModelList<InnerAdministratorModel> getModelList() {
        return modelList;
    }

    public static AdministratorModel getInstance() {
        if (instance == null)
            instance = new AdministratorModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Administrators.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "ADMINISTRATORS";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "administrator";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"UserID", "isActive"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerAdministratorModel model : this.modelList.getList()){
            Object[] obj = {model.UserID, model.isActive};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public AdministratorModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AdministratorModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Administrator";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public AdministratorModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AdministratorModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "ADMINISTRATORS";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Administrators.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "ADMINISTRATORS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "administrator";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerAdministratorModel model = new InnerAdministratorModel();
            model.UserID = rs.getInt("USERID");
            model.isActive = Boolean.parseBoolean(rs.getString("ISACTIVE"));

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<AdministratorModel.InnerAdministratorModel> predicate =  null;

        switch (column) {
            case "UserID":
                predicate = (AdministratorModel.InnerAdministratorModel model) -> {
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
                case "isActive":
                predicate = (AdministratorModel.InnerAdministratorModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        boolean fieldValue = (boolean) field.get(model);
                        if(comparator == "==")
                            return fieldValue == Boolean.parseBoolean(value);
                        else if(comparator == "!=")
                            return fieldValue != Boolean.parseBoolean(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
        }

        this.modelList = this.modelList.filter(predicate, value);
    }


    @Override
    public ModelList<InnerAdministratorModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("ADMINISTRATORS", "Administrators.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("ADMINISTRATORS", "ADMINISTRATORS");
        } else {
            tables = new HashMap<>();
            tables.put("ADMINISTRATORS", "administrator");
        }


        ResultSet formats = db.getAllTableData(tables.get("ADMINISTRATORS"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerAdministratorModel model = new InnerAdministratorModel();
            model.UserID = formats.getInt("USERID");
            model.isActive = formats.getString("ISACTIVE") == "1";

            this.modelList.add(model);
        }

        try{
            logger.log("AdministratorModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerAdministratorModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("ISACTIVE", oneRow.get(0).isActive ? "1" : "0");
        Map<String, String> where = new HashMap<>();
        where.put("USERID", oneRow.get(0).UserID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("AdministratorModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerAdministratorModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("USERID", row.get(0).UserID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("AdministratorModel delete data");
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
            logger.log("AdministratorModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerAdministratorModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("USERID", row.UserID + ""));
        values.add(new Pair<>("ISACTIVE", row.isActive ? "1" : "0"));

        db.insert(this.tableName, values);
        try{
            logger.log("AdministratorModel insert data");
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
