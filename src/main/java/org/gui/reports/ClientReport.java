package org.gui.reports;

import org.actions.MainService;
import org.gui.custom.ComboItem;
import org.gui.main.MainGUI;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ClientReport {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static ClientReport instance = null;
    private JPanel pnlMain;
    private JButton btnExit;
    private JButton btnEmail;

    public ClientReport(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (ClientReport.instance != null)
            throw new RuntimeException("ClientReport is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static ClientReport getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new ClientReport(parentFrame, caller);

        return instance;
    }

    public void main() throws Exception{
        this.parentFrame.setEnabled(false);
        this.parentFrame.setFocusable(false);
        this.parentFrame.setVisible(false);

        JFrame frame = new JFrame("Vintage Rent");
        frame.setUndecorated(true);
        frame.setMinimumSize(new Dimension(400, 300));
        createUIComponents();
        frame.setContentPane(this.pnlMain);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridy = 1;
        c3.gridx = 0;
        c3.anchor = GridBagConstraints.NORTH;
        c3.gridwidth = 1;
        c3.gridheight = 1;
        c3.weightx = 0.5;
        c3.weighty = 0.5;
        JLabel lblClient = new JLabel("Client");
        lblClient.setText("Name");
        pnlMain.add(lblClient, c3);

        c3.gridx = 1;
        JComboBox<ComboItem> cmbClient = new JComboBox<>();
        ClientModel clientModel = ClientModel.getInstance();
        clientModel.setDatabaseType(caller.getDatabaseType());
        clientModel.getData();
        for (ClientModel.InnerClientModel client : clientModel.getModelList().getList()) {
            cmbClient.addItem(new ComboItem(client.SurnameClient, client.UserID + ""));
        }
        pnlMain.add(cmbClient, c3);

        c3.gridy = 2;
        c3.gridx = 0;
        c3.anchor = GridBagConstraints.EAST;
        c3.gridheight = 2;
        c3.gridwidth = 2;
        c3.weightx = 1;
        c3.weighty = 1;
        c3.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane();

        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel();
        table.setModel(dtm);
        scrollPane.setViewportView(table);

        try{
            getClientReport(Integer.parseInt(cmbClient.getItemAt(0).getValue().toString()), dtm, table);
        } catch (Exception ex) {
            System.out.println("Client report exception: " + ex.getMessage());
        }
        cmbClient.addActionListener(e -> {
            try {
                DefaultTableModel dtm2 = new DefaultTableModel();
                getClientReport(Integer.parseInt(cmbClient.getItemAt(cmbClient.getSelectedIndex()).getValue().toString()), dtm2, table);
            } catch (Exception ex) {
                System.out.println("Client report exception: " + ex.getMessage());
            }
        });
        pnlMain.add(scrollPane, c3);

        c3.gridy = 4;
        c3.gridx = 0;
        c3.gridheight = 1;
        c3.gridwidth = 1;
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.anchor = GridBagConstraints.NORTH;
        this.btnEmail = new JButton("Email");
        this.btnEmail.setText("Email");
        this.btnEmail.addActionListener(e -> {
            String email = JOptionPane.showInputDialog("Enter email address");
            if(email != null && !email.isEmpty()) {
                try {
                    if(MainService.sendEmail(email,  Integer.parseInt(cmbClient.getItemAt(cmbClient.getSelectedIndex()).getValue().toString()), "ClientReport", caller.getDatabaseType()))
                        JOptionPane.showMessageDialog(null, "Email sent successfully");
                    else
                        JOptionPane.showMessageDialog(null, "Email failed to send");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Email failed to send");
                }
            }
        });
        this.pnlMain.add(this.btnEmail, c3);


        c3.gridx = 1;
        this.btnExit = new JButton("Exit");
        this.btnExit.setText("Exit");
        this.btnExit.addActionListener(e -> {
            closeFrame(frame, false);
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
            this.caller.initFormatTable(null, null, null);
        this.parentFrame.setEnabled(true);
        this.parentFrame.setFocusable(true);
        this.parentFrame.setVisible(true);

    }

    public void getClientReport(int clientId, DefaultTableModel model, JTable table) throws Exception {
        Map<String, String> clientReport = new MainService().ClientRents(clientId, caller.getDatabaseType());
        for(Map.Entry<String, String> entry : clientReport.entrySet()) {
            model.addColumn(entry.getKey());
        }
        model.addRow(clientReport.values().toArray());
        table.setModel(model);
    }
}
