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

public class CameraTypeModel extends Model implements LinkModelToDatabase<ModelList<CameraTypeModel.InnerCameraTypeModel>, CameraTypeModel.InnerCameraTypeModel> {
    public static class InnerCameraTypeModel extends AbstractInnerModel implements Comparable<InnerCameraTypeModel> {
        public int IDTip;
        public String Denumire;

        @Override
        public int compareTo(InnerCameraTypeModel o) {
            return this.IDTip - o.IDTip;
        }
    }
    private String tableName = "TIPCAMERA";

    private static CameraTypeModel instance = null;
    private ModelList<InnerCameraTypeModel> modelList = null;

    public ModelList<InnerCameraTypeModel> getModelList() {
        return modelList;
    }

    public static CameraTypeModel getInstance() {
        if (instance == null)
            instance = new CameraTypeModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "TipCamera.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "TIPCAMERA";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDTip", "Denumire"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerCameraTypeModel model : this.modelList.getList()){
            Object[] obj = {model.IDTip, model.Denumire};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public CameraTypeModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "TipCamera";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public CameraTypeModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraTypeModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "TipCamera";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "TipCamera.csv";
        else
            this.tableName = "TIPCAMERA";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerCameraTypeModel model = new InnerCameraTypeModel();
            model.IDTip = rs.getInt("IDTIP");
            model.Denumire = rs.getString("DENUMIRE");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerCameraTypeModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("TIPCAMERA", "TipCamera.csv");
        } else {
            tables = new HashMap<>();
            tables.put("TIPCAMERA", "TIPCAMERA");
        }


        ResultSet formats = db.getAllTableData(tables.get("TIPCAMERA"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerCameraTypeModel model = new InnerCameraTypeModel();
            model.IDTip = formats.getInt("IDTIP");
            model.Denumire = formats.getString("DENUMIRE");

            this.modelList.add(model);
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerCameraTypeModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("DENUMIRE", oneRow.get(0).Denumire);
        Map<String, String> where = new HashMap<>();
        where.put("IDTIP", oneRow.get(0).IDTip + "");
        db.update(this.tableName, set, where);
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerCameraTypeModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDTIP", row.get(0).IDTip + "");
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
    public void insertRow(InnerCameraTypeModel row) throws Exception {
;
    }
}
