package org.gui.main;

import org.models.RentModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainGUI { // Singleton
    private static MainGUI instance = null;

    private JPanel panel1;
    private JPanel pnlMain;
    private JScrollPane jscrPane;
    private JTable tblMain;

    public MainGUI() {
        // Singleton
        if(MainGUI.instance != null)
            throw new RuntimeException("MainGUI is a singleton class. Use getInstance() instead.");
        else
            instance = this;
    }

    public static MainGUI getInstance() {
        if (instance == null)
            throw new RuntimeException("No instance of MainGUI has been created");

        return instance;
    }

    private void initRentTable() {
        this.tblMain = new JTable();
        RentModel rentModel = new RentModel();
        try {
            rentModel.getData();
            this.tblMain.setModel(rentModel.getTableModel());
        } catch (Exception e) {
            System.out.println("Error in trying to initialize rent table: " + e.getMessage());
        }
    }

    public void main(String[] args) {
        JFrame frame = new JFrame("Vintage Rent");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add rent table
        initRentTable();
        DefaultTableModel tableModel = (DefaultTableModel) this.tblMain.getModel();
        tableModel.fireTableStructureChanged();
        System.out.println(this.tblMain.getModel().getRowCount());

        // Center frame
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - frame.getWidth()) / 2;
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        // Frame size 600x400
//        frame.setSize(600, 400);

        frame.pack();
        frame.setVisible(true);
    }


}
