package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Users.SystemsAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class SystemsAdminGUI extends JFrame {
    private SystemsAdmin loggedInUser;

    private JLabel title;
    private JLabel label;
    private JButton backupBtn;

    private String LABEL_CONTENT = "Press the button below to backup";

    public SystemsAdminGUI(SystemsAdmin user) {
        loggedInUser = user;

        // Initialise
        Container contentPane = this.getContentPane();

        title = new JLabel("Welcome, Systems Admin!");
        title.setHorizontalAlignment(JTextField.CENTER);

        label = new JLabel(LABEL_CONTENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        backupBtn = new JButton("Backup database");

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(title, BorderLayout.NORTH);
        contentPane.add(label, BorderLayout.CENTER);
        contentPane.add(backupBtn, BorderLayout.SOUTH);

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        backupBtn.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Login");
        setMinimumSize(new Dimension(250, 120));
        pack();
        setVisible(true);
    }


    /**
     * Handles events on the UI
     */
    private class ButtonListener implements ActionListener {
        /**
         * If shutdown button pressed, close server
         */
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == backupBtn) {
                loggedInUser.backupDB();
                // Alert of update
                JOptionPane.showMessageDialog(null, "Backup has completed.");
            }
        }
    }

    /**
     * Closes the server
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
