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
    private JLabel lblDataInchiriere;
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
//        this.parentFrame.setVisible(false);

        JFrame frame = new JFrame("Vintage Rent");
        frame.setUndecorated(true);
        createUIComponents();
        frame.setContentPane(this.pnlMain);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 1;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblMarca = new JLabel("Marca");
        lblMarca.setText("Marca");
        pnlMain.add(lblMarca, c3);

        c3.gridx = 2;
        JTextField txtMarca = new JTextField();
        txtMarca.setText("Canon");
        pnlMain.add(txtMarca, c3);

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
        ModelList<FormatModel.InnerFormatModel> listFormats = null;
        try {
            listFormats = formatModel.getData();
        } catch (Exception e) {
            listFormats = new ModelList<>();
        }
        for (FormatModel.InnerFormatModel format : listFormats.getList()) {
            cmbFormat.addItem(new ComboItem(format.Denumire, format.IDFormat + ""));
        }
        pnlMain.add(cmbFormat, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblCameraType = new JLabel("TipCamera");
        lblCameraType.setText("TipCamera");
        pnlMain.add(lblCameraType, c3);

        c3.gridx = 2;
        JComboBox cmbCameraType = new JComboBox();
        CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
        ModelList<CameraTypeModel.InnerCameraTypeModel> listCameraTypes = null;
        try {
            listCameraTypes = cameraTypeModel.getData();
        } catch (Exception e) {
            listCameraTypes = new ModelList<>();
        }
        for (CameraTypeModel.InnerCameraTypeModel type : listCameraTypes.getList()) {
            cmbCameraType.addItem(new ComboItem(type.Denumire, type.IDTip + ""));
        }
        pnlMain.add(cmbCameraType, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JLabel lblMontura = new JLabel("Montura");
        lblMontura.setText("Montura");
        pnlMain.add(lblMontura, c3);

        c3.gridx = 2;
        JComboBox cmbMontura = new JComboBox();
        MountModel monturaModel = MountModel.getInstance();
        ModelList<MountModel.InnerMountModel> listMounts = null;
        try {
            listMounts = monturaModel.getData();
        } catch (Exception e) {
            listMounts = new ModelList<>();
        }
        for (MountModel.InnerMountModel montura : listMounts.getList()) {
            cmbMontura.addItem(new ComboItem(montura.Denumire, montura.IDMontura + ""));
        }
        pnlMain.add(cmbMontura, c3);

        c3.gridy = 7;
        c3.gridx = 1;
        JLabel lblAnFabricatie = new JLabel("AnFabricatie");
        lblAnFabricatie.setText("AnFabricatie");
        pnlMain.add(lblAnFabricatie, c3);

        c3.gridx = 2;
        JTextField txtAnFabricatie = new JTextField();
        txtAnFabricatie.setText("1985");
        pnlMain.add(txtAnFabricatie, c3);

        c3.gridy = 8;
        c3.gridx = 1;
        JLabel lblPret = new JLabel("Pret");
        lblPret.setText("Este Pret");
        pnlMain.add(lblPret, c3);

        c3.gridx = 2;
        JTextField txtPret = new JTextField();
        txtPret.setText("999");
        pnlMain.add(txtPret, c3);


        c3.gridy = 9;
        c3.gridx = 1;
        JLabel lblPretInchiriere = new JLabel("Pret Inchiriere");
        lblPretInchiriere.setText("Pret Inchiriere");
        pnlMain.add(lblPretInchiriere, c3);

        c3.gridx = 2;
        JTextField txtPretInchiriere = new JTextField();
        txtPretInchiriere.setText("19");
        pnlMain.add(txtPretInchiriere, c3);


        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            CameraModel cameraModel = CameraModel.getInstance();
            CameraModel.InnerCameraModel camera = new CameraModel.InnerCameraModel();
            camera.Marca = txtMarca.getText();
            camera.ModelCamera = txtModel.getText();
            camera.IDFormat = Integer.parseInt(((ComboItem) cmbFormat.getSelectedItem()).getValue());
            camera.IDTip = Integer.parseInt(((ComboItem) cmbCameraType.getSelectedItem()).getValue());
            camera.IDMontura = Integer.parseInt(((ComboItem) cmbMontura.getSelectedItem()).getValue());
            camera.AnFabricatie = Integer.parseInt(txtAnFabricatie.getText());
            camera.Pret = Integer.parseInt(txtPret.getText());
            camera.PretInchiriere = Integer.parseInt(txtPretInchiriere.getText());
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
            this.caller.initCameraTable();
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
