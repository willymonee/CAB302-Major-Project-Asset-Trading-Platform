package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
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

class CreateAssetGUI extends JFrame {
    private final ITAdmin loggedInUser;
    private final NetworkDataSource data;
    private JTextField assetName;
    private JTextArea messaging;
    private JButton createAssetButton;

    public CreateAssetGUI(ITAdmin loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        initUI();

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        createAssetButton.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("CREATE Asset");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    private void initUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(createAssetPanel());
    }

    public JPanel createAssetPanel() {
        JPanel displayPanel = new JPanel();
        GroupLayout layout = new GroupLayout(displayPanel);
        displayPanel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        JLabel assetNameLabel = new JLabel("Asset Name");

        assetName = new JTextField(20);
        messaging = new JTextArea();
        messaging.setEditable(false);
        messaging.setLineWrap(true);
        messaging.setWrapStyleWord(true);
        createAssetButton = new JButton("Create");

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(assetNameLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(assetName)
                .addComponent(createAssetButton, GroupLayout.Alignment.CENTER)
                .addComponent(messaging, GroupLayout.Alignment.CENTER));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(assetNameLabel)
                .addComponent(assetName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(createAssetButton));
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
            if (source == createAssetButton) {
                createOrgUnitPressed();
            }
        }

        /**
         * Create user
         */
        private void createOrgUnitPressed() {
            String assetNameIn = assetName.getText();

            // Check inputs are not null
            String output = "";
            try {
                GUI.checkInputEmpty(assetNameIn);

                Asset asset = loggedInUser.createNewAsset(assetNameIn);
                output = data.storeAsset(asset);
            }
            catch (EmptyFieldException e) {
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
