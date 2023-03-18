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

public class MountModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public MountModelTest(){
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
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            assertNotNull(modelList);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            modelList = mountModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            TableModel tm = mountModel.getTableModel();
            assertNotNull(tm);


            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            tm = mountModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            mountModel.updateData(modelList);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            modelList = mountModel.getModelList();
            mountModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            MountModel.InnerMountModel data = modelList.getList().get(0);
            ++data.IDMontura;

            int result = data.IDMontura;

            mountModel.insertRow(data);


            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            mountModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            MountModel.InnerMountModel data = modelList.getList().get(0);
            data.IDMontura = id;
            List<MountModel.InnerMountModel> list = new ArrayList<>();
            list.add(data);
            ModelList<MountModel.InnerMountModel> dataModelList = new ModelList<>(list);

            mountModel.deleteRow(dataModelList);


            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            mountModel.deleteRow(dataModelList);
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
        MountModel mountModel = MountModel.getInstance();
        assertNotNull(mountModel);
    }
}