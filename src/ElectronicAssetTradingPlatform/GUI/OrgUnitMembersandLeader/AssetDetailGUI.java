package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class AssetDetailGUI extends JFrame {
    private final OrganisationalUnitMembers loggedInUser;
    private final NetworkDataSource data;

    public AssetDetailGUI(OrganisationalUnitMembers loggedInUser, NetworkDataSource data) {
        this.loggedInUser = loggedInUser;
        this.data = data;

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(makeMarketPlaceHistoryTable());

        // Decorate the frame and make it visible
        setTitle("Asset Detail");
        setMinimumSize(new Dimension(400, 300));
        pack();
        setVisible(true);
    }

    private JPanel makeMarketPlaceHistoryTable() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Buyer", "Seller", "Price Per Unit", "Quantity", "Date"};
        Object[][] data = {
                {"Buyer2", "Seller4", (float)1, 10, new Date(System.currentTimeMillis()-900000000)},
                {"Buyer1", "Seller1", (float)1.2, 5, new Date(System.currentTimeMillis()-2000000000)},
                {"Buyer4", "Seller2", (float)1.3, 2, new Date(System.currentTimeMillis()-1000000000)},
                {"Buyer2", "Seller3", (float)1.4, 5, new Date(System.currentTimeMillis()-1300000000)},
                {"Buyer2", "Seller2", (float)1, 7, new Date(System.currentTimeMillis()-2000000000)},
                {"Buyer3", "Seller2", (float)1.3, 12, new Date(System.currentTimeMillis()-1500000000)},
                {"Buyer2", "Seller4", (float)1.2, 10, new Date(System.currentTimeMillis()-1000000000)}
        };

        if (data.length > 1) {
            // Sort by date
            Arrays.sort(data, Comparator.comparingLong(a -> ((Date) a[4]).getTime()));

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setPreferredScrollableViewportSize(new Dimension(
                    500,
                    table.getRowHeight() * data.length)
            );
            JScrollPane tablePane = new JScrollPane(table);

            MarketplaceHistoryGraph graph = new MarketplaceHistoryGraph(data, 400, 300);

            JPanel graphContainer = new JPanel();
            graphContainer.add(graph);
            graphContainer.setBorder(new EmptyBorder(20, -10, 10, -10));

            JLabel title = new JLabel("Asset Price History", SwingConstants.CENTER);
            panel.add(title, BorderLayout.NORTH);
            panel.add(tablePane, BorderLayout.SOUTH);
            panel.add(graphContainer, BorderLayout.CENTER);
        } else {
            JLabel title = new JLabel("Asset Price History", SwingConstants.CENTER);
            panel.add(title, BorderLayout.NORTH);
            JLabel text = new JLabel("Data not found");
            JPanel textContainer = new JPanel();
            textContainer.add(text);
            textContainer.setBorder(new EmptyBorder(20, -10, 10, -10));
            panel.add(textContainer, BorderLayout.CENTER);
        }

        return panel;
    }

    /**
     * Closes the window
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { dispose(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NetworkDataSource net = new NetworkDataSource();
                net.run();
                new AssetDetailGUI(new OrganisationalUnitMembers("a", "a", "a", "a"), net);
            }
        });
    }
}
