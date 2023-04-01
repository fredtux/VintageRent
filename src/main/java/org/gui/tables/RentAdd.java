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

public class RentAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static RentAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public RentAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (RentAdd.instance != null)
            throw new RuntimeException("RentAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static RentAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new RentAdd(parentFrame, caller);

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


        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 1;
        c2.gridy = 1;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 1;
        c2.weighty = 0.1;
        c2.anchor = GridBagConstraints.NORTH;
        c2.fill = GridBagConstraints.BOTH;

        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 1;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;

//        pnlDate.add(JDatePickerImpl1);
        JDatePickerImpl1.setBounds(220,350,120,30);

        this.lblRentDate = new JLabel("Data inchiriere");
        this.lblRentDate.setText("Data inchiriere");
        pnlMain.add(this.lblRentDate, c3);
        c3.gridx = 2;
        pnlMain.add(JDatePickerImpl1, c3);

        c2.gridy = 2;
//        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 2;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblDurata = new JLabel("Durata");
        lblDurata.setText("Durata");
        pnlMain.add(lblDurata, c3);


        c3.gridy = 2;
        c3.gridx = 2;
        c3.anchor = GridBagConstraints.WEST;
//        c3.fill = GridBagConstraints.BOTH;
        JTextField txtDurata = new JTextField();
        txtDurata.setText("1");
        pnlMain.add(txtDurata, c3);

        c3.gridy = 3;
        c3.gridx = 1;
        JLabel lblCamera = new JLabel("Camera");
        lblCamera.setText("Camera");
        pnlMain.add(lblCamera, c3);

        c3.gridx = 2;
        JComboBox cmbCamera = new JComboBox();
        CameraModel cameraModel = CameraModel.getInstance();
        cameraModel.setDatabaseType(caller.getDatabaseType());
        ModelList<CameraModel.InnerCameraModel> listCameras = null;
        try {
            listCameras = cameraModel.getData();
        } catch (Exception e) {
            listCameras = new ModelList<>();
        }
        for (CameraModel.InnerCameraModel camera : listCameras.getList()) {
            cmbCamera.addItem(new ComboItem(camera.Brand + " " + camera.ModelCamera, String.valueOf(camera.IDCamera)));
        }
        pnlMain.add(cmbCamera, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblClient = new JLabel("Client");
        lblClient.setText("Client");
        pnlMain.add(lblClient, c3);

        c3.gridx = 2;
        JComboBox cmbClient = new JComboBox();
        ClientModel clientModel = ClientModel.getInstance();
        clientModel.setDatabaseType(caller.getDatabaseType());
        ModelList<ClientModel.InnerClientModel> listClients = null;
        try {
            listClients = clientModel.getData();
        } catch (Exception e) {
            listClients = new ModelList<>();
        }
        for (ClientModel.InnerClientModel client : listClients.getList()) {
            cmbClient.addItem(new ComboItem(client.SurnameClient, client.UserID + ""));
        }
        pnlMain.add(cmbClient, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JLabel lblAngajat = new JLabel("Angajat");
        lblAngajat.setText("Angajat");
        pnlMain.add(lblAngajat, c3);

        c3.gridx = 2;
        JComboBox cmbAngajat = new JComboBox();
        EmployeeModel angajatModel = EmployeeModel.getInstance();
        angajatModel.setDatabaseType(caller.getDatabaseType());
        ModelList<EmployeeModel.InnerEmployeeModel> listEmployees = null;
        try {
            listEmployees = angajatModel.getData();
        } catch (Exception e) {
            listEmployees = new ModelList<>();
        }
        for (EmployeeModel.InnerEmployeeModel angajat : listEmployees.getList()) {
            cmbAngajat.addItem(new ComboItem(angajat.SurnameAngajat, angajat.UserID + ""));
        }
        pnlMain.add(cmbAngajat, c3);

        c3.gridy = 7;
        c3.gridx = 1;
        JLabel lblObiectiv = new JLabel("Obiectiv");
        lblObiectiv.setText("Obiectiv");
        pnlMain.add(lblObiectiv, c3);

        c3.gridx = 2;
        JComboBox cmbObiectiv = new JComboBox();
        ObjectiveModel obiectivModel = ObjectiveModel.getInstance();
        obiectivModel.setDatabaseType(caller.getDatabaseType());
        ModelList<ObjectiveModel.InnerObjectiveModel> listObjectives = null;
        try {
            listObjectives = obiectivModel.getData();
        } catch (Exception e) {
            listObjectives = new ModelList<>();
        }
        for (ObjectiveModel.InnerObjectiveModel obiectiv : listObjectives.getList()) {
            cmbObiectiv.addItem(new ComboItem(obiectiv.Name, obiectiv.ObjectiveID + ""));
        }
        pnlMain.add(cmbObiectiv, c3);

        c3.gridy = 8;
        c3.gridx = 1;
        JLabel lblIsReturned = new JLabel("Este returnat");
        lblIsReturned.setText("Este returnat");
        pnlMain.add(lblIsReturned, c3);

        c3.gridx = 2;
        JComboBox cmbIsReturned = new JComboBox();
        cmbIsReturned.addItem(new ComboItem("Da", "1"));
        cmbIsReturned.addItem(new ComboItem("Nu", "0"));
        pnlMain.add(cmbIsReturned, c3);

        c3.gridy = 9;
        c3.gridx = 1;
        JLabel lblPenaltyFee = new JLabel("PenaltyFee");
        lblPenaltyFee.setText("PenaltyFee");
        pnlMain.add(lblPenaltyFee, c3);

        c3.gridx = 2;
        JTextField txtPenaltyFee = new JTextField();
        txtPenaltyFee.setText("0");
        pnlMain.add(txtPenaltyFee, c3);


        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            RentModel rentModel = RentModel.getInstance();
            RentModel.InnerRentModel rent = new RentModel.InnerRentModel();
            rent.IDCAMERA = Integer.parseInt(((ComboItem) cmbCamera.getSelectedItem()).getValue());
            rent.IDCLIENT = Integer.parseInt(((ComboItem) cmbClient.getSelectedItem()).getValue());
            rent.IDANGAJAT = Integer.parseInt(((ComboItem) cmbAngajat.getSelectedItem()).getValue());
            rent.OBJECTIVEID = Integer.parseInt(((ComboItem) cmbObiectiv.getSelectedItem()).getValue());
            rent.DURATION_IN_DAYS = Integer.parseInt(txtDurata.getText());
            int ret = Integer.parseInt(((ComboItem) cmbIsReturned.getSelectedItem()).getValue());
            rent.IS_RETURNED = ret == 1;
            rent.PENALTYFEE = Integer.parseInt(txtPenaltyFee.getText());
            rent.RENT_DATE = (Date) JDatePickerImpl1.getModel().getValue();

            try {
                rentModel.insertRow(rent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
        });
        c3.gridy = 10;
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

//        frame.setSize(600, 600);
        // Set relative to main window
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
            this.caller.initRentTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);
    }
}
