package org.models;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;
import org.database.oracle.OracleConnection;
import org.junit.Test;
import org.vintage.Main;

import javax.swing.table.TableModel;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RentModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;

    public RentModelTest(){
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
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            assertNotNull(modelList);

            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private Date insert(){
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATA_IN_ZILE - o1.DURATA_IN_ZILE);
            RentModel.InnerRentModel data = modelList.getList().get(0);
            data.DATA_INCHIRIERE = Date.valueOf(LocalDateTime.now().toLocalDate());

            Date result = data.DATA_INCHIRIERE;

            rentModel.insertRow(data);


            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            rentModel.insertRow(data);

            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(Date id){
        try {
            RentModel rentModel = RentModel.getInstance();
            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            rentModel.getData();

            ModelList<RentModel.InnerRentModel> modelList = rentModel.getModelList();
            modelList.sort((o1, o2) -> o2.DURATA_IN_ZILE - o1.DURATA_IN_ZILE);
            RentModel.InnerRentModel data = modelList.getList().get(0);
            data.DATA_INCHIRIERE = id;
            List<RentModel.InnerRentModel> list = new ArrayList<>();
            list.add(data);
            ModelList<RentModel.InnerRentModel> dataModelList = new ModelList<>(list);

            rentModel.deleteRow(dataModelList);


            rentModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            rentModel.getData();

            rentModel.deleteRow(dataModelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void deleteRow() {
        try {
            Date newId = this.insert();
            this.delete(newId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void insertRow() {
        try {
            Date newId = this.insert();
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