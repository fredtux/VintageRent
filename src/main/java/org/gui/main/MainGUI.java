package org.gui.main;

import org.models.RentModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainGUI { // Singleton
    private static MainGUI instance = null;

    private JPanel panel1;
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
        RentModel rentModel = new RentModel();
        try {
            rentModel.getData();
            DefaultTableModel rm = rentModel.getTableModel();
            this.tblMain.setModel(rm);
            rm.fireTableDataChanged();
        } catch (Exception e) {
            System.out.println("Error in trying to initialize rent table: " + e.getMessage());
        }
    }

    public void main(String[] args) {
        JFrame frame = new JFrame("Vintage Rent");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(e -> System.exit(0));
        menu.add(menuItem);

        JMenu menu2 = new JMenu("About");
        menuBar.add(menu2);

        frame.setJMenuBar(menuBar);

        this.jscrPane.setViewportView(this.tblMain);

        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 1;
        c2.gridwidth = 3;
        c2.gridheight = 2;
        c2.weightx = 1;
        c2.weighty = 0.1;
        c2.anchor = GridBagConstraints.NORTH;
        c2.fill = GridBagConstraints.BOTH;
        this.panel1.add(this.jscrPane,c2);

        frame.getContentPane().add(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

        initRentTable();

//        // Frame size 600x400
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

    }


}
