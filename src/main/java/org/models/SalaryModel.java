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

public class SalaryModel extends Model implements LinkModelToDatabase<ModelList<SalaryModel.InnerSalaryModel>, SalaryModel.InnerSalaryModel> {
    public static class InnerSalaryModel extends AbstractInnerModel implements Comparable<InnerSalaryModel> {
        public int SalaryID;
        public int Salary;
        public double Bonus;

        @Override
        public int compareTo(InnerSalaryModel o) {
            return this.SalaryID - o.SalaryID;
        }
    }
    private String tableName = "SALARY";

    private static SalaryModel instance = null;
    private ModelList<InnerSalaryModel> modelList = null;

    public ModelList<InnerSalaryModel> getModelList() {
        return modelList;
    }

    public static SalaryModel getInstance() {
        if (instance == null)
            instance = new SalaryModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Salary.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "SALARY";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "salary";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"SalaryID", "Salary", "Bonus"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerSalaryModel model : this.modelList.getList()){
            Object[] obj = {model.SalaryID, model.Salary, model.Bonus};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public SalaryModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("SalaryModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Salary";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public SalaryModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("SalaryModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Salary";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Salary.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "SALARY";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "salary";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerSalaryModel model = new InnerSalaryModel();
            model.SalaryID = rs.getInt("SALARYID");
            model.Salary = rs.getInt("SALARY");
            model.Bonus = rs.getDouble("BONUS");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<SalaryModel.InnerSalaryModel> predicate =  null;

        switch (column) {
            case "SalaryID":
                case "Salary":
                predicate = (SalaryModel.InnerSalaryModel model) -> {
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

            case "Bonus":
                predicate = (SalaryModel.InnerSalaryModel model) -> {
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
    public ModelList<InnerSalaryModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("SALARY", "Salary.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("SALARY", "SALARY");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("SALARY", "salary");
        }


        ResultSet formats = db.getAllTableData(tables.get("SALARY"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerSalaryModel model = new InnerSalaryModel();
            model.SalaryID = formats.getInt("SALARYID");
            model.Salary = formats.getInt("SALARY");
            model.Bonus = formats.getDouble("BONUS");

            this.modelList.add(model);
        }

        try{
            logger.log("SalaryModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerSalaryModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("SALARY", oneRow.get(0).Salary + "");
        set.put("BONUS", oneRow.get(0).Bonus + "");
        Map<String, String> where = new HashMap<>();
        where.put("SALARYID", oneRow.get(0).SalaryID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("SalaryModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

    }

    @Override
    public void deleteRow(ModelList<InnerSalaryModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("SALARYID", row.get(0).SalaryID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("SalaryModel delete data");
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
            logger.log("SalaryModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerSalaryModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("SALARYID",""));
        values.add(new Pair<>("SALARY", row.Salary + ""));
        values.add(new Pair<>("BONUS", row.Bonus + ""));

        db.insert(this.tableName, values);

        try{
            logger.log("SalaryModel insert data");
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
