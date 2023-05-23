package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClientTypeModelTest {
    DatabaseConnection inmem = null;

    public ClientTypeModelTest(){
        try {
            this.inmem = this.getInMemory();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private DatabaseConnection getInMemory(){
        DatabaseConnection result = null;
        try{
            result = new InMemory();
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.INMEMORY);
        }

        return result;
    }

    @Test
    public void getModelList() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            clientTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = MainService.getModelList(clientTypeModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            TableModel tm = MainService.getTableModel(clientTypeModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.TypeID - o1.TypeID);
            ClientTypeModel.InnerClientTypeModel data = modelList.getList().get(0);

            data.Name = "New Test";

            List<ClientTypeModel.InnerClientTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientTypeModel.InnerClientTypeModel> dataModelList = new ModelList<>(list);

            MainService.update(clientTypeModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            ClientTypeModel.InnerClientTypeModel data = new ClientTypeModel.InnerClientTypeModel();
            data.TypeID = 0;
            data.Name = "Test";
            data.Discount = 0.0;

            MainService.insert(clientTypeModel, data);

            MainService.getData(clientTypeModel);
            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = MainService.getModelList(clientTypeModel);
            modelList.sort((o1, o2) -> o2.TypeID - o1.TypeID);
            data = modelList.getList().get(0);

            result.add(data.TypeID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(clientTypeModel, "==", id.get(0).toString(), "TypeID");

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            ClientTypeModel.InnerClientTypeModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.TypeID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(clientTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(clientTypeModel);

            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = clientTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.TypeID - o1.TypeID);
            ClientTypeModel.InnerClientTypeModel data = modelList.getList().get(0);

            List<ClientTypeModel.InnerClientTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ClientTypeModel.InnerClientTypeModel> dataModelList = new ModelList<>(list);

            MainService.delete(clientTypeModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void insertUpdateDelete() {
        try {
            List<Integer> newId = this.insert();
            this.testFilteredData(newId);
            this.update(newId);
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

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(ClientTypeModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            ClientTypeModel classModel = ClientTypeModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<ClientTypeModel.InnerClientTypeModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}