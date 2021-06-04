package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class HistoryTesting {


    @BeforeEach
    @Test
    public void setup() {
        ETPDataSource etp = new ETPDataSource();
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");
    }

    // Testing works with DB Directly
    @Test
    public void testInsert() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");

        m.insertCompletedTrade(buyOffer, sellOffer, 2);
    }

    // Testing works with DB Directly
    @Test
    public void testGetAssetHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        List<List<Object>> assetPriceHistory = new ArrayList<List<Object>>();
        assetPriceHistory = m.getAssetPriceHistory("iPhone 10");
        System.out.println("Rows found " + assetPriceHistory.size());
        System.out.println(assetPriceHistory);
    }

    @Test
    public void testNetworkInsert() {
        NetworkDataSource net = new NetworkDataSource();
        net.run();
        BuyOffer buyOffer = new BuyOffer("iPhone 10", 2, 33.0, "そら", "Human Resources");
        SellOffer sellOffer = new SellOffer("iPhone 10", 2, 33.0, "willymon", "Human Resources");
        net.addAssetHistory(buyOffer, sellOffer, 2);
    }

    @Test
    public void testNetworkGetAssetHistory() {
        NetworkDataSource net = new NetworkDataSource();
        net.run();
        try {
            net.getAssetHistory("iPhone 10");
            System.out.println(net.getAssetHistory("iPhone 10"));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUnitHistory() {
        MarketplaceHistoryDataSource m = MarketplaceHistoryDataSource.getInstance();
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();

        try {
            unitTradeHistory = m.getUnitTradeHistory(1);
        } catch (LessThanZeroException e) {
            e.printStackTrace();
        }
        System.out.println("Rows found: " + unitTradeHistory.size());

        // Print hashmap out
        for(Map.Entry<Integer, TradeHistory> entry : unitTradeHistory.entrySet()) {
            Integer key = entry.getKey();
            TradeHistory value = entry.getValue();

            System.out.println("Key: " + key + ", " + "Buy/Sell: " + value.getBuyOrSell() + ", Asset Name: "
                    + value.getAssetName() + ", Quantity: " + value.getTradedQuantity() + ", Price: "
                    + value.getPrice() + ", Total: " + value.getTotal() + ", Date: "
                    + value.getDateFulfilled() + ", To/From: " + value.getunitNameOfTrader());
        }
    }

    @Test
    public void testNetworkGetUnitTradeHistory() throws LessThanZeroException{
        NetworkDataSource net = new NetworkDataSource();
        net.run();
        TreeMap<Integer, TradeHistory> unitTradeHistory = new TreeMap<>();
        try {
            unitTradeHistory = net.getUnitTradeHistory("Human Resources");

            for(Map.Entry<Integer, TradeHistory> entry : unitTradeHistory.entrySet()) {
                Integer key = entry.getKey();
                TradeHistory value = entry.getValue();

                System.out.println("Key: " + key + ", " + "Buy/Sell: " + value.getBuyOrSell() + ", Asset Name: "
                        + value.getAssetName() + ", Quantity: " + value.getTradedQuantity() + ", Price: "
                        + value.getPrice() + ", Total: " + value.getTotal() + ", Date: "
                        + value.getDateFulfilled() + ", To/From: " + value.getunitNameOfTrader());
            }
        }
        catch (LessThanZeroException e) {
            e.printStackTrace();
        }

        System.out.println(net.getUnitTradeHistory("Human Resources"));
    }
}
