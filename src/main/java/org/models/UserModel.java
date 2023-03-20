package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel extends Model implements LinkModelToDatabase<ModelList<UserModel.InnerUserModel>, UserModel.InnerUserModel> {
    public static class InnerUserModel extends AbstractInnerModel implements Comparable<InnerUserModel> {
        public int UserID;
        public String UserName;
        public String Password;
        public String Surname;
        public String Firstname;
        public String CNP;
        public String Email;

        @Override
        public int compareTo(InnerUserModel o) {
            return this.UserID - o.UserID;
        }
    }
    private String tableName = "USERS";

    private static UserModel instance = null;
    private ModelList<InnerUserModel> modelList = null;

    public ModelList<InnerUserModel> getModelList() {
        return modelList;
    }

    public static UserModel getInstance() {
        if (instance == null)
            instance = new UserModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Users.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "USERS";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "user";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"UserID", "UserName", "Password", "Surname", "Firstname", "CNP", "Email"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerUserModel model : this.modelList.getList()){
            Object[] obj = {model.UserID, model.UserName, model.Password, model.Surname, model.Firstname, model.CNP, model.Email};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public UserModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("UserModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Users";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public UserModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("UserModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Users";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Users.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "USERS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "user";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerUserModel model = new InnerUserModel();
            model.UserID = rs.getInt("USERID");
            model.UserName = rs.getString("USERNAME");
            model.Password = rs.getString("PASSWORD");
            model.Surname = rs.getString("SURNAME");
            model.Firstname = rs.getString("FIRSTNAME");
            model.CNP = rs.getString("CNP");
            model.Email = rs.getString("EMAIL");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerUserModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("USERS", "Users.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("USERS", "USERS");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("USERS", "user");
        }


        ResultSet angajati = db.getAllTableData(tables.get("USERS"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(angajati.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            InnerUserModel model = new InnerUserModel();
            model.UserID = angajati.getInt("USERID");
            model.UserName = angajati.getString("USERNAME");
            model.Password = angajati.getString("PASSWORD");
            model.Surname = angajati.getString("SURNAME");
            model.Firstname = angajati.getString("FIRSTNAME");
            model.CNP = angajati.getString("CNP");
            model.Email = angajati.getString("EMAIL");

            this.modelList.add(model);
        }

        try{
            logger.log("UserModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerUserModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("USERID", "'" + oneRow.get(0).UserID + "'");
        set.put("USERNAME", "'" + oneRow.get(0).UserName + "'");
        set.put("PASSWORD", "'" + oneRow.get(0).Password + "'");
        set.put("SURNAME", "'" + oneRow.get(0).Surname + "'");
        set.put("FIRSTNAME", "'" + oneRow.get(0).Firstname + "'");
        set.put("CNP", "'" + oneRow.get(0).CNP + "'");
        set.put("EMAIL", "'" + oneRow.get(0).Email + "'");

        Map<String, String> where = new HashMap<>();
        where.put("USERID", String.valueOf(oneRow.get(0).UserID));

        db.update(this.tableName, set, where);
        try{
            logger.log("UserModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerUserModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("USERID", row.get(0).UserID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("UserModel delete data");
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
            logger.log("UserModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerUserModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("USERID",""));
        values.add(new Pair<>("USERNAME", "'" + row.UserName + "'"));
        values.add(new Pair<>("PASSWORD", "'" + makeMD5(row.Password) + "'"));
        values.add(new Pair<>("SURNAME", "'" + row.Surname + "'"));
        values.add(new Pair<>("FIRSTNAME", "'" + row.Firstname + "'"));
        values.add(new Pair<>("CNP", "'" + row.CNP + "'"));
        values.add(new Pair<>("EMAIL", "'" + row.Email + "'"));

        db.insert(this.tableName, values);

        try{
            logger.log("UserModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    private String makeMD5(String plainText){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
