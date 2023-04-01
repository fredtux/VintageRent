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

public class MountModelTest {
    DatabaseConnection inmem = null;

    public MountModelTest(){
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
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            ModelList<MountModel.InnerMountModel> modelList = MainService.getModelList(mountModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            TableModel tm = MainService.getTableModel(mountModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.MountID - o1.MountID);
            MountModel.InnerMountModel data = modelList.getList().get(0);

            data.Name = "Test2";

            List<MountModel.InnerMountModel> list = new ArrayList<>();
            list.add(data);
            ModelList<MountModel.InnerMountModel> dataModelList = new ModelList<>(list);

            MainService.update(mountModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            MountModel.InnerMountModel data = new MountModel.InnerMountModel();
            data.MountID = 0;
            data.Name = "Test";

            MainService.insert(mountModel, data);

            MainService.getData(mountModel);
            ModelList<MountModel.InnerMountModel> modelList = MainService.getModelList(mountModel);
            modelList.sort((o1, o2) -> o2.MountID - o1.MountID);
            data = modelList.getList().get(0);

            result.add(data.MountID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(mountModel, "==", id.get(0).toString(), "MountID");

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            MountModel.InnerMountModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.MountID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.MountID - o1.MountID);
            MountModel.InnerMountModel data = modelList.getList().get(0);

            List<MountModel.InnerMountModel> list = new ArrayList<>();
            list.add(data);
            ModelList<MountModel.InnerMountModel> dataModelList = new ModelList<>(list);

            MainService.delete(mountModel, dataModelList);
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
        MountModel mountModel = MountModel.getInstance();
        assertNotNull(mountModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(MountModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}