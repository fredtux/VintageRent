package org.vintage;

import org.database.oracle.OracleConnection;
import org.gui.main.MainGUI;
import org.gui.splash.Splash;

public class Main {

    private static final String AUTHOR = "Dinu Florin-Silviu";
    private static final String IMAGE = "/Splash/vintage_rent.png";

    private static final String PROJECT_NAME = "Vintage Rent";
    private static final int SPLASH_TIME = 0;

    public static final String ORACLE_DB_ADDR = "172.20.128.2";
    public static void main(String[] args) {

        Thread tmain = new Thread(new Runnable() {
            @Override
            public void run() {
                Splash splashscreen = null;
                try {
                    // Show splash
                    splashscreen = new Splash(IMAGE, AUTHOR, PROJECT_NAME);
                    splashscreen.splash.setVisible(true);
                    try {
                        Thread.sleep(SPLASH_TIME);
                    } catch (InterruptedException ex) {
                        System.out.println("Error sleeping splash: " + ex.getMessage());
                    }

                    // Connect to Oracle Database and check if it's initialized
                    OracleConnection orcl = new OracleConnection(ORACLE_DB_ADDR, "c##tux", "fmilove", "oracle.jdbc.driver.OracleDriver", "ORCLCDB", "C##TUX", "1521");
                    orcl.connect();

                    if(!orcl.isInitialized())
                        orcl.init();

                    splashscreen.splash.setVisible(false);
                    splashscreen.splash.dispose();

                    // Show main GUI
                    MainGUI mainGUI = new MainGUI();
                    mainGUI.main(null);
                } catch (Exception ex) {
                    System.out.println("Error sleeping main thread: " + ex.getMessage());
                    return;
                }
            }
        });

        tmain.start();

    }
}