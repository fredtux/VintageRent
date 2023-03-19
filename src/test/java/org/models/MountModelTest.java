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
            result = new InMemory(Main.ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "XE", "C##TUX", "1521");
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
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            MountModel.InnerMountModel data = modelList.getList().get(0);

            data.Denumire = "Test2";

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
            data.IDMontura = 0;
            data.Denumire = "Test";

            MainService.insert(mountModel, data);

            MainService.getData(mountModel);
            ModelList<MountModel.InnerMountModel> modelList = MainService.getModelList(mountModel);
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
            data = modelList.getList().get(0);

            result.add(data.IDMontura);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            MountModel mountModel = MountModel.getInstance();
            MainService.setDatabaseType(mountModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mountModel);

            ModelList<MountModel.InnerMountModel> modelList = mountModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDMontura - o1.IDMontura);
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
}