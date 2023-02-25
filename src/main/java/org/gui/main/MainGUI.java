package org.gui.main;

import javax.swing.*;

public class MainGUI { // Singleton
    private static MainGUI instance = null;

    private JPanel panel1;
    private JPanel pnlMain;

    public MainGUI() {
        // Singleton
        if(instance != null)
            throw new RuntimeException("MainGUI is a singleton class. Use getInstance() instead.");
        else
            instance = this;
    }

    public static MainGUI getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vintage Rent");
        frame.setContentPane(new MainGUI().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center frame
        frame.setLocationRelativeTo(null);

        // Frame size 600x400
        frame.setSize(600, 400);

        frame.pack();
        frame.setVisible(true);
    }


}
