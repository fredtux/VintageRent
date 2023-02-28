package org.gui.tables;

import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.models.CameraModel;
import org.models.ModelList;
import org.models.RentModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Properties;

public class RentAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static RentAdd instance = null;
    private JLabel lblDataInchiriere;
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

        this.lblDataInchiriere = new JLabel("Data inchiriere");
        this.lblDataInchiriere.setText("Data inchiriere");
        pnlMain.add(this.lblDataInchiriere, c3);
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
        ModelList<CameraModel.InnerCameraModel> listCameras = null;
        try {
            listCameras = cameraModel.getData();
        } catch (Exception e) {
            listCameras = new ModelList<>();
        }
        for (CameraModel.InnerCameraModel camera : listCameras.getList()) {
            cmbCamera.addItem(new ComboItem(camera.Marca + " " + camera.ModelCamera, String.valueOf(camera.IDCamera)));
        }
        pnlMain.add(cmbCamera, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblClient = new JLabel("Client");
        lblClient.setText("Client");
        pnlMain.add(lblClient, c3);

        c3.gridx = 2;
        JTextField txtClient = new JTextField();
        txtClient.setText("5");
        pnlMain.add(txtClient, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JLabel lblAngajat = new JLabel("Angajat");
        lblAngajat.setText("Angajat");
        pnlMain.add(lblAngajat, c3);

        c3.gridx = 2;
        JTextField txtAngajat = new JTextField();
        txtAngajat.setText("10");
        pnlMain.add(txtAngajat, c3);

        c3.gridy = 7;
        c3.gridx = 1;
        JLabel lblObiectiv = new JLabel("Obiectiv");
        lblObiectiv.setText("Obiectiv");
        pnlMain.add(lblObiectiv, c3);

        c3.gridx = 2;
        JTextField txtObiectiv = new JTextField();
        txtObiectiv.setText("1");
        pnlMain.add(txtObiectiv, c3);

        c3.gridy = 8;
        c3.gridx = 1;
        JLabel lblEsteReturnat = new JLabel("Este returnat");
        lblEsteReturnat.setText("Este returnat");
        pnlMain.add(lblEsteReturnat, c3);

        c3.gridx = 2;
        JComboBox cmbEsteReturnat = new JComboBox();
        cmbEsteReturnat.addItem(new ComboItem("Da", "1"));
        cmbEsteReturnat.addItem(new ComboItem("Nu", "0"));
        pnlMain.add(cmbEsteReturnat, c3);

        c3.gridy = 9;
        c3.gridx = 1;
        JLabel lblPenalizare = new JLabel("Penalizare");
        lblPenalizare.setText("Penalizare");
        pnlMain.add(lblPenalizare, c3);

        c3.gridx = 2;
        JTextField txtPenalizare = new JTextField();
        txtPenalizare.setText("0");
        pnlMain.add(txtPenalizare, c3);


        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            RentModel rentModel = RentModel.getInstance();
            RentModel.InnerRentModel rent = new RentModel.InnerRentModel();
            rent.IDCAMERA = Integer.parseInt(((ComboItem) cmbCamera.getSelectedItem()).getValue());
            rent.IDCLIENT = Integer.parseInt(txtClient.getText());
            rent.IDANGAJAT = Integer.parseInt(txtAngajat.getText());
            rent.IDOBIECTIV = Integer.parseInt(txtObiectiv.getText());
            rent.DURATA_IN_ZILE = Integer.parseInt(txtDurata.getText());
            int ret = Integer.parseInt(((ComboItem) cmbEsteReturnat.getSelectedItem()).getValue());
            rent.ESTE_RETURNAT = ret == 1;
            rent.PENALIZARE = Integer.parseInt(txtPenalizare.getText());
            rent.DATA_INCHIRIERE = (Date) JDatePickerImpl1.getModel().getValue();

            try {
                rentModel.insertRow(rent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            frame.dispose();
            caller.initRentTable();
            this.parentFrame.setEnabled(true);
            this.parentFrame.setFocusable(true);
            this.parentFrame.setSize(800, 600);
            this.parentFrame.setVisible(true);
        });
        c3.gridy = 10;
        c3.gridx = 1;
        this.pnlMain.add(btnAdd, c3);

        c3.gridx = 2;
        c3.anchor = GridBagConstraints.EAST;
        this.btnExit = new JButton("Exit");
        this.btnExit.setText("Cancel");
        this.btnExit.addActionListener(e -> {
            frame.dispose();
            this.parentFrame.setEnabled(true);
            this.parentFrame.setFocusable(true);
            this.parentFrame.setSize(800, 600);
            this.parentFrame.setVisible(true);
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
}
