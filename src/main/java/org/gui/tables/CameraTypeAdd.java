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

public class CameraTypeAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static CameraTypeAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JPanel pnlMain;
    private JButton btnExit;

    public CameraTypeAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (CameraTypeAdd.instance != null)
            throw new RuntimeException("CameraTypeAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static CameraTypeAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new CameraTypeAdd(parentFrame, caller);

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
        JLabel lblName = new JLabel("Name");
        lblName.setText("Name");
        pnlMain.add(lblName, c3);

        c3.gridx = 2;
        JTextField txtName = new JTextField();
        txtName.setText("N/A");
        pnlMain.add(txtName, c3);

        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            CameraTypeModel cameraTypeModel = CameraTypeModel.getInstance();
            CameraTypeModel.InnerCameraTypeModel camera = new CameraTypeModel.InnerCameraTypeModel();
            camera.Name = txtName.getText();
            try {
                MainService.insert(cameraTypeModel, camera);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
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
        });
        this.pnlMain.add(this.btnExit, c3);


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Set relative to main window
        frame.setLocationRelativeTo(this.parentFrame);
    }

    public void createUIComponents() {
        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initCameraTypeTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
