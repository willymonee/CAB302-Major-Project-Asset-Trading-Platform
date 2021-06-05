package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.UnitFactory;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.EmptyFieldException;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Exceptions.MissingAssetException;
import ElectronicAssetTradingPlatform.GUI.GUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

class EditOrgAssetsGUI extends JFrame {
    private final ITAdmin loggedInUser;
    private final NetworkDataSource data;
    private JTextField unitName;
    private JTextField assetName;
    private JComboBox editType;
    private JTextField amount;
    private JTextArea messaging;
    private JButton editOrgUnitAssetsButton;

    public EditOrgAssetsGUI(ITAdmin loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        editOrgUnitAssetsButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("EDIT Organisational Assets");
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
        contentPane.add(editOrgUnitAssetsPanel());
    }

    /**
     * Makes the Edit User form
     *
     * @return JPanel of edit user form
     */
    private JPanel editOrgUnitAssetsPanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel unitNameLabel = new JLabel("Unit Name");
        JLabel assetNameLabel = new JLabel("Asset Name");
        JLabel editTypeLabel = new JLabel("Edit Type");
        JLabel amountLabel = new JLabel("Amount");

        unitName = new JTextField(20);
        assetName = new JTextField(20);
        editType = new JComboBox(UnitFactory.EditAssetType.values());
        amount = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        editOrgUnitAssetsButton = new JButton("Edit");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitNameLabel)
                .addComponent(assetNameLabel)
                .addComponent(editTypeLabel)
                .addComponent(amountLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitName)
                .addComponent(assetName)
                .addComponent(editType)
                .addComponent(amount)
                .addComponent(editOrgUnitAssetsButton, GroupLayout.Alignment.CENTER)
                .addComponent(messaging, GroupLayout.Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(unitNameLabel)
                .addComponent(unitName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(assetNameLabel)
                .addComponent(assetName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(editTypeLabel)
                .addComponent(editType));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(amountLabel)
                .addComponent(amount));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(editOrgUnitAssetsButton));
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
            if (source == editOrgUnitAssetsButton) {
                editOrgUnitAssetsPressed();
            }
        }

        /**
         * Create user
         */
        private void editOrgUnitAssetsPressed() {
            String unitNameIn = unitName.getText();
            String assetNameIn = assetName.getText();
            String amountIn = amount.getText();
            int amountInToInteger;
            UnitFactory.EditAssetType type = (UnitFactory.EditAssetType)  editType.getSelectedItem();
            String editTypeIn = Objects.requireNonNull(type).toString();

            OrganisationalUnit outOrgUnit = null;

            // Check inputs are not null
            String output = "";
            try {
                GUI.checkInputEmpty(unitNameIn);
                GUI.checkInputEmpty(assetNameIn);
                GUI.checkInputEmpty(editTypeIn);
                GUI.checkInputEmpty(amountIn);

                amountInToInteger = Integer.parseInt(amountIn);

                OrganisationalUnit orgUnitToBeEdited = data.retrieveOrgUnit(unitNameIn);

                if (type == UnitFactory.EditAssetType.addAssets) {
                    outOrgUnit = loggedInUser.addOrganisationalUnitAssets(orgUnitToBeEdited, assetNameIn, amountInToInteger);
                }
                if (type == UnitFactory.EditAssetType.removeAssets) {
                    outOrgUnit = loggedInUser.removeOrganisationalUnitAssets(orgUnitToBeEdited, assetNameIn, amountInToInteger);
                }

                output = data.setOrgUnitAssets(outOrgUnit, assetNameIn, outOrgUnit.getAssetQuantity(assetNameIn));

            }
            catch (DatabaseException e) {
                output = e.getMessage();
            } catch (EmptyFieldException e) {
                // Empty input error
                output = "Input is empty or invalid, please enter correct details into all fields.";
            }
            catch (NumberFormatException e) {
                // Wrong input error
                output = "Asset input is invalid, please enter correct input format into credits field.";
            } catch (LessThanZeroException e) {
                // LessThanZero input error
                output = "Cannot remove more assets than owned, please enter a valid credit amount. ";
            } catch (MissingAssetException e) {
                // Wrong asset input error
                output = "The asset specified is not owned by the unit or does not exist, please input a valid asset. ";
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
