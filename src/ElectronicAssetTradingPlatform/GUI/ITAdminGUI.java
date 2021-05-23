package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ITAdminGUI extends JFrame {
    private JTextField username;
    private JTextField userType;
    private JTextField unitName;
    private JButton createButton;

    /**
     * Constructor sets up user interface, adds listeners and displays.
     */
    public ITAdminGUI() {
        initCreateUser();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());

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
        userType = new JTextField(20);
        unitName = new JTextField(20);
        createButton = new JButton("Create");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup().addComponent(usernameLabel)
                .addComponent(userTypeLabel).addComponent(unitNameLabel));
        hGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(username)
                .addComponent(userType).addComponent(unitName).addComponent(createButton));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
                .addComponent(usernameLabel).addComponent(username));
        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
                .addComponent(userTypeLabel).addComponent(userType));
        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
                .addComponent(unitNameLabel).addComponent(unitName));
        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
                .addComponent(unitNameLabel).addComponent(createButton));
        layout.setVerticalGroup(vGroup);

        return addressPanel;
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
                new ITAdminGUI();
            }
        });
    }
}
