package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.*;
import org.junit.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        assetPriceHistory = m.getAssetPriceHistory(1);
        System.out.println("Rows found " + assetPriceHistory.size());
        System.out.println(assetPriceHistory);
    }
}
