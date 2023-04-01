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

public class UserAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static UserAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JDatePanelImpl JDatePanelImpl2;
    private JDatePickerImpl JDatePickerImpl2;
    private JPanel pnlMain;
    private JButton btnExit;

    public UserAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (UserAdd.instance != null)
            throw new RuntimeException("UserAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static UserAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new UserAdd(parentFrame, caller);

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


        JLabel lblUserName = new JLabel("Username");
        lblUserName.setText("Username");
        pnlMain.add(lblUserName, c3);
        c3.gridx = 2;

        JTextField txtUserName = new JTextField();
        txtUserName.setText("New user");
        pnlMain.add(txtUserName, c3);

        c3.gridy = 2;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setText("Password");
        pnlMain.add(lblPassword, c3);


        c3.gridx = 2;
        c3.anchor = GridBagConstraints.WEST;
        JPasswordField txtPassword = new JPasswordField();
        pnlMain.add(txtPassword, c3);

        c3.gridy = 3;
        c3.gridx = 1;
        JLabel lblSurname = new JLabel("Surname");
        lblSurname.setText("Surname");
        pnlMain.add(lblSurname, c3);

        c3.gridx = 2;
        JTextField txtSurname = new JTextField();
        txtSurname.setText("Surname");
        pnlMain.add(txtSurname, c3);

        c3.gridy = 4;
        c3.gridx = 1;
        JLabel lblFirstname = new JLabel("Firstname");
        lblFirstname.setText("Firstname");
        pnlMain.add(lblFirstname, c3);

        c3.gridx = 2;
        JTextField txtFirstname = new JTextField();
        txtFirstname.setText("Firstname");
        pnlMain.add(txtFirstname, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblCNP = new JLabel("CNP");
        lblCNP.setText("CNP");
        pnlMain.add(lblCNP, c3);

        c3.gridx = 2;
        JTextField txtCNP = new JTextField();
        txtCNP.setText("CNP");
        pnlMain.add(txtCNP, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setText("Email");
        pnlMain.add(lblEmail, c3);

        c3.gridx = 2;
        JTextField txtEmail = new JTextField();
        txtEmail.setText("email@gmail.com");
        pnlMain.add(txtEmail, c3);

        c3.gridy = 7;
        c3.gridx = 1;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            UserModel userModel = UserModel.getInstance();
            UserModel.InnerUserModel user = new UserModel.InnerUserModel();
            user.Firstname = txtFirstname.getText();
            user.Surname = txtSurname.getText();
            user.CNP = txtCNP.getText();
            user.Email = txtEmail.getText();
            user.UserName = txtUserName.getText();
            user.Password = txtPassword.getText();

            try {
                MainService.insert(userModel, user);
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
            this.caller.initUserTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
