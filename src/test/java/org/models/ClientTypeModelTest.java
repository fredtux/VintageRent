package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientTypeModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public ClientTypeModelTest(){
        try {
            ModelInit.logInit();
            ModelInit.csvInit();

            this.orcl = this.getOracle();
            if(!orcl.isInitialized())
                orcl.init();
            this.csv = CsvConnection.getInstance(DatabaseConnection.DatabaseType.CSV);
            this.inmem = this.getInMemory();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private DatabaseConnection getInMemory(){
        DatabaseConnection result = null;
        try{
            result = new InMemory(Main.ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "XE", "C##TUX", "1521");
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.INMEMORY);
        }

        return result;
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
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            assertNotNull(modelList);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            assertNotNull(modelList);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            TableModel tm = clientTypeModel.getTableModel();
            assertNotNull(tm);


            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            tm = clientTypeModel.getTableModel();
            assertNotNull(tm);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();

            tm = clientTypeModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            clientTypeModel.updateData(modelList);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            clientTypeModel.updateData(modelList);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            if(modelList.getList().size() > 0)
                clientTypeModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            ClientTypeModel.InnerClientTypeModel data = modelList.getList().get(0);
            ++data.IDTip;

            result.add(data.IDTip);

            clientTypeModel.insertRow(data);


            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            data = modelList.getList().get(0);
            ++data.IDTip;

            result.add(data.IDTip);

            clientTypeModel.insertRow(data);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();

            try {
                data = modelList.getList().get(0);
                ++data.IDTip;
            } catch (Exception e){

            }

            result.add(data.IDTip);

            clientTypeModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            clientTypeModel.getData();

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            ClientTypeModel.InnerClientTypeModel data = modelList.getList().get(0);
            data.IDTip = id.get(0);
            List<ClientTypeModel.InnerClientTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientTypeModel.InnerClientTypeModel> dataModelList = new ModelList<>(list);

            clientTypeModel.deleteRow(dataModelList);


            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            clientTypeModel.getData();

            data.IDTip = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            clientTypeModel.deleteRow(dataModelList);

            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            clientTypeModel.getData();

            modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            clientTypeModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            List<Integer> newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            List<Integer> newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getInstance() {
        ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
        assertNotNull(clientTypeModel);
    }
}