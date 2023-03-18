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

public class ObjectiveModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public ObjectiveModelTest(){
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
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = objectiveModel.getModelList();
            assertNotNull(modelList);

            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();

            modelList = objectiveModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            TableModel tm = objectiveModel.getTableModel();
            assertNotNull(tm);


            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();

            tm = objectiveModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = objectiveModel.getModelList();
            objectiveModel.updateData(modelList);

            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();

            modelList = objectiveModel.getModelList();
            objectiveModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int insert(){
        try {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = objectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDObiectiv - o1.IDObiectiv);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);
            ++data.IDObiectiv;

            int result = data.IDObiectiv;

            objectiveModel.insertRow(data);


            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();

            objectiveModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return -1;
    }

    public void delete(int id){
        try {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            objectiveModel.getData();

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = objectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDObiectiv - o1.IDObiectiv);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);
            data.IDObiectiv = id;
            List<ObjectiveModel.InnerObjectiveModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ObjectiveModel.InnerObjectiveModel> dataModelList = new ModelList<>(list);

            objectiveModel.deleteRow(dataModelList);


            objectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            objectiveModel.getData();

            objectiveModel.deleteRow(dataModelList);
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
        ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
        assertNotNull(objectiveModel);
    }
}