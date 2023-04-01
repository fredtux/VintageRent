package org.gui.tables;

import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.models.*;

import javax.swing.*;
import java.awt.*;

public class AdministratorSubdomainAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static AdministratorSubdomainAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public AdministratorSubdomainAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (AdministratorSubdomainAdd.instance != null)
            throw new RuntimeException("AdministratorSubdomainAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static AdministratorSubdomainAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new AdministratorSubdomainAdd(parentFrame, caller);

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
        JLabel lblName = new JLabel("Administrator");
        lblName.setText("Administrator");
        pnlMain.add(lblName, c3);

        c3.gridx = 1;
        JComboBox cmbName = new JComboBox();
        UserModel userModel = UserModel.getInstance();
        userModel.setDatabaseType(caller.getDatabaseType());
        ModelList<UserModel.InnerUserModel> listUsers = null;
        try {
            listUsers = userModel.getData();
        } catch (Exception e) {
            listUsers = new ModelList<>();
        }
        for (UserModel.InnerUserModel user : listUsers.getList()) {
            cmbName.addItem(new ComboItem(user.Surname + " " + user.Firstname, user.UserID + ""));
        }
        pnlMain.add(cmbName, c3);

        c3.gridy = 1;
        c3.gridx = 0;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblSubdomain = new JLabel("Subdomain");
        lblSubdomain.setText("Subdomain");
        pnlMain.add(lblSubdomain, c3);

        c3.gridx = 1;
        JComboBox cmbSubdomain = new JComboBox();
        SubdomainModel subdomainModel = SubdomainModel.getInstance();
        subdomainModel.setDatabaseType(caller.getDatabaseType());
        ModelList<SubdomainModel.InnerSubdomainModel> listSubdomains = null;
        try {
            listSubdomains = subdomainModel.getData();
        } catch (Exception e) {
            listSubdomains = new ModelList<>();
        }
        for (SubdomainModel.InnerSubdomainModel subdomain : listSubdomains.getList()) {
            cmbSubdomain.addItem(new ComboItem(subdomain.Name, subdomain.SubdomainID + ""));
        }
        pnlMain.add(cmbSubdomain, c3);

        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            AdministratorSubdomainModel administratorSubdomainModel = AdministratorSubdomainModel.getInstance();
            AdministratorSubdomainModel.InnerAdministratorSubdomainModel administratorsubdomain = new AdministratorSubdomainModel.InnerAdministratorSubdomainModel();
            administratorsubdomain.IDAdministrator = Integer.parseInt(((ComboItem) cmbName.getSelectedItem()).getValue());
            administratorsubdomain.SubdomainID = Integer.parseInt(((ComboItem) cmbSubdomain.getSelectedItem()).getValue());
            try {
                administratorSubdomainModel.insertRow(administratorsubdomain);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
//            this.parentFrame.setVisible(true);
//            Dimension size = new Dimension();
//            size.setSize(800, 600);
//            this.parentFrame.setSize(size);;
        });
        c3.gridy = 2;
        c3.gridx = 1;
        this.pnlMain.add(btnAdd, c3);

        c3.gridx = 2;
        c3.anchor = GridBagConstraints.EAST;
        this.btnExit = new JButton("Exit");
        this.btnExit.setText("Cancel");
        this.btnExit.addActionListener(e -> {
            closeFrame(frame, false);
//            this.parentFrame.setVisible(true);
//            Dimension size = new Dimension();
//            size.setSize(800, 600);
//            this.parentFrame.setSize(size);
        });
        this.pnlMain.add(this.btnExit, c3);


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//        frame.setSize(600, 600);
        // Set relative to main window
        frame.setLocationRelativeTo(this.parentFrame);
    }

    public void createUIComponents() {
        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initAdministratorSubdomainsTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
