package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
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
    OrganisationalUnitMembers userB;
    //static MarketplaceDataSource marketplaceDataSource;
    static UnitDataSource unitDataSource;

    @BeforeEach
    @Test
    public void setupMarketplace() {
        ETPDataSource etp = new ETPDataSource();
        userA = new OrganisationalUnitMembers("willymon", "pw", "salt", "Human Resources");
        userB = new OrganisationalUnitMembers("hana", "pw", "salt", "Management");
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
        //userA.listBuyOrder("Table", 1, 1);
    }

    @Test
    public void testInsertSellOffer() {
        // creating a buy offer and adding it into the database through the user
        //userA.listSellOrder("iPhone 10", 1, 20);
        //userB.listSellOrder("Table", 3, 5);
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
        //userA.removeBuyOffer(31);

    }

    @Test
    public void removeSellOffer() {
        //userA.removeSellOffer(26);
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
        userA.listBuyOrder("iPhone 10", 3, 20);

//        BuyOffer buyOffer = BuyOfferData.getInstance().getOffer(29);
//        int matchingID = buyOffer.getMatchedPriceOffer();
//        buyOffer.reduceMatchingOfferQuantities(matchingID);

    }

    // Test updating the quantity of a sell offer
    @Test
    public void updateSellOfferQuantity() {
        //SellOfferData.getInstance().updateOfferQuantity(4, 23);
        userA.listSellOrder("Table", 3, 1);
        // doing it manually
//        SellOffer sellOffer = SellOfferData.getInstance().getOffer(40);
//        int matchingID = sellOffer.getMatchedPriceOffer();
//        sellOffer.reduceMatchingOfferQuantities(matchingID);
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

    // test failing to insert a buy offer for an asset not in the system
    // test fails because exception is caught at an earlier stage
    @Test
    public void testFailInsertOfferAssetNotInSystem() {
        //assertThrows(SQLException.class, () -> userA.listBuyOrder("Robodog", 1,10000));
    }






    @AfterAll
    public static void closeDB() throws SQLException {
        MarketplaceDataSource.getInstance().close();
    }
}
