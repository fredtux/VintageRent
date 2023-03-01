package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
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
        public int IDUtilizator;
        public String NumeUtilizator;
        public String Parola;
        public String Nume;
        public String Prenume;
        public String CNP;
        public String Email;

        @Override
        public int compareTo(InnerUserModel o) {
            return this.IDUtilizator - o.IDUtilizator;
        }
    }
    private String tableName = "UTILIZATORI";

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
            this.tableName = "Utilizatori.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "UTILIZATORI";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDUtilizator", "NumeUtilizator", "Parola", "Nume", "Prenume", "CNP", "Email"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerUserModel model : this.modelList.getList()){
            Object[] obj = {model.IDUtilizator, model.NumeUtilizator, model.Parola, model.Nume, model.Prenume, model.CNP, model.Email};
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

        this.name = "Utilizatori";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public UserModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("UserModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Utilizatori";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Utilizatori.csv";
        else
            this.tableName = "UTILIZATORI";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerUserModel model = new InnerUserModel();
            model.IDUtilizator = rs.getInt("IDUTILIZATOR");
            model.NumeUtilizator = rs.getString("NUMEUTILIZATOR");
            model.Parola = rs.getString("PAROLA");
            model.Nume = rs.getString("NUME");
            model.Prenume = rs.getString("PRENUME");
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
            tables.put("UTILIZATORI", "Utilizatori.csv");
        } else {
            tables = new HashMap<>();
            tables.put("UTILIZATORI", "UTILIZATORI");
        }


        ResultSet angajati = db.getAllTableData(tables.get("UTILIZATORI"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(angajati.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            InnerUserModel model = new InnerUserModel();
            model.IDUtilizator = angajati.getInt("IDUTILIZATOR");
            model.NumeUtilizator = angajati.getString("NUMEUTILIZATOR");
            model.Parola = angajati.getString("PAROLA");
            model.Nume = angajati.getString("NUME");
            model.Prenume = angajati.getString("PRENUME");
            model.CNP = angajati.getString("CNP");
            model.Email = angajati.getString("EMAIL");

            this.modelList.add(model);
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerUserModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("IDUTILIZATOR", "'" + oneRow.get(0).IDUtilizator + "'");
        set.put("NUMEUTILIZATOR", "'" + oneRow.get(0).NumeUtilizator + "'");
        set.put("PAROLA", "'" + oneRow.get(0).Parola + "'");
        set.put("NUME", "'" + oneRow.get(0).Nume + "'");
        set.put("PRENUME", "'" + oneRow.get(0).Prenume + "'");
        set.put("CNP", "'" + oneRow.get(0).CNP + "'");
        set.put("EMAIL", "'" + oneRow.get(0).Email + "'");

        Map<String, String> where = new HashMap<>();
        where.put("IDUtilizator", String.valueOf(oneRow.get(0).IDUtilizator));

        db.update(this.tableName, set, where);
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerUserModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDUTILIZATOR", row.get(0).IDUtilizator + "");
        db.delete(this.tableName, where);
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
    }

    @Override
    public void insertRow(InnerUserModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDUTILIZATOR",""));
        values.add(new Pair<>("NUMEUTILIZATOR", row.NumeUtilizator));
        values.add(new Pair<>("PAROLA", row.Parola));
        values.add(new Pair<>("NUME", row.Nume));
        values.add(new Pair<>("PRENUME", row.Prenume));
        values.add(new Pair<>("CNP", row.CNP));
        values.add(new Pair<>("EMAIL", row.Email));

        db.insert(this.tableName, values);
    }
}
