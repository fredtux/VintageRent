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

public class SalaryModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public SalaryModelTest(){
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
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            ModelList<SalaryModel.InnerSalaryModel> modelList = salaryModel.getModelList();
            assertNotNull(modelList);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            assertNotNull(modelList);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            TableModel tm = salaryModel.getTableModel();
            assertNotNull(tm);


            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            tm = salaryModel.getTableModel();
            assertNotNull(tm);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();

            tm = salaryModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            ModelList<SalaryModel.InnerSalaryModel> modelList = salaryModel.getModelList();
            salaryModel.updateData(modelList);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            salaryModel.updateData(modelList);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            if(modelList.getList().size() > 0)
                salaryModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            ModelList<SalaryModel.InnerSalaryModel> modelList = salaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            SalaryModel.InnerSalaryModel data = modelList.getList().get(0);
            ++data.IDSalariu;

            result.add(data.IDSalariu);

            salaryModel.insertRow(data);


            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            data = modelList.getList().get(0);
            ++data.IDSalariu;

            result.add(data.IDSalariu);

            salaryModel.insertRow(data);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();

            try {
                data = modelList.getList().get(0);
                ++data.IDSalariu;
            } catch (Exception e){

            }

            result.add(data.IDSalariu);

            salaryModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            SalaryModel salaryModel = SalaryModel.getInstance();
            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            salaryModel.getData();

            ModelList<SalaryModel.InnerSalaryModel> modelList = salaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            SalaryModel.InnerSalaryModel data = modelList.getList().get(0);
            data.IDSalariu = id.get(0);
            List<SalaryModel.InnerSalaryModel> list = new ArrayList<>();
            list.add(data);
            ModelList<SalaryModel.InnerSalaryModel> dataModelList = new ModelList<>(list);

            salaryModel.deleteRow(dataModelList);


            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            salaryModel.getData();

            data.IDSalariu = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            salaryModel.deleteRow(dataModelList);

            salaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            salaryModel.getData();

            modelList = salaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            salaryModel.deleteRow(dataModelList);
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
        SalaryModel salaryModel = SalaryModel.getInstance();
        assertNotNull(salaryModel);
    }
}