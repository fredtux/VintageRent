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
        public int DURATA_IN_ZILE;
        public boolean ESTE_RETURNAT;
        public double PENALIZARE;
        public Date DATA_INCHIRIERE;
        public int IDCAMERA;
        public int IDCLIENT;
        public int IDOBIECTIV;
        public int IDANGAJAT;
        public String NUME_CAMERA;
        public String NUME_CLIENT;
        public String NUME_OBIECTIV;
        public String NUME_ANGAJAT;
    }
    private String tableName = "INCHIRIERE";

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
            this.tableName = "Inchiriere.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "INCHIRIERE";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"DURATA_IN_ZILE", "ESTE_RETURNAT", "PENALIZARE", "DATA_INCHIRIERE", "IDCAMERA", "IDCLIENT", "IDOBIECTIV", "IDANGAJAT"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerRentModel model : this.modelList.getList()){
            Object[] obj = {model.DURATA_IN_ZILE, model.ESTE_RETURNAT, model.PENALIZARE, model.DATA_INCHIRIERE, model.IDCAMERA, model.IDCLIENT, model.IDOBIECTIV, model.IDANGAJAT};
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
            this.tableName = "Inchiriere.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "INCHIRIERE";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>();

        while(rs.next()){
            InnerRentModel model = new InnerRentModel();
            model.DURATA_IN_ZILE = rs.getInt("DURATAINZILE");
            try {
                model.DATA_INCHIRIERE = rs.getDate("DATAINCHIRIERE");
            } catch (Exception e) {
                LocalDateTime localDateTime = LocalDateTime.parse(rs.getString("DATAINCHIRIERE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.DATA_INCHIRIERE = Date.valueOf(localDateTime.toLocalDate());
            }
            model.IDANGAJAT = rs.getInt("IDANGAJAT");
            model.IDCAMERA = rs.getInt("IDCAMERA");
            model.IDCLIENT = rs.getInt("IDCLIENT");
            model.IDOBIECTIV = rs.getInt("IDOBIECTIV");
            model.ESTE_RETURNAT = rs.getBoolean("ESTERETURNAT");
            model.PENALIZARE = rs.getDouble("PENALIZARE");
            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerRentModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("INCHIRIERE", "Inchiriere.csv");
            tables.put("CAMERE", "Camere.csv");
            tables.put("CLIENTI", "Clienti.csv");
            tables.put("ANGAJATI", "Angajati.csv");
            tables.put("OBIECTIVE", "Obiective.csv");
        } else {
            tables = new HashMap<>();
            tables.put("INCHIRIERE", "INCHIRIERE");
            tables.put("CAMERE", "CAMERE");
            tables.put("CLIENTI", "CLIENTI");
            tables.put("ANGAJATI", "ANGAJATI");
            tables.put("OBIECTIVE", "OBIECTIVE");
        }


        ResultSet inchiriere = db.getAllTableData(tables.get("INCHIRIERE"));
        ResultSet camere = db.getAllTableData(tables.get("CAMERE"));
        ResultSet clienti = db.getAllTableData(tables.get("CLIENTI"));
        ResultSet angajati = db.getAllTableData(tables.get("ANGAJATI"));
        ResultSet obiective = db.getAllTableData(tables.get("OBIECTIVE"));

        Map<Integer, String> camereMap = new HashMap<>();
        while(camere.next())
            camereMap.put(camere.getInt("IDCAMERA"), camere.getString("MODELCAMERA"));

        Map<Integer, String> clientiMap = new HashMap<>();
        while(clienti.next())
            clientiMap.put(clienti.getInt("IDUTILIZATOR"), clienti.getString("IDUTILIZATOR"));

        Map<Integer, String> angajatiMap = new HashMap<>();
        while(angajati.next())
            angajatiMap.put(angajati.getInt("IDUTILIZATOR"), angajati.getString("IDUTILIZATOR"));

        Map<Integer, String> obiectiveMap = new HashMap<>();
        while(obiective.next())
            obiectiveMap.put(obiective.getInt("IDOBIECTIV"), obiective.getString("DENUMIRE"));

        this.modelList = new ModelList<>();
        while(inchiriere.next()){
            InnerRentModel model = new InnerRentModel();
            try {
                model.DATA_INCHIRIERE = inchiriere.getDate("DATAINCHIRIERE");
            } catch (Exception e) {
                LocalDateTime localDateTime = LocalDateTime.parse(inchiriere.getString("DATAINCHIRIERE"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.DATA_INCHIRIERE = Date.valueOf(localDateTime.toLocalDate());
            }
            model.DURATA_IN_ZILE = inchiriere.getInt("DURATAINZILE");
            model.IDCAMERA = inchiriere.getInt("IDCAMERA");
            model.IDCLIENT = inchiriere.getInt("IDCLIENT");
            model.IDANGAJAT = inchiriere.getInt("IDANGAJAT");
            model.IDOBIECTIV = inchiriere.getInt("IDOBIECTIV");
            model.ESTE_RETURNAT = inchiriere.getBoolean("ESTERETURNAT");
            model.PENALIZARE = inchiriere.getDouble("PENALIZARE");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

        Map<String, String> set = new HashMap<>();
        set.put("DURATAINZILE", oneRow.get(0).DURATA_IN_ZILE + "");
        set.put("ESTERETURNAT", oneRow.get(0).ESTE_RETURNAT ? "'1'" : "'0'");
        set.put("PENALIZARE", oneRow.get(0).PENALIZARE + "");
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("DATAINCHIRIERE", "TO_DATE('" + oneRow.get(0).DATA_INCHIRIERE + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("DATAINCHIRIERE", oneRow.get(0).DATA_INCHIRIERE + "");
        }

        Map<String, String> where = new HashMap<>();
        where.put("IDANGAJAT", oneRow.get(0).IDANGAJAT + "");
        where.put("IDCAMERA", oneRow.get(0).IDCAMERA + "");
        where.put("IDCLIENT", oneRow.get(0).IDCLIENT + "");
        where.put("IDOBIECTIV", oneRow.get(0).IDOBIECTIV + "");
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

        Map<String, String> where = new HashMap<>();
        where.put("IDANGAJAT", row.get(0).IDANGAJAT + "");
        where.put("IDCAMERA", row.get(0).IDCAMERA + "");
        where.put("IDCLIENT", row.get(0).IDCLIENT + "");
        where.put("IDOBIECTIV", row.get(0).IDOBIECTIV + "");
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
            values.add(new Pair<String, String>("DATAINCHIRIERE", "TO_DATE('" + sdf.format(row.DATA_INCHIRIERE) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("DATAINCHIRIERE", sdf.format(row.DATA_INCHIRIERE) + ""));
        }
        values.add(new Pair<String, String>("DURATAINZILE", row.DURATA_IN_ZILE + ""));
        values.add(new Pair<String, String>("IDCAMERA", row.IDCAMERA + ""));
        values.add(new Pair<String, String>("IDCLIENT", row.IDCLIENT + ""));
        values.add(new Pair<String, String>("IDANGAJAT", row.IDANGAJAT + ""));
        values.add(new Pair<String, String>("IDOBIECTIV", row.IDOBIECTIV + ""));
        values.add(new Pair<String, String>("ESTERETURNAT", row.ESTE_RETURNAT ? "'1'" : "'0'"));
        values.add(new Pair<String, String>("PENALIZARE", row.PENALIZARE + ""));

        db.insert(this.tableName, values);

        try{
            logger.log("RentModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
