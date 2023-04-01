package org.gui.tables;

import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.models.*;

import javax.swing.*;
import java.awt.*;

public class CameraAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static CameraAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public CameraAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (CameraAdd.instance != null)
            throw new RuntimeException("CameraAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static CameraAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new CameraAdd(parentFrame, caller);

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
        c3.gridy = 1;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblBrand = new JLabel("Brand");
        lblBrand.setText("Brand");
        pnlMain.add(lblBrand, c3);

        c3.gridx = 2;
        JTextField txtBrand = new JTextField();
        txtBrand.setText("Canon");
        pnlMain.add(txtBrand, c3);

        c3.gridy = 2;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblModel = new JLabel("Model");
        lblModel.setText("Model");
        pnlMain.add(lblModel, c3);

        c3.gridy = 2;
        c3.gridx = 2;
        c3.anchor = GridBagConstraints.WEST;
        JTextField txtModel = new JTextField();
        txtModel.setText("AE-1");
        pnlMain.add(txtModel, c3);

        c3.gridy = 3;
        c3.gridx = 1;
        JLabel lblFormat = new JLabel("Format");
        lblFormat.setText("Format");
        pnlMain.add(lblFormat, c3);

        c3.gridx = 2;
        JComboBox cmbFormat = new JComboBox();
        FormatModel formatModel = FormatModel.getInstance();
        formatModel.setDatabaseType(caller.getDatabaseType());
        ModelList<FormatModel.InnerFormatModel> listFormats = null;
        try {
            listFormats = formatModel.getData();
        } catch (Exception e) {
            listFormats = new ModelList<>();
        }
        for (FormatModel.InnerFormatModel format : listFormats.getList()) {
            cmbFormat.addItem(new ComboItem(format.Name, format.FormatID + ""));
        }
        pnlMain.add(cmbFormat, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblCameraType = new JLabel("CameraType");
        lblCameraType.setText("CameraType");
        pnlMain.add(lblCameraType, c3);

        c3.gridx = 2;
        JComboBox cmbCameraType = new JComboBox();
        CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
        cameraTypeModel.setDatabaseType(caller.getDatabaseType());
        ModelList<CameraTypeModel.InnerCameraTypeModel> listCameraTypes = null;
        try {
            listCameraTypes = cameraTypeModel.getData();
        } catch (Exception e) {
            listCameraTypes = new ModelList<>();
        }
        for (CameraTypeModel.InnerCameraTypeModel type : listCameraTypes.getList()) {
            cmbCameraType.addItem(new ComboItem(type.Name, type.TypeID + ""));
        }
        pnlMain.add(cmbCameraType, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JLabel lblMount = new JLabel("Mount");
        lblMount.setText("Mount");
        pnlMain.add(lblMount, c3);

        c3.gridx = 2;
        JComboBox cmbMount = new JComboBox();
        MountModel monturaModel = MountModel.getInstance();
        monturaModel.setDatabaseType(caller.getDatabaseType());
        ModelList<MountModel.InnerMountModel> listMounts = null;
        try {
            listMounts = monturaModel.getData();
        } catch (Exception e) {
            listMounts = new ModelList<>();
        }
        for (MountModel.InnerMountModel montura : listMounts.getList()) {
            cmbMount.addItem(new ComboItem(montura.Name, montura.MountID + ""));
        }
        pnlMain.add(cmbMount, c3);

        c3.gridy = 7;
        c3.gridx = 1;
        JLabel lblManufacturingYear = new JLabel("ManufacturingYear");
        lblManufacturingYear.setText("ManufacturingYear");
        pnlMain.add(lblManufacturingYear, c3);

        c3.gridx = 2;
        JTextField txtManufacturingYear = new JTextField();
        txtManufacturingYear.setText("1985");
        pnlMain.add(txtManufacturingYear, c3);

        c3.gridy = 8;
        c3.gridx = 1;
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setText("Este Price");
        pnlMain.add(lblPrice, c3);

        c3.gridx = 2;
        JTextField txtPrice = new JTextField();
        txtPrice.setText("999");
        pnlMain.add(txtPrice, c3);


        c3.gridy = 9;
        c3.gridx = 1;
        JLabel lblRentalPrice = new JLabel("Price Rental");
        lblRentalPrice.setText("Price Rental");
        pnlMain.add(lblRentalPrice, c3);

        c3.gridx = 2;
        JTextField txtRentalPrice = new JTextField();
        txtRentalPrice.setText("19");
        pnlMain.add(txtRentalPrice, c3);


        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            CameraModel cameraModel = CameraModel.getInstance();
            CameraModel.InnerCameraModel camera = new CameraModel.InnerCameraModel();
            camera.Brand = txtBrand.getText();
            camera.ModelCamera = txtModel.getText();
            camera.FormatID = Integer.parseInt(((ComboItem) cmbFormat.getSelectedItem()).getValue());
            camera.TypeID = Integer.parseInt(((ComboItem) cmbCameraType.getSelectedItem()).getValue());
            camera.MountID = Integer.parseInt(((ComboItem) cmbMount.getSelectedItem()).getValue());
            camera.ManufacturingYear = Integer.parseInt(txtManufacturingYear.getText());
            camera.Price = Integer.parseInt(txtPrice.getText());
            camera.RentalPrice = Integer.parseInt(txtRentalPrice.getText());
            try {
                cameraModel.insertRow(camera);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
//            this.parentFrame.setVisible(true);
//            Dimension size = new Dimension();
//            size.setSize(800, 600);
//            this.parentFrame.setSize(size);;
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
            this.caller.initCameraTable(null ,null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
