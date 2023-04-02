package org.models;

import org.actions.MainService;
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

public class SubdomainModelTest {
    DatabaseConnection inmem = null;

    public SubdomainModelTest(){
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
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            subdomainModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            ModelList<SubdomainModel.InnerSubdomainModel> modelList = MainService.getModelList(subdomainModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            TableModel tm = MainService.getTableModel(subdomainModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            ModelList<SubdomainModel.InnerSubdomainModel> modelList = subdomainModel.getModelList();
            modelList.sort((o1, o2) -> o2.SubdomainID - o1.SubdomainID);
            SubdomainModel.InnerSubdomainModel data = modelList.getList().get(0);

            data.Name = "Name1";

            List<SubdomainModel.InnerSubdomainModel> list = new ArrayList<>();
            list.add(data);
            ModelList<SubdomainModel.InnerSubdomainModel> dataModelList = new ModelList<>(list);

            MainService.update(subdomainModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            SubdomainModel.InnerSubdomainModel data = new SubdomainModel.InnerSubdomainModel();
            data.SubdomainID = 0;
            data.Name = "Name1";

            MainService.insert(subdomainModel, data);

            MainService.getData(subdomainModel);
            ModelList<SubdomainModel.InnerSubdomainModel> modelList = MainService.getModelList(subdomainModel);
            modelList.sort((o1, o2) -> o2.SubdomainID - o1.SubdomainID);
            data = modelList.getList().get(0);

            result.add(data.SubdomainID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(subdomainModel, "==", id.get(0).toString(), "SubdomainID");

            ModelList<SubdomainModel.InnerSubdomainModel> modelList = subdomainModel.getModelList();
            SubdomainModel.InnerSubdomainModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.SubdomainID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(subdomainModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(subdomainModel);

            ModelList<SubdomainModel.InnerSubdomainModel> modelList = subdomainModel.getModelList();
            modelList.sort((o1, o2) -> o2.SubdomainID - o1.SubdomainID);
            SubdomainModel.InnerSubdomainModel data = modelList.getList().get(0);

            List<SubdomainModel.InnerSubdomainModel> list = new ArrayList<>();
            list.add(data);
            ModelList<SubdomainModel.InnerSubdomainModel> dataModelList = new ModelList<>(list);

            MainService.delete(subdomainModel, dataModelList);
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
        SubdomainModel subdomainModel = SubdomainModel.getInstance();
        assertNotNull(subdomainModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(SubdomainModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            SubdomainModel classModel = SubdomainModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<SubdomainModel.InnerSubdomainModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}