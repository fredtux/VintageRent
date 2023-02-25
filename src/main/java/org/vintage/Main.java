package org.vintage;

import org.gui.main.MainGUI;
import org.gui.splash.Splash;

public class Main {

    private static final String AUTHOR = "Dinu Florin-Silviu";
    private static final String IMAGE = "/Splash/vintage_rent.png";

    private static final String PROJECT_NAME = "Vintage Rent";
    private static final int SPLASH_TIME = 0;
    public static void main(String[] args) {

        Thread tmain = new Thread(new Runnable() {
            @Override
            public void run() {
                Splash splashscreen = null;
                try {
                    splashscreen = new Splash(IMAGE, AUTHOR, PROJECT_NAME);
                    // Show splash
                    splashscreen.splash.setVisible(true);
                    try {
                        Thread.sleep(SPLASH_TIME);
                        splashscreen.splash.setVisible(false);
                        splashscreen.splash.dispose();
                    } catch (InterruptedException ex) {
                        System.out.println("Error sleeping splash: " + ex.getMessage());
                    } finally {
                        splashscreen.splash.setVisible(false);
                        splashscreen.splash.dispose();
                    }

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