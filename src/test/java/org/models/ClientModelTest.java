package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientModelTest {
    DatabaseConnection inmem = null;

    public ClientModelTest(){
        try {
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

    @Test
    public void getModelList() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            clientModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            ModelList<ClientModel.InnerClientModel> modelList = MainService.getModelList(clientModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            TableModel tm = MainService.getTableModel(clientModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            ClientModel.InnerClientModel data = modelList.getList().get(0);

            data.SurnameClient = "New Test";

            List<ClientModel.InnerClientModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientModel.InnerClientModel> dataModelList = new ModelList<>(list);

            MainService.update(clientModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            ClientModel.InnerClientModel data = new ClientModel.InnerClientModel();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data.UserID = 1;
            data.SurnameClient = "Test";
            data.BirthDate = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));

            MainService.insert(clientModel, data);

            MainService.getData(clientModel);
            ModelList<ClientModel.InnerClientModel> modelList = MainService.getModelList(clientModel);
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            data = modelList.getList().get(0);

            result.add(data.UserID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            ClientModel clientModel = ClientModel.getInstance();
            MainService.setDatabaseType(clientModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientModel);

            ModelList<ClientModel.InnerClientModel> modelList = clientModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            ClientModel.InnerClientModel data = modelList.getList().get(0);

            List<ClientModel.InnerClientModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientModel.InnerClientModel> dataModelList = new ModelList<>(list);

            MainService.delete(clientModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void insertUpdateDelete() {
        try {
            List<Integer> newId = this.insert();
            this.update(newId);
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