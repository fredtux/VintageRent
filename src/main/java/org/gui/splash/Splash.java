package org.gui.splash;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Splash {

    public JWindow splash = null;

    public Splash(String imageName, String authorName, String projectName) throws Exception {
        if (imageName == null || authorName == null || projectName == null) {
            throw new IllegalArgumentException("Invalid Splash constructor parameters");
        }


        // Read image
        ImageIcon imgSplash = null;
        try {
            imgSplash = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(imageName)));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error reading splash image: " + ex.getMessage());
            return;
        }

        // Resize image to 200 height and proportional width
        Image img = imgSplash.getImage();
        Image newimg = img.getScaledInstance((img.getWidth(null) * 300 / img.getHeight(null)), 300, java.awt.Image.SCALE_SMOOTH);
        imgSplash = new ImageIcon(newimg);

        // Create splash
        this.splash = new JWindow();

        // South center for authorname
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.setBackground(Color.BLACK);
        JLabel lblAuthor = new JLabel(authorName);
        lblAuthor.setFont(new Font("Fira Code", Font.BOLD, 12));
        lblAuthor.setForeground(new Color(237, 231, 228));
        south.add(lblAuthor);

        // North center for project name
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        north.setBackground(Color.BLACK);
        JLabel lblProject = new JLabel(projectName);
        lblProject.setFont(new Font("Fira Code", Font.BOLD, 12));
        lblProject.setForeground(new Color(237, 231, 228));
        north.add(lblProject);

        this.splash.getContentPane().setLayout(new BorderLayout());
        this.splash.getContentPane().add(north, "North");
        this.splash.getContentPane().add(south, "South");
        this.splash.getContentPane().add(new JLabel(imgSplash), "Center");
        this.splash.getContentPane().setBackground(new Color(219, 189, 165));

        // Resize and center splash
        this.splash.setSize(300,300);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.splash.setLocation(dim.width / 2 - this.splash.getSize().width / 2, dim.height / 2 - this.splash.getSize().height / 2);

        this.splash.pack();

    }
}
