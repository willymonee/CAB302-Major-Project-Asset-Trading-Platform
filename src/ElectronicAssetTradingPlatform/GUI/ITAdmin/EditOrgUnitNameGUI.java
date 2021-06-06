package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.EmptyFieldException;
import ElectronicAssetTradingPlatform.GUI.GUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI for editing the organisational unit's name
 */
class EditOrgUnitNameGUI extends JFrame {
    private final ITAdmin loggedInUser;
    private final NetworkDataSource data;
    private JTextField unitName;
    private JTextField newUnitName;
    private JTextArea messaging;
    private JButton editOrgUnitNameButton;

    public EditOrgUnitNameGUI(ITAdmin loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        editOrgUnitNameButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("EDIT Organisational Unit Name");
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
        contentPane.add(editOrgUnitNamePanel());
    }

    /**
     * Makes the Edit User form
     *
     * @return JPanel of edit user form
     */
    private JPanel editOrgUnitNamePanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel unitNameLabel = new JLabel("Unit Name");
        JLabel newUnitNameLabel = new JLabel("New Name");

        unitName = new JTextField(20);
        newUnitName = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        editOrgUnitNameButton = new JButton("Edit");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitNameLabel)
                .addComponent(newUnitNameLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitName)
                .addComponent(newUnitName)
                .addComponent(editOrgUnitNameButton, GroupLayout.Alignment.CENTER)
                .addComponent(messaging, GroupLayout.Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(unitNameLabel)
                .addComponent(unitName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(newUnitNameLabel)
                .addComponent(newUnitName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(editOrgUnitNameButton));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
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
            if (source == editOrgUnitNameButton) {
                editOrgUnitNamePressed();
            }
        }

        /**
         * Create user
         */
        private void editOrgUnitNamePressed() {
            String unitNameIn = unitName.getText();
            String newUnitNameIn = newUnitName.getText();

            String oldUnitName;
            OrganisationalUnit outOrgUnit;

            // Check inputs are not null
            String output = "";
            try {
                GUI.checkInputEmpty(unitNameIn);
                GUI.checkInputEmpty(newUnitNameIn);

                OrganisationalUnit orgUnitToBeEdited = data.retrieveOrgUnit(unitNameIn);
                oldUnitName = orgUnitToBeEdited.getUnitName();

                outOrgUnit = loggedInUser.editOrganisationalUnitName(orgUnitToBeEdited, newUnitNameIn);


                output = data.editOrgUnitName(outOrgUnit, oldUnitName);
            }
            catch (DatabaseException e) {
                output = e.getMessage();
            } catch (EmptyFieldException e) {
                // Empty input error
                output = "Input is empty or invalid, please enter correct details into all fields.";
            }

            messaging.setText(output);
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
