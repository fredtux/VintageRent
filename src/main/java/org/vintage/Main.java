package org.vintage;

import com.formdev.flatlaf.FlatDarkLaf;
import org.apache.commons.lang3.SystemUtils;
import org.database.DatabaseConnection;
import org.database.memory.InMemory;
import org.database.oracle.OracleConnection;
import org.gui.main.MainGUI;
import org.gui.splash.Splash;
import org.logger.CsvLogger;
import org.models.ModelInit;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private static final String AUTHOR = "Dinu Florin-Silviu";
    private static final String IMAGE = "/Splash/vintage_rent.png";

    private static final String PROJECT_NAME = "Vintage Rent";
    private static final int SPLASH_TIME = 0;

    public static String ORACLE_DB_ADDR = "172.21.128.2";

    private static AtomicBoolean isOracleUp = new AtomicBoolean(true);
    public static void main(String[] args) {
        if(SystemUtils.IS_OS_WINDOWS)
            ORACLE_DB_ADDR = "0.0.0.0";

        FlatDarkLaf.setup();
        System.setProperty("flatlaf.menuBarEmbedded", "false");

        try {
            ModelInit.logInit();
        } catch (Exception e) {
            e.printStackTrace();
        }


        CsvLogger logger = CsvLogger.getInstance();
        try{
            logger.log("Starting application");
        } catch (Exception ex) {
            System.out.println("Error logging to CSV: " + ex.getMessage());
        }

        Thread tmain = new Thread(new Runnable() {
            @Override
            public void run() {
                Splash splashscreen = null;
                try {
                    // Show splash
                    splashscreen = new Splash(IMAGE, AUTHOR, PROJECT_NAME);
                    splashscreen.splash.setVisible(true);
                    try {
                        Thread tdb = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    // Connect to Oracle Database and check if it's initialized
                                    DatabaseConnection orcl = new OracleConnection(ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "ORCLCDB", "C##TUX", "1521");
                                    orcl.connect();

                                    if(!orcl.isInitialized())
                                        orcl.init();

                                    ModelInit.init();
                                } catch (Exception ex) {
                                    System.out.println("Error connecting to database: " + ex.getMessage());
                                    isOracleUp.set(false);
                                    try {
                                        ModelInit.csvInit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                try{
                                    DatabaseConnection inmem = new InMemory();
                                    ModelInit.inmemInit();
                                } catch (Exception ex) {
                                    System.out.println("Error setting up in memory data storage: " + ex.getMessage());
                                }
                            }
                        });

                        // Start tdb and measure runtime
                        long startTime = System.currentTimeMillis();
                        tdb.start();
                        tdb.join();
                        long endTime = System.currentTimeMillis();

                        // Sleep splash for SPLASH_TIME - runtime
                        long runtime = endTime - startTime;
                        if(runtime < SPLASH_TIME)
                            Thread.sleep(SPLASH_TIME - runtime);

                    } catch (InterruptedException ex) {
                        System.out.println("Error sleeping splash: " + ex.getMessage());
                    }

                    splashscreen.splash.setVisible(false);
                    splashscreen.splash.dispose();

                    // Show main GUI
                    MainGUI mainGUI = new MainGUI();
                    if(!isOracleUp.get())
                        mainGUI.setDatabaseType(DatabaseConnection.DatabaseType.CSV);
                    mainGUI.main(null);
                } catch (Exception ex) {
                    System.out.println("Error sleeping main thread: " + ex.getMessage());
                    return;
                }
            }
        });

        try {
            tmain.start();
            tmain.join();

            try{
                logger.log("Ending Main");
            } catch (Exception ex) {
                System.out.println("Error logging to CSV: " + ex.getMessage());
            }
        } catch (InterruptedException ex) {
            System.out.println("Error starting main thread: " + ex.getMessage());
        }

    }
}