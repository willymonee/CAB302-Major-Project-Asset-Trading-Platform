package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
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

class EditAssetNameGUI extends JFrame {
    private final ITAdmin loggedInUser;
    private final NetworkDataSource data;
    private JTextField assetName;
    private JTextField newAssetName;
    private JTextArea messaging;
    private JButton editAssetNameButton;

    public EditAssetNameGUI(ITAdmin loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        editAssetNameButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("EDIT Asset Name");
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
        contentPane.add(editAssetNamePanel());
    }

    /**
     * Makes the Edit User form
     *
     * @return JPanel of edit user form
     */
    private JPanel editAssetNamePanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel assetNameLabel = new JLabel("Asset Name");
        JLabel newAssetNameLabel = new JLabel("New Name");

        assetName = new JTextField(20);
        newAssetName = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        editAssetNameButton = new JButton("Edit");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(assetNameLabel)
                .addComponent(newAssetNameLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(assetName)
                .addComponent(newAssetName)
                .addComponent(editAssetNameButton, GroupLayout.Alignment.CENTER)
                .addComponent(messaging, GroupLayout.Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(assetNameLabel)
                .addComponent(assetName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(newAssetNameLabel)
                .addComponent(newAssetName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(editAssetNameButton));
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
            if (source == editAssetNameButton) {
                editAssetNamePressed();
            }
        }

        /**
         * Create user
         */
        private void editAssetNamePressed() {
            String assetNameIn = assetName.getText();
            String newAssetNameIn = newAssetName.getText();

            String oldAssetName;
            Asset outAsset;

            // Check inputs are not null
            String output = "";
            try {
                GUI.checkInputEmpty(assetNameIn);
                GUI.checkInputEmpty(newAssetNameIn);

                Asset assetToBeEdited = data.retrieveAsset(assetNameIn);

                oldAssetName = assetToBeEdited.getAssetName();

                outAsset = loggedInUser.editAssetName(assetToBeEdited, newAssetNameIn);


                output = data.editAssetName(outAsset, oldAssetName);
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
