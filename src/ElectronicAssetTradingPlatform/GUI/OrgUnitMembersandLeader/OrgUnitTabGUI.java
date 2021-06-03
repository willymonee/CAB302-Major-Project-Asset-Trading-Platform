package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.TradeHistory;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class OrgUnitTabGUI extends JPanel implements ChangeListener {
    private NetworkDataSource dataSource;
    private OrganisationalUnitMembers member;

    private JPanel content;

    public OrgUnitTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
        this.member = member;
        this.dataSource = dataSource;

        content = makeContent();
        this.add(content);
    }

    /**
     * Create JPanel of entire content
     * @return Content of the tab
     */
    public JPanel makeContent() {
        JPanel wrapper = Helper.createPanel(Color.WHITE);
        wrapper.setPreferredSize(new Dimension(850, 800));
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        String creditsText = member.getUnitName() + ": ";
        try {
            creditsText += member.getUnitCredits(dataSource) + " credits";
        } catch (DatabaseException e) {
            creditsText += "Not found";
        }

        JLabel unitCreditsLabel = Helper.createLabel(creditsText, 16);
        unitCreditsLabel.setBorder(new EmptyBorder(10, 550, 10, 0));
        wrapper.add(unitCreditsLabel);

        JLabel tradeHistoryLabel = Helper.createLabel(member.getUnitName() + "\'s Trade History", 20);
        tradeHistoryLabel.setBorder(new EmptyBorder(10, 30, 10, 0));
        wrapper.add(tradeHistoryLabel);
        JPanel tradeTable = unitTradeHistoryTable();
        tradeTable.setBackground(Color.WHITE);
        wrapper.add(tradeTable);

        JLabel unitAssetsLabel = Helper.createLabel(member.getUnitName() + "\'s Assets", 20);
        unitAssetsLabel.setBorder(new EmptyBorder(10, 220, 10, 0));
        wrapper.add(unitAssetsLabel);
        JPanel assetTable = unitAssetTable();
        assetTable.setBackground(Color.WHITE);
        wrapper.add(assetTable);

        JLabel unitMembersLabel = Helper.createLabel(member.getUnitName() + "\'s Members", 20);
        unitMembersLabel.setBorder(new EmptyBorder(10, 220, 10, 0));
        wrapper.add(unitMembersLabel);
        JPanel memTable = unitMemberTable();
        memTable.setBackground(Color.WHITE);
        wrapper.add(memTable);

        return wrapper;
    }

    /**
     * Table for the unit's trade history
     * @return JPanel containing the history table
     */
    public JPanel unitTradeHistoryTable() {
        String[] columnNames = {"Buy(+)/Sell(-)", "Name", "Quantity", "Price", "Total", "Trade Date", "Recipient Unit"};

        TreeMap<Integer, TradeHistory> data = null;
        try {
            data = dataSource.getUnitTradeHistory(member.getUnitName());
        } catch (LessThanZeroException e) {
            e.printStackTrace();
        }

        Collection values = data.values();

        // Create Object[][] for table data
        Object[][] tableData = new Object[data.size()][];
        int count = 0;
        for (Object row : values) {
            TradeHistory rowData = (TradeHistory) row;
            int quantity = rowData.getTradedQuantity();
            float price = rowData.getPrice();
            tableData[count] = new Object[]{
                    rowData.getBuyOrSell(),
                    rowData.getAssetName(),
                    quantity,
                    price,
                    quantity * price,
                    rowData.getDateFulfilled(),
                    rowData.getunitNameOfTrader()
            };
            count++;
        }

        JTable table = new JTable(tableData, columnNames);
        Helper.formatTable(table);
        table.setPreferredScrollableViewportSize(new Dimension(840, table.getRowHeight() * table.getRowCount()));
        table.setMaximumSize(new Dimension(840, table.getRowHeight()*4));
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setVisible(true);

        JPanel panel = new JPanel();
        panel.add(tablePane);
        panel.setBorder(new EmptyBorder(10, -10, 10, -10));

        return panel;
    }

    /**
     * Table for the assets owned by the unit
     * @return JPanel containing the asset table
     */
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

    /**
     * Table for the unit's members
     * @return JPanel containing the member table
     */
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

    /**
     * Event handling of switching between tabs - refresh the UI
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        this.remove(content);
        content = makeContent();
        this.add(content);
    }
}
