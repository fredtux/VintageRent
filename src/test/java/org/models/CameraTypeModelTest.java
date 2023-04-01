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

public class CameraTypeModelTest {
    DatabaseConnection inmem = null;

    public CameraTypeModelTest(){
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
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = MainService.getModelList(cameraTypeModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            TableModel tm = MainService.getTableModel(cameraTypeModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.TypeID - o1.TypeID);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);

            data.Name = "New Test";

            List<CameraTypeModel.InnerCameraTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraTypeModel.InnerCameraTypeModel> dataModelList = new ModelList<>(list);

            MainService.update(cameraTypeModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            CameraTypeModel.InnerCameraTypeModel data = new CameraTypeModel.InnerCameraTypeModel();
            data.TypeID = 0;
            data.Name = "Test";

            MainService.insert(cameraTypeModel, data);

            MainService.getData(cameraTypeModel);
            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = MainService.getModelList(cameraTypeModel);
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
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(cameraTypeModel, "==", id.get(0).toString(), "TypeID");

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.TypeID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            MainService.setDatabaseType(cameraTypeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraTypeModel);

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.TypeID - o1.TypeID);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);

            List<CameraTypeModel.InnerCameraTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraTypeModel.InnerCameraTypeModel> dataModelList = new ModelList<>(list);

            MainService.delete(cameraTypeModel, dataModelList);
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
        CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
        assertNotNull(cameraTypeModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(CameraTypeModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}