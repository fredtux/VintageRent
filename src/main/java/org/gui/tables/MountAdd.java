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

public class MountAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static MountAdd instance = null;
    private JPanel pnlMain;
    private JButton btnExit;

    public MountAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (MountAdd.instance != null)
            throw new RuntimeException("MountAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static MountAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new MountAdd(parentFrame, caller);

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

        JLabel lblTypeName = new JLabel("Mount Name");
        lblTypeName.setText("Mount Name");
        pnlMain.add(lblTypeName, c3);
        c3.gridx = 1;
        JTextField txtTypeName = new JTextField();
        txtTypeName.setText("");
        pnlMain.add(txtTypeName, c3);

        c3.gridy = 1;
        c3.gridx = 0;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            MountModel mountModel = MountModel.getInstance();
            MountModel.InnerMountModel mount = new MountModel.InnerMountModel();
            mount.Name = txtTypeName.getText();

            try {
                MainService.insert(mountModel, mount);
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
            this.caller.initMountTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
