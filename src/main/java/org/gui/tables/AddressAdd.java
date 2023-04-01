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
import java.util.HashMap;
import java.util.Map;

public class AddressAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static AddressAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public AddressAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (AddressAdd.instance != null)
            throw new RuntimeException("AddressAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static AddressAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new AddressAdd(parentFrame, caller);

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
        JLabel lblStreet = new JLabel("Street");
        lblStreet.setText("Street");
        pnlMain.add(lblStreet, c3);
        c3.gridx = 1;
        JTextField txtStreet = new JTextField();
        txtStreet.setText("");
        pnlMain.add(txtStreet, c3);

        c3.gridy = 1;
        c3.gridx = 0;
        JLabel lblCity = new JLabel("City");
        lblCity.setText("City");
        pnlMain.add(lblCity, c3);
        c3.gridx = 1;
        JTextField txtCity = new JTextField();
        txtCity.setText("");
        pnlMain.add(txtCity, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        JLabel lblCounty = new JLabel("County");
        lblCounty.setText("County");
        pnlMain.add(lblCounty, c3);
        c3.gridx = 1;
        JTextField txtCounty = new JTextField();
        txtCounty.setText("");
        pnlMain.add(txtCounty, c3);

        c3.gridy = 3;
        c3.gridx = 0;
        JLabel lblPostalCode = new JLabel("Postal Code");
        lblPostalCode.setText("Postal Code");
        pnlMain.add(lblPostalCode, c3);
        c3.gridx = 1;
        JTextField txtPostalCode = new JTextField();
        txtPostalCode.setText("");
        pnlMain.add(txtPostalCode, c3);

        c3.gridy = 4;
        c3.gridx = 0;
        JLabel lblClient = new JLabel("Client");
        lblClient.setText("Client");
        pnlMain.add(lblClient, c3);
        c3.gridx = 1;
        JComboBox cmbClient = new JComboBox();
        UserModel userModel = UserModel.getInstance();
        try {
            MainService.setDatabaseType(userModel, caller.getDatabaseType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.util.List<UserModel.InnerUserModel> listUsers = null;
        Map<Integer, String> mapUsers = null;
        try {
            listUsers = MainService.getData(userModel).getList();

            // Map users by UserID and FullName
            mapUsers = new HashMap<>();
            for (UserModel.InnerUserModel user : listUsers) {
                mapUsers.put(user.UserID, user.Firstname + " " + user.Surname);
            }
        } catch (Exception e) {
            mapUsers = new HashMap<>();
        }

        ClientModel clientModel = ClientModel.getInstance();
        try {
            MainService.setDatabaseType(clientModel, caller.getDatabaseType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelList<ClientModel.InnerClientModel> listClients = null;
        try {
            listClients = MainService.getData(clientModel);
        } catch (Exception e) {
            listClients = new ModelList<>();
        }

        for (ClientModel.InnerClientModel client : listClients.getList()) {
            cmbClient.addItem(new ComboItem(mapUsers.get(client.UserID), client.UserID + ""));
        }
        pnlMain.add(cmbClient, c3);

        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            AddressModel addressModel = AddressModel.getInstance();
            AddressModel.InnerAddressModel address = new AddressModel.InnerAddressModel();
            address.Street = txtStreet.getText();
            address.City = txtCity.getText();
            address.County = txtCounty.getText();
            address.PostalCode = txtPostalCode.getText();
            address.IDClient = Integer.parseInt(((ComboItem) cmbClient.getSelectedItem()).getValue());
            try {
                MainService.insert(addressModel, address);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
//            this.parentFrame.setVisible(true);
//            Dimension size = new Dimension();
//            size.setSize(800, 600);
//            this.parentFrame.setSize(size);;
        });
        c3.gridy = 5;
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
            this.caller.initAddressesTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
