package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryModel extends Model implements LinkModelToDatabase<ModelList<SalaryModel.InnerSalaryModel>, SalaryModel.InnerSalaryModel> {
    public static class InnerSalaryModel extends AbstractInnerModel implements Comparable<InnerSalaryModel> {
        public int IDSalariu;
        public int Salariu;
        public double Bonus;

        @Override
        public int compareTo(InnerSalaryModel o) {
            return this.IDSalariu - o.IDSalariu;
        }
    }
    private String tableName = "SALARIU";

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
            this.tableName = "Salariu.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "SALARIU";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDSalariu", "Salariu", "Bonus"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerSalaryModel model : this.modelList.getList()){
            Object[] obj = {model.IDSalariu, model.Salariu, model.Bonus};
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

        this.name = "Salariu";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public SalaryModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("SalaryModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Salariu";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Salariu.csv";
        else
            this.tableName = "SALARIU";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerSalaryModel model = new InnerSalaryModel();
            model.IDSalariu = rs.getInt("IDSALARIU");
            model.Salariu = rs.getInt("SALARIU");
            model.Bonus = rs.getDouble("BONUS");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerSalaryModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("SALARIU", "Salariu.csv");
        } else {
            tables = new HashMap<>();
            tables.put("SALARIU", "SALARIU");
        }


        ResultSet formats = db.getAllTableData(tables.get("SALARIU"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerSalaryModel model = new InnerSalaryModel();
            model.IDSalariu = formats.getInt("IDSALARIU");
            model.Salariu = formats.getInt("SALARIU");
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
        set.put("SALARIU", oneRow.get(0).Salariu + "");
        set.put("BONUS", oneRow.get(0).Bonus + "");
        Map<String, String> where = new HashMap<>();
        where.put("IDSALARIU", oneRow.get(0).IDSalariu + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("SalaryModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerSalaryModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDSALARIU", row.get(0).IDSalariu + "");
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
        values.add(new Pair<>("IDSALARIU",""));
        values.add(new Pair<>("SALARIU", row.Salariu + ""));
        values.add(new Pair<>("BONUS", row.Bonus + ""));

        db.insert(this.tableName, values);

        try{
            logger.log("SalaryModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
