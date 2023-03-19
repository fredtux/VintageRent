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
            result = new InMemory(Main.ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "XE", "C##TUX", "1521");
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
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            UserModel.InnerUserModel data = modelList.getList().get(0);

            data.Prenume = "Test2";

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
            data.IDUtilizator = 0;
            data.Parola = "123";
            data.NumeUtilizator = "Test";
            data.Prenume = "Test";
            data.CNP = "123";
            data.Email = "";

            MainService.insert(muserModel, data);

            MainService.getData(muserModel);
            ModelList<UserModel.InnerUserModel> modelList = MainService.getModelList(muserModel);
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            data = modelList.getList().get(0);

            result.add(data.IDUtilizator);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            UserModel muserModel = UserModel.getInstance();
            MainService.setDatabaseType(muserModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(muserModel);

            ModelList<UserModel.InnerUserModel> modelList = muserModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
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
}