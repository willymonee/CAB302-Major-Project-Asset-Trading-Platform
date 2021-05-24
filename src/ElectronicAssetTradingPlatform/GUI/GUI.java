package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Users.UsersFactory;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame {
    private JTextField username;
    private JTextField password;
    private JTextArea messaging;
    private JButton loginBtn;
    private NetworkDataSource data;

    public GUI(NetworkDataSource data) {
        // Get server connection
        this.data = data;

        // Intialise
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(makeCreateUserPanel());

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        loginBtn.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Login");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    public static void loginForm() {
        // Get input from form

    }

    /**
     * Makes the Create User form
     * @return JPanel of form
     */
    public JPanel makeCreateUserPanel() {
        JPanel addressPanel = new JPanel();
        GroupLayout layout = new GroupLayout(addressPanel);
        addressPanel.setLayout(layout);

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

        return addressPanel;
    }


    public static void createITAdminGUI() {

    }
    public static void memberGUI() {

    }
    public static void leaderGUI() {

    }
    public static void systemAdminGUI() {

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
                User.checkInputEmpty(usernameIn);
                User.checkInputEmpty(passwordIn);
                user = data.retrieveUser(usernameIn);
            } catch (User.EmptyFieldException | NetworkDataSource.DatabaseException e) {
                // Empty input error
                output = "Username is invalid.";
            }

            if (user == null) output = "Username is invalid.";

            // Check input password is stored password
            if (user != null && Hashing.compareHashPass(user.getSalt(), passwordIn, user.getPassword())) {
                // Successful login
                messaging.setText("Success! Logging in...");

                // Opens appropriate GUI
                ITAdmin finalUser = (ITAdmin) user;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new ITAdminGUI( finalUser, data);
                    }
                });

                // 'Close' this window
                dispose();
            } else {
                messaging.setText("Login error");
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
}
