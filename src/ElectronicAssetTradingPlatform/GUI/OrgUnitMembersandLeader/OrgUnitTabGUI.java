package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.TradeHistory;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class OrgUnitTabGUI extends JPanel {
    private NetworkDataSource dataSource;
    private OrganisationalUnitMembers member;

    public OrgUnitTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
        this.member = member;
        this.dataSource = dataSource;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String creditsText = member.getUnitName() + "\'s Credits: ";
        try {
            creditsText += member.getUnitCredits(dataSource);
        } catch (DatabaseException e) {
            creditsText += "Not found";
        }

        JLabel unitCreditsLabel = Helper.createLabel(creditsText, 16);
        unitCreditsLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        unitCreditsLabel.setHorizontalAlignment(JLabel.RIGHT);
        this.add(unitCreditsLabel);

        JLabel tradeHistoryLabel = Helper.createLabel(member.getUnitName() + "\'s Trade History", 20);
        tradeHistoryLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(tradeHistoryLabel);
        this.add(unitTradeHistoryTable());

        JLabel unitAssetsLabel = Helper.createLabel(member.getUnitName() + "\'s Assets", 20);
        unitAssetsLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        unitAssetsLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(unitAssetsLabel);
        this.add(unitAssetTable());

        JLabel unitMembersLabel = Helper.createLabel(member.getUnitName() + "\'s Members", 20);
        unitMembersLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        unitMembersLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(unitMembersLabel);
        this.add(unitMemberTable());
    }

    public JPanel unitTradeHistoryTable() {
        String[] columnNames = {"Name", "Quantity", "Price", "Total", "Trade Date", "Created by", "To/From"};

        // TODO: replace mock (db) with server data  (MarketplaceHistoryDataSource.getUnitTradeHistory)
        TreeMap<Integer, TradeHistory> data = new TreeMap<>();
        data.put(0, new TradeHistory("iPhone 16", 5, 11.25F, new Date(System.currentTimeMillis()), "User1", new OrganisationalUnit("org3", 10)));
        data.put(1, new TradeHistory("iPhone 11", 3, 10, new Date(System.currentTimeMillis()), "User4", new OrganisationalUnit("org10", 10)));
        data.put(2, new TradeHistory("iPhone 17", 25, 10.5F, new Date(System.currentTimeMillis()), "User2", new OrganisationalUnit("org12", 10)));
        data.put(3, new TradeHistory("iPhone 10", 5, 11, new Date(System.currentTimeMillis()), "User4", new OrganisationalUnit("org11", 10)));
        data.put(4, new TradeHistory("iPhone 15", 6, 13, new Date(System.currentTimeMillis()), "User2", new OrganisationalUnit("org10", 10)));
        data.put(5, new TradeHistory("iPhone 16", 5, 10, new Date(System.currentTimeMillis()), "User2", new OrganisationalUnit("org3", 10)));
        data.put(6, new TradeHistory("iPhone 11", 8, 14, new Date(System.currentTimeMillis()), "User1", new OrganisationalUnit("org5", 10)));
        data.put(7, new TradeHistory("iPhone 16", 5, 11.5F, new Date(System.currentTimeMillis()), "User1", new OrganisationalUnit("org4", 10)));
        data.put(8, new TradeHistory("iPhone 11", 10, 12, new Date(System.currentTimeMillis()), "User4", new OrganisationalUnit("org7", 10)));
        data.put(9, new TradeHistory("iPhone 11", 5, 10, new Date(System.currentTimeMillis()), "User1", new OrganisationalUnit("org7", 10)));

        Collection values = data.values();
        //dataSource.

        // Create Object[][] for table data
        Object[][] tableData = new Object[data.size()][];
        int count = 0;
        for (Object row : values) {
            TradeHistory rowData = (TradeHistory) row;
            int quantity = rowData.getTradedQuantity();
            float price = rowData.getPrice();
            tableData[count] = new Object[]{
                    rowData.getAssetName(),
                    quantity,
                    price,
                    quantity * price,
                    rowData.getDateFulfilled().toString(),
                    rowData.getCreatorUsername(),
                    rowData.getunitOfTrader().getUnitName()
            };
            count++;
        }

        JTable table = new JTable(tableData, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(800, table.getRowCount() * table.getRowHeight()));
        Helper.formatTable(table);
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setVisible(true);

        JPanel panel = new JPanel();
        panel.add(tablePane);
        panel.setBorder(new EmptyBorder(10, -10, 10, -10));

        return panel;
    }

    public JPanel unitAssetTable() {
        JPanel panel = new JPanel();

        try {
            // Table
            ArrayList<String> assetNames = dataSource.retrieveAllAssets();
            HashMap<String, Integer> assets = member.getUnitAssets(dataSource);

            String[] columnNames = {"Asset Name", "Quantity Owned"};

            // Create Object[][] for table data
            Object[][] tableData = new Object[assetNames.size()][];
            for (int i = 0; i < assetNames.size(); i++) {
                Object quantity = assets.get(assetNames.get(i));
                // Not found -> set to 0
                if (quantity == null) quantity = 0;

                tableData[i] = new Object[]{
                        assetNames.get(i), quantity
                };
            }

            JTable assetTable = new JTable(tableData, columnNames);
            Helper.formatTable(assetTable);
            assetTable.setPreferredScrollableViewportSize(new Dimension(400, assetTable.getRowCount() * assetTable.getRowHeight()));
            assetTable.setMaximumSize(new Dimension(400,300));
            JScrollPane tablePane = new JScrollPane(assetTable);
            tablePane.setVisible(true);

            panel.add(tablePane);
        } catch (DatabaseException e) {
            // Error
            JLabel assetDisplayLabel = Helper.createLabel("No assets to display", 12);
            assetDisplayLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
            panel.add(assetDisplayLabel);
        }


        panel.setBorder(new EmptyBorder(10, -10, 10, -10));

        return panel;
    }

    public JPanel unitMemberTable() {
        JPanel panel = new JPanel();

        try {
            // Table
            ArrayList<String[]> assetNames = dataSource.retrieveAllMembers(member.getUnitName());

            String[] columnNames = {"Username", "User Type"};

            // Create Object[][] for table data
            Object[][] tableData = new Object[assetNames.size()][];
            int count = 0;
            for (String[] assetName : assetNames) {
                tableData[count] = new Object[]{
                        assetName[0],
                        assetName[1]
                };
                count++;
            }

            JTable table = new JTable(tableData, columnNames);
            Helper.formatTable(table);
            table.setPreferredScrollableViewportSize(new Dimension(400, table.getRowCount() * table.getRowHeight()));
            table.setMaximumSize(new Dimension(400,400));
            JScrollPane tablePane = new JScrollPane(table);
            tablePane.setVisible(true);

            panel.add(tablePane);
        } catch (DatabaseException e) {
            // Error
            JLabel assetDisplayLabel = Helper.createLabel("No assets to display", 12);
            assetDisplayLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
            panel.add(assetDisplayLabel);
        }


        panel.setBorder(new EmptyBorder(10, -10, 10, -10));

        return panel;
    }
}
