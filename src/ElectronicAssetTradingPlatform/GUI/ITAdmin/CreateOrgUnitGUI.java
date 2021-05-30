package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
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

class CreateOrgUnitGUI extends JFrame {
    private final ITAdmin loggedInUser;
    private final NetworkDataSource data;
    private JTextField unitName;
    private JTextField credits;
    private JTextArea messaging;
    private JButton createOrgUnitButton;

    public CreateOrgUnitGUI(ITAdmin loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        createOrgUnitButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("CREATE Organisational Unit");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    private void initUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(createOrgUnitPanel());
    }

    public JPanel createOrgUnitPanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel unitNameLabel = new JLabel("Organisational Unit Name");
        JLabel creditsLabel = new JLabel("Credits");

        unitName = new JTextField(20);
        credits = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        createOrgUnitButton = new JButton("Create");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitNameLabel)
                .addComponent(creditsLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(unitName)
                .addComponent(credits)
                .addComponent(createOrgUnitButton, GroupLayout.Alignment.CENTER)
                .addComponent(messaging, GroupLayout.Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(unitNameLabel)
                .addComponent(unitName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(creditsLabel)
                .addComponent(credits));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(createOrgUnitButton));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(messaging));
        layout.setVerticalGroup(vGroup);

        return displayPanel;
    }

    private class ButtonListener implements ActionListener {

        /**
         * Any action is performed
         */
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == createOrgUnitButton) {
                createOrgUnitPressed();
            }
        }

        /**
         * Create user
         */
        private void createOrgUnitPressed() {
            String unitNameIn = unitName.getText();
            String creditsIn = credits.getText(); // store credits data
            float creditsInToFloat; // initialise float

            // Check inputs are not null
            String output = "";
            try {
                GUI.checkInputEmpty(unitNameIn);
                GUI.checkInputEmpty(creditsIn);

                creditsInToFloat = Float.parseFloat(creditsIn); // parse credits data to float

                OrganisationalUnit orgUnit = loggedInUser.createOrganisationalUnit(unitNameIn, creditsInToFloat);
                output = data.storeOrgUnit(orgUnit);
            }
            catch (EmptyFieldException e) {
                // Empty input error
                output = "Input is empty or invalid, please enter correct details into all fields.";
            }
            catch (NumberFormatException e) {
                // Empty input error
                output = "Credits input is invalid, please enter correct input format into credits field.";
            }

            messaging.setText(output);

            // CommonComponents.displayDBUser(unitNameIn, data);
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

