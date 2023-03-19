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

public class CameraModelTest {
    DatabaseConnection inmem = null;

    public CameraModelTest(){
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
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            ModelList<CameraModel.InnerCameraModel> modelList = MainService.getModelList(cameraModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            TableModel tm = MainService.getTableModel(cameraModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            CameraModel.InnerCameraModel data = modelList.getList().get(0);

            ++data.Pret;

            List<CameraModel.InnerCameraModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraModel.InnerCameraModel> dataModelList = new ModelList<>(list);

            MainService.update(cameraModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            CameraModel.InnerCameraModel data = new CameraModel.InnerCameraModel();
            data.IDCamera = 10001;
            data.ModelCamera = "Test";
            data.IDMontura = 1;
            data.IDFormat = 1;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.AnFabricatie = 1970;
            data.Marca = "Test";
            data.IDTip = 1;

            MainService.insert(cameraModel, data);

            MainService.getData(cameraModel);
            ModelList<CameraModel.InnerCameraModel> modelList = MainService.getModelList(cameraModel);
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            data = modelList.getList().get(0);

            result.add(data.IDCamera);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            MainService.setDatabaseType(cameraModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(cameraModel);

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            CameraModel.InnerCameraModel data = modelList.getList().get(0);

            List<CameraModel.InnerCameraModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraModel.InnerCameraModel> dataModelList = new ModelList<>(list);

            MainService.delete(cameraModel, dataModelList);
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
        CameraModel cameraModel = CameraModel.getInstance();
        assertNotNull(cameraModel);
    }
}