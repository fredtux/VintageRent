package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RentModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public RentModelTest(){
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
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            assertNotNull(modelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            modelList = rentModel.getModelList();
            assertNotNull(modelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();

            modelList = rentModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            TableModel tm = rentModel.getTableModel();
            assertNotNull(tm);


            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            tm = rentModel.getTableModel();
            assertNotNull(tm);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();

            tm = rentModel.getTableModel();
            assertNotNull(tm);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            rentModel.updateData(modelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            modelList = rentModel.getModelList();
            rentModel.updateData(modelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();

            modelList = rentModel.getModelList();
            if(modelList.getList().size() > 0)
                rentModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Date> insert(){
        try {
            List<Date> result = new ArrayList<>();

            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATA_IN_ZILE - o1.DURATA_IN_ZILE);
            RentModel.InnerRentModel data = modelList.getList().get(0);
            data.DATA_INCHIRIERE = Date.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            result.add(data.DATA_INCHIRIERE);

            rentModel.insertRow(data);


            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            result.add(data.DATA_INCHIRIERE);

            rentModel.insertRow(data);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();

            result.add(data.DATA_INCHIRIERE);

            rentModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Date> id){
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATA_IN_ZILE - o1.DURATA_IN_ZILE);
            RentModel.InnerRentModel data = modelList.getList().get(0);
            data.DATA_INCHIRIERE = id.get(0);
            List<RentModel.InnerRentModel> list = new ArrayList<>();
            list.add(data);
            ModelList<RentModel.InnerRentModel> dataModelList = new ModelList<>(list);

            rentModel.deleteRow(dataModelList);


            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            data.DATA_INCHIRIERE = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            rentModel.deleteRow(dataModelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            rentModel.getData();

            modelList = rentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATA_IN_ZILE - o1.DURATA_IN_ZILE);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            rentModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            List<Date> newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            List<Date> newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getInstance() {
        RentModel rentModel = RentModel.getInstance();
        assertNotNull(rentModel);
    }
}