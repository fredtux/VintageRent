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

public class ClientModel extends Model implements LinkModelToDatabase<ModelList<ClientModel.InnerClientModel>, ClientModel.InnerClientModel> {
    public static class InnerClientModel extends AbstractInnerModel implements Comparable<InnerClientModel> {
        public int IDUtilizator;
        public Date DataNasterii;
        public int IDTip;
        public String DenumireTip;
        public double DiscountTip;
        public String NumeClient;

        @Override
        public int compareTo(InnerClientModel o) {
            return this.IDUtilizator - o.IDUtilizator;
        }
    }
    private String tableName = "CLIENTI";

    private static ClientModel instance = null;
    private ModelList<InnerClientModel> modelList = null;

    public ModelList<InnerClientModel> getModelList() {
        return modelList;
    }

    public static ClientModel getInstance() {
        if (instance == null)
            instance = new ClientModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Clienti.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "CLIENTI";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDUtilizator", "DataNasterii", "IDTip", "DenumireTip", "DiscountTip", "NumeClient"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for(InnerClientModel model : this.modelList.getList()){
            Object[] obj = {model.IDUtilizator, model.DataNasterii, model.IDTip, model.DenumireTip, model.DiscountTip, model.NumeClient};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public ClientModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Clienti";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public ClientModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("ClientModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Clienti";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Clienti.csv";
        else
            this.tableName = "CLIENTI";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerClientModel model = new InnerClientModel();
            model.IDUtilizator = rs.getInt("IDUtilizator");
            model.DataNasterii = rs.getDate("DataNasterii");
            model.IDTip = rs.getInt("IDTip");
            model.NumeClient = rs.getString("NumeClient");

            this.modelList.add(model);
        }
    }

    @Override
    public ModelList<InnerClientModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("CLIENTI", "Clienti.csv");
            tables.put("TIPCLIENTI", "TipClient.csv");
            tables.put("UTILIZATORI", "Utilizatori.csv");
        } else {
            tables = new HashMap<>();
            tables.put("CLIENTI", "CLIENTI");
            tables.put("TIPCLIENTI", "TIPCLIENT");
            tables.put("UTILIZATORI", "UTILIZATORI");
        }


        ResultSet clienti = db.getAllTableData(tables.get("CLIENTI"));
        ResultSet tipClienti = db.getAllTableData(tables.get("TIPCLIENTI"));
        ResultSet utilizatori = db.getAllTableData(tables.get("UTILIZATORI"));

        Map<Integer, String> typeMap = new HashMap<>();
        Map<Integer, Double> discountMap = new HashMap<>();
        while(tipClienti.next()){
            typeMap.put(tipClienti.getInt("IDTIP"), tipClienti.getString("DENUMIRE"));
            discountMap.put(tipClienti.getInt("IDTIP"), tipClienti.getDouble("DISCOUNT"));
        }
        Map<Integer, String> userMap = new HashMap<>();
        while(utilizatori.next()){
            userMap.put(utilizatori.getInt("IDUTILIZATOR"), utilizatori.getString("NUME") + " " + utilizatori.getString("PRENUME"));
        }

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(clienti.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            InnerClientModel model = new InnerClientModel();
            model.IDUtilizator = clienti.getInt("IDUtilizator");
            model.DataNasterii = new Date(sdf.parse(clienti.getString("DataNasterii")).getTime());
            model.IDTip = clienti.getInt("IDTip");
            model.DenumireTip = typeMap.get(clienti.getInt("IDTip"));
            model.DiscountTip = discountMap.get(clienti.getInt("IDTip"));
            model.NumeClient = userMap.get(clienti.getInt("IDUtilizator"));

            this.modelList.add(model);
        }

        try{
            logger.log("ClientModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerClientModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("IDUtilizator", "'" + oneRow.get(0).IDUtilizator + "'");
        set.put("DataNasterii", "'" + oneRow.get(0).DataNasterii + "'");

        Map<String, String> where = new HashMap<>();
        where.put("IDUtilizator", String.valueOf(oneRow.get(0).IDUtilizator));

        db.update(this.tableName, set, where);
        try{
            logger.log("ClientModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET MARCA = '" + oneRow.get(0).Marca + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRET = " + oneRow.get(0).Pret + ", PRETINCHIRIERE = " + oneRow.get(0).PretInchiriere + ", ANFABRICATIE = " + oneRow.get(0).AnFabricatie + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerClientModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDUtilizator", row.get(0).IDUtilizator + "");
        db.delete(this.tableName, where);
        try{
            logger.log("ClientModel delete data");
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
            logger.log("ClientModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerClientModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDUTILIZATOR", row.IDUtilizator + ""));
        values.add(new Pair<>("IDTIP", row.IDTip + ""));
        values.add(new Pair<>("DATANASTERII", row.DataNasterii + ""));


        db.insert(this.tableName, values);

        try{
            logger.log("ClientModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
}
