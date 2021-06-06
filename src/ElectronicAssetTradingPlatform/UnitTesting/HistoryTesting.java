package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;

import ElectronicAssetTradingPlatform.Users.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

/**
 * Class for testing functionality of the MarketplaceHistoryDataSource functions
 */
public class HistoryTesting {

    /**
     * Initalise History Testing tests
     */
    @BeforeAll @Test
    public static void Start() {
        System.out.println("HistoryTesting");
    }

    /**
     * Setup database to read/insert to
     */
    @BeforeEach
    @Test
    public void setup() {
        ETPDataSource etp = new ETPDataSource();
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");
    }

//    // Testing works with DB Directly (This test works and does insert into the database)
//    @Test
//    public void testInsert() {
//        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
//        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
//        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");
//
//        m.insertCompletedTrade(buyOffer, sellOffer, 2);
//    }

    /**
     * Test the size/number of rows found from the Marketplace_history database
     * table for assets named iPhone 10
     */
    @Test
    public void testAssetHistorySize() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        assetPriceHistory = m.getAssetPriceHistory("iPhone 10");
        assertEquals(assetPriceHistory.size(),1);
    }

    /**
     * Test getting the date of traded and price an asset named
     * iPhone 10 was sold at from the Marketplace_history datbase table
     */
    @Test
    public void testGetAssetHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        assetPriceHistory = m.getAssetPriceHistory("iPhone 10");
        assertEquals(assetPriceHistory.toString(), "[[2021-05-31, 33.0]]");
    }

    /**
     * Test the class type of assetPriceHistory
     */
    @Test
    public void testAssetHistoryObj() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        assetPriceHistory = m.getAssetPriceHistory("iPhone 10");
        assertNotEquals(assetPriceHistory.getClass(), "class java.util.ArrayList");
    }

    /**
     * Testing for fetching the history of an asset that does not exist
     * in the database/system at all
     */
    @Test
    public void testInvalidAssetInHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        assetPriceHistory = m.getAssetPriceHistory("non existing asset");
        assertEquals(assetPriceHistory, null);
    }

    // Test inserting a new row in the Marketplace_history works with the network
//    @Test
//    public void testNetworkInsert() {
//        NetworkDataSource net = new NetworkDataSource();
//        net.run();
//        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
//        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");
//        net.addAssetHistory(buyOffer, sellOffer, 2);
//    }
//

    // Test fetching asset history of an asset works with network
//    @Test
//    public void testNetworkGetAssetHistory() {
//        NetworkDataSource net = new NetworkDataSource();
//        net.run();
//        try {
//            net.getAssetHistory("iPhone 10");
//            System.out.println(net.getAssetHistory("iPhone 10"));
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Test the rows found from the database for  Unit 1's trade history
     * is equal to the number of entries (key and value pair) in the tree map
     */
    @Test
    public void testUnitHistorySize() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();

        try {
            unitTradeHistory = m.getUnitTradeHistory(1);
        } catch (LessThanZeroException e) {
            e.printStackTrace();
        }

        assertEquals(2, unitTradeHistory.size());
    }

    /**
     * Test fetching of the unit 1's history from the Marketplace_history database table
     */
    @Test
    public void testUnitHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();

        try {
            unitTradeHistory = m.getUnitTradeHistory(1);
        } catch (LessThanZeroException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Integer, TradeHistory> entry : unitTradeHistory.entrySet()) {
            Integer key = entry.getKey();
            TradeHistory value = entry.getValue();

            if ((key == 3) &&
            (value.getBuyOrSell().equals("-")) &&
            (value.getAssetName().equals("iPhone 10")) &&
            (value.getTradedQuantity() == 2) &&
            (value.getPrice() == 33.0) &&
            (value.getTotal() == 66.0 ) &&
            (value.getDateFulfilled().equals("2021-05-31")) &&
            (value.getunitNameOfTrader().equals("Management"))) {
                // Found at least 1
                assert true;
                return;
            }
        }

        assert false;
    }

    /**
     * Test fetching history for an invalid/non-existing unit
     */
    @Test
    public void testInvalidUnitHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();

        try {
            unitTradeHistory = m.getUnitTradeHistory(1000000000);
        } catch (LessThanZeroException e) {
            e.printStackTrace();
        }
        assertEquals(unitTradeHistory.size(), 0);
    }

    // Test the fetching of a Unit's Trade History works with the network
//    @Test
//    public void testNetworkGetUnitTradeHistory() throws LessThanZeroException{
//        NetworkDataSource net = new NetworkDataSource();
//        net.run();
//        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();
//        try {
//            unitTradeHistory = net.getUnitTradeHistory("Human Resources");
//
//            for(Map.Entry<Integer, TradeHistory> entry : unitTradeHistory.entrySet()) {
//                Integer key = entry.getKey();
//                TradeHistory value = entry.getValue();
//
//                System.out.println("Key: " + key + ", " + "Buy/Sell: " + value.getBuyOrSell() + ", Asset Name: "
//                        + value.getAssetName() + ", Quantity: " + value.getTradedQuantity() + ", Price: "
//                        + value.getPrice() + ", Total: " + value.getTotal() + ", Date: "
//                        + value.getDateFulfilled() + ", To/From: " + value.getunitNameOfTrader());
//            }
//        }
//        catch (LessThanZeroException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(net.getUnitTradeHistory("Human Resources"));
//    }
}
