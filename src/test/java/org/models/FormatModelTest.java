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

public class FormatModelTest {
    DatabaseConnection inmem = null;

    public FormatModelTest(){
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
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            ModelList<FormatModel.InnerFormatModel> modelList = MainService.getModelList(formatModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            TableModel tm = MainService.getTableModel(formatModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.FormatID - o1.FormatID);
            FormatModel.InnerFormatModel data = modelList.getList().get(0);

            data.Name = "Test2";

            List<FormatModel.InnerFormatModel> list = new ArrayList<>();
            list.add(data);
            ModelList<FormatModel.InnerFormatModel> dataModelList = new ModelList<>(list);

            MainService.update(formatModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            FormatModel.InnerFormatModel data = new FormatModel.InnerFormatModel();
            data.FormatID = 0;
            data.Name = "Test";
            data.FilmWidth = "Test";

            MainService.insert(formatModel, data);

            MainService.getData(formatModel);
            ModelList<FormatModel.InnerFormatModel> modelList = MainService.getModelList(formatModel);
            modelList.sort((o1, o2) -> o2.FormatID - o1.FormatID);
            data = modelList.getList().get(0);

            result.add(data.FormatID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(formatModel, "==", id.get(0).toString(), "FormatID");

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            FormatModel.InnerFormatModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.FormatID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            FormatModel formatModel = FormatModel.getInstance();
            MainService.setDatabaseType(formatModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(formatModel);

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.FormatID - o1.FormatID);
            FormatModel.InnerFormatModel data = modelList.getList().get(0);

            List<FormatModel.InnerFormatModel> list = new ArrayList<>();
            list.add(data);
            ModelList<FormatModel.InnerFormatModel> dataModelList = new ModelList<>(list);

            MainService.delete(formatModel, dataModelList);
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
        FormatModel formatModel = FormatModel.getInstance();
        assertNotNull(formatModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(FormatModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            FormatModel classModel = FormatModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<FormatModel.InnerFormatModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}