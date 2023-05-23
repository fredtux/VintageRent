package org.gui.tables;

import org.actions.MainService;
import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.models.*;

import javax.swing.*;
import java.awt.*;

public class SubdomainAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static SubdomainAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public SubdomainAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (SubdomainAdd.instance != null)
            throw new RuntimeException("SubdomainAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static SubdomainAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new SubdomainAdd(parentFrame, caller);

        return instance;
    }

    public void main() {
        this.parentFrame.setEnabled(false);
        this.parentFrame.setFocusable(false);
        this.parentFrame.setVisible(false);

        JFrame frame = new JFrame("Vintage Rent");
        frame.setUndecorated(true);
        createUIComponents();
        frame.setContentPane(this.pnlMain);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 0;
        c3.gridx = 0;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblName = new JLabel("Name");
        lblName.setText("Name");
        pnlMain.add(lblName, c3);

        c3.gridx = 1;
        JTextField txtName = new JTextField();
        txtName.setText("");
        pnlMain.add(txtName, c3);

        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            SubdomainModel subdomainModel = SubdomainModel.getInstance();
            SubdomainModel.InnerSubdomainModel subdomain = new SubdomainModel.InnerSubdomainModel();
            subdomain.Name = txtName.getText();
            try {
                MainService.insert(subdomainModel, subdomain);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
        });
        c3.gridy = 1;
        c3.gridx = 0;
        this.pnlMain.add(btnAdd, c3);

        c3.gridx = 1;
        c3.anchor = GridBagConstraints.EAST;
        this.btnExit = new JButton("Exit");
        this.btnExit.setText("Cancel");
        this.btnExit.addActionListener(e -> {
            closeFrame(frame, false);
        });
        this.pnlMain.add(this.btnExit, c3);


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Set relative to main window
        frame.setLocationRelativeTo(this.parentFrame);
    }

    public void createUIComponents() {
        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initSubdomainsTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
