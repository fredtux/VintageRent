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

public class EmployeeAdd {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static EmployeeAdd instance = null;
    private JLabel lblRentDate;
    private JDatePanelImpl JDatePanelImpl1;
    private JDatePickerImpl JDatePickerImpl1;
    private JDatePanelImpl JDatePanelImpl2;
    private JDatePickerImpl JDatePickerImpl2;
    private JPanel pnlMain;
    private JButton btnExit;

    public EmployeeAdd(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (EmployeeAdd.instance != null)
            throw new RuntimeException("EmployeeAdd is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static EmployeeAdd getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new EmployeeAdd(parentFrame, caller);

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

        this.lblRentDate = new JLabel("Birth date");
        this.lblRentDate.setText("Birth date");
        pnlMain.add(this.lblRentDate, c3);
        c3.gridx = 2;
        pnlMain.add(JDatePickerImpl1, c3);

        c2.gridy = 2;
//        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 2;
        c3.gridx = 1;
        c3.anchor = GridBagConstraints.WEST;
        JLabel lblHireDate = new JLabel("Hire Date");
        lblHireDate.setText("Hire Date");
        pnlMain.add(lblHireDate, c3);


        c3.gridx = 2;
        c3.anchor = GridBagConstraints.WEST;
        JDatePickerImpl2.setBounds(220,350,120,30);
        pnlMain.add(JDatePickerImpl2, c3);

        c3.gridy = 3;
        c3.gridx = 1;
        JLabel lblManager = new JLabel("Manager");
        lblManager.setText("Manager");
        pnlMain.add(lblManager, c3);

        c3.gridx = 2;
        JComboBox cmbManager = new JComboBox();
        EmployeeModel managerModel = EmployeeModel.getInstance();
        managerModel.setDatabaseType(caller.getDatabaseType());
        ModelList<EmployeeModel.InnerEmployeeModel> listManagers = null;
        try {
            listManagers = managerModel.getData();
        } catch (Exception e) {
            listManagers = new ModelList<>();
        }
        for (EmployeeModel.InnerEmployeeModel manager : listManagers.getList()) {
            cmbManager.addItem(new ComboItem(manager.EmployeeName, manager.UserID + ""));
        }
        pnlMain.add(cmbManager, c3);

        c3.gridy = 4;
        c3.gridx = 1;
        JLabel lblSalary = new JLabel("Salary");
        lblSalary.setText("Salary");
        pnlMain.add(lblSalary, c3);

        c3.gridx = 2;
        JComboBox cmbSalary = new JComboBox();
        SalaryModel salaryModel = SalaryModel.getInstance();
        salaryModel.setDatabaseType(caller.getDatabaseType());
        ModelList<SalaryModel.InnerSalaryModel> listSalaries = null;
        try {
            listSalaries = salaryModel.getData();
        } catch (Exception e) {
            listSalaries = new ModelList<>();
        }
        for (SalaryModel.InnerSalaryModel salary : listSalaries.getList()) {
            cmbSalary.addItem(new ComboItem(salary.Salary + "", salary.SalaryID + ""));
        }
        pnlMain.add(cmbSalary, c3);

        c3.gridy = 5;
        c3.gridx = 1;
        JLabel lblUtilizator = new JLabel("User name");
        lblUtilizator.setText("User name");
        pnlMain.add(lblUtilizator, c3);

        c3.gridx = 2;
        JComboBox cmbUtilizator = new JComboBox();
        UserModel userModel = UserModel.getInstance();
        userModel.setDatabaseType(caller.getDatabaseType());
        ModelList<UserModel.InnerUserModel> listUsers = null;
        try {
            listUsers = userModel.getData();
        } catch (Exception e) {
            listUsers = new ModelList<>();
        }
        for (UserModel.InnerUserModel user : listUsers.getList()) {
            cmbUtilizator.addItem(new ComboItem(user.Surname + " " + user.Firstname, user.UserID + ""));
        }
        pnlMain.add(cmbUtilizator, c3);

        c3.gridy = 6;
        c3.gridx = 1;
        JButton btnAdd = new JButton("Add");
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            EmployeeModel employeeModel = EmployeeModel.getInstance();
            EmployeeModel.InnerEmployeeModel employee = new EmployeeModel.InnerEmployeeModel();
            employee.BirthDate = LocalDateTime.parse(JDatePickerImpl1.getModel().getValue().toString() + " 00:00:00", dtf);
            employee.HireDate = LocalDateTime.parse(JDatePickerImpl2.getModel().getValue().toString() + " 00:00:00", dtf);
            employee.IDManager = Integer.parseInt(((ComboItem) cmbManager.getSelectedItem()).getValue());
            employee.SalaryID = Integer.parseInt(((ComboItem) cmbSalary.getSelectedItem()).getValue());
            employee.UserID = Integer.parseInt(((ComboItem) cmbUtilizator.getSelectedItem()).getValue());
            try {
                employeeModel.insertRow(employee);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            closeFrame(frame, true);
        });
        c3.gridy = 7;
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



        SqlDateModel datepickerModel2 = new SqlDateModel();
        datepickerModel2.setValue(Date.valueOf(dtf.format(LocalDateTime.now())));
        this.JDatePanelImpl2 = new JDatePanelImpl(datepickerModel2, datepickerProperties);

        this.JDatePickerImpl2 = new JDatePickerImpl(this.JDatePanelImpl2, new DateLabelFormatter());
        this.JDatePickerImpl2.setTextEditable(false);

        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        if(initParent)
            this.caller.initEmployeeTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
