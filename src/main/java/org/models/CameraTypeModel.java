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

public class CameraTypeModel extends Model implements LinkModelToDatabase<ModelList<CameraTypeModel.InnerCameraTypeModel>, CameraTypeModel.InnerCameraTypeModel> {

    public static class InnerCameraTypeModel extends AbstractInnerModel implements Comparable<InnerCameraTypeModel> {
        public int TypeID;
        public String Name;

        @Override
        public int compareTo(InnerCameraTypeModel o) {
            return this.TypeID - o.TypeID;
        }
    }
    private String tableName = "CAMERATYPE";

    private static CameraTypeModel instance = null;
    private ModelList<InnerCameraTypeModel> modelList = null;

    public ModelList<InnerCameraTypeModel> getModelList() {
        return modelList;
    }

    public static CameraTypeModel getInstance() {
        if (instance == null)
            instance = new CameraTypeModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "CameraType.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CAMERATYPE";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "camera_type";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"TypeID", "Name"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override

            public boolean isCellEditable(int row, int column) {
                return column >= 1;
            }
        };
        for(InnerCameraTypeModel model : this.modelList.getList()){
            Object[] obj = {model.TypeID, model.Name};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public CameraTypeModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "CameraType";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public CameraTypeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "CameraType";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "CameraType.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CAMERATYPE";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "camera_type";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerCameraTypeModel model = new InnerCameraTypeModel();
            model.TypeID = rs.getInt("TYPEID");
            model.Name = rs.getString("NAME");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<CameraTypeModel.InnerCameraTypeModel> predicate =  null;

        switch (column) {
            case "Name":
                predicate = (CameraTypeModel.InnerCameraTypeModel model) -> {
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
                predicate = (CameraTypeModel.InnerCameraTypeModel model) -> {
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
        }

        this.modelList = this.modelList.filter(predicate, value);
    }


    @Override
    public ModelList<InnerCameraTypeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CAMERATYPE", "CameraType.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("CAMERATYPE", "CAMERATYPE");
        } else {
            tables = new HashMap<>();
            tables.put("CAMERATYPE", "camera_type");
        }


        ResultSet formats = db.getAllTableData(tables.get("CAMERATYPE"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerCameraTypeModel model = new InnerCameraTypeModel();
            model.TypeID = formats.getInt("TYPEID");
            model.Name = formats.getString("NAME");

            this.modelList.add(model);
        }

        try{
            logger.log("CameraTypeModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerCameraTypeModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("NAME", "'" + oneRow.get(0).Name + "'");
        Map<String, String> where = new HashMap<>();
        where.put("TYPEID", oneRow.get(0).TypeID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("CameraTypeModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerCameraTypeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("TYPEID", row.get(0).TypeID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("CameraTypeModel delete data");
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
            logger.log("CameraTypeModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerCameraTypeModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("TYPEID", ""));
        values.add(new Pair<>("NAME", "'" + row.Name + "'"));

        db.insert(this.tableName, values);
        try{
            logger.log("CameraTypeModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
