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

public class SalaryModelTest {
    DatabaseConnection inmem = null;

    public SalaryModelTest(){
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
            SalaryModel msalaryModel = SalaryModel.getInstance();
            msalaryModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            ModelList<SalaryModel.InnerSalaryModel> modelList = MainService.getModelList(msalaryModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            TableModel tm = MainService.getTableModel(msalaryModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            ModelList<SalaryModel.InnerSalaryModel> modelList = msalaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            SalaryModel.InnerSalaryModel data = modelList.getList().get(0);

            ++data.Salariu;

            List<SalaryModel.InnerSalaryModel> list = new ArrayList<>();
            list.add(data);
            ModelList<SalaryModel.InnerSalaryModel> dataModelList = new ModelList<>(list);

            MainService.update(msalaryModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            SalaryModel.InnerSalaryModel data = new SalaryModel.InnerSalaryModel();
            data.IDSalariu = 0;
            data.Salariu = 100;
            data.Bonus = 100;

            MainService.insert(msalaryModel, data);

            MainService.getData(msalaryModel);
            ModelList<SalaryModel.InnerSalaryModel> modelList = MainService.getModelList(msalaryModel);
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            data = modelList.getList().get(0);

            result.add(data.IDSalariu);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            SalaryModel msalaryModel = SalaryModel.getInstance();
            MainService.setDatabaseType(msalaryModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(msalaryModel);

            ModelList<SalaryModel.InnerSalaryModel> modelList = msalaryModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDSalariu - o1.IDSalariu);
            SalaryModel.InnerSalaryModel data = modelList.getList().get(0);

            List<SalaryModel.InnerSalaryModel> list = new ArrayList<>();
            list.add(data);
            ModelList<SalaryModel.InnerSalaryModel> dataModelList = new ModelList<>(list);

            MainService.delete(msalaryModel, dataModelList);
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
        SalaryModel msalaryModel = SalaryModel.getInstance();
        assertNotNull(msalaryModel);
    }
}