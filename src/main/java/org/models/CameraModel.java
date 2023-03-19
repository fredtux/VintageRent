package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraModel extends Model implements LinkModelToDatabase<ModelList<CameraModel.InnerCameraModel>, CameraModel.InnerCameraModel> {
    public static class InnerCameraModel extends AbstractInnerModel implements Comparable<InnerCameraModel> {
        public String Marca;
        public String ModelCamera;
        public int AnFabricatie;
        public double Pret;
        public double PretInchiriere;
        public int IDCamera;

        public String DenumireFormat;
        public String LatimeFilm;
        public String DenumireTip;
        public String DenumireMontura;
        public int IDFormat;
        public int IDTip;
        public int IDMontura;

        @Override
        public int compareTo(InnerCameraModel o) {
            return this.IDCamera - o.IDCamera;
        }
    }
    private String tableName = "CAMERE";

    private static CameraModel instance = null;
    private ModelList<InnerCameraModel> modelList = null;

    public ModelList<InnerCameraModel> getModelList() {
        return modelList;
    }

    public static CameraModel getInstance() {
        if (instance == null)
            instance = new CameraModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Camere.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CAMERE";
        } else if (databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "camera";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDCamera", "Marca", "ModelCamera", "AnFabricatie", "Pret", "PretInchiriere", "DenumireFormat", "LatimeFilm", "DenumireTip", "DenumireMontura"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerCameraModel model : this.modelList.getList()){
            Object[] obj = {model.IDCamera, model.Marca, model.ModelCamera, model.AnFabricatie, model.Pret, model.PretInchiriere, model.DenumireFormat, model.LatimeFilm, model.DenumireTip, model.DenumireMontura};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public CameraModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public CameraModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("CameraModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Camera";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Camere.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "CAMERE";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "camera";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerCameraModel model = new InnerCameraModel();
            model.Marca = rs.getString("MARCA");
            model.ModelCamera = rs.getString("MODELCAMERA");
            model.AnFabricatie = rs.getInt("ANFABRICATIE");
            model.Pret = rs.getDouble("PRET");
            model.PretInchiriere = rs.getDouble("PRETINCHIRIERE");
            model.IDCamera = rs.getInt("IDCAMERA");
            model.DenumireTip = rs.getString("DENUMIRETIP");
            model.DenumireFormat = rs.getString("DENUMIREFORMAT");
            model.LatimeFilm = rs.getString("LATIMEFILM");
            model.DenumireMontura = rs.getString("DENUMIREMONTURA");
            model.IDFormat = rs.getInt("IDFORMAT");
            model.IDTip = rs.getInt("IDTIP");
            model.IDMontura = rs.getInt("IDMONTURA");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerCameraModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CAMERE", "Camere.csv");
            tables.put("FORMAT", "Format.csv");
            tables.put("TIPCAMERA", "TipCamera.csv");
            tables.put("MONTURA", "Montura.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE){
            tables = new HashMap<>();
            tables.put("CAMERE", "CAMERE");
            tables.put("FORMAT", "FORMAT");
            tables.put("TIPCAMERA", "TIPCAMERA");
            tables.put("MONTURA", "MONTURA");
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            tables = new HashMap<>();
            tables.put("CAMERE", "camera");
            tables.put("FORMAT", "format");
            tables.put("TIPCAMERA", "camera_type");
            tables.put("MONTURA", "mount");
        }


        ResultSet cameras = db.getAllTableData(tables.get("CAMERE"));
        ResultSet formats = db.getAllTableData(tables.get("FORMAT"));
        ResultSet types = db.getAllTableData(tables.get("TIPCAMERA"));
        ResultSet mounts = db.getAllTableData(tables.get("MONTURA"));

        // Make a HashMap of formats with IDFORMAT as key
        Map<Integer, String> formatMap = new HashMap<>();
        while(formats.next()) {
            formatMap.put(formats.getInt("IDFORMAT"), formats.getString("DENUMIRE"));
        }

        // Make a HashMap of types with IDTIP as key
        Map<Integer, String> typeMap = new HashMap<>();
        while(types.next()) {
            typeMap.put(types.getInt("IDTIP"), types.getString("DENUMIRE"));
        }

        // Make a HashMap of mounts with IDMONTURA as key
        Map<Integer, String> mountMap = new HashMap<>();
        while(mounts.next()) {
            mountMap.put(mounts.getInt("IDMONTURA"), mounts.getString("DENUMIRE"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(cameras.next()) {
            InnerCameraModel model = new InnerCameraModel();
            model.Marca = cameras.getString("MARCA");
            model.ModelCamera = cameras.getString("MODELCAMERA");
            model.AnFabricatie = cameras.getInt("ANFABRICATIE");
            model.Pret = cameras.getDouble("PRET");
            model.PretInchiriere = cameras.getDouble("PRETINCHIRIERE");
            model.IDCamera = cameras.getInt("IDCAMERA");
            model.DenumireTip = typeMap.get(cameras.getInt("IDTIP"));
            model.DenumireFormat = formatMap.get(cameras.getInt("IDFORMAT"));
            model.LatimeFilm = formatMap.get(cameras.getInt("IDFORMAT"));
            model.DenumireMontura = mountMap.get(cameras.getInt("IDMONTURA"));
            model.IDFormat = cameras.getInt("IDFORMAT");
            model.IDTip = cameras.getInt("IDTIP");
            model.IDMontura = cameras.getInt("IDMONTURA");

            this.modelList.add(model);
        }

        try{
            logger.log("CameraModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerCameraModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("MARCA", "'" + oneRow.get(0).Marca + "'");
        set.put("MODELCAMERA", "'" + oneRow.get(0).ModelCamera + "'");
        set.put("IDFORMAT", String.valueOf(oneRow.get(0).IDFormat));
        set.put("IDTIP", String.valueOf(oneRow.get(0).IDTip));
        set.put("IDMONTURA", String.valueOf(oneRow.get(0).IDMontura));
        set.put("ANFABRICATIE", String.valueOf(oneRow.get(0).AnFabricatie));
        set.put("PRET", String.valueOf(oneRow.get(0).Pret));
        set.put("PRETINCHIRIERE", String.valueOf(oneRow.get(0).PretInchiriere));

        Map<String, String> where = new HashMap<>();
        where.put("IDCAMERA", String.valueOf(oneRow.get(0).IDCamera));
        db.update(this.tableName, set, where);
        try{
            logger.log("CameraModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerCameraModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDCAMERA", row.get(0).IDCamera + "");
        db.delete(this.tableName, where);
        try{
            logger.log("CameraModel delete data");
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
            logger.log("CameraModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerCameraModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDCAMERA", ""));
        values.add(new Pair<>("MARCA", "'" + row.Marca + "'"));
        values.add(new Pair<>("MODELCAMERA", "'" + row.ModelCamera + "'"));
        values.add(new Pair<>("IDFORMAT", String.valueOf(row.IDFormat)));
        values.add(new Pair<>("IDTIP", String.valueOf(row.IDTip)));
        values.add(new Pair<>("IDMONTURA", String.valueOf(row.IDMontura)));
        values.add(new Pair<>("ANFABRICATIE", String.valueOf(row.AnFabricatie)));
        values.add(new Pair<>("PRET", String.valueOf(row.Pret)));
        values.add(new Pair<>("PRETINCHIRIERE", String.valueOf(row.PretInchiriere)));


        db.insert(this.tableName, values);
        try{
            logger.log("CameraModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
