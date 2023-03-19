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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeModelTest {
    DatabaseConnection inmem = null;

    public EmployeeModelTest(){
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
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            employeeModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = MainService.getModelList(employeeModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            TableModel tm = MainService.getTableModel(employeeModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            EmployeeModel.InnerEmployeeModel data = modelList.getList().get(0);

            data.NumeAngajat = "New Test";

            List<EmployeeModel.InnerEmployeeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<EmployeeModel.InnerEmployeeModel> dataModelList = new ModelList<>(list);

            MainService.update(employeeModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            EmployeeModel.InnerEmployeeModel data = new EmployeeModel.InnerEmployeeModel();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data.IDUtilizator = 1;
            data.DataNasterii = LocalDateTime.parse("2000-01-01 00:00:00", dtf);
            data.DataAngajarii = LocalDateTime.parse("2020-01-01 00:00:00", dtf);
            data.IDSalariu = 1;
            data.IDManager = 1;
            data.NumeAngajat = "Test";

            MainService.insert(employeeModel, data);

            MainService.getData(employeeModel);
            ModelList<EmployeeModel.InnerEmployeeModel> modelList = MainService.getModelList(employeeModel);
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
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            MainService.setDatabaseType(employeeModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(employeeModel);

            ModelList<EmployeeModel.InnerEmployeeModel> modelList = employeeModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDUtilizator - o1.IDUtilizator);
            EmployeeModel.InnerEmployeeModel data = modelList.getList().get(0);

            List<EmployeeModel.InnerEmployeeModel> list = new ArrayList<>();
            list.add(data);
            ModelList<EmployeeModel.InnerEmployeeModel> dataModelList = new ModelList<>(list);

            MainService.delete(employeeModel, dataModelList);
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
        EmployeeModel employeeModel = EmployeeModel.getInstance();
        assertNotNull(employeeModel);
    }
}