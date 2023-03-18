package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
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

    public CameraTypeModelTest(){
        try {
            ModelInit.logInit();
            ModelInit.csvInit();

            this.orcl = this.getOracle();
            if(!orcl.isInitialized())
                orcl.init();
            this.csv = CsvConnection.getInstance(DatabaseConnection.DatabaseType.CSV);
        } catch (Exception e) {
            fail(e.getMessage());
        }
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
            cameraTypeModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);
            ++data.IDTip;

            int result = data.IDTip;

            cameraTypeModel.insertRow(data);


            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            cameraTypeModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            cameraTypeModel.getData();

            ModelList<CameraTypeModel.InnerCameraTypeModel> modelList = cameraTypeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDTip - o1.IDTip);
            CameraTypeModel.InnerCameraTypeModel data = modelList.getList().get(0);
            data.IDTip = id;
            List<CameraTypeModel.InnerCameraTypeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<CameraTypeModel.InnerCameraTypeModel> dataModelList = new ModelList<>(list);

            cameraTypeModel.deleteRow(dataModelList);


            cameraTypeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            cameraTypeModel.getData();

            cameraTypeModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            int newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            int newId = this.insert();
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