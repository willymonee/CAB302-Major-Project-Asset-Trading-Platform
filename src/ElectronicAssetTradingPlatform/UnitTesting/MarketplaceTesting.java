package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.*;
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
        userA = new OrganisationalUnitMembers("userN", "pw", "salt", "UnitX");
        dataSource = new MarketplaceDataSource();
        asset = new Asset("testAsset");
    }

    /*
    Preconditions
    INSERT INTO Organisational_Units VALUES(1, "UnitX", 300.00);
    INSERT INTO USER_Accounts VALUES(1, "userN", "pw", "salt", "Organisational Unit Member", 1);

    Delete insertions/ mock db entries
    DELETE FROM Organisational_Units WHERE Unit_ID = 1;
    DELETE FROM USER_Accounts WHERE Username = "userN";
    DELETE FROM Marketplace WHERE Unit_ID = 1;
     */
    @Test
    public void testInsert() {
        if (dataSource == null) {
            dataSource = new MarketplaceDataSource();
        }
        dataSource.insertBuyOffer(userA, asset, "50");
    }
}
