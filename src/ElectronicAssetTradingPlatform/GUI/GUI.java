package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Exceptions.EmptyFieldException;
import ElectronicAssetTradingPlatform.GUI.ITAdmin.ITAdminGUI;
import ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader.*;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Login page GUI of the client
 */
public class GUI extends JFrame {
    private JTextField username;
    private JTextField password;
    private JTextArea messaging;
    private JButton loginBtn;
    private NetworkDataSource data;

    public GUI(NetworkDataSource data) {
        // Get server connection
        this.data = data;

        // Initialise
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(loginForm());

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        loginBtn.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Login");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    /**
     * Validates the input is empty or just spaces
     * @param str Input text field value
     * @throws EmptyFieldException Throws exception if empty or just spaces
     */
    public static void checkInputEmpty(String str) throws EmptyFieldException {
        if (str == null || str.isBlank()) throw new EmptyFieldException("Invalid input");
    }

    /**
     * Makes the Create User form
     * @return JPanel of form
     */
    public JPanel loginForm() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel usernameLabel = new JLabel("Username");
        JLabel userTypeLabel = new JLabel("Password");

        username = new JTextField(20);
        password = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        loginBtn = new JButton("Login");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(usernameLabel)
                .addComponent(userTypeLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(username)
                .addComponent(password)
                .addComponent(loginBtn, Alignment.CENTER)
                .addComponent(messaging, Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(usernameLabel)
                .addComponent(username));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(userTypeLabel)
                .addComponent(password));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(loginBtn));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(messaging));
        layout.setVerticalGroup(vGroup);

        return panel;
    }


    public void iTAdminGUI(ITAdmin user) {
        new ITAdminGUI(user, data);
    }

    public void memberGUI(OrganisationalUnitMembers member) {
        new OrgUnitMemGUI(member, data);
    }

    public void memberGUI(OrganisationalUnitLeader member) {
        new OrgUnitMemGUI(member, data);
    }

    public void systemAdminGUI(SystemsAdmin user) {
        new SystemsAdminGUI(user);
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
            if (source == loginBtn) {
                loginPressed();
            }
        }

        /**
         * Create user
         */
        private void loginPressed() {
            String usernameIn = username.getText();
            String passwordIn = password.getText();

            // Check inputs are not null
            String output = "";
            User user = null;
            try {
                checkInputEmpty(usernameIn);
                checkInputEmpty(passwordIn);
                user = data.retrieveUser(usernameIn);
            } catch (EmptyFieldException | DatabaseException e) {
                // Empty input error
                output = "Username is invalid.";
            }

            if (user == null) output = "Username is invalid.";

            // Check input password is stored password
            if (user != null && Hashing.compareHashPass(user.getSalt(), passwordIn, user.getPassword())) {
                // Successful login
                messaging.setText("Success! Logging in...");

                // Opens appropriate GUI
                User finalUser = user;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        switch (UsersFactory.UserType.valueOf(finalUser.getUserType())) {
                            case ITAdmin -> iTAdminGUI((ITAdmin) finalUser);
                            case SystemsAdmin -> systemAdminGUI((SystemsAdmin) finalUser);
                            case OrganisationalUnitLeader -> memberGUI((OrganisationalUnitLeader) finalUser);
                            case OrganisationalUnitMembers -> memberGUI((OrganisationalUnitMembers) finalUser);
                        }
                    }
                });

                // 'Close' this window
                dispose();
            } else {
                if (output == "") output = "Login error";
                messaging.setText(output);
            }
        }
    }

    /**
     * Closes the app
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    /**
     * Starts the application
     * @param args inputs
     */
    public static void main(String[] args) {
        // Starts with login
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkDataSource net = new NetworkDataSource();
                net.run();
                new GUI(net);
            }
        });
    }
}
