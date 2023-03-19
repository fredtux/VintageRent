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

public class FormatModelTest {
    DatabaseConnection orcl = null;
    DatabaseConnection csv = null;
    DatabaseConnection inmem = null;

    public FormatModelTest(){
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
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            assertNotNull(modelList);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            modelList = formatModel.getModelList();
            assertNotNull(modelList);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();

            modelList = formatModel.getModelList();
            assertNotNull(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void setDatabaseType() {
        try{
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getTableModel() {
        try {
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            TableModel tm = formatModel.getTableModel();
            assertNotNull(tm);


            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            tm = formatModel.getTableModel();
            assertNotNull(tm);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();

            tm = formatModel.getTableModel();
            assertNotNull(tm);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getData() {
        try {
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateData() {
        try {
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            formatModel.updateData(modelList);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            modelList = formatModel.getModelList();
            formatModel.updateData(modelList);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();

            modelList = formatModel.getModelList();
            if(modelList.getList().size() > 0)
                formatModel.updateData(modelList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private List<Integer> insert(){
        try {
            List<Integer> result = new ArrayList<>();

            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDFormat - o1.IDFormat);
            FormatModel.InnerFormatModel data = modelList.getList().get(0);
            ++data.IDFormat;

            result.add(data.IDFormat);

            formatModel.insertRow(data);


            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDFormat - o1.IDFormat);
            data = modelList.getList().get(0);
            ++data.IDFormat;

            result.add(data.IDFormat);

            formatModel.insertRow(data);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();

            try {
                data = modelList.getList().get(0);
                ++data.IDFormat;
            } catch (Exception e){

            }

            result.add(data.IDFormat);

            formatModel.insertRow(data);


            return result;
        } catch (Exception e) {
            fail(e.getMessage());
        }

        return null;
    }

    public void delete(List<Integer> id){
        try {
            FormatModel formatModel = FormatModel.getInstance();
            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.ORACLE);
            formatModel.getData();

            ModelList<FormatModel.InnerFormatModel> modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDFormat - o1.IDFormat);
            FormatModel.InnerFormatModel data = modelList.getList().get(0);
            data.IDFormat = id.get(0);
            List<FormatModel.InnerFormatModel> list = new ArrayList<>();
            list.add(data);
            ModelList<FormatModel.InnerFormatModel> dataModelList = new ModelList<>(list);

            formatModel.deleteRow(dataModelList);


            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
            formatModel.getData();

            data.IDFormat = id.get(1);
            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            formatModel.deleteRow(dataModelList);

            formatModel.setDatabaseType(DatabaseConnection.DatabaseType.INMEMORY);
            formatModel.getData();

            modelList = formatModel.getModelList();
            modelList.sort((o1, o2) -> o2.IDFormat - o1.IDFormat);
            data = modelList.getList().get(0);

            list = new ArrayList<>();
            list.add(data);
            dataModelList = new ModelList<>(list);

            formatModel.deleteRow(dataModelList);
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
        FormatModel formatModel = FormatModel.getInstance();
        assertNotNull(formatModel);
    }
}