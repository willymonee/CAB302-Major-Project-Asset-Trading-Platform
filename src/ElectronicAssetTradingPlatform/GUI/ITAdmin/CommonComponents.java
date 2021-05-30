package ElectronicAssetTradingPlatform.GUI.ITAdmin;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
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
            table.setPreferredScrollableViewportSize(new Dimension(450, table.getRowHeight()));
            JScrollPane tablePane = new JScrollPane(table);
            JLabel label = new JLabel("Showing user \'" + outUser.getUsername() + "\' from database");

            Container pane = new Container();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            pane.add(label);
            pane.add(tablePane);

            JOptionPane.showMessageDialog(null, pane);
        } catch (DatabaseException ignore) {}
    }
}
