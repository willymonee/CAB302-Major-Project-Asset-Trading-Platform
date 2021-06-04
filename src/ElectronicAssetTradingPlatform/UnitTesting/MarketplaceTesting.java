package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;


public class MarketplaceTesting {

    static UsersDataSource usersDataSource;
    Asset asset;
    OrganisationalUnitMembers userA;
    OrganisationalUnitMembers userB;
    OrganisationalUnit humanResources;
    OrganisationalUnit management;
    UnitDataSource unitDataSource;
    NetworkDataSource dataSource;


    @BeforeEach
    @Test
    public void setupMarketplace() {
        unitDataSource = new UnitDataSource();
        ETPDataSource etp = new ETPDataSource();
        userA = new OrganisationalUnitMembers("willymon", "pw", "salt", "Human Resources");
        userB = new OrganisationalUnitMembers("hana", "pw", "salt", "Management");
        humanResources = new OrganisationalUnit("Human Resources", 1000);
        management = new OrganisationalUnit("Management", 1000);

        dataSource = new NetworkDataSource();
        dataSource.run();

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

        // remove all buy offers
        MarketplaceDataSource.getInstance().removeAllOffers();
        // set Human Resources to have 1000 credits
        dataSource.editOrgUnitCredits(humanResources, 1002);
        dataSource.editOrgUnitCredits(management, 1000);
        // reset org assets
        dataSource.editOrgUnitAssets(humanResources, "iPhone 10", 1);
        dataSource.editOrgUnitAssets(humanResources, "Pencils", 100);
        dataSource.editOrgUnitAssets(humanResources, "Coffee Machine", 5);
        dataSource.editOrgUnitAssets(humanResources, "Chair", 0);
        dataSource.editOrgUnitAssets(management, "Coffee Machine", 1);
        dataSource.editOrgUnitAssets(management, "Chair", 20);
        dataSource.editOrgUnitAssets(management, "iPhone 10", 10);
        dataSource.editOrgUnitAssets(management, "Pencils", 0);

        // base sell offers
        userA.listSellOrder("Pencils", 10, 20);
        userA.listSellOrder("Coffee Machine", 1, 50);
        userB.listSellOrder("iPhone 10", 2, 30);
        userB.listSellOrder("iPhone 10", 2, 20);
        userB.listSellOrder("Chair", 2, 15);
        userB.listSellOrder("Chair", 2, 20);
        // base buy offers
        userB.listBuyOrder("Pencils", 15, 1);
        userB.listBuyOrder("Coffee Machine", 2, 40);
        userA.listBuyOrder("iPhone 10", 1, 10);
        userA.listBuyOrder("Chair", 1, 5);
    }

    @Test
    public void testInsertSellOffer() {
        // creating a buy offer and adding it into the database through the user
        //BuyOfferData.getInstance().removeOffer(216);
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
//        userA.removeBuyOffer(128);
//        userA.removeBuyOffer(127);
//        userA.removeBuyOffer(126);
//        userA.removeBuyOffer(125);
//        userA.removeBuyOffer(124);
    }

    @Test
    public void removeSellOffer() {
        //userA.removeSellOffer(36);
    }

    // retrieve buy offers from a user's organisational unit
    @Test
    public void testRetrieveOrgBuyOffers() {
        //System.out.println(userA.getOrgBuyOffers());
    }

    // retrieve sell offers from a user's organisational unit
    @Test
    public void testRetrieveOrgSellOffers() {
        //System.out.println(userA.getOrgSellOffers());
    }

    // retrieve buy offers of a particular asset
    @Test
    public void testRetrieveAssetBuyOffers() {
        //userB.listSellOrderNoResolve("Table", 2, 10);
        //System.out.println(BuyOfferData.getInstance().getAssetOffers("iPhone 10"));
        //System.out.println(SellOfferData.getInstance().getAssetOffers("Table"));
    }

    // list a buy offer and look to resolve it
    @Test
    public void resolveBuyOffer() {
        //userB.listBuyOrder("Table", 3, 10);
    }

    // list a sell offer and look to resolve it
    @Test
    public void resolveSellOffer() {
         //userA.listSellOrder("Table", 3, 2);
    }


    // test failing to insert a offer when offer quantity is negative
    @Test
    public void testFailInsertOfferNegativeQuantity() {
        //assertThrows(IllegalArgumentException.class, () -> userA.listBuyOrder("Table", -1, 50));
    }

    // test failing to insert a buy offer when offer price is negative
    @Test
    public void testFailInsertOfferNegativePrice() {
        //assertThrows(IllegalArgumentException.class, () -> userA.listBuyOrder("Table", 1, -50));
    }

    // test failing to insert a buy offer for an asset not in the system
    // test fails because exception is caught at an earlier stage
    @Test
    public void testFailInsertOfferAssetNotInSystem() {
        //assertThrows(SQLException.class, () -> userA.listBuyOrder("Robodog", 1,10000));
    }



    /**
     * Deprecated tests (methods are private)
     */
    // Temp test to see if return matching sell orders to a particular buy order if they are orders for the same asset
    // will become deprecated once getMatchingSellOffers() becomes a private method
    @Test
    public void matchBuyOfferAssetToSell() {
        //BuyOffer buyOffer = BuyOfferData.getInstance().getOffer(21);
        //System.out.println(buyOffer.getMatchingSellOffers());
    }

    // Temp test to see if return matching buy orders to a particular sell order if they are orders for the same asset
    // will become deprecated once getMatchingBuyOffers() becomes a private method
    @Test
    public void matchSellOfferAssetToBuy() {
        //SellOffer sellOffer = SellOfferData.getInstance().getOffer(25);
        //System.out.println(sellOffer.matchingBuyOffers());
    }

    // Temp test to return the ID of a matching sell offer with the lowest price but whose price is equal or less than
    // a particular buy order,
    @Test
    public void matchBuyOfferAssetPriceToSell() {
        //BuyOffer buyOffer = BuyOfferData.getInstance().getOffer(26);
        //System.out.println("A matching sell offer with the best price is: #"+ buyOffer.getMatchedPriceOffer());
    }

    // Temp test to return the ID of a matching buy offer whose price is equal or higher than the sell offer prioritising
    // whichever offer was added into the database first
    @Test
    public void matchSellOfferAssetPriceToBuy() {
        //SellOffer sellOffer = SellOfferData.getInstance().getOffer(24);
        //System.out.println("The first buy offer with equal or greater price is: #"+ sellOffer.getMatchedPriceOffer());
    }

    // Test updating the quantity of a buy offer when it matches against a sell offer
    @Test
    public void updateBuyOfferQuantity() {
        //userA.listBuyOrder("iPhone 10", 3, 20);

        //BuyOffer buyOffer = BuyOfferData.getInstance().getOffer(29);
        //int matchingID = buyOffer.getMatchedPriceOffer();
        //buyOffer.reduceMatchingOfferQuantities(matchingID);

    }

    // Test updating the quantity of a sell offer
    @Test
    public void updateSellOfferQuantity() {
        //SellOfferData.getInstance().updateOfferQuantity(4, 23);
        //userA.listSellOrder("Table", 3, 1);
        //doing it manually
        //SellOffer sellOffer = SellOfferData.getInstance().getOffer(40);
        //int matchingID = sellOffer.getMatchedPriceOffer();
        //sellOffer.reduceMatchingOfferQuantities(matchingID);
    }


    @AfterAll
    public static void closeDB() throws SQLException {
        MarketplaceDataSource.getInstance().close();
    }
}
