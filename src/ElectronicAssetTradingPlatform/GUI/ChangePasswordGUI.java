package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.User;

import javax.swing.GroupLayout.Alignment;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePasswordGUI extends JFrame {
    User loggedInUser;
    NetworkDataSource data;

    private JTextField password;
    private JTextField newPassword;
    private JTextArea messaging;
    private JButton changeButton;

    /**
     * Constructor sets up user interface, adds listeners and displays.
     */
    public ChangePasswordGUI(NetworkDataSource dataSource, User user) {
        data = dataSource;
        loggedInUser = user;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        changeButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("EDIT Existing User");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    /**
     * Initialise UI
     */
    private void initUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(editUserPanel());
    }

    /**
     * Makes the Edit User form
     *
     * @return JPanel of edit user form
     */
    private JPanel editUserPanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel passwordLabel = new JLabel("Current Password");
        JLabel newPasswordLabel = new JLabel("New Password");

        password = new JTextField(20);
        newPassword = new JTextField(20);
        changeButton = new JButton("Change Password");
        messaging = new JTextArea();

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(passwordLabel)
                .addComponent(newPasswordLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(password)
                .addComponent(newPassword)
                .addComponent(changeButton, Alignment.CENTER)
                .addComponent(messaging, Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(passwordLabel)
                .addComponent(password));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(newPasswordLabel)
                .addComponent(newPassword));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(changeButton));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(messaging));
        layout.setVerticalGroup(vGroup);

        return displayPanel;
    }

    /**
     * Handles events on the UI
     */
    private class ButtonListener implements ActionListener {

        /**
         * Any action is performed
         */
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == changeButton) {
                changePasswordPressed();
            }
        }

        /**
         * Create user
         */
        private void changePasswordPressed() {
            String passwordText = password.getText();
            String newPasswordText = newPassword.getText();

            // Check correct password
            if (Hashing.compareHashPass(loggedInUser.getSalt(), passwordText, loggedInUser.getPassword())) {
                loggedInUser.changePassword(newPasswordText);

                String output = "";
                output = data.editPassword(loggedInUser);

                messaging.setText(output);
            } else {
                messaging.setText("Incorrect password.");
            }
        }
    }

    /**
     * Closes the window
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    }
}
