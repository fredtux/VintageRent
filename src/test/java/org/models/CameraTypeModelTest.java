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

public class CameraTypeModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public CameraTypeModelTest(){
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
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            assertNotNull(modelList);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            assertNotNull(modelList);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            TableModel tm = cameraTypeModel.getTableModel();
            assertNotNull(tm);


            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            tm = cameraTypeModel.getTableModel();
            assertNotNull(tm);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraTypeModel.getData();

            tm = cameraTypeModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraTypeModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            cameraTypeModel.updateData(modelList);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            assertNotNull(modelList);
            if(modelList.getList().size() > 0)
                cameraTypeModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);
            ++data.IDTip;

            result.add(data.IDTip);

            cameraTypeModel.insertRow(data);


            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            data = modelList.getList().get(0);
            ++data.IDTip;

            result.add(data.IDTip);

            cameraTypeModel.insertRow(data);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            try {
                data = modelList.getList().get(0);
                ++data.IDTip;
            } catch (Exception e){

            }

            result.add(data.IDTip);

            cameraTypeModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);
            data.IDTip = id.get(0);
            List<CameraTypeModel.InnerCameraTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraTypeModel.InnerCameraTypeModel> dataModelList = new ModelList<>(list);

            cameraTypeModel.deleteRow(dataModelList);


            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            data.IDTip = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            cameraTypeModel.deleteRow(dataModelList);

            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            cameraTypeModel.getData();

            modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            cameraTypeModel.deleteRow(dataModelList);
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
        CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
        assertNotNull(cameraTypeModel);
    }
}