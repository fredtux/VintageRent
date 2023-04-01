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

public class ObjectiveAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static ObjectiveAdd instance = null;
    private JPanel pnlMain;
    private JButton btnExit;

    public ObjectiveAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (ObjectiveAdd.instance != null)
            throw new RuntimeException("ObjectiveAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static ObjectiveAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new ObjectiveAdd(parentFrame, caller);

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

        JLabel lblObjectiveName = new JLabel("Objective Name");
        lblObjectiveName.setText("Objective Name");
        pnlMain.add(lblObjectiveName, c3);
        c3.gridx = 1;
        JTextField txtObjectiveName = new JTextField();
        txtObjectiveName.setText("");
        pnlMain.add(txtObjectiveName, c3);

        c3.gridy = 1;
        c3.gridx = 0;
        JLabel lblFocalDistance = new JLabel("Focal Distance");
        lblFocalDistance.setText("Focal Distance");
        pnlMain.add(lblFocalDistance, c3);
        c3.gridx = 1;
        JTextField txtFocalDistance = new JTextField();
        txtFocalDistance.setText("");
        pnlMain.add(txtFocalDistance, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        JLabel lblMinimumAperture = new JLabel("Minimum Aperture");
        lblMinimumAperture.setText("Minimum Aperture");
        pnlMain.add(lblMinimumAperture, c3);
        c3.gridx = 1;
        JTextField txtMinimumAperture = new JTextField();
        txtMinimumAperture.setText("");
        pnlMain.add(txtMinimumAperture, c3);

        c3.gridy = 3;
        c3.gridx = 0;
        JLabel lblMaximumAperture = new JLabel("Maximum Aperture");
        lblMaximumAperture.setText("Maximum Aperture");
        pnlMain.add(lblMaximumAperture, c3);
        c3.gridx = 1;
        JTextField txtMaximumAperture = new JTextField();
        txtMaximumAperture.setText("");
        pnlMain.add(txtMaximumAperture, c3);

        c3.gridy = 4;
        c3.gridx = 0;
        JLabel lblDiameter = new JLabel("Diameter");
        lblDiameter.setText("Diameter");
        pnlMain.add(lblDiameter, c3);
        c3.gridx = 1;
        JTextField txtDiameter = new JTextField();
        txtDiameter.setText("");
        pnlMain.add(txtDiameter, c3);

        c3.gridy = 5;
        c3.gridx = 0;
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setText("Price");
        pnlMain.add(lblPrice, c3);
        c3.gridx = 1;
        JTextField txtPrice = new JTextField();
        txtPrice.setText("");
        pnlMain.add(txtPrice, c3);

        c3.gridy = 6;
        c3.gridx = 0;
        JLabel lblRentalPrice = new JLabel("Rental Price");
        lblRentalPrice.setText("Rental Price");
        pnlMain.add(lblRentalPrice, c3);
        c3.gridx = 1;
        JTextField txtRentalPrice = new JTextField();
        txtRentalPrice.setText("");
        pnlMain.add(txtRentalPrice, c3);

        c3.gridy = 7;
        c3.gridx = 0;
        JLabel lblMount = new JLabel("Mount");
        lblMount.setText("Mount");
        pnlMain.add(lblMount, c3);
        c3.gridx = 1;
        JComboBox<ComboItem> cmbMount = new JComboBox<>();
        MountModel mountModel = MountModel.getInstance();
        mountModel.setDatabaseType(caller.getDatabaseType());
        ModelList<MountModel.InnerMountModel> listMounts = null;
        try {
            listMounts = mountModel.getData();
        } catch (Exception e) {
            listMounts = new ModelList<>();
        }
        for (MountModel.InnerMountModel mounts : listMounts.getList()) {
            cmbMount.addItem(new ComboItem(mounts.Name, mounts.MountID + ""));
        }
        pnlMain.add(cmbMount, c3);


        c3.gridy = 8;
        c3.gridx = 0;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            ObjectiveModel objectiveModel = ObjectiveModel.getInstance();
            ObjectiveModel.InnerObjectiveModel objective = new ObjectiveModel.InnerObjectiveModel();
            objective.Name = txtObjectiveName.getText();
            objective.FocalDistance = Integer.parseInt(txtFocalDistance.getText());
            objective.MinimumAperture = Integer.parseInt(txtMinimumAperture.getText());
            objective.MaximumAperture = Integer.parseInt(txtMaximumAperture.getText());
            objective.Diameter = Integer.parseInt(txtDiameter.getText());
            objective.Price = Integer.parseInt(txtPrice.getText());
            objective.RentalPrice = Integer.parseInt(txtRentalPrice.getText());
            objective.MountID = Integer.parseInt(((ComboItem) cmbMount.getSelectedItem()).getValue());

            try {
                objectiveModel.insertRow(objective);
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
            this.caller.initObjectiveTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
