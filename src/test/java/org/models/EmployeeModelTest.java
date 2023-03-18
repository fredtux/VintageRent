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

public class EmployeeModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public EmployeeModelTest(){
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
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            assertNotNull(modelList);

            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();

            modelList = employeeModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            TableModel tm = employeeModel.getTableModel();
            assertNotNull(tm);


            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();

            tm = employeeModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            employeeModel.updateData(modelList);

            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();

            modelList = employeeModel.getModelList();
            employeeModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            EmployeeModel.InnerEmployeeModel data = modelList.getList().get(0);
            data.IDUtilizator = 1;

            int result = data.IDUtilizator;

            employeeModel.insertRow(data);


            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();

            employeeModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            employeeModel.getData();

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            EmployeeModel.InnerEmployeeModel data = modelList.getList().get(0);
            data.IDUtilizator = id;
            List<EmployeeModel.InnerEmployeeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<EmployeeModel.InnerEmployeeModel> dataModelList = new ModelList<>(list);

            employeeModel.deleteRow(dataModelList);


            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            employeeModel.getData();

            employeeModel.deleteRow(dataModelList);
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
        EmployeeModel employeeModel = EmployeeModel.getInstance();
        assertNotNull(employeeModel);
    }
}