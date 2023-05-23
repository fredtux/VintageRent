package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AddressModelTest {
    DatabaseConnection inmem = null;

    public AddressModelTest(){
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
            AddressModel addressModel = AddressModel.getInstance();
            addressModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);

            ModelList<AddressModel.InnerAddressModel> modelList = MainService.getModelList(addressModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);

            TableModel tm = MainService.getTableModel(addressModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);

            ModelList<AddressModel.InnerAddressModel> modelList = addressModel.getModelList();
            modelList.sort((o1, o2) -> o2.AddressID - o1.AddressID);
            AddressModel.InnerAddressModel data = modelList.getList().get(0);

            data.Street = "Street2";

            List<AddressModel.InnerAddressModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AddressModel.InnerAddressModel> dataModelList = new ModelList<>(list);

            MainService.update(addressModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);

            AddressModel.InnerAddressModel data = new AddressModel.InnerAddressModel();
            data.AddressID = 0;
            data.Street = "Street1";

            MainService.insert(addressModel, data);

            MainService.getData(addressModel);
            ModelList<AddressModel.InnerAddressModel> modelList = MainService.getModelList(addressModel);
            modelList.sort((o1, o2) -> o2.AddressID - o1.AddressID);
            data = modelList.getList().get(0);

            result.add(data.AddressID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(addressModel, "==", id.get(0).toString(), "AddressID");

            ModelList<AddressModel.InnerAddressModel> modelList = addressModel.getModelList();
            AddressModel.InnerAddressModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.AddressID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(addressModel);

            ModelList<AddressModel.InnerAddressModel> modelList = addressModel.getModelList();
            modelList.sort((o1, o2) -> o2.AddressID - o1.AddressID);
            AddressModel.InnerAddressModel data = modelList.getList().get(0);

            List<AddressModel.InnerAddressModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AddressModel.InnerAddressModel> dataModelList = new ModelList<>(list);

            MainService.delete(addressModel, dataModelList);
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
        AddressModel addressModel = AddressModel.getInstance();
        assertNotNull(addressModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(AddressModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            AddressModel addressModel = AddressModel.getInstance();
            MainService.setDatabaseType(addressModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(addressModel);
            ModelList<AddressModel.InnerAddressModel> modelList = MainService.getModelList(addressModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}