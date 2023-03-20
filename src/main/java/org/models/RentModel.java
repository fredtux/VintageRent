package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
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

public class RentModel extends Model implements LinkModelToDatabase<ModelList<RentModel.InnerRentModel>, RentModel.InnerRentModel> {
    public static class InnerRentModel extends AbstractInnerModel{
        public int DURATION_IN_DAYS;
        public boolean IS_RETURNED;
        public double PENALTYFEE;
        public Date RENT_DATE;
        public int IDCAMERA;
        public int IDCLIENT;
        public int OBJECTIVEID;
        public int IDANGAJAT;
        public String NAME_CAMERA;
        public String NAME_CLIENT;
        public String NAME_OBIECTIV;
        public String NAME_ANGAJAT;
    }
    private String tableName = "RENTAL";

    private static RentModel instance = null;
    private ModelList<InnerRentModel> modelList = null;

    public ModelList<InnerRentModel> getModelList() {
        return modelList;
    }

    public static RentModel getInstance() {
        if (instance == null)
            instance = new RentModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Rental.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "RENTAL";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "rent";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"DURATION_IN_DAYS", "IS_RETURNED", "PENALTYFEE", "RENT_DATE", "IDCAMERA", "IDCLIENT", "OBJECTIVEID", "IDANGAJAT"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerRentModel model : this.modelList.getList()){
            Object[] obj = {model.DURATION_IN_DAYS, model.IS_RETURNED, model.PENALTYFEE, model.RENT_DATE, model.IDCAMERA, model.IDCLIENT, model.OBJECTIVEID, model.IDANGAJAT};
            tableModel.addRow(obj);
        }

        return tableModel;
    }

    public RentModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("RentModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Rent";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public RentModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("RentModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Rent";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Rental.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "RENTAL";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "rent";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>();

        while(rs.next()){
            InnerRentModel model = new InnerRentModel();
            model.DURATION_IN_DAYS = rs.getInt("DURATIONINDAYS");
            try {
                model.RENT_DATE = rs.getDate("RENTDATE");
            } catch (Exception e) {
                LocalDateTime localDateTime = LocalDateTime.parse(rs.getString("RENTDATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.RENT_DATE = Date.valueOf(localDateTime.toLocalDate());
            }
            model.IDANGAJAT = rs.getInt("IDANGAJAT");
            model.IDCAMERA = rs.getInt("IDCAMERA");
            model.IDCLIENT = rs.getInt("IDCLIENT");
            model.OBJECTIVEID = rs.getInt("OBJECTIVEID");
            model.IS_RETURNED = rs.getBoolean("ISRETURNED");
            model.PENALTYFEE = rs.getDouble("PENALTYFEE");
            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerRentModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("RENTAL", "Rental.csv");
            tables.put("CAMERA", "Camera.csv");
            tables.put("CLIENTS", "Clients.csv");
            tables.put("EMPLOYEE", "Employee.csv");
            tables.put("OBJECTIVE", "Objective.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("RENTAL", "RENTAL");
            tables.put("CAMERA", "CAMERA");
            tables.put("CLIENTS", "CLIENTS");
            tables.put("EMPLOYEE", "EMPLOYEE");
            tables.put("OBJECTIVE", "OBJECTIVE");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("RENTAL", "rent");
            tables.put("CAMERA", "camera");
            tables.put("CLIENTS", "client");
            tables.put("EMPLOYEE", "employee");
            tables.put("OBJECTIVE", "objective");
        }


        ResultSet inchiriere = db.getAllTableData(tables.get("RENTAL"));
        ResultSet camere = db.getAllTableData(tables.get("CAMERA"));
        ResultSet clienti = db.getAllTableData(tables.get("CLIENTS"));
        ResultSet angajati = db.getAllTableData(tables.get("EMPLOYEE"));
        ResultSet obiective = db.getAllTableData(tables.get("OBJECTIVE"));

        Map<Integer, String> camereMap = new HashMap<>();
        while(camere.next())
            camereMap.put(camere.getInt("IDCAMERA"), camere.getString("MODELCAMERA"));

        Map<Integer, String> clientiMap = new HashMap<>();
        while(clienti.next())
            clientiMap.put(clienti.getInt("USERID"), clienti.getString("USERID"));

        Map<Integer, String> angajatiMap = new HashMap<>();
        while(angajati.next())
            angajatiMap.put(angajati.getInt("USERID"), angajati.getString("USERID"));

        Map<Integer, String> obiectiveMap = new HashMap<>();
        while(obiective.next())
            obiectiveMap.put(obiective.getInt("OBJECTIVEID"), obiective.getString("NAME"));

        this.modelList = new ModelList<>();
        while(inchiriere.next()){
            InnerRentModel model = new InnerRentModel();
            try {
                model.RENT_DATE = inchiriere.getDate("RENTDATE");
            } catch (Exception e) {
                try {
                    LocalDateTime localDateTime = LocalDateTime.parse(inchiriere.getString("RENTDATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    model.RENT_DATE = Date.valueOf(localDateTime.toLocalDate());
                } catch (Exception ex) {
                    try{
                        LocalDateTime localDateTime = LocalDateTime.parse(inchiriere.getString("RENTDATE"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        model.RENT_DATE = Date.valueOf(localDateTime.toLocalDate());
                    } catch(Exception exx){
                        model.RENT_DATE = null;
                    }
                }
            }
            model.DURATION_IN_DAYS = inchiriere.getInt("DURATIONINDAYS");
            model.IDCAMERA = inchiriere.getInt("IDCAMERA");
            model.IDCLIENT = inchiriere.getInt("IDCLIENT");
            model.IDANGAJAT = inchiriere.getInt("IDANGAJAT");
            model.OBJECTIVEID = inchiriere.getInt("OBJECTIVEID");
            try {
                model.IS_RETURNED = inchiriere.getBoolean("ISRETURNED");
            } catch (Exception e) {
                model.IS_RETURNED = false;
            }
            try {
                model.PENALTYFEE = inchiriere.getDouble("PENALTYFEE");
            } catch (Exception e) {
                model.PENALTYFEE = 0;
            }
            this.modelList.add(model);
        }

        try{
            logger.log("RentModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerRentModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, String> set = new HashMap<>();
        set.put("DURATIONINDAYS", oneRow.get(0).DURATION_IN_DAYS + "");
        set.put("ISRETURNED", oneRow.get(0).IS_RETURNED ? "'1'" : "'0'");
        set.put("PENALTYFEE", oneRow.get(0).PENALTYFEE + "");
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("RENTDATE", "TO_DATE('" + oneRow.get(0).RENT_DATE + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("RENTDATE", oneRow.get(0).RENT_DATE + "");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            set.put("RENTDATE", oneRow.get(0).RENT_DATE + "");
        }

        Map<String, String> where = new HashMap<>();
        where.put("IDANGAJAT", oneRow.get(0).IDANGAJAT + "");
        where.put("IDCAMERA", oneRow.get(0).IDCAMERA + "");
        where.put("IDCLIENT", oneRow.get(0).IDCLIENT + "");
        where.put("OBJECTIVEID", oneRow.get(0).OBJECTIVEID + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("RentModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void deleteRow(ModelList<InnerRentModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, String> where = new HashMap<>();
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            where.put("RENTDATE", "TO_DATE('" + sdf.format(row.get(0).RENT_DATE) + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            where.put("RENTDATE", sdf.format(row.get(0).RENT_DATE) + " 00:00:00");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            where.put("RENTDATE", sdf.format(row.get(0).RENT_DATE) + " 00:00:00");
        }
        where.put("DURATIONINDAYS", row.get(0).DURATION_IN_DAYS + "");
        where.put("ISRETURNED", row.get(0).IS_RETURNED ? "'1'" : "'0'");
        where.put("PENALTYFEE", row.get(0).PENALTYFEE + "");
        where.put("IDANGAJAT", row.get(0).IDANGAJAT + "");
        where.put("IDCAMERA", row.get(0).IDCAMERA + "");
        where.put("IDCLIENT", row.get(0).IDCLIENT + "");
        where.put("OBJECTIVEID", row.get(0).OBJECTIVEID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("RentModel delete data");
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
            csv = ((CsvConnection) DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.CSV));
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
            logger.log("RentModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerRentModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Pair<String, String>> values = new ArrayList<>();
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("RENTDATE", "TO_DATE('" + sdf.format(row.RENT_DATE) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("RENTDATE", sdf.format(row.RENT_DATE) + ""));
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            values.add(new Pair<String, String>("RENTDATE", sdf.format(row.RENT_DATE) + ""));
        }
        values.add(new Pair<String, String>("DURATIONINDAYS", row.DURATION_IN_DAYS + ""));
        values.add(new Pair<String, String>("IDCAMERA", row.IDCAMERA + ""));
        values.add(new Pair<String, String>("IDCLIENT", row.IDCLIENT + ""));
        values.add(new Pair<String, String>("IDANGAJAT", row.IDANGAJAT + ""));
        values.add(new Pair<String, String>("OBJECTIVEID", row.OBJECTIVEID + ""));
        values.add(new Pair<String, String>("ISRETURNED", row.IS_RETURNED ? "'1'" : "'0'"));
        values.add(new Pair<String, String>("PENALTYFEE", row.PENALTYFEE + ""));

        db.insert(this.tableName, values);

        try{
            logger.log("RentModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
