package org.gui.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {

    private JPanel panel1;
    private JPanel pnlMain;
    private JButton btnMsg;

    public MainGUI() {
        btnMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hello World!");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vintage Rent");
        frame.setContentPane(new MainGUI().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center frame
        frame.setLocationRelativeTo(null);

        // Frame size 600x400
        frame.setSize(600, 400);

        frame.pack();
        frame.setVisible(true);
    }


}
