package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ObjectiveModel extends Model implements LinkModelToDatabase<ModelList<ObjectiveModel.InnerObjectiveModel>, ObjectiveModel.InnerObjectiveModel> {
    public static class InnerObjectiveModel extends AbstractInnerModel implements Comparable<InnerObjectiveModel> {
        public int ObjectiveID;
        public String Name;
        public int FocalDistance;
        public double MinimumAperture;
        public double MaximumAperture;
        public int Diameter;
        public int Price;
        public int RentalPrice;
        public int MountID;
        public String NameMount;


        @Override
        public int compareTo(InnerObjectiveModel o) {
            return this.ObjectiveID - o.ObjectiveID;
        }
    }
    private String tableName = "OBJECTIVE";

    private static ObjectiveModel instance = null;
    private ModelList<InnerObjectiveModel> modelList = null;

    public ModelList<InnerObjectiveModel> getModelList() {
        return modelList;
    }

    public static ObjectiveModel getInstance() {
        if (instance == null)
            instance = new ObjectiveModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Objective.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "OBJECTIVE";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "objective";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"ObjectiveID", "Name", "FocalDistance", "MinimumAperture", "MaximumAperture", "Diameter", "Price", "RentalPrice", "MountID", "NameMount"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerObjectiveModel model : this.modelList.getList()){
            Object[] obj = {model.ObjectiveID, model.Name, model.FocalDistance, model.MinimumAperture, model.MaximumAperture, model.Diameter, model.Price, model.RentalPrice, model.MountID, model.NameMount};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public ObjectiveModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ObjectiveModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Objective";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ObjectiveModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ObjectiveModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Objective";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Objective.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "OBJECTIVE";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "objective";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerObjectiveModel model = new InnerObjectiveModel();
            model.ObjectiveID = rs.getInt("OBJECTIVEID");
            model.Name = rs.getString("NAME");
            model.FocalDistance = rs.getInt("FOCALDISTANCE");
            model.MinimumAperture = rs.getDouble("MINIMUMAPERTURE");
            model.MaximumAperture = rs.getDouble("MAXIMUMAPERTURE");
            model.Diameter = rs.getInt("DIAMETER");
            model.Price = rs.getInt("PRICE");


            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<ObjectiveModel.InnerObjectiveModel> predicate =  null;

        switch (column) {
            case "Name":
                case "NameMount":
                predicate = (ObjectiveModel.InnerObjectiveModel model) -> {
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
            case "ObjectiveID":
                case "FocalDistance":
                case "Diameter":
                case "Price":
                case "RentalPrice":
                case "MountID":
                predicate = (ObjectiveModel.InnerObjectiveModel model) -> {
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

            case "MinimumAperture":
                case "MaximumAperture":
                predicate = (ObjectiveModel.InnerObjectiveModel model) -> {
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
    public ModelList<InnerObjectiveModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("OBJECTIVE", "Objective.csv");
            tables.put("MOUNT", "Mount.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("OBJECTIVE", "OBJECTIVE");
            tables.put("MOUNT", "MOUNT");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("OBJECTIVE", "objective");
            tables.put("MOUNT", "mount");
        }


        ResultSet obiective = db.getAllTableData(tables.get("OBJECTIVE"));
        ResultSet monturi = db.getAllTableData(tables.get("MOUNT"));

        Map<Integer, String> monturaMap = new HashMap<>();
        while(monturi.next()){
            monturaMap.put(monturi.getInt("MOUNTID"), monturi.getString("NAME"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(obiective.next()) {
            InnerObjectiveModel model = new InnerObjectiveModel();
            model.ObjectiveID = obiective.getInt("OBJECTIVEID");
            model.Name = obiective.getString("NAME");
            model.FocalDistance = obiective.getInt("FOCALDISTANCE");
            try {
                model.MinimumAperture = obiective.getDouble("MINIMUMAPERTURE");
            } catch (Exception ex){
                model.MinimumAperture = 0.0;
            }
            try {
                model.MaximumAperture = obiective.getDouble("MAXIMUMAPERTURE");
            } catch (Exception ex){
                model.MaximumAperture = 0.0;
            }
            model.Diameter = obiective.getInt("DIAMETER");
            model.Price = obiective.getInt("PRICE");
            model.RentalPrice = obiective.getInt("RENTALPRICE");
            model.MountID = obiective.getInt("MOUNTID");
            try {
                model.NameMount = monturaMap.get(model.MountID);
            } catch (Exception ex){
                model.NameMount = "";
            }

            this.modelList.add(model);
        }

        try{
            logger.log("ObjectiveModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerObjectiveModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("NAME", "'" + oneRow.get(0).Name + "'");
        set.put("FOCALDISTANCE", String.valueOf(oneRow.get(0).FocalDistance));
        set.put("MINIMUMAPERTURE", String.valueOf(oneRow.get(0).MinimumAperture));
        set.put("MAXIMUMAPERTURE", String.valueOf(oneRow.get(0).MaximumAperture));
        set.put("DIAMETER", String.valueOf(oneRow.get(0).Diameter));
        set.put("PRICE", String.valueOf(oneRow.get(0).Price));
        set.put("RENTALPRICE", String.valueOf(oneRow.get(0).RentalPrice));


        Map<String, String> where = new HashMap<>();
        where.put("OBJECTIVEID", oneRow.get(0).ObjectiveID + "");

        db.update(this.tableName, set, where);
        try{
            logger.log("ObjectiveModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerObjectiveModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("OBJECTIVEID", row.get(0).ObjectiveID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("ObjectiveModel delete data");
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
            logger.log("ObjectiveModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerObjectiveModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("OBJECTIVEID", ""));
        values.add(new Pair<>("NAME", "'" + row.Name + "'"));
        values.add(new Pair<>("FOCALDISTANCE", String.valueOf(row.FocalDistance)));
        values.add(new Pair<>("MINIMUMAPERTURE", String.valueOf(row.MinimumAperture)));
        values.add(new Pair<>("MAXIMUMAPERTURE", String.valueOf(row.MaximumAperture)));
        values.add(new Pair<>("DIAMETER", String.valueOf(row.Diameter)));
        values.add(new Pair<>("PRICE", String.valueOf(row.Price)));
        values.add(new Pair<>("RENTALPRICE", String.valueOf(row.RentalPrice)));
        values.add(new Pair<>("MOUNTID", String.valueOf(row.MountID)));


        db.insert(this.tableName, values);
        try{
            logger.log("ObjectiveModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
