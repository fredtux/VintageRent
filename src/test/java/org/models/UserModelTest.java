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

public class UserModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public UserModelTest(){
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
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            ModelList<UserModel.InnerUserModel> modelList = userModel.getModelList();
            assertNotNull(modelList);

            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();

            modelList = userModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            TableModel tm = userModel.getTableModel();
            assertNotNull(tm);


            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();

            tm = userModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            ModelList<UserModel.InnerUserModel> modelList = userModel.getModelList();
            userModel.updateData(modelList);

            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();

            modelList = userModel.getModelList();
            userModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            ModelList<UserModel.InnerUserModel> modelList = userModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            UserModel.InnerUserModel data = modelList.getList().get(0);
            ++data.IDUtilizator;
            data.NumeUtilizator = "Unit Test";

            int result = data.IDUtilizator;

            userModel.insertRow(data);


            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();

            userModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            UserModel userModel = UserModel.getInstance();
            userModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            userModel.getData();

            ModelList<UserModel.InnerUserModel> modelList = userModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            UserModel.InnerUserModel data = modelList.getList().get(0);
            data.IDUtilizator = id;
            List<UserModel.InnerUserModel> list = new ArrayList<>();
            list.add(data);
            ModelList<UserModel.InnerUserModel> dataModelList = new ModelList<>(list);

            userModel.deleteRow(dataModelList);


            userModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            userModel.getData();

            userModel.deleteRow(dataModelList);
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
        UserModel userModel = UserModel.getInstance();
        assertNotNull(userModel);
    }
}