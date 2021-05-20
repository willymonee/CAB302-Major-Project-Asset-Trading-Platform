package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.AssetTrading.BuyOfferData;
import ElectronicAssetTradingPlatform.AssetTrading.SellOfferData;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;


public class MarketplaceTesting {

    static UsersDataSource usersDataSource;
    Asset asset;
    OrganisationalUnitMembers userA;
    //static MarketplaceDataSource marketplaceDataSource;
    static UnitDataSource unitDataSource;

    @BeforeEach
    @Test
    public void setupMarketplace() {
        ETPDataSource etp = new ETPDataSource();
        userA = new OrganisationalUnitMembers("willymon", "pw", "salt", "Human Resources");
        // marketplaceDataSource = new MarketplaceDataSource();
//        try {
//            usersDataSource = new UsersDataSource();
//            usersDataSource.insertUser(userA);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }




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
    public void testInsertBuyOffer() {
        // creating a buy offer and adding it into the database through the user
        // userA.listBuyOrder("Table", 1, 50);
    }

    // test failing to insert a offer when offer quantity is negative
    @Test
    public void testFailInsertOfferNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> userA.listBuyOrder("Table", -1, 50));
    }

    // test failing to insert a buy offer when offer price is negative
    @Test
    public void testFailInsertOfferNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> userA.listBuyOrder("Table", 1, -50));
    }

    @Test
    public void testInsertSellOffer() {
        // creating a buy offer and adding it into the database through the user
        //userA.listSellOrder("iPhone 10", 1, 20);
    }

    @Test
    public void testRetrieveBuyOffers() {
        // print them out from BuyOfferData
        //System.out.println(BuyOfferData.getInstance());
    }

    @Test
    public void testRetrieveSellOffers() {
        // print them out from SellOfferData
        //System.out.println(SellOfferData.getInstance());
    }

    @Test
    public void removeBuyOffer() {
        //userA.removeBuyOffer(28);
    }

    @Test
    public void removeSellOffer() {
        //userA.removeSellOffer(30);
    }

    @Test
    public void testRetrieveOrgBuyOffers() {
        System.out.println(userA.getOrgBuyOffers());
    }

    @Test
    public void testRetrieveOrgSellOffers() {
        System.out.println(userA.getOrgSellOffers());
    }

    @AfterAll
    public static void closeDB() throws SQLException {
        MarketplaceDataSource.getInstance().close();
    }
}
