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

public class CameraModel extends Model implements LinkModelToDatabase<ModelList<CameraModel.InnerCameraModel>, CameraModel.InnerCameraModel> {


    public static class InnerCameraModel extends AbstractInnerModel implements Comparable<InnerCameraModel> {
        public String Brand;
        public String ModelCamera;
        public int ManufacturingYear;
        public double Price;
        public double RentalPrice;
        public int IDCamera;

        public String NameFormat;
        public String FilmWidth;
        public String NameTip;
        public String NameMount;
        public int FormatID;
        public int TypeID;
        public int MountID;

        @Override
        public int compareTo(InnerCameraModel o) {
            return this.IDCamera - o.IDCamera;
        }


    }
    private String tableName = "CAMERA";

    private static CameraModel instance = null;
    private ModelList<InnerCameraModel> modelList = null;

    public ModelList<InnerCameraModel> getModelList() {
        return modelList;
    }

    public static CameraModel getInstance() {
        if (instance == null)
            instance = new CameraModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Camera.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CAMERA";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "camera";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDCamera", "Brand", "ModelCamera", "ManufacturingYear", "Price", "RentalPrice", "NameFormat", "FilmWidth", "NameTip", "NameMount"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerCameraModel model : this.modelList.getList()){
            Object[] obj = {model.IDCamera, model.Brand, model.ModelCamera, model.ManufacturingYear, model.Price, model.RentalPrice, model.NameFormat, model.FilmWidth, model.NameTip, model.NameMount};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public CameraModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public CameraModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Camera.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CAMERA";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "camera";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerCameraModel model = new InnerCameraModel();
            model.Brand = rs.getString("BRAND");
            model.ModelCamera = rs.getString("MODELCAMERA");
            model.ManufacturingYear = rs.getInt("MANUFACTURINGYEAR");
            model.Price = rs.getDouble("PRICE");
            model.RentalPrice = rs.getDouble("RENTALPRICE");
            model.IDCamera = rs.getInt("IDCAMERA");
            model.NameTip = rs.getString("NAMETIP");
            model.NameFormat = rs.getString("NAMEFORMAT");
            model.FilmWidth = rs.getString("FILMWIDTH");
            model.NameMount = rs.getString("NAMEMOUNT");
            model.FormatID = rs.getInt("FORMATID");
            model.TypeID = rs.getInt("TYPEID");
            model.MountID = rs.getInt("MOUNTID");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<InnerCameraModel> predicate =  null;

        switch (column) {
            case "Brand":
            case "ModelCamera":
            case "NameFormat":
            case "FilmWidth":
            case "NameTip":
            case "NameMount":
                predicate = (InnerCameraModel model) -> {
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
            case "ManufacturingYear":
            case "IDCamera":
            case "FormatID":
            case "TypeID":
            case "MountID":
                predicate = (InnerCameraModel model) -> {
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
            case "Price":
            case "RentalPrice":
                predicate = (InnerCameraModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        double fieldValue = (double) field.get(model);
                        if(comparator == "==")
                            return fieldValue == Double.parseDouble(value);
                        else if(comparator == "!=")
                            return fieldValue !=  Double.parseDouble(value);
                        else if(comparator == "<")
                            return fieldValue <  Double.parseDouble(value);
                        else if(comparator == ">")
                            return fieldValue >  Double.parseDouble(value);
                        else if(comparator == "<=")
                            return fieldValue <=  Double.parseDouble(value);
                        else if(comparator == ">=")
                            return fieldValue >=  Double.parseDouble(value);
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
    public ModelList<InnerCameraModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CAMERA", "Camera.csv");
            tables.put("FORMAT", "Format.csv");
            tables.put("CAMERATYPE", "CameraType.csv");
            tables.put("MOUNT", "Mount.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("CAMERA", "CAMERA");
            tables.put("FORMAT", "FORMAT");
            tables.put("CAMERATYPE", "CAMERATYPE");
            tables.put("MOUNT", "MOUNT");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("CAMERA", "camera");
            tables.put("FORMAT", "format");
            tables.put("CAMERATYPE", "camera_type");
            tables.put("MOUNT", "mount");
        }


        ResultSet cameras = db.getAllTableData(tables.get("CAMERA"));
        ResultSet formats = db.getAllTableData(tables.get("FORMAT"));
        ResultSet types = db.getAllTableData(tables.get("CAMERATYPE"));
        ResultSet mounts = db.getAllTableData(tables.get("MOUNT"));

        // Make a HashMap of formats with FORMATID as key
        Map<Integer, String> formatMap = new HashMap<>();
        while(formats.next()) {
            formatMap.put(formats.getInt("FORMATID"), formats.getString("NAME"));
        }

        // Make a HashMap of types with TYPEID as key
        Map<Integer, String> typeMap = new HashMap<>();
        while(types.next()) {
            typeMap.put(types.getInt("TYPEID"), types.getString("NAME"));
        }

        // Make a HashMap of mounts with MOUNTID as key
        Map<Integer, String> mountMap = new HashMap<>();
        while(mounts.next()) {
            mountMap.put(mounts.getInt("MOUNTID"), mounts.getString("NAME"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(cameras.next()) {
            InnerCameraModel model = new InnerCameraModel();
            model.Brand = cameras.getString("BRAND");
            model.ModelCamera = cameras.getString("MODELCAMERA");
            model.ManufacturingYear = cameras.getInt("MANUFACTURINGYEAR");
            model.Price = cameras.getDouble("PRICE");
            model.RentalPrice = cameras.getDouble("RENTALPRICE");
            model.IDCamera = cameras.getInt("IDCAMERA");
            model.NameTip = typeMap.get(cameras.getInt("TYPEID"));
            model.NameFormat = formatMap.get(cameras.getInt("FORMATID"));
            model.FilmWidth = formatMap.get(cameras.getInt("FORMATID"));
            model.NameMount = mountMap.get(cameras.getInt("MOUNTID"));
            model.FormatID = cameras.getInt("FORMATID");
            model.TypeID = cameras.getInt("TYPEID");
            model.MountID = cameras.getInt("MOUNTID");

            this.modelList.add(model);
        }

        try{
            logger.log("CameraModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerCameraModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("BRAND", "'" + oneRow.get(0).Brand + "'");
        set.put("MODELCAMERA", "'" + oneRow.get(0).ModelCamera + "'");
//        set.put("FORMATID", String.valueOf(oneRow.get(0).FormatID));
//        set.put("TYPEID", String.valueOf(oneRow.get(0).TypeID));
//        set.put("MOUNTID", String.valueOf(oneRow.get(0).MountID));
        set.put("MANUFACTURINGYEAR", String.valueOf(oneRow.get(0).ManufacturingYear));
        set.put("PRICE", String.valueOf(oneRow.get(0).Price));
        set.put("RENTALPRICE", String.valueOf(oneRow.get(0).RentalPrice));

        Map<String, String> where = new HashMap<>();
        where.put("IDCAMERA", String.valueOf(oneRow.get(0).IDCamera));
        db.update(this.tableName, set, where);
        try{
            logger.log("CameraModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerCameraModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDCAMERA", row.get(0).IDCamera + "");
        db.delete(this.tableName, where);
        try{
            logger.log("CameraModel delete data");
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
            logger.log("CameraModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerCameraModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDCAMERA", ""));
        values.add(new Pair<>("BRAND", "'" + row.Brand + "'"));
        values.add(new Pair<>("MODELCAMERA", "'" + row.ModelCamera + "'"));
        values.add(new Pair<>("FORMATID", String.valueOf(row.FormatID)));
        values.add(new Pair<>("TYPEID", String.valueOf(row.TypeID)));
        values.add(new Pair<>("MOUNTID", String.valueOf(row.MountID)));
        values.add(new Pair<>("MANUFACTURINGYEAR", String.valueOf(row.ManufacturingYear)));
        values.add(new Pair<>("PRICE", String.valueOf(row.Price)));
        values.add(new Pair<>("RENTALPRICE", String.valueOf(row.RentalPrice)));


        db.insert(this.tableName, values);
        try{
            logger.log("CameraModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
