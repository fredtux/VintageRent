package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectiveModel extends Model implements LinkModelToDatabase<ModelList<ObjectiveModel.InnerObjectiveModel>, ObjectiveModel.InnerObjectiveModel> {
    public static class InnerObjectiveModel extends AbstractInnerModel implements Comparable<InnerObjectiveModel> {
        public int IDObiectiv;
        public String Denumire;
        public int DistantaFocala;
        public double DiafragmaMinima;
        public int Diametru;
        public int Pret;
        public int PretInchiriere;
        public int IDMontura;
        public String DenumireMontura;


        @Override
        public int compareTo(InnerObjectiveModel o) {
            return this.IDObiectiv - o.IDObiectiv;
        }
    }
    private String tableName = "OBIECTIVE";

    private static ObjectiveModel instance = null;
    private ModelList<InnerObjectiveModel> modelList = null;

    public ModelList<InnerObjectiveModel> getModelList() {
        return modelList;
    }

    public static ObjectiveModel getInstance() {
        if (instance == null)
            instance = new ObjectiveModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Obiective.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "OBIECTIVE";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDObiectiv", "Denumire", "DistantaFocala", "DiafragmaMinima", "Diametru", "Pret", "PretInchiriere", "IDMontura", "DenumireMontura"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerObjectiveModel model : this.modelList.getList()){
            Object[] obj = {model.IDObiectiv, model.Denumire, model.DistantaFocala, model.DiafragmaMinima, model.Diametru, model.Pret, model.PretInchiriere, model.IDMontura, model.DenumireMontura};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public ObjectiveModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ObjectiveModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Obiective";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ObjectiveModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ObjectiveModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Obiective";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Obiective.csv";
        else
            this.tableName = "OBIECTIVE";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerObjectiveModel model = new InnerObjectiveModel();
            model.IDObiectiv = rs.getInt("IDOBIECTIV");
            model.Denumire = rs.getString("DENUMIRE");
            model.DistantaFocala = rs.getInt("DISTANTAFOCALA");
            model.DiafragmaMinima = rs.getDouble("DIAFRAGMAMINIMA");
            model.Diametru = rs.getInt("DIAMETRU");
            model.Pret = rs.getInt("PRET");


            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerObjectiveModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("OBIECTIVE", "Obiective.csv");
            tables.put("MONTURA", "Montura.csv");
        } else {
            tables = new HashMap<>();
            tables.put("OBIECTIVE", "OBIECTIVE");
            tables.put("MONTURA", "MONTURA");
        }


        ResultSet obiective = db.getAllTableData(tables.get("OBIECTIVE"));
        ResultSet monturi = db.getAllTableData(tables.get("MONTURA"));

        Map<Integer, String> monturaMap = new HashMap<>();
        while(monturi.next()){
            monturaMap.put(monturi.getInt("IDMONTURA"), monturi.getString("DENUMIRE"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(obiective.next()) {
            InnerObjectiveModel model = new InnerObjectiveModel();
            model.IDObiectiv = obiective.getInt("IDOBIECTIV");
            model.Denumire = obiective.getString("DENUMIRE");
            model.DistantaFocala = obiective.getInt("DISTANTAFOCALA");
            model.DiafragmaMinima = obiective.getDouble("DIAFRAGMAMINIMA");
            model.Diametru = obiective.getInt("DIAMETRU");
            model.Pret = obiective.getInt("PRET");
            model.PretInchiriere = obiective.getInt("PRETINCHIRIERE");
            model.IDMontura = obiective.getInt("IDMONTURA");
            model.DenumireMontura = monturaMap.get(model.IDMontura);

            this.modelList.add(model);
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerObjectiveModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("DENUMIRE", oneRow.get(0).Denumire);
        set.put("DISTANTAFOCALA", String.valueOf(oneRow.get(0).DistantaFocala));
        set.put("DIAFRAGMAMINIMA", String.valueOf(oneRow.get(0).DiafragmaMinima));
        set.put("DIAMETRU", String.valueOf(oneRow.get(0).Diametru));
        set.put("PRET", String.valueOf(oneRow.get(0).Pret));
        set.put("PRETINCHIRIERE", String.valueOf(oneRow.get(0).PretInchiriere));


        Map<String, String> where = new HashMap<>();
        where.put("IDOBIECTIV", oneRow.get(0).IDObiectiv + "");

        db.update(this.tableName, set, where);
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerObjectiveModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDOBIECTIV", row.get(0).IDObiectiv + "");
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
    public void insertRow(InnerObjectiveModel row) throws Exception {
        ;
    }
}
