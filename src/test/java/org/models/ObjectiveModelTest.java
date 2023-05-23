package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectiveModelTest {
    DatabaseConnection inmem = null;

    public ObjectiveModelTest(){
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
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            mobjectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = MainService.getModelList(mobjectiveModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            TableModel tm = MainService.getTableModel(mobjectiveModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = mobjectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.ObjectiveID - o1.ObjectiveID);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);

            ++data.RentalPrice;

            List<ObjectiveModel.InnerObjectiveModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ObjectiveModel.InnerObjectiveModel> dataModelList = new ModelList<>(list);

            MainService.update(mobjectiveModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ObjectiveModel.InnerObjectiveModel data = new ObjectiveModel.InnerObjectiveModel();
            data.ObjectiveID = 0;
            data.RentalPrice = 100;
            data.Price = 100;
            data.ObjectiveID = 1;
            data.Name = "Test";
            data.MaximumAperture = 0.0;
            data.MinimumAperture = 0.0;
            data.FocalDistance = 100;
            data.Diameter = 100;

            MainService.insert(mobjectiveModel, data);

            MainService.getData(mobjectiveModel);
            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = MainService.getModelList(mobjectiveModel);
            modelList.sort((o1, o2) -> o2.ObjectiveID - o1.ObjectiveID);
            data = modelList.getList().get(0);

            result.add(data.ObjectiveID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(mobjectiveModel, "==", id.get(0).toString(), "ObjectiveID");

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = mobjectiveModel.getModelList();
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.ObjectiveID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = mobjectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.ObjectiveID - o1.ObjectiveID);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);

            List<ObjectiveModel.InnerObjectiveModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ObjectiveModel.InnerObjectiveModel> dataModelList = new ModelList<>(list);

            MainService.delete(mobjectiveModel, dataModelList);
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
        ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
        assertNotNull(mobjectiveModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(ObjectiveModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            ObjectiveModel classModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}