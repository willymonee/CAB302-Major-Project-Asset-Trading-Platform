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
                {"Buyer1", "Seller1", (float)2, 5, new Date(System.currentTimeMillis()-2000000000)},
                {"Buyer4", "Seller2", (float)3, 2, new Date(System.currentTimeMillis()-1000000000)},
                {"Buyer2", "Seller3", (float)4, 5, new Date(System.currentTimeMillis()-1300000000)},
                {"Buyer2", "Seller2", (float)5, 7, new Date(System.currentTimeMillis()-2000000000)},
                {"Buyer3", "Seller2", (float)6, 12, new Date(System.currentTimeMillis()-1500000000)},
                {"Buyer2", "Seller4", (float)7, 10, new Date(System.currentTimeMillis()-1000000000)}
        };

        // Sort by date
        Arrays.sort(data, new java.util.Comparator<Object[]>() {
            public int compare(Object[] a, Object[] b) {
                return Long.compare(((Date)a[4]).getTime(), ((Date)b[4]).getTime());
            }
        });

        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setPreferredScrollableViewportSize(new Dimension(
                500,
                table.getRowHeight() * data.length)
        );
        JScrollPane tablePane = new JScrollPane(table);

        MarketplaceHistoryGraph graph = new MarketplaceHistoryGraph();

        ArrayList<Float> y_values = new ArrayList<>();
        ArrayList<Date> x_values = new ArrayList<>();
        for (Object[] row : data) {
            y_values.add((float)row[2]);
            x_values.add((Date)row[4]);
        }

        // Get ceiling/floor
        float min_y = Collections.min(y_values);
        float max_y = Collections.max(y_values);
        long min_x = Collections.min(x_values).getTime();
        long max_x = Collections.max(x_values).getTime();

        // Reduce min to 0
        float diff_x = max_x - min_x;
        float diff_y = max_y - min_y;

        for (int i = 0; i < data.length-1; i++) {
            float y2 = y_values.get(i+1);

            float value1 = 1 - (y_values.get(i) - min_y) / diff_y;
            float value2 = 1 - (y2 - min_y) / diff_y;

            float date1 = 1 - (x_values.get(i).getTime() - min_x) / diff_x;
            float date2 = 1 - (x_values.get(i+1).getTime() - min_x) / diff_x;

            graph.addLine((int)(date1*250), (int)(value1*250), (int)(date2*250), (int)(value2*250), String.valueOf(y2));
        }

        graph.setPreferredSize(new Dimension(300, 300));

        JPanel graphContainer = new JPanel();
        graphContainer.add(graph);
        graphContainer.setBorder(new EmptyBorder(20, -10, 10, -10));
        graphContainer.setBackground(Color.WHITE);

        JLabel title = new JLabel("Asset Price History");
        panel.add(title, BorderLayout.NORTH);
        panel.add(tablePane, BorderLayout.SOUTH);
        panel.add(graphContainer, BorderLayout.CENTER);

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
