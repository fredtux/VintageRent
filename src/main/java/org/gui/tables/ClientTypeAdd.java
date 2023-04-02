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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Properties;

public class ClientTypeAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static ClientTypeAdd instance = null;
    private JPanel pnlMain;
    private JButton btnExit;

    public ClientTypeAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (ClientTypeAdd.instance != null)
            throw new RuntimeException("ClientTypeAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static ClientTypeAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new ClientTypeAdd(parentFrame, caller);

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

        JLabel lblTypeName = new JLabel("Type Name");
        lblTypeName.setText("Type Name");
        pnlMain.add(lblTypeName, c3);
        c3.gridx = 1;
        JTextField txtTypeName = new JTextField();
        txtTypeName.setText("");
        pnlMain.add(txtTypeName, c3);

        c3.gridx = 0;
        c3.gridy = 1;
        JLabel lblDiscount = new JLabel("Discount");
        lblDiscount.setText("Discount");
        pnlMain.add(lblDiscount, c3);

        c3.gridx = 1;
        JTextField txtDiscount = new JTextField();
        txtDiscount.setText("");
        pnlMain.add(txtDiscount, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            ClientTypeModel clientTypeModel = ClientTypeModel.getInstance();
            ClientTypeModel.InnerClientTypeModel clientType = new ClientTypeModel.InnerClientTypeModel();
            clientType.Name = txtTypeName.getText();
            clientType.Discount = Double.parseDouble(txtDiscount.getText());

            try {
                MainService.insert(clientTypeModel, clientType);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
        });
        this.pnlMain.add(btnAdd, c3);

        c3.gridx = 2;
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

        frame.setLocationRelativeTo(this.parentFrame);
    }

    public void createUIComponents() {
        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initClientTypeTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
