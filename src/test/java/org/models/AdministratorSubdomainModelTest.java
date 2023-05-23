package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdministratorSubdomainModelTest {
    DatabaseConnection inmem = null;

    public AdministratorSubdomainModelTest(){
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
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            administratorSubdomainModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = MainService.getModelList(administratorSubdomainModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            TableModel tm = MainService.getTableModel(administratorSubdomainModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = administratorSubdomainModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDAdministrator - o1.IDAdministrator);
            AdministratorSubdomainModel.InnerAdministratorSubdomainModel data = modelList.getList().get(0);

            data.SubdomainID = 2;

            List<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> dataModelList = new ModelList<>(list);

            MainService.update(administratorSubdomainModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            AdministratorSubdomainModel.InnerAdministratorSubdomainModel data = new AdministratorSubdomainModel.InnerAdministratorSubdomainModel();
            data.IDAdministrator = 0;
            data.SubdomainID = 1;

            MainService.insert(administratorSubdomainModel, data);

            MainService.getData(administratorSubdomainModel);
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = MainService.getModelList(administratorSubdomainModel);
            modelList.sort((o1, o2) -> o2.IDAdministrator - o1.IDAdministrator);
            data = modelList.getList().get(0);

            result.add(data.IDAdministrator);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(administratorSubdomainModel, "==", id.get(0).toString(), "IDAdministrator");

            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = administratorSubdomainModel.getModelList();
            AdministratorSubdomainModel.InnerAdministratorSubdomainModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.IDAdministrator));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(administratorSubdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(administratorSubdomainModel);

            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = administratorSubdomainModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDAdministrator - o1.IDAdministrator);
            AdministratorSubdomainModel.InnerAdministratorSubdomainModel data = modelList.getList().get(0);

            List<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> list = new ArrayList<>();
            list.add(data);
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> dataModelList = new ModelList<>(list);

            MainService.delete(administratorSubdomainModel, dataModelList);
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
        AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
        assertNotNull(administratorSubdomainModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(AdministratorSubdomainModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            AdministratorSubdomainModel classModel = AdministratorSubdomainModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<AdministratorSubdomainModel.InnerAdministratorSubdomainModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}