package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MountModel extends Model implements LinkModelToDatabase<ModelList<MountModel.InnerMountModel>, MountModel.InnerMountModel> {
    public static class InnerMountModel extends AbstractInnerModel implements Comparable<InnerMountModel> {
        public int MountID;
        public String Name;

        @Override
        public int compareTo(InnerMountModel o) {
            return this.MountID - o.MountID;
        }
    }
    private String tableName = "MOUNT";

    private static MountModel instance = null;
    private ModelList<InnerMountModel> modelList = null;

    public ModelList<InnerMountModel> getModelList() {
        return modelList;
    }

    public static MountModel getInstance() {
        if (instance == null)
            instance = new MountModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Mount.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "MOUNT";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "mount";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"TypeID", "Name"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerMountModel model : this.modelList.getList()){
            Object[] obj = {model.MountID, model.Name};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public MountModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("MountModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Mount";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public MountModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("MountModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Mount";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Mount.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "MOUNT";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "mount";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerMountModel model = new InnerMountModel();
            model.MountID = rs.getInt("MOUNTID");
            model.Name = rs.getString("NAME");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<MountModel.InnerMountModel> predicate =  null;

        switch (column) {
            case "Name":
                predicate = (MountModel.InnerMountModel model) -> {
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
            case "MountID":
                predicate = (MountModel.InnerMountModel model) -> {
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
    public ModelList<InnerMountModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("MOUNT", "Mount.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("MOUNT", "MOUNT");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("MOUNT", "mount");
        }


        ResultSet formats = db.getAllTableData(tables.get("MOUNT"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerMountModel model = new InnerMountModel();
            model.MountID = formats.getInt("MOUNTID");
            model.Name = formats.getString("NAME");

            this.modelList.add(model);
        }

        try{
            logger.log("MountModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerMountModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("NAME", "'" + oneRow.get(0).Name + "'");
        Map<String, String> where = new HashMap<>();
        where.put("MOUNTID", oneRow.get(0).MountID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("MountModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

    }

    @Override
    public void deleteRow(ModelList<InnerMountModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("MOUNTID", row.get(0).MountID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("MountModel delete data");
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
            logger.log("MountModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerMountModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("MOUNTID", ""));
        values.add(new Pair<>("NAME", "'" + row.Name + "'"));

        db.insert(this.tableName, values);

        try{
            logger.log("MountModel insert data");
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
