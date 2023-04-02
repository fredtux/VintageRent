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

public class SalaryAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static SalaryAdd instance = null;
    private JPanel pnlMain;
    private JButton btnExit;

    public SalaryAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (SalaryAdd.instance != null)
            throw new RuntimeException("SalaryAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static SalaryAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new SalaryAdd(parentFrame, caller);

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

        JLabel lblSalary = new JLabel("Salary");
        lblSalary.setText("Salary");
        pnlMain.add(lblSalary, c3);
        c3.gridx = 1;
        JTextField txtSalary = new JTextField();
        txtSalary.setText("");
        pnlMain.add(txtSalary, c3);

        c3.gridx = 0;
        c3.gridy = 1;
        JLabel lblBonus = new JLabel("Bonus");
        lblBonus.setText("Bonus");
        pnlMain.add(lblBonus, c3);
        c3.gridx = 1;
        JTextField txtBonus = new JTextField();
        txtBonus.setText("");
        pnlMain.add(txtBonus, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            SalaryModel salaryModel = SalaryModel.getInstance();
            SalaryModel.InnerSalaryModel salary = new SalaryModel.InnerSalaryModel();
            salary.Salary = Integer.parseInt(txtSalary.getText());
            salary.Bonus = Double.parseDouble(txtBonus.getText());

            try {
                MainService.insert(salaryModel, salary);
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
            this.caller.initSalaryTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
