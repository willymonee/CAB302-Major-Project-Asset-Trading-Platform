package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Users.UsersFactory;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ITAdminGUI extends JFrame {
    private JTextField username;
    private JComboBox userType;
    private JTextField unitName;
    private JTextArea messaging;
    private JButton createUserButton;

    ITAdmin loggedInUser;

    /**
     * Constructor sets up user interface, adds listeners and displays.
     */
    public ITAdminGUI(ITAdmin user) {
        loggedInUser = user;

        initCreateUser();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        createUserButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Welcome, ITAdmin");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    /**
     * Initialise UI
     */
    private void initCreateUser() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(makeCreateUserPanel());
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

        return addressPanel;
    }

    /**
     * Handles events for the three buttons on the UI.
     *
     * @author Malcolm Corney
     * @version $Id: Exp $
     *
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
         * When the save button is pressed, check that the name field contains
         * something. If it does, create a new Person object and attempt to add it
         * to the data model. Change the fields back to not editable and make the
         * save button inactive.
         *
         * Check the list size to see if the delete button should be enabled.
         */
        private void createUserPressed() {
            String usernameIn = username.getText();
            String unitNameIn = unitName.getText();
            String userTypeIn = userType.getSelectedItem().toString();

            // Check inputs are not null
            String output = "";
            try {
                ITAdmin.checkInputEmpty(usernameIn);
                ITAdmin.checkInputEmpty(unitNameIn);
                ITAdmin.checkInputEmpty(userTypeIn);
                User user = loggedInUser.createUser(usernameIn, unitNameIn, userTypeIn);
                output = NetworkDataSource.storeUser(user);
            } catch (User.EmptyFieldException | User.UserTypeException e) {
                // Empty input error
                output = "Input is empty or invalid, please enter correct details into all fields.";
            }

            messaging.setText(output);
        }
    }

    /**
     * Implements the windowClosing method from WindowAdapter/WindowListener to
     * persist the contents of the data/model.
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    /**
     * Testing only
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ITAdminGUI(new ITAdmin("test", "test", "test"));
            }
        });
    }
}
