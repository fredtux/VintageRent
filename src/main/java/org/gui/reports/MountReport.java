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

public class MountReport {
    private MainGUI caller = null;
    private JFrame parentFrame = null;
    private static MountReport instance = null;
    private JPanel pnlMain;
    private JButton btnExit;
    private JButton btnEmail;

    public MountReport(JFrame parentFrame, MainGUI caller) {
        // Singleton
        if (MountReport.instance != null)
            throw new RuntimeException("MountReport is a singleton class. Use getInstance() instead.");

        instance = this;
        this.parentFrame = parentFrame;
        this.caller = caller;
    }

    public static MountReport getInstance(JFrame parentFrame, MainGUI caller) {
        if (instance == null)
            instance = new MountReport(parentFrame, caller);

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
        JLabel lblMount = new JLabel("Mount");
        lblMount.setText("Mount");
        pnlMain.add(lblMount, c3);

        c3.gridx = 1;
        JComboBox<ComboItem> cmbMount = new JComboBox<>();
        MountModel mountModel = MountModel.getInstance();
        MainService.setDatabaseType(mountModel, caller.getDatabaseType());
        MainService.getData(mountModel);
        for (MountModel.InnerMountModel mount : mountModel.getModelList().getList()) {
            cmbMount.addItem(new ComboItem(mount.Name, mount.MountID + ""));
        }
        pnlMain.add(cmbMount, c3);

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
            getMountReport(Integer.parseInt(cmbMount.getItemAt(0).getValue().toString()), dtm, table);
        } catch (Exception ex) {
            System.out.println("Client report exception: " + ex.getMessage());
        }
        cmbMount.addActionListener(e -> {
            try {
                DefaultTableModel dtm2 = new DefaultTableModel();
                getMountReport(Integer.parseInt(cmbMount.getItemAt(cmbMount.getSelectedIndex()).getValue().toString()), dtm2, table);
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
                    if(MainService.sendEmail(email,  Integer.parseInt(cmbMount.getItemAt(cmbMount.getSelectedIndex()).getValue().toString()), "MountReport", caller.getDatabaseType()))
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

    public void getMountReport(int mountId, DefaultTableModel model, JTable table) throws Exception {
        java.util.List<Model.AbstractInnerModel> MountReport = MainService.getForMount(mountId, caller.getDatabaseType());
        model.addColumn("Name");
        model.addColumn("Type");

        for (Model.AbstractInnerModel item : MountReport) {
            if(item instanceof ObjectiveModel.InnerObjectiveModel)
                model.addRow(new Object[]{((ObjectiveModel.InnerObjectiveModel) item).Name, "Objective"});
            else if(item instanceof CameraModel.InnerCameraModel)
                model.addRow(new Object[]{((CameraModel.InnerCameraModel) item).Brand + " - " + ((CameraModel.InnerCameraModel) item).Brand, "Camera"});
        }

        table.setModel(model);
    }
}
