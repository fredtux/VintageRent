package org.actions;

import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.junit.Test;
import org.models.ModelInit;
import org.vintage.Main;

import java.util.Map;

import static org.junit.Assert.*;

public class MainServiceTest {
    DatabaseConnection inmem = null;

    public MainServiceTest(){
        try {
            this.inmem = this.getInMemory();
            ModelInit.inmemInit();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private DatabaseConnection getInMemory(){
        DatabaseConnection result = null;
        try{
            result = new InMemory();
        } catch (Exception e) {
            result = DatabaseConnection.getInstance(DatabaseConnection.DatabaseType.INMEMORY);
        }

        return result;
    }

    @Test
    public void formatSalesTest(){
        try {
            Map<String, String> result = MainService.formatSales(1, DatabaseConnection.DatabaseType.INMEMORY);
            assertNotNull(result);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void ClientRentsTest(){
        try {
            Map<String, String> result = MainService.ClientRents(5, DatabaseConnection.DatabaseType.INMEMORY);
            assertNotNull(result);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}