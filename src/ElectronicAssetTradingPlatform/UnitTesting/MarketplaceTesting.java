package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.*;
import org.junit.Test;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class MarketplaceTesting {

    Asset asset;
    OrganisationalUnitMembers userA;
    MarketplaceDataSource dataSource;

    @BeforeEach
    @Test
    public void setupMarketplace() {
        ETPDataSource etp = new ETPDataSource();
        userA = new OrganisationalUnitMembers("userN", "pw", "salt", "unitX");
        dataSource = new MarketplaceDataSource();
        if (dataSource == null) {
            System.out.println("hi");
        }
        asset = new Asset("testAsset");
    }

    @Test
    public void testInsert() {
        if (dataSource == null) {
            System.out.println("hi");
            dataSource = new MarketplaceDataSource();
        }
        dataSource.insertBuyOffer(userA, asset, "50");
    }
}
