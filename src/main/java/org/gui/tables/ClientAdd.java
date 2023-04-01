package org.gui.tables;

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

public class ClientAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static ClientAdd instance = null;

    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public ClientAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (ClientAdd.instance != null)
            throw new RuntimeException("ClientAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static ClientAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new ClientAdd(parentFrame, caller);

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

        JLabel lblBirthDate = new JLabel("Brith date");
        lblBirthDate.setText("Brith date");
        pnlMain.add(lblBirthDate, c3);
        c3.gridx = 1;
        pnlMain.add(JDatePickerImpl1, c3);

//        pnlDate.add(JDatePickerImpl1);
        JDatePickerImpl1.setBounds(220,350,120,30);



        c3.gridx = 0;
        c3.gridy = 1;
        JLabel lblName = new JLabel("Name");
        lblName.setText("Name");
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

        c3.gridy = 2;
        c3.gridx = 0;
        JLabel lblType = new JLabel("Type");
        lblType.setText("Type");
        pnlMain.add(lblType, c3);

        c3.gridx = 1;
        JComboBox cmbType = new JComboBox();
        ClientTypeModel clientType = ClientTypeModel.getInstance();
        clientType.setDatabaseType(caller.getDatabaseType());
        ModelList<ClientTypeModel.InnerClientTypeModel> listTypes = null;
        try {
            listTypes = clientType.getData();
        } catch (Exception e) {
            listTypes = new ModelList<>();
        }
        for (ClientTypeModel.InnerClientTypeModel typeOfClient : listTypes.getList()) {
            cmbType.addItem(new ComboItem(typeOfClient.Name, typeOfClient.TypeID + ""));
        }
        pnlMain.add(cmbType, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ClientModel clientModel = ClientModel.getInstance();
            ClientModel.InnerClientModel client = new ClientModel.InnerClientModel();
            client.UserID = Integer.parseInt(((ComboItem) cmbName.getSelectedItem()).getValue());
            client.BirthDate = Date.valueOf(JDatePickerImpl1.getModel().getValue().toString());
            client.TypeID = Integer.parseInt(((ComboItem) cmbType.getSelectedItem()).getValue());
            try {
                clientModel.insertRow(client);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
        });
        c3.gridy = 3;
        c3.gridx = 1;
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
        SqlDateModel datepickerModel = new SqlDateModel();
        Properties datepickerProperties = new Properties();
        datepickerProperties.put("text.today", "Day");
        datepickerProperties.put("text.month", "Month");
        datepickerProperties.put("text.year", "Year");
        // Set date as today
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        datepickerModel.setValue(Date.valueOf(dtf.format(LocalDateTime.now())));
        this.JDatePanelImpl1 = new JDatePanelImpl(datepickerModel, datepickerProperties);

        class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

            private String datePattern = "yyyy-MM-dd";
            private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

            @Override
            public Object stringToValue(String text) throws ParseException {
                return dateFormatter.parseObject(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    return dateFormatter.format(cal.getTime());
                }

                return "";
            }

        }
        this.JDatePickerImpl1 = new JDatePickerImpl(this.JDatePanelImpl1, new DateLabelFormatter());
        this.JDatePickerImpl1.setTextEditable(false);

        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initClientTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
