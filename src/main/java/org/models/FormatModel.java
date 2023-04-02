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

public class FormatModel extends Model implements LinkModelToDatabase<ModelList<FormatModel.InnerFormatModel>, FormatModel.InnerFormatModel> {
    public static class InnerFormatModel extends AbstractInnerModel implements Comparable<InnerFormatModel> {
        public int FormatID;
        public String Name;
        public String FilmWidth;

        @Override
        public int compareTo(InnerFormatModel o) {
            return this.FormatID - o.FormatID;
        }
    }
    private String tableName = "FORMAT";

    private static FormatModel instance = null;
    private ModelList<InnerFormatModel> modelList = null;

    public ModelList<InnerFormatModel> getModelList() {
        return modelList;
    }

    public static FormatModel getInstance() {
        if (instance == null)
            instance = new FormatModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Format.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "FORMAT";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "format";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"FormatID", "Name", "FilmWidth"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerFormatModel model : this.modelList.getList()){
            Object[] obj = {model.FormatID, model.Name, model.FilmWidth};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public FormatModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("FormatModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Format";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public FormatModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("FormatModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Format.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "FORMAT";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "format";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerFormatModel model = new InnerFormatModel();
            model.FormatID = rs.getInt("FormatID");
            model.Name = rs.getString("Name");
            model.FilmWidth = rs.getString("FilmWidth");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<FormatModel.InnerFormatModel> predicate =  null;

        switch (column) {
            case "Name":
                case "FilmWidth":
                predicate = (FormatModel.InnerFormatModel model) -> {
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
            case "FormatID":
                predicate = (FormatModel.InnerFormatModel model) -> {
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
    public ModelList<InnerFormatModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("FORMAT", "Format.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("FORMAT", "FORMAT");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("FORMAT", "format");
        }


        ResultSet formats = db.getAllTableData(tables.get("FORMAT"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerFormatModel model = new InnerFormatModel();
            model.FormatID = formats.getInt("FORMATID");
            model.Name = formats.getString("NAME");
            model.FilmWidth = formats.getString("FILMWIDTH");

            this.modelList.add(model);
        }

        try{
            logger.log("FormatModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerFormatModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("NAME", "'" + oneRow.get(0).Name + "'");
        set.put("FILMWIDTH", "'" + oneRow.get(0).FilmWidth + "'");
        Map<String, String> where = new HashMap<>();
        where.put("FORMATID", String.valueOf(oneRow.get(0).FormatID));
        db.update(this.tableName, set, where);
        try{
            logger.log("FormatModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerFormatModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("FORMATID", row.get(0).FormatID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("FormatModel delete data");
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
            logger.log("FormatModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerFormatModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("FORMATID", ""));
        values.add(new Pair<>("NAME", "'" + row.Name + "'"));
        values.add(new Pair<>("FILMWIDTH", "'" + row.FilmWidth + "'"));

        db.insert(this.tableName, values);

        try{
            logger.log("FormatModel insert data");
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
