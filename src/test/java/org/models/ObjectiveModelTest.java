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

public class ObjectiveModelTest {
    DatabaseConnection inmem = null;

    public ObjectiveModelTest(){
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
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            mobjectiveModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = MainService.getModelList(mobjectiveModel);
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            TableModel tm = MainService.getTableModel(mobjectiveModel);
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void update(List<Integer> id){
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = mobjectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDObiectiv - o1.IDObiectiv);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);

            ++data.PretInchiriere;

            List<ObjectiveModel.InnerObjectiveModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ObjectiveModel.InnerObjectiveModel> dataModelList = new ModelList<>(list);

            MainService.update(mobjectiveModel, dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ObjectiveModel.InnerObjectiveModel data = new ObjectiveModel.InnerObjectiveModel();
            data.IDObiectiv = 0;
            data.PretInchiriere = 100;
            data.Pret = 100;
            data.IDObiectiv = 1;
            data.Denumire = "Test";
            data.DiafragmaMaxima = 0.0;
            data.DiafragmaMinima = 0.0;
            data.DistantaFocala = 100;
            data.Diametru = 100;

            MainService.insert(mobjectiveModel, data);

            MainService.getData(mobjectiveModel);
            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = MainService.getModelList(mobjectiveModel);
            modelList.sort((o1, o2) -> o2.IDObiectiv - o1.IDObiectiv);
            data = modelList.getList().get(0);

            result.add(data.IDObiectiv);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
            MainService.setDatabaseType(mobjectiveModel, DatabaseConnection.DatabaseType.INMEMORY);
            MainService.getData(mobjectiveModel);

            ModelList<ObjectiveModel.InnerObjectiveModel> modelList = mobjectiveModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDObiectiv - o1.IDObiectiv);
            ObjectiveModel.InnerObjectiveModel data = modelList.getList().get(0);

            List<ObjectiveModel.InnerObjectiveModel> list = new ArrayList<>();
            list.add(data);
            ModelList<ObjectiveModel.InnerObjectiveModel> dataModelList = new ModelList<>(list);

            MainService.delete(mobjectiveModel, dataModelList);
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
        ObjectiveModel mobjectiveModel = ObjectiveModel.getInstance();
        assertNotNull(mobjectiveModel);
    }
}