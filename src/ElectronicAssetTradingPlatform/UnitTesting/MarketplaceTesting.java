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

import java.util.TreeMap;


public class MarketplaceTesting {

    static UsersDataSource usersDataSource;
    Asset asset;
    OrganisationalUnitMembers userA;
    OrganisationalUnitMembers userB;
    OrganisationalUnit humanResources;
    OrganisationalUnit management;
    UnitDataSource unitDataSource;
    NetworkDataSource dataSource;


    @BeforeAll @Test
    public static void Start() {
        System.out.println("Marketplace");
    }

    @BeforeEach
    @Test
    public void setupMarketplace() {
        unitDataSource = new UnitDataSource();
        ETPDataSource etp = new ETPDataSource();
        userA = new OrganisationalUnitMembers("willymon", "pw", "salt", "Human Resources");
        userB = new OrganisationalUnitMembers("hana", "pw", "salt", "Management");
        humanResources = new OrganisationalUnit("Human Resources", 1000);
        management = new OrganisationalUnit("Management", 1000);

//        dataSource = new NetworkDataSource();
//        dataSource.run();

        // remove all buy offers
        MarketplaceDataSource.getInstance().removeAllOffers();
        // set Human Resources to have 1000 credits
//        dataSource.setOrgUnitCredits(humanResources, 1002);
//        dataSource.setOrgUnitCredits(management, 1000);
//        // reset org assets
//        dataSource.setOrgUnitAssets(humanResources, "iPhone 10", 1);
//        dataSource.setOrgUnitAssets(humanResources, "Pencils", 100);
//        dataSource.setOrgUnitAssets(humanResources, "Coffee Machine", 5);
//        dataSource.setOrgUnitAssets(humanResources, "Chair", 0);
//        dataSource.setOrgUnitAssets(management, "Coffee Machine", 1);
//        dataSource.setOrgUnitAssets(management, "Chair", 20);
//        dataSource.setOrgUnitAssets(management, "iPhone 10", 10);
//        dataSource.setOrgUnitAssets(management, "Pencils", 0);

        // base sell offers
//        userA.listSellOrder("Pencils", 10, 20);
//        userA.listSellOrder("Coffee Machine", 1, 50);
//        userB.listSellOrder("iPhone 10", 2, 30);
//        userB.listSellOrder("iPhone 10", 2, 20);
//        userB.listSellOrder("Chair", 2, 15);
//        userB.listSellOrder("Chair", 2, 20);
//        // base buy offers
//        userB.listBuyOrder("Pencils", 15, 1);
//        userB.listBuyOrder("Coffee Machine", 2, 40);
//        userA.listBuyOrder("iPhone 10", 1, 10);
//        userA.listBuyOrder("Chair", 1, 5);

    }

    @Test
    public void testInsertBuyOffer() {
        // create buy offer object and  insert it into the database
        BuyOffer buyOffer = new BuyOffer("Chair", 5, 20, "hana", "Management");
        MarketplaceDataSource.getInstance().insertBuyOffer(buyOffer);
        // retrieve all offers and find the placed offer
        TreeMap<Integer, BuyOffer > buyOffers = MarketplaceDataSource.getInstance().getBuyOffers();
        int ID = MarketplaceDataSource.getInstance().getPlacedOfferID();
        // asserting that the placed offer should have these properties
        assertEquals("Chair\t5\t $20.0\thana\tManagement", buyOffers.get(ID).toString(),
                "Listing Buy Offer Failed");
    }

    @Test
    public void testInsertSellOffer() {
        // create sell offer object and insert it into the database
        SellOffer sellOffer = new SellOffer("iPhone 10", 1, 100, "willymon", "Human Resources");
        MarketplaceDataSource.getInstance().insertSellOffer(sellOffer);
        // retrieve all offers and find the placed offer
        TreeMap<Integer, SellOffer> sellOffers = MarketplaceDataSource.getInstance().getSellOffers();
        int ID = MarketplaceDataSource.getInstance().getPlacedOfferID();
        // asserting that the placed offer should have these properties
        assertEquals("iPhone 10\t1\t $100.0\twillymon\tHuman Resources", sellOffers.get(ID).toString(),
                "Listing Sell Offer Failed");
    }

    @Test
    public void removeBuyOffer() {
        // create buy offer object and  insert it into the database
        BuyOffer buyOffer = new BuyOffer("Chair", 5, 20, "hana", "Management");
        MarketplaceDataSource.getInstance().insertBuyOffer(buyOffer);
        // remove it
        int ID = MarketplaceDataSource.getInstance().getPlacedOfferID();
        MarketplaceDataSource.getInstance().removeOffer(ID);
        // check that there are no buy offers
        TreeMap<Integer, BuyOffer > buyOffers = MarketplaceDataSource.getInstance().getBuyOffers();
        assertTrue(buyOffers.isEmpty(), "Failed to remove buy offer");
    }

    @Test
    public void removeSellOffer() {
        // create sell offer object and  insert it into the database
        SellOffer sellOffer = new SellOffer("iPhone 10", 1, 100, "willymon", "Human Resources");
        MarketplaceDataSource.getInstance().insertSellOffer(sellOffer);
        // remove it
        int ID = MarketplaceDataSource.getInstance().getPlacedOfferID();
        MarketplaceDataSource.getInstance().removeOffer(ID);
        // check that there are no buy offers
        TreeMap<Integer, SellOffer> sellOffers = MarketplaceDataSource.getInstance().getSellOffers();
        assertTrue(sellOffers.isEmpty(), "Failed to remove sell offer");
    }

    // test failing to insert an offer when offer quantity is negative
    @Test
    public void testFailInsertOfferNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () ->  new SellOffer("iPhone 10", -1, 100,
                "willymon", "Human Resources"));
    }

    // test failing to insert an offer when offer price is negative
    @Test
    public void testFailInsertOfferNegativePrice() {
        assertThrows(IllegalArgumentException.class, () ->  new BuyOffer("iPhone 10", 1, -100,
                "willymon", "Human Resources"));
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









//    @AfterAll
//    public static void closeDB() throws SQLException {
//        MarketplaceDataSource.getInstance().close();
//    }
}
