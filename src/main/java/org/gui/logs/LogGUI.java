package org.gui.logs;

import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.logger.CsvLogger;
import org.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class LogGUI {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static LogGUI instance = null;
    private JPanel pnlMain;

    private JButton btnExit;

    public LogGUI(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (LogGUI.instance != null)
            throw new RuntimeException("LogGUI is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static LogGUI getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new LogGUI(parentFrame, caller);

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
        c3.gridwidth = 2;
        c3.gridheight = 1;
        c3.anchor = GridBagConstraints.NORTH;
        JCheckBox chkThisSeesion = new JCheckBox("Sesiunea curenta");
        chkThisSeesion.setText("Sesiunea curenta");
        chkThisSeesion.setSelected(true);

        pnlMain.add(chkThisSeesion, c3);


        c3.gridy = 2;
        c3.gridx = 1;
        c3.gridheight = 2;
        c3.gridwidth = 2;
        c3.fill = GridBagConstraints.BOTH;

        JTable table = new JTable();

        DefaultTableModel tableModel = getTableModel(true);

        table.setModel(tableModel);

        chkThisSeesion.addActionListener(e -> {
                    if (chkThisSeesion.isSelected()) {
                        DefaultTableModel tbl = getTableModel(true);
                        table.setModel(tbl);
                        tbl.fireTableDataChanged();
                    } else {
                        DefaultTableModel tbl = getTableModel(false);
                        table.setModel(tbl);
                        tbl.fireTableDataChanged();
                    }
                }
        );


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        pnlMain.add(scrollPane, c3);


        c3.gridx = 1;
        c3.gridy = 5;
        c3.gridwidth = 2;
        c3.gridheight = 1;
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.anchor = GridBagConstraints.EAST;
        this.btnExit = new JButton("Exit");
        this.btnExit.setText("Exit");
        this.btnExit.addActionListener(e -> {
            closeFrame(frame, false);
        });
        this.pnlMain.add(this.btnExit, c3);


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.setLocationRelativeTo(this.parentFrame);
    }

    private DefaultTableModel getTableModel(boolean today) {
        CsvLogger csvLogger = CsvLogger.getInstance();
        Pair<java.util.List<String>, List<List<String>>> log = null;
        try {
            if(today)
                log = csvLogger.readLogToday();
            else
                log = csvLogger.readLog();
        } catch (Exception e) {
            System.out.println("Problem reading logs: " + e.getMessage());
        }

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Event", "Time"}, 0);

        if(!today) {
            Object[] rowObj = new Object[2];
            rowObj[0] = log.first.get(0);
            rowObj[1] = log.first.get(1);
            tableModel.addRow(rowObj);
        }



        for (int i = 0; i < log.second.size(); i++) {
            List<String> row = log.second.get(i);
            Object[] rowObj = new Object[row.size()];
            for (int j = 0; j < row.size(); j++)
                rowObj[j] = row.get(j);
            tableModel.addRow(rowObj);
        }

        return tableModel;
    }

    public void createUIComponents() {
        this.pnlMain = new JPanel(new GridBagLayout());
    }

    public void closeFrame(JFrame frame, boolean initParent) {
        frame.dispose();
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }
}
