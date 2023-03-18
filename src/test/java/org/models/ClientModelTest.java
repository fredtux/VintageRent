package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public ClientModelTest(){
        try {
            ModelInit.logInit();
            ModelInit.csvInit();

            this.orcl = this.getOracle();
            this.csv = CsvConnection.getInstance(DatabaseConnection.DatabaseType.CSV);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    private DatabaseConnection getOracle(){
        DatabaseConnection result = null;
        try{
            result = new OracleConnection(Main.ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "XE", "C##TUX", "1521");
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.ORACLE);
        }

        try{
            result.connect();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return result;
    }

    @Test
    public void getModelList() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            assertNotNull(modelList);

            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();

            modelList = clientModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            TableModel tm = clientModel.getTableModel();
            assertNotNull(tm);


            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();

            tm = clientModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            clientModel.updateData(modelList);

            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();

            modelList = clientModel.getModelList();
            clientModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            ClientModel.InnerClientModel data = modelList.getList().get(0);
            data.IDUtilizator = 2;

            int result = data.IDUtilizator;

            clientModel.insertRow(data);


            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();

            clientModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientModel.getData();

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            ClientModel.InnerClientModel data = modelList.getList().get(0);
            data.IDUtilizator = id;
            List<ClientModel.InnerClientModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientModel.InnerClientModel> dataModelList = new ModelList<>(list);

            clientModel.deleteRow(dataModelList);


            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientModel.getData();

            clientModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            int newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            int newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getInstance() {
        ClientModel clientModel = ClientModel.getInstance();
        assertNotNull(clientModel);
    }
}