package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdministratorModelTest {
    DatabaseConnection inmem = null;

    public AdministratorModelTest(){
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
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            administratorModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            ModelList<AdministratorModel.InnerAdministratorModel> modelList = MainService.getModelList(administratorModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            TableModel tm = MainService.getTableModel(administratorModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            ModelList<AdministratorModel.InnerAdministratorModel> modelList = administratorModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            AdministratorModel.InnerAdministratorModel data = modelList.getList().get(0);

            data.isActive = false;

            List<AdministratorModel.InnerAdministratorModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AdministratorModel.InnerAdministratorModel> dataModelList = new ModelList<>(list);

            MainService.update(administratorModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            AdministratorModel.InnerAdministratorModel data = new AdministratorModel.InnerAdministratorModel();
            data.UserID = 0;
            data.isActive = true;

            MainService.insert(administratorModel, data);

            MainService.getData(administratorModel);
            ModelList<AdministratorModel.InnerAdministratorModel> modelList = MainService.getModelList(administratorModel);
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            data = modelList.getList().get(0);

            result.add(data.UserID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(administratorModel, "==", id.get(0).toString(), "UserID");

            ModelList<AdministratorModel.InnerAdministratorModel> modelList = administratorModel.getModelList();
            AdministratorModel.InnerAdministratorModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.UserID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(administratorModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorModel);

            ModelList<AdministratorModel.InnerAdministratorModel> modelList = administratorModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            AdministratorModel.InnerAdministratorModel data = modelList.getList().get(0);

            List<AdministratorModel.InnerAdministratorModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AdministratorModel.InnerAdministratorModel> dataModelList = new ModelList<>(list);

            MainService.delete(administratorModel, dataModelList);
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
        AdministratorModel administratorModel = AdministratorModel.getInstance();
        assertNotNull(administratorModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(AdministratorModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void truncate(){
        try{
            AdministratorModel classModel = AdministratorModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<AdministratorModel.InnerAdministratorModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}