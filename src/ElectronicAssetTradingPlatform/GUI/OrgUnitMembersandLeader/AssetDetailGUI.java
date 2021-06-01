package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class AssetDetailGUI extends JFrame {
    private final OrganisationalUnitMembers loggedInUser;
    private final NetworkDataSource dataSource;
    private final Asset selectedAsset;

    public AssetDetailGUI(OrganisationalUnitMembers loggedInUser, NetworkDataSource data, Asset selectedAsset) {
        this.loggedInUser = loggedInUser;
        this.dataSource = data;
        this.selectedAsset = selectedAsset;

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

        List<List<Object>> data;
        // {Date date, float price}
        try {
            data = dataSource.getAssetHistory(selectedAsset.getAssetName());
        } catch (DatabaseException e) {
            data = new ArrayList<>();
        }

        if (data.size() > 1) {
            // Sort by date
            data.sort(Comparator.comparingLong(a -> ((Date) a.get(0)).getTime()));
            Object[][] dataArrayIn = new Object[data.size()][];
            int count = 0;
            for (List<Object> row : data) {
                dataArrayIn[count] = row.toArray();
                count++;
            }

            MarketplaceHistoryGraph graph = new MarketplaceHistoryGraph(dataArrayIn, 400, 300);

            JPanel graphContainer = new JPanel();
            graphContainer.add(graph);
            graphContainer.setBorder(new EmptyBorder(20, -10, 10, -10));

            JLabel title = new JLabel("Asset Price History", SwingConstants.CENTER);
            panel.add(title, BorderLayout.NORTH);
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
        // TODO: remove main done
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NetworkDataSource net = new NetworkDataSource();
                net.run();
                new AssetDetailGUI(new OrganisationalUnitMembers("a", "a", "a", "a"), net, new Asset("Table"));
            }
        });
    }
}
