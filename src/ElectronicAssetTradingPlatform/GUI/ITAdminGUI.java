package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Users.UsersFactory;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class ITAdminGUI extends JFrame {
    ITAdmin loggedInUser;
    NetworkDataSource data;

    private JButton goToCreateUser;
    private JButton goToEditOrgAssets;
    private JButton goToCreateOrgUnit;
    private JButton goToEditOrgCredits;
    private JButton goToCreateAsset;
    private JButton goToEditAssetName;
    private JButton goToEditUser;
    private JButton goToEditOrgUnitName;
    private JButton goToChangePassword;

    /**
     * Creates IT Admin Main Menu
     * @param user the logged in IT Admin
     * @param dataSource the server connection
     */
    public ITAdminGUI(ITAdmin user, NetworkDataSource dataSource) {
        data = dataSource;
        loggedInUser = user;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        goToCreateUser.addActionListener(new ButtonListener());
        goToEditOrgAssets.addActionListener(new ButtonListener());
        goToCreateOrgUnit.addActionListener(new ButtonListener());
        goToEditOrgCredits.addActionListener(new ButtonListener());
        goToCreateAsset.addActionListener(new ButtonListener());
        goToEditAssetName.addActionListener(new ButtonListener());
        goToEditUser.addActionListener(new ButtonListener());
        goToEditOrgUnitName.addActionListener(new ButtonListener());
        goToChangePassword.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Welcome, ITAdmin");
        setMinimumSize(new Dimension(600, 400));
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
        contentPane.add(goToButtonsPanel());
    }

    /**
     * Makes the Navigation menu
     *
     * @return JPanel of edit user form
     */
    private JPanel goToButtonsPanel() {
        JPanel displayPanel = new JPanel();
        // Create a 4x2 grid layout
        GridLayout layout = new GridLayout(0,2);
        displayPanel.setLayout(layout);

        goToCreateAsset = new JButton("CREATE Asset");
        goToCreateOrgUnit = new JButton("CREATE Organisational Unit");
        goToCreateUser = new JButton("CREATE User");
        goToEditAssetName = new JButton("EDIT Existing Asset Name");
        goToEditOrgAssets = new JButton("EDIT Organisational Unit's Assets");
        goToEditOrgCredits = new JButton("EDIT Organisational Unit's Credits");
        goToEditOrgUnitName = new JButton("EDIT Organisational Unit's Name");
        goToEditUser = new JButton("EDIT Existing User");
        goToChangePassword = new JButton("Change my password");


        displayPanel.add(goToCreateUser);
        displayPanel.add(goToEditOrgUnitName);
        displayPanel.add(goToCreateOrgUnit);
        displayPanel.add(goToEditOrgCredits);
        displayPanel.add(goToCreateAsset);
        displayPanel.add(goToEditAssetName);
        displayPanel.add(goToEditUser);
        displayPanel.add(goToEditOrgAssets);
        displayPanel.add(goToChangePassword);

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
            if (goToCreateAsset.equals(source)) {
                new CreateAssetGUI();
            } else if (goToCreateOrgUnit.equals(source)) {
                new CreateOrgUnitGUI();
            } else if (goToCreateUser.equals(source)) {
                new CreateUserGUI();
            } else if (goToEditAssetName.equals(source)) {
                new EditAssetNameGUI();
            } else if (goToEditOrgAssets.equals(source)) {
                new EditOrgAssetsGUI();
            } else if (goToEditOrgCredits.equals(source)) {
                new EditOrgCreditsGUI();
            } else if (goToEditOrgUnitName.equals(source)) {
                new EditOrgUnitNameGUI();
            } else if (goToEditUser.equals(source)) {
                new EditUserGUI();
            } else if (goToChangePassword.equals(source)) {
                new ChangePasswordGUI(data, loggedInUser);
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

    private class EditUserGUI extends JFrame {
        private JTextField username;
        private JComboBox userType;
        private JTextField unitName;
        private JTextArea messaging;
        private JButton editUserButton;

        /**
         * Constructor sets up user interface, adds listeners and displays.
         */
        public EditUserGUI() {
            initUI();

            // add listeners to interactive components
            addWindowListener(new ClosingListener());
            editUserButton.addActionListener(new ButtonListener());

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

            JLabel usernameLabel = new JLabel("Username");
            JLabel userTypeLabel = new JLabel("User Type");
            JLabel unitNameLabel = new JLabel("Unit Name");

            username = new JTextField(20);
            userType = new JComboBox(UsersFactory.UserType.values());
            unitName = new JTextField(20);
            messaging = new JTextArea();
            messaging.setEditable(false);
            messaging.setLineWrap(true);
            messaging.setWrapStyleWord(true);
            editUserButton = new JButton("Edit");

            // Create a sequential group for the horizontal axis.
            GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(usernameLabel)
                    .addComponent(userTypeLabel)
                    .addComponent(unitNameLabel));
            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(username)
                    .addComponent(userType)
                    .addComponent(unitName)
                    .addComponent(editUserButton, Alignment.CENTER)
                    .addComponent(messaging, Alignment.CENTER));
            layout.setHorizontalGroup(hGroup);

            // Create a sequential group for the vertical axis.
            GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(username));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(userTypeLabel)
                    .addComponent(userType));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(unitNameLabel)
                    .addComponent(unitName));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(editUserButton));
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
                if (source == editUserButton) {
                    editUserPressed();
                }
            }

            /**
             * Create user
             */
            private void editUserPressed() {
                String usernameIn = username.getText();
                String unitNameIn = unitName.getText();
                String userTypeIn = Objects.requireNonNull(userType.getSelectedItem()).toString();

                // Check inputs are not null
                String output = "";
                try {
                    ITAdmin.checkInputEmpty(usernameIn);
                    ITAdmin.checkInputEmpty(userTypeIn);

                    String[] params = loggedInUser.editUser(usernameIn, userTypeIn, unitNameIn);

                    output = data.editUser(params[0], params[1], params[2]);
                } catch (User.EmptyFieldException | User.UserTypeException e) {
                    // Empty input error
                    output = "Input is empty or invalid, please enter correct details into all fields.";
                }

                messaging.setText(output);

                displayDBUser(usernameIn);
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

    private class CreateUserGUI extends JFrame {
        private JTextField username;
        private JComboBox userType;
        private JTextField unitName;
        private JTextArea messaging;
        private JButton createUserButton;

        /**
         * Constructor sets up user interface, adds listeners and displays.
         */
        public CreateUserGUI() {
            initUI();

            // add listeners to interactive components
            addWindowListener(new ClosingListener());
            createUserButton.addActionListener(new ButtonListener());

            // decorate the frame and make it visible
            setTitle("CREATE User");
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
            contentPane.add(createUserPanel());
        }

        /**
         * Makes the Create User form
         *
         * @return JPanel of create form
         */
        public JPanel createUserPanel() {
            JPanel displayPanel = new JPanel();
            GroupLayout layout = new GroupLayout(displayPanel);
            displayPanel.setLayout(layout);

            // Turn on automatically adding gaps between components
            layout.setAutoCreateGaps(true);

            // Turn on automatically creating gaps between components that touch
            // the edge of the container and the container.
            layout.setAutoCreateContainerGaps(true);

            JLabel usernameLabel = new JLabel("Username");
            JLabel userTypeLabel = new JLabel("User Type");
            JLabel unitNameLabel = new JLabel("Unit Name");

            username = new JTextField(20);
            userType = new JComboBox(UsersFactory.UserType.values());
            unitName = new JTextField(20);
            messaging = new JTextArea();
            messaging.setEditable(false);
            messaging.setLineWrap(true);
            messaging.setWrapStyleWord(true);
            createUserButton = new JButton("Create");

            // Create a sequential group for the horizontal axis.
            GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(usernameLabel)
                    .addComponent(userTypeLabel)
                    .addComponent(unitNameLabel));
            hGroup.addGroup(layout.createParallelGroup()
                    .addComponent(username)
                    .addComponent(userType)
                    .addComponent(unitName)
                    .addComponent(createUserButton, Alignment.CENTER)
                    .addComponent(messaging, Alignment.CENTER));
            layout.setHorizontalGroup(hGroup);

            // Create a sequential group for the vertical axis.
            GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(username));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(userTypeLabel)
                    .addComponent(userType));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(unitNameLabel)
                    .addComponent(unitName));
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(createUserButton));
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
                if (source == createUserButton) {
                    createUserPressed();
                }
            }

            /**
             * Create user
             */
            private void createUserPressed() {
                String usernameIn = username.getText();
                String unitNameIn = unitName.getText();
                String userTypeIn = Objects.requireNonNull(userType.getSelectedItem()).toString();

                // Check inputs are not null
                String output = "";
                try {
                    ITAdmin.checkInputEmpty(usernameIn);
                    ITAdmin.checkInputEmpty(userTypeIn);

                    User user = loggedInUser.createUser(usernameIn, unitNameIn, userTypeIn);
                    output = data.storeUser(user);
                } catch (User.EmptyFieldException | User.UserTypeException e) {
                    // Empty input error
                    output = "Input is empty or invalid, please enter correct details into all fields.";
                }

                messaging.setText(output);

                displayDBUser(usernameIn);
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

    private class CreateAssetGUI extends JFrame {}
    private class CreateOrgUnitGUI extends JFrame {}
    private class EditAssetNameGUI extends JFrame {}
    private class EditOrgAssetsGUI extends JFrame {}
    private class EditOrgCreditsGUI extends JFrame {}
    private class EditOrgUnitNameGUI extends JFrame {}

    private void displayDBUser(String username) {
        try {
            User outUser = data.retrieveUser(username);

            // Attempt to get unit name
            String outUserUnitName = "";
            try {
                outUserUnitName = ((OrganisationalUnitMembers) outUser).getUnitName();
            } catch (ClassCastException ignored) {}

            // Popup the newly created user
            String[] columnNames = {"Username", "User Type", "Unit Name"};
            Object[][] data = {
                    {outUser.getUsername(), outUser.getUserType(), outUserUnitName}
            };

            JTable table = new JTable(data, columnNames);
            JLabel label = new JLabel("Showing user \'" + outUser.getUsername() + "\' from database");

            Container pane = new Container();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.add(label);
            pane.add(table);

            JOptionPane.showMessageDialog(null, pane);
        } catch (NetworkDataSource.DatabaseException ignore) {}
    }
}
