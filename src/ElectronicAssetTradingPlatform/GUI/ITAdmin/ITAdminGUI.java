package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.GUI.ChangePasswordGUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Landing page GUI for the IT Admin
 */
public class ITAdminGUI extends JFrame {
    private ITAdmin loggedInUser;
    private NetworkDataSource data;

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
                new CreateAssetGUI(loggedInUser, data);
            } else if (goToCreateOrgUnit.equals(source)) {
                new CreateOrgUnitGUI(loggedInUser, data);
            } else if (goToCreateUser.equals(source)) {
                new CreateUserGUI(loggedInUser, data);
            } else if (goToEditAssetName.equals(source)) {
                new EditAssetNameGUI(loggedInUser, data);
            } else if (goToEditOrgAssets.equals(source)) {
                new EditOrgAssetsGUI(loggedInUser, data);
            } else if (goToEditOrgCredits.equals(source)) {
                new EditOrgCreditsGUI(loggedInUser, data);
            } else if (goToEditOrgUnitName.equals(source)) {
                new EditOrgUnitNameGUI(loggedInUser, data);
            } else if (goToEditUser.equals(source)) {
                new EditUserGUI(loggedInUser, data);
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
}
