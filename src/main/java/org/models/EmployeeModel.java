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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class EmployeeModel extends Model implements LinkModelToDatabase<ModelList<EmployeeModel.InnerEmployeeModel>, EmployeeModel.InnerEmployeeModel> {
    public static class InnerEmployeeModel extends AbstractInnerModel implements Comparable<InnerEmployeeModel> {
        public int UserID;
        public LocalDateTime BirthDate;
        public LocalDateTime HireDate;
        public int IDManager;
        public int SalaryID;
        public int Salary;
        public String EmployeeName;
        public String ManagerName;

        @Override
        public int compareTo(InnerEmployeeModel o) {
            return this.UserID - o.UserID;
        }
    }
    private String tableName = "EMPLOYEE";

    private static EmployeeModel instance = null;
    private ModelList<InnerEmployeeModel> modelList = null;

    public ModelList<InnerEmployeeModel> getModelList() {
        return modelList;
    }

    public static EmployeeModel getInstance() {
        if (instance == null)
            instance = new EmployeeModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Employee.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "EMPLOYEE";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "employee";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"UserID", "BirthDate", "HireDate", "IDManager", "SalaryID", "Salary", "EmployeeName", "ManagerName"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        for(InnerEmployeeModel model : this.modelList.getList()){
            Object[] obj = {model.UserID, model.BirthDate, model.HireDate, model.IDManager, model.SalaryID, model.Salary, model.EmployeeName, model.ManagerName};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public EmployeeModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("EmployeeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Employee";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public EmployeeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("EmployeeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Employee";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Employee.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "EMPLOYEE";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "employee";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while(rs.next()){
            InnerEmployeeModel model = new InnerEmployeeModel();
            model.UserID = rs.getInt("USERID");
            model.BirthDate = LocalDateTime.parse(rs.getString("BIRTHDATE"));
            model.HireDate = LocalDateTime.parse(rs.getString("HIREDATE"));
            model.IDManager = rs.getInt("IDMANAGER");
            model.SalaryID = rs.getInt("SALARYID");
            model.Salary = rs.getInt("SALARY");
            model.EmployeeName = rs.getString("EmployeeName");
            model.ManagerName = rs.getString("ManagerName");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<EmployeeModel.InnerEmployeeModel> predicate =  null;

        switch (column) {
            case "EmployeeName":
            case "ManagerName":
                predicate = (EmployeeModel.InnerEmployeeModel model) -> {
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
            case "UserID":
            case "IDManager":
            case "SalaryID":
            case "Salary":

                predicate = (EmployeeModel.InnerEmployeeModel model) -> {
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
            case "BirthDate":
                case "HireDate":
                    predicate = (EmployeeModel.InnerEmployeeModel model) -> {
                        try {
                            Field field = model.getClass().getDeclaredField(column);
                            field.setAccessible(true);
                            LocalDateTime fieldValue = (LocalDateTime) field.get(model);
                            if(comparator == "==")
                                return fieldValue.equals(LocalDateTime.parse(value));
                            else if(comparator == "!=")
                                return !fieldValue.equals(LocalDateTime.parse(value));
                            else if(comparator == "<")
                                return fieldValue.compareTo(LocalDateTime.parse(value)) < 0;
                            else if(comparator == ">")
                                return fieldValue.compareTo(LocalDateTime.parse(value)) > 0;
                            else if(comparator == "<=")
                                return fieldValue.compareTo(LocalDateTime.parse(value)) <= 0;
                            else if(comparator == ">=")
                                return fieldValue.compareTo(LocalDateTime.parse(value)) >= 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    };
        }

        this.modelList = this.modelList.filter(predicate, value);
    }

    @Override
    public ModelList<InnerEmployeeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("EMPLOYEE", "Employee.csv");
            tables.put("SALARY", "Salary.csv");
            tables.put("USERS", "Users.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("EMPLOYEE", "EMPLOYEE");
            tables.put("SALARY", "SALARY");
            tables.put("USERS", "USERS");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("EMPLOYEE", "employee");
            tables.put("SALARY", "salary");
            tables.put("USERS", "user");
        }


        ResultSet angajati = db.getAllTableData(tables.get("EMPLOYEE"));
        ResultSet salarii = db.getAllTableData(tables.get("SALARY"));
        ResultSet utilizatori = db.getAllTableData(tables.get("USERS"));

        Map<Integer, Integer> salaryMap = new HashMap<>();
        while(salarii.next()){
            salaryMap.put(salarii.getInt("SALARYID"), salarii.getInt("SALARY"));
        }

        Map<Integer, String> userMap = new HashMap<>();
        while(utilizatori.next()){
            userMap.put(utilizatori.getInt("USERID"), utilizatori.getString("SURNAME") + " " + utilizatori.getString("FIRSTNAME"));
        }

        // Add fields to cameras
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        this.modelList = new ModelList<>(true);
        while(angajati.next()) {
            InnerEmployeeModel model = new InnerEmployeeModel();
            model.UserID = angajati.getInt("USERID");
            try {
                model.BirthDate = LocalDateTime.parse(angajati.getString("BIRTHDATE"), formatter);
            } catch (Exception ex){
                try {
                    model.BirthDate = LocalDateTime.parse(angajati.getString("BIRTHDATE"), formatter2);
                } catch (Exception ex2){
                    model.BirthDate = null;
                }
            }
            try {
                model.HireDate = LocalDateTime.parse(angajati.getString("HIREDATE"), formatter);
            } catch (Exception ex){
                try {
                    model.HireDate = LocalDateTime.parse(angajati.getString("HIREDATE"), formatter2);
                } catch (Exception ex2){
                    model.HireDate = null;
                }
            }
            String manager = angajati.getString("IDMANAGER");
            if(manager == null || manager == ""){
                model.IDManager = 0;
            } else {
                model.IDManager = Integer.parseInt(manager);
            }
            model.SalaryID = angajati.getInt("SALARYID");
            try {
                model.Salary = salaryMap.get(model.SalaryID);
            } catch (Exception ex){
                model.Salary = 0;
            }
            try {
                model.EmployeeName = userMap.get(model.UserID);
            } catch (Exception ex){
                model.EmployeeName = "";
            }
            try{
            model.ManagerName = userMap.get(model.IDManager);
            } catch (Exception ex){
                model.ManagerName = "";
            }

            this.modelList.add(model);
        }

        try{
            logger.log("EmployeeMOdel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerEmployeeModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, String> set = new HashMap<>();
        set.put("USERID", "'" + oneRow.get(0).UserID + "'");
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("BIRTHDATE", "TO_DATE('" + oneRow.get(0).BirthDate.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("BIRTHDATE", oneRow.get(0).BirthDate.format(dtf) + "");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            set.put("BIRTHDATE", oneRow.get(0).BirthDate.format(dtf) + "");
        }

        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("HIREDATE", "TO_DATE('" + oneRow.get(0).HireDate.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("HIREDATE", oneRow.get(0).HireDate.format(dtf) + "");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            set.put("HIREDATE", oneRow.get(0).HireDate.format(dtf) + "");
        }


        Map<String, String> where = new HashMap<>();
        where.put("UserID", String.valueOf(oneRow.get(0).UserID));

        db.update(this.tableName, set, where);
        try{
            logger.log("EmployeeModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

    }

    @Override
    public void deleteRow(ModelList<InnerEmployeeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("USERID", row.get(0).UserID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("EmployeeModel delete data");
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
            logger.log("EmployeeModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerEmployeeModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<String, String>("USERID", row.UserID + ""));
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("BIRTHDATE", "TO_DATE('" + row.BirthDate.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("BIRTHDATE", row.BirthDate.format(dtf) + ""));
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            values.add(new Pair<String, String>("BIRTHDATE", row.BirthDate.format(dtf) + ""));
        }

        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("HIREDATE", "TO_DATE('" + row.HireDate.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("HIREDATE", row.HireDate.format(dtf) + ""));
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            values.add(new Pair<String, String>("HIREDATE", row.HireDate.format(dtf) + ""));
        }

        values.add(new Pair<>("IDMANAGER", row.IDManager + ""));
        values.add(new Pair<>("SALARYID", row.SalaryID + ""));

        db.insert(this.tableName, values);
        try{
            logger.log("EmployeeModel insert data");
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
