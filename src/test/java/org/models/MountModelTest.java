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

public class MountModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public MountModelTest(){
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
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            assertNotNull(modelList);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            modelList = mountModel.getModelList();
            assertNotNull(modelList);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
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
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
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

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
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

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
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

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            mountModel.getData();

            modelList = mountModel.getModelList();
            if(modelList.getList().size() > 0)
                mountModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            MountModel.InnerMountModel data = modelList.getList().get(0);
            ++data.IDMontura;

            result.add(data.IDMontura);

            mountModel.insertRow(data);


            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            data = modelList.getList().get(0);
            ++data.IDMontura;

            result.add(data.IDMontura);

            mountModel.insertRow(data);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            mountModel.getData();

            try {
                data = modelList.getList().get(0);
                ++data.IDMontura;
            } catch (Exception e){

            }

            result.add(data.IDMontura);

            mountModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            MountModel mountModel = MountModel.getInstance();
            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            mountModel.getData();

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            MountModel.InnerMountModel data = modelList.getList().get(0);
            data.IDMontura = id.get(0);
            List<MountModel.InnerMountModel> list = new ArrayList<>();
            list.add(data);
            ModelList<MountModel.InnerMountModel> dataModelList = new ModelList<>(list);

            mountModel.deleteRow(dataModelList);


            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            mountModel.getData();

            data.IDMontura = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            mountModel.deleteRow(dataModelList);

            mountModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            mountModel.getData();

            modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            mountModel.deleteRow(dataModelList);
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
        MountModel mountModel = MountModel.getInstance();
        assertNotNull(mountModel);
    }
}