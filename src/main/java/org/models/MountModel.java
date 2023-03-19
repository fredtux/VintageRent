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

public class MountModel extends Model implements LinkModelToDatabase<ModelList<MountModel.InnerMountModel>, MountModel.InnerMountModel> {
    public static class InnerMountModel extends AbstractInnerModel implements Comparable<InnerMountModel> {
        public int IDMontura;
        public String Denumire;

        @Override
        public int compareTo(InnerMountModel o) {
            return this.IDMontura - o.IDMontura;
        }
    }
    private String tableName = "MONTURA";

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
            this.tableName = "Montura.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "MONTURA";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "mount";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDTip", "Denumire"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerMountModel model : this.modelList.getList()){
            Object[] obj = {model.IDMontura, model.Denumire};
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

        this.name = "Montura";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public MountModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("MountModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Montura";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Montura.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "MONTURA";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "mount";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerMountModel model = new InnerMountModel();
            model.IDMontura = rs.getInt("IDMONTURA");
            model.Denumire = rs.getString("DENUMIRE");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerMountModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("MONTURA", "Montura.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("MONTURA", "MONTURA");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("MONTURA", "mount");
        }


        ResultSet formats = db.getAllTableData(tables.get("MONTURA"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerMountModel model = new InnerMountModel();
            model.IDMontura = formats.getInt("IDMONTURA");
            model.Denumire = formats.getString("DENUMIRE");

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
        set.put("DENUMIRE", "'" + oneRow.get(0).Denumire + "'");
        Map<String, String> where = new HashMap<>();
        where.put("IDMONTURA", oneRow.get(0).IDMontura + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("MountModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerMountModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDMONTURA", row.get(0).IDMontura + "");
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
        values.add(new Pair<>("IDMONTURA", ""));
        values.add(new Pair<>("DENUMIRE", "'" + row.Denumire + "'"));

        db.insert(this.tableName, values);

        try{
            logger.log("MountModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
