package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;

import javax.swing.*;
import java.awt.*;

public class CommonComponents {
    public static void displayDBUser(String username, NetworkDataSource dataSource) {
        try {
            User outUser = dataSource.retrieveUser(username);

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
