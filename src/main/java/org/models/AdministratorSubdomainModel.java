package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AdministratorSubdomainModel extends Model implements LinkModelToDatabase<ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel>, AdministratorSubdomainModel.InnerAdministratorSubdomainModel> {

    public static class InnerAdministratorSubdomainModel extends AbstractInnerModel implements Comparable<InnerAdministratorSubdomainModel> {
        public int IDAdministrator;
        public int SubdomainID;

        @Override
        public int compareTo(InnerAdministratorSubdomainModel o) {
            return this.IDAdministrator - o.IDAdministrator;
        }
    }
    private String tableName = "ADMINISTRATOR_SUBDOMAINS";

    private static AdministratorSubdomainModel instance = null;
    private ModelList<InnerAdministratorSubdomainModel> modelList = null;

    public ModelList<InnerAdministratorSubdomainModel> getModelList() {
        return modelList;
    }

    public static AdministratorSubdomainModel getInstance() {
        if (instance == null)
            instance = new AdministratorSubdomainModel();

        return instance;
    }

    public void setDatabaseType(DatabaseConnection.DatabaseType databaseType) {
        this.databaseType = databaseType;

        if(databaseType == DatabaseConnection.DatabaseType.CSV){
            this.tableName = "Administrator_Subdomains.csv";
        } else if (databaseType == DatabaseConnection.DatabaseType.ORACLE){
            this.tableName = "ADMINISTRATOR_SUBDOMAINS";
        } else if(databaseType == DatabaseConnection.DatabaseType.INMEMORY){
            this.tableName = "administrator_subdomain";
        }
    }

    public DefaultTableModel getTableModel() {
        String[] columns = {"IDAdministrator", "SubdomainID"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for(InnerAdministratorSubdomainModel model : this.modelList.getList()){
            Object[] obj = {model.IDAdministrator, model.SubdomainID};
            tableModel.addRow(obj);
        }


        return tableModel;
    }

    public AdministratorSubdomainModel() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AdministratorSubdomainModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "Administrator_Subdomain";
        this.databaseType = DatabaseConnection.DatabaseType.ORACLE;
    }

    public AdministratorSubdomainModel(DatabaseConnection.DatabaseType t) {
        // Singleton
        if(instance != null)
            throw new RuntimeException("AdministratorSubdomainModel is a singleton class. Use getInstance() instead.");
        else
            instance = this;

        this.name = "ADMINISTRATOR_SUBDOMAINS";
        this.databaseType = t;
        if(t == DatabaseConnection.DatabaseType.CSV)
            this.tableName = "Administrator_Subdomains.csv";
        else if(t == DatabaseConnection.DatabaseType.ORACLE)
            this.tableName = "ADMINISTRATOR_SUBDOMAINS";
        else if(t == DatabaseConnection.DatabaseType.INMEMORY)
            this.tableName = "administrator_subdomain";
    }

    private void transferToModelList(ResultSet rs) throws Exception{
        this.modelList = new ModelList<>(true);

        while(rs.next()){
            InnerAdministratorSubdomainModel model = new InnerAdministratorSubdomainModel();
            model.IDAdministrator = rs.getInt("ADMINISTRATORID");
            model.SubdomainID = rs.getInt("SubdomainID");

            this.modelList.add(model);
        }
    }

    public void getFilteredData(String comparator, String value, String column) throws Exception{
        this.getData();

        Predicate<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> predicate =  null;

        switch (column) {
            case "IDAdministrator":
                case "SubdomainID":
                predicate = (AdministratorSubdomainModel.InnerAdministratorSubdomainModel model) -> {
                    try {
                        Field field = model.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        int fieldValue = (int) field.get(model);
                        if(comparator == "==")
                            return fieldValue == Integer.parseInt(value);
                        else if(comparator == "!=")
                            return fieldValue != Integer.parseInt(value);
                        else if(comparator == "<")
                            return fieldValue < Integer.parseInt(value);
                        else if(comparator == ">")
                            return fieldValue > Integer.parseInt(value);
                        else if(comparator == "<=")
                            return fieldValue <= Integer.parseInt(value);
                        else if(comparator == ">=")
                            return fieldValue >= Integer.parseInt(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                break;
        }

        this.modelList = this.modelList.filter(predicate, value);
    }


    @Override
    public ModelList<InnerAdministratorSubdomainModel> getData() throws Exception{
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> tables = null;
        if(databaseType == DatabaseConnection.DatabaseType.CSV) {
            tables = new HashMap<>();
            tables.put("ADMINISTRATOR_SUBDOMAINS", "Administrator_Subdomains.csv");
        } else if(databaseType == DatabaseConnection.DatabaseType.ORACLE) {
            tables = new HashMap<>();
            tables.put("ADMINISTRATOR_SUBDOMAINS", "ADMINISTRATOR_SUBDOMAINS");
        } else {
            tables = new HashMap<>();
            tables.put("ADMINISTRATOR_SUBDOMAINS", "administrator_subdomain");
        }


        ResultSet formats = db.getAllTableData(tables.get("ADMINISTRATOR_SUBDOMAINS"));

        // Add fields to cameras
        this.modelList = new ModelList<>(true);
        while(formats.next()) {
            InnerAdministratorSubdomainModel model = new InnerAdministratorSubdomainModel();
            model.IDAdministrator = formats.getInt("IDADMINISTRATOR");
            model.SubdomainID = formats.getInt("SUBDOMAINID");

            this.modelList.add(model);
        }

        try{
            logger.log("AdministratorSubdomainModel got data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        return this.modelList;
    }

    @Override
    public void updateData(ModelList<InnerAdministratorSubdomainModel> oneRow) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        Map<String, String> set = new HashMap<>();
        set.put("SUBDOMAINID", oneRow.get(0).SubdomainID + "");
        Map<String, String> where = new HashMap<>();
        where.put("IDADMINISTRATOR", oneRow.get(0).IDAdministrator + "");
        db.update(this.tableName, set, where);
        try{
            logger.log("AdministratorSubdomainModel update data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
//        db.update("UPDATE " + this.tableName + " SET BRAND = '" + oneRow.get(0).Brand + "', MODELCAMERA = '" + oneRow.get(0).ModelCamera + "', PRICE = " + oneRow.get(0).Price + ", RENTALPRICE = " + oneRow.get(0).RentalPrice + ", MANUFACTURINGYEAR = " + oneRow.get(0).ManufacturingYear + " WHERE IDCAMERA = " + oneRow.get(0).IDCamera);
    }

    @Override
    public void deleteRow(ModelList<InnerAdministratorSubdomainModel> row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        Map<String, String> where = new HashMap<>();
        where.put("IDADMINISTRATOR", row.get(0).IDAdministrator + "");
        where.put("SUBDOMAINID", row.get(0).SubdomainID + "");
        db.delete(this.tableName, where);
        try{
            logger.log("AdministratorSubdomainModel delete data");
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
            logger.log("AdministratorSubdomainModel throw data into csv");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }

    @Override
    public void insertRow(InnerAdministratorSubdomainModel row) throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);

        List<Pair<String, String>> values = new ArrayList<>();
        values.add(new Pair<>("IDADMINISTRATOR", row.IDAdministrator + ""));
        values.add(new Pair<>("SUBDOMAINID", row.SubdomainID + ""));

        db.insert(this.tableName, values);
        try{
            logger.log("AdministratorSubdomainModel insert data");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }
    }
    @Override
    public void truncate() throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance(databaseType);
        this.setDatabaseType(databaseType);
        db.truncate(this.tableName);
        this.getData();
    }
}
