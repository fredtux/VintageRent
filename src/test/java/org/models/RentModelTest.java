package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RentModelTest {
    DatabaseConnection inmem = null;

    public RentModelTest(){
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
            RentModel mrentModel = RentModel.getInstance();
            mrentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            ModelList<RentModel.InnerRentModel> modelList = MainService.getModelList(mrentModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            TableModel tm = MainService.getTableModel(mrentModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            ModelList<RentModel.InnerRentModel> modelList = mrentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATION_IN_DAYS - o1.DURATION_IN_DAYS);
            RentModel.InnerRentModel data = modelList.getList().get(0);

            ++data.DURATION_IN_DAYS;

            List<RentModel.InnerRentModel> list = new ArrayList<>();
            list.add(data);
            ModelList<RentModel.InnerRentModel> dataModelList = new ModelList<>(list);

            MainService.update(mrentModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            RentModel.InnerRentModel data = new RentModel.InnerRentModel();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            data.RENT_DATE = Date.valueOf(sdf.format(new java.util.Date(946684800000L)));
            data.DURATION_IN_DAYS = 1;
            data.IDANGAJAT = 1;
            data.IDCLIENT = 1;
            data.OBJECTIVEID = 1;
            data.IS_RETURNED = false;
            data.IDCAMERA = 1;
            data.PENALTYFEE = 0;

            MainService.insert(mrentModel, data);

            MainService.getData(mrentModel);
            ModelList<RentModel.InnerRentModel> modelList = MainService.getModelList(mrentModel);
            modelList.sort((o1, o2) -> o2.DURATION_IN_DAYS - o1.DURATION_IN_DAYS);
            data = modelList.getList().get(0);

            result.add(data.DURATION_IN_DAYS);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(mrentModel, "==", id.get(0).toString(), "DURATION_IN_DAYS");

            ModelList<RentModel.InnerRentModel> modelList = mrentModel.getModelList();
            RentModel.InnerRentModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.DURATION_IN_DAYS));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            RentModel mrentModel = RentModel.getInstance();
            MainService.setDatabaseType(mrentModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mrentModel);

            ModelList<RentModel.InnerRentModel> modelList = mrentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATION_IN_DAYS - o1.DURATION_IN_DAYS);
            RentModel.InnerRentModel data = modelList.getList().get(0);

            List<RentModel.InnerRentModel> list = new ArrayList<>();
            list.add(data);
            ModelList<RentModel.InnerRentModel> dataModelList = new ModelList<>(list);

            MainService.delete(mrentModel, dataModelList);
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
        RentModel mrentModel = RentModel.getInstance();
        assertNotNull(mrentModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(RentModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            RentModel classModel = RentModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<RentModel.InnerRentModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}