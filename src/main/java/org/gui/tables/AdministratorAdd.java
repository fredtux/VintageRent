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

public class AdministratorAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static AdministratorAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public AdministratorAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (AdministratorAdd.instance != null)
            throw new RuntimeException("AdministratorAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static AdministratorAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new AdministratorAdd(parentFrame, caller);

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
        JLabel lblName = new JLabel("User");
        lblName.setText("User");
        pnlMain.add(lblName, c3);

        c3.gridx = 1;
        JComboBox cmbUtilizator = new JComboBox();
        UserModel userModel = UserModel.getInstance();
        try {
            MainService.setDatabaseType(userModel, caller.getDatabaseType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelList<UserModel.InnerUserModel> listUsers = null;
        try {
            listUsers = MainService.getData(userModel);
        } catch (Exception e) {
            listUsers = new ModelList<>();
        }
        for (UserModel.InnerUserModel user : listUsers.getList()) {
            cmbUtilizator.addItem(new ComboItem(user.Surname + " " + user.Firstname, user.UserID + ""));
        }
        pnlMain.add(cmbUtilizator, c3);

        c3.gridy = 1;
        c3.gridx = 0;
        JLabel lblIsActive = new JLabel("Is Active");
        lblIsActive.setText("Is Active");
        pnlMain.add(lblIsActive, c3);

        c3.gridx = 1;
        JCheckBox chkIsActive = new JCheckBox();
        chkIsActive.setText("Is Active");
        pnlMain.add(chkIsActive, c3);

        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            AdministratorModel administratorModel = AdministratorModel.getInstance();
            AdministratorModel.InnerAdministratorModel administrator = new AdministratorModel.InnerAdministratorModel();
            administrator.UserID = Integer.parseInt(((ComboItem) cmbUtilizator.getSelectedItem()).getValue());
            administrator.isActive = chkIsActive.isSelected();
            try {
                MainService.insert(administratorModel, administrator);
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
        c3.gridx = 0;
        this.pnlMain.add(btnAdd, c3);

        c3.gridx = 1;
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
            this.caller.initAdministratorsTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
