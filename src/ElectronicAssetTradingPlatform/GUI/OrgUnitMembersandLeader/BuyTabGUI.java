package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;


public class BuyTabGUI extends JPanel {
    // Global variables
    private OrganisationalUnitMembers loggedInMember;
    private NetworkDataSource data;
    private JPanel wrapper;
    private JPanel orgBuyOffersPanel;
    private JLabel orgBuyOfferLabel;
    private JLabel welcomeMessage;
    private JPanel marketBuyOffersPanel;

    TreeMap<Integer, BuyOffer> buyOffers;


    public BuyTabGUI(OrganisationalUnitMembers member, NetworkDataSource dataSource) {
        loggedInMember = member;
        data = dataSource;
        wrapper = Helper.createPanel(Color.WHITE);

        BoxLayout boxlayout = new BoxLayout(wrapper, BoxLayout.Y_AXIS);
        wrapper.setLayout(boxlayout);
        wrapper.setPreferredSize(new Dimension(850, 600));
        this.add(wrapper);
        // add a welcome message
        welcomeMessage = Helper.createLabel(memberTextDisplay(), 16);
        welcomeMessage.setHorizontalAlignment(JLabel.CENTER);
        welcomeMessage.setBorder(new EmptyBorder(10, 0, 10, 0));
        wrapper.add(welcomeMessage);
        // add org buy offer panel
        orgBuyOffersPanel = Helper.createPanel(Color.WHITE);
        orgBuyOfferLabel = Helper.createLabel(member.getUnitName() + "'s Buy Offers:", 20);
        orgBuyOfferLabel.setHorizontalAlignment(JLabel.LEFT);
        orgBuyOfferLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        // add a label to the org buy offer panel
        orgBuyOffersPanel.add(orgBuyOfferLabel);
        JTable buyOffersTable = unitBuyOffersTable();
        // add a table to the org buy offer panel
        JScrollPane scrollPane = new JScrollPane(buyOffersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        scrollPane.getViewport().setBackground(Color.WHITE);


        orgBuyOffersPanel.add(scrollPane);
        wrapper.add(orgBuyOffersPanel);

//        marketBuyOffersPanel = Helper.createPanel(Color.PINK);
//        marketBuyOffersPanel.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
//        wrapper.add(marketBuyOffersPanel);


    // TODO: BUTTON COLUMN TO EDIT ASSET LISTING https://camposha.info/java-jtable-buttoncolumn-tutorial/
    }

    private String memberTextDisplay() {
        String memberTextDisplay = "";
        memberTextDisplay += "Welcome" + loggedInMember.getUsername();
        // this.add(new JTextArea("welcome " + loggedInMember.getUsername()));
        try {
            float credits = data.getCredits(loggedInMember.getUnitName());
            System.out.println(credits);
            memberTextDisplay += "\t" + loggedInMember.getUnitName() + ": " + Float.toString(credits) + " credits";
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
        return memberTextDisplay;
    }

    private TreeMap<Integer, BuyOffer> getUnitBuyOffers() throws Exception {
        buyOffers = null;
        buyOffers = BuyOfferData.getInstance().getOrgOffersMap(loggedInMember.getUnitName());


        if (buyOffers == null) {
            throw new Exception(); // maybe don't 'throw' an exception just display nothing
        }

        return buyOffers;

    }

    // https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths by Paul Vargas
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    // TODO : REMOVE ORG UNIT AS A COLUMN, AND ISNTEAD HAVE A BUTTON TO EDIT THE ASSET OFFER LISTING
    private JTable unitBuyOffersTable() {
        String data[][] = getRowData();
        String columns[] = { "Offer ID", "Asset Name", "Quantity", "Price", "Offer Creator", "Edit/Delete"};
        JTable buyOffersTable = new JTable(data, columns);
        //buyOffersTable.setBounds(30,40,200,200);
        resizeColumnWidth(buyOffersTable);
        buyOffersTable.setRowHeight(25);
        buyOffersTable.setFont(new Font ( "Dialog", Font.PLAIN, 14));
        buyOffersTable.getTableHeader().setPreferredSize(new Dimension(150,25));
        buyOffersTable.getTableHeader().setFont(new Font ( "Dialog", Font.BOLD, 14));
        buyOffersTable.getTableHeader().setReorderingAllowed(false);
        // center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for(int x = 0; x < columns.length; x++){
            buyOffersTable.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
        // prevent editing of cells in table
        buyOffersTable.setDefaultEditor(Object.class, null);
        return buyOffersTable;

    }

    private String[][] getRowData() {
        try {
            buyOffers = getUnitBuyOffers();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        String[][] data = new String[buyOffers.size()][];
        int count = 0;
        for(Map.Entry<Integer, BuyOffer> entry : buyOffers.entrySet()) {
            //Integer key = entry.getKey();
            BuyOffer value = entry.getValue();


            data[count] = new String[] {
                String.valueOf(value.getOfferID()),
                value.getAssetName(),
                String.valueOf(value.getQuantity()),
                String.valueOf(value.getPricePerUnit()),
                value.getUsername(),
                value.getUnitName()
            };
            count++;
        }
        return data;
    }
}
