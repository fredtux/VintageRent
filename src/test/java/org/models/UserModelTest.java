package org.models;

import org.actions.MainService;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserModelTest {
    DatabaseConnection inmem = null;

    public UserModelTest(){
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
            UserModel muserModel = UserModel.getInstance();
            muserModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            ModelList<UserModel.InnerUserModel> modelList = MainService.getModelList(muserModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            TableModel tm = MainService.getTableModel(muserModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            ModelList<UserModel.InnerUserModel> modelList = muserModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            UserModel.InnerUserModel data = modelList.getList().get(0);

            data.Firstname = "Test2";

            List<UserModel.InnerUserModel> list = new ArrayList<>();
            list.add(data);
            ModelList<UserModel.InnerUserModel> dataModelList = new ModelList<>(list);

            MainService.update(muserModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            UserModel.InnerUserModel data = new UserModel.InnerUserModel();
            data.UserID = 0;
            data.Password = "123";
            data.UserName = "Test";
            data.Firstname = "Test";
            data.CNP = "123";
            data.Email = "";

            MainService.insert(muserModel, data);

            MainService.getData(muserModel);
            ModelList<UserModel.InnerUserModel> modelList = MainService.getModelList(muserModel);
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            data = modelList.getList().get(0);

            result.add(data.UserID);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void testFilteredData(List<Integer> id){
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getFilteredData(muserModel, "==", id.get(0).toString(), "UserID");

            ModelList<UserModel.InnerUserModel> modelList = muserModel.getModelList();
            UserModel.InnerUserModel data = modelList.getList().get(0);

            assertEquals(id.get(0), Integer.valueOf(data.UserID));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void delete(List<Integer> id){
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            ModelList<UserModel.InnerUserModel> modelList = muserModel.getModelList();
            modelList.sort((o1, o2) -> o2.UserID - o1.UserID);
            UserModel.InnerUserModel data = modelList.getList().get(0);

            List<UserModel.InnerUserModel> list = new ArrayList<>();
            list.add(data);
            ModelList<UserModel.InnerUserModel> dataModelList = new ModelList<>(list);

            MainService.delete(muserModel, dataModelList);
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
        UserModel muserModel = UserModel.getInstance();
        assertNotNull(muserModel);
    }

    @Test
    public void getAttributes(){
        try {
            List<String> attributes = MainService.getAttributes(UserModel.class);
            assertNotNull(attributes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void truncate(){
        try{
            UserModel classModel = UserModel.getInstance();
            MainService.setDatabaseType(classModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.truncate(classModel);
            ModelList<UserModel.InnerUserModel> modelList = MainService.getModelList(classModel);
            assertEquals(0, modelList.getList().size());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}