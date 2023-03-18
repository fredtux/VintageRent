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

public class EmployeeModel extends Model implements LinkModelToDatabase<ModelList<EmployeeModel.InnerEmployeeModel>, EmployeeModel.InnerEmployeeModel> {
    public static class InnerEmployeeModel extends AbstractInnerModel implements Comparable<InnerEmployeeModel> {
        public int IDUtilizator;
        public LocalDateTime DataNasterii;
        public LocalDateTime DataAngajarii;
        public int IDManager;
        public int IDSalariu;
        public int Salariu;
        public String NumeAngajat;
        public String NumeManager;

        @Override
        public int compareTo(InnerEmployeeModel o) {
            return this.IDUtilizator - o.IDUtilizator;
        }
    }
    private String tableName = "ANGAJATI";

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
            this.tableName = "Angajati.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "ANGAJATI";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDUtilizator", "DataNasterii", "DataAngajarii", "IDManager", "IDSalariu", "Salariu", "NumeAngajat", "NumeManager"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerEmployeeModel model : this.modelList.getList()){
            Object[] obj = {model.IDUtilizator, model.DataNasterii, model.DataAngajarii, model.IDManager, model.IDSalariu, model.Salariu, model.NumeAngajat, model.NumeManager};
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

        this.name = "Angajati";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public EmployeeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("EmployeeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Angajati";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Angajati.csv";
        else
            this.tableName = "ANGAJATI";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while(rs.next()){
            InnerEmployeeModel model = new InnerEmployeeModel();
            model.IDUtilizator = rs.getInt("IDUTILIZATOR");
            model.DataNasterii = LocalDateTime.parse(rs.getString("DATANASTERII"));
            model.DataAngajarii = LocalDateTime.parse(rs.getString("DATAANGAJARII"));
            model.IDManager = rs.getInt("IDMANAGER");
            model.IDSalariu = rs.getInt("IDSALARIU");
            model.Salariu = rs.getInt("SALARIU");
            model.NumeAngajat = rs.getString("NUMEANGAJAT");
            model.NumeManager = rs.getString("NUMEMANAGER");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerEmployeeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("ANGAJATI", "Angajati.csv");
            tables.put("SALARIU", "Salariu.csv");
            tables.put("UTILIZATORI", "Utilizatori.csv");
        } else {
            tables = new HashMap<>();
            tables.put("ANGAJATI", "ANGAJATI");
            tables.put("SALARIU", "SALARIU");
            tables.put("UTILIZATORI", "UTILIZATORI");
        }


        ResultSet angajati = db.getAllTableData(tables.get("ANGAJATI"));
        ResultSet salarii = db.getAllTableData(tables.get("SALARIU"));
        ResultSet utilizatori = db.getAllTableData(tables.get("UTILIZATORI"));

        Map<Integer, Integer> salariuMap = new HashMap<>();
        while(salarii.next()){
            salariuMap.put(salarii.getInt("IDSALARIU"), salarii.getInt("IDSALARIU"));
        }

        Map<Integer, String> userMap = new HashMap<>();
        while(utilizatori.next()){
            userMap.put(utilizatori.getInt("IDUTILIZATOR"), utilizatori.getString("NUME") + " " + utilizatori.getString("PRENUME"));
        }

        // Add fields to cameras
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.modelList = new ModelList<>(true);
        while(angajati.next()) {
            InnerEmployeeModel model = new InnerEmployeeModel();
            model.IDUtilizator = angajati.getInt("IDUTILIZATOR");
            model.DataNasterii = LocalDateTime.parse(angajati.getString("DATANASTERII"), formatter);
            model.DataAngajarii = LocalDateTime.parse(angajati.getString("DATAANGAJARII"), formatter);
            String manager = angajati.getString("IDMANAGER");
            if(manager == null || manager == ""){
                model.IDManager = 0;
            } else {
                model.IDManager = Integer.parseInt(manager);
            }
            model.IDSalariu = angajati.getInt("IDSALARIU");
            model.Salariu = salariuMap.get(model.IDSalariu);
            model.NumeAngajat = userMap.get(model.IDUtilizator);
            model.NumeManager = userMap.get(model.IDManager);

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
        set.put("IDUTILIZATOR", "'" + oneRow.get(0).IDUtilizator + "'");
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("DATANASTERII", "TO_DATE('" + oneRow.get(0).DataNasterii.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("DATANASTERII", oneRow.get(0).DataNasterii.format(dtf) + "");
        }

        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            set.put("DATAANGAJARII", "TO_DATE('" + oneRow.get(0).DataAngajarii.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')");
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            set.put("DATAANGAJARII", oneRow.get(0).DataAngajarii.format(dtf) + "");
        }


        Map<String, String> where = new HashMap<>();
        where.put("IDUtilizator", String.valueOf(oneRow.get(0).IDUtilizator));

        db.update(this.tableName, set, where);
        try{
            logger.log("EmployeeModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerEmployeeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDUTILIZATOR", row.get(0).IDUtilizator + "");
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
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<String, String>("IDUTILIZATOR", row.IDUtilizator + ""));
        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("DATANASTERII", "TO_DATE('" + row.DataNasterii.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("DATANASTERII", row.DataNasterii.format(dtf) + ""));
        }

        if(databaseType == DatabaseConnection.DatabaseType.ORACLE)
            values.add(new Pair<String, String>("DATAANGAJARII", "TO_DATE('" + row.DataAngajarii.format(dtf) + "', 'YYYY-MM-DD HH24:MI:SS')"));
        else if(databaseType == DatabaseConnection.DatabaseType.CSV){
            values.add(new Pair<String, String>("DATAANGAJARII", row.DataAngajarii.format(dtf) + ""));
        }

        values.add(new Pair<>("IDMANAGER", row.IDManager + ""));
        values.add(new Pair<>("IDSALARIU", row.IDSalariu + ""));

        db.insert(this.tableName, values);
        try{
            logger.log("EmployeeModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
