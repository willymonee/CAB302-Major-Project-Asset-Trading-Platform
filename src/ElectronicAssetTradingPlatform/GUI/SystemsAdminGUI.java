package ElectronicAssetTradingPlatform.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class SystemsAdminGUI extends JFrame {
    private JLabel title;
    private JLabel label;
    private JButton backupBtn;

    private String DB_FILENAME = "ETP.db";
    private String BACKUP_FILENAME = "dbBackup.db";

    private String LABEL_CONTENT = "Press the button below to backup";

    public SystemsAdminGUI() {
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
                backup();
            }
        }
    }

    /**
     * Copies the DB file and backs it up as a separate file
     */
    private void backup() {
        // From https://www.baeldung.com/java-copy-file
        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(DB_FILENAME));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(BACKUP_FILENAME))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            label.setText("Backing up...please wait and do not close the application.");
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        label.setText(LABEL_CONTENT);
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
