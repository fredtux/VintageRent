package org.models;

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
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public CameraModelTest(){
        try {
            ModelInit.logInit();
            ModelInit.csvInit();

            this.orcl = this.getOracle();
            if(!orcl.isInitialized())
                orcl.init();
            this.csv = CsvConnection.getInstance(DatabaseConnection.DatabaseType.CSV);
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


    private DatabaseConnection getOracle(){
        DatabaseConnection result = null;
        try{
            result = new OracleConnection(Main.ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "XE", "C##TUX", "1521");
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.ORACLE);
        }

        try{
            result.connect();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return result;
    }

    @Test
    public void getModelList() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            assertNotNull(modelList);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            assertNotNull(modelList);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            TableModel tm = cameraModel.getTableModel();
            assertNotNull(tm);


            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            tm = cameraModel.getTableModel();
            assertNotNull(tm);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();

            tm = cameraModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            cameraModel.updateData(modelList);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            cameraModel.updateData(modelList);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            assertNotNull(modelList);
            if(modelList.size() > 0)
                cameraModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            CameraModel.InnerCameraModel data = modelList.getList().get(0);
            ++data.IDCamera;

            result.add(data.IDCamera);

            cameraModel.insertRow(data);


            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            data = modelList.getList().get(0);
            ++data.IDCamera;

            result.add(data.IDCamera);

            cameraModel.insertRow(data);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            try {
                data = modelList.getList().get(0);
                ++data.IDCamera;
            } catch (Exception e){

            }

            result.add(data.IDCamera);

            cameraModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            CameraModel cameraModel = CameraModel.getInstance();
            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraModel.getData();

            ModelList<CameraModel.InnerCameraModel> modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            CameraModel.InnerCameraModel data = modelList.getList().get(0);
            data.IDCamera = id.get(0);
            List<CameraModel.InnerCameraModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraModel.InnerCameraModel> dataModelList = new ModelList<>(list);

            cameraModel.deleteRow(dataModelList);


            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraModel.getData();

            data.IDCamera = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            cameraModel.deleteRow(dataModelList);

            cameraModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraModel.getData();

            modelList = cameraModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDCamera - o1.IDCamera);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            cameraModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            List<Integer> newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            List<Integer> newId = this.insert();
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