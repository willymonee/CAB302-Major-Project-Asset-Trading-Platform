package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.SellOffer;
import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;
import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.OrganisationalUnitMembers;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Deprecated Unit Testing used for early implementation of Offers --> See MarketplaceTesting for updated
 */
public class OfferTestingDeprecated {
    // declare a member object
    OrganisationalUnitMembers member;
    OrganisationalUnitMembers otherMember;

    // declare organisational unit objects
    OrganisationalUnit humanResources;
    OrganisationalUnit management;


    // declare date object
    Date date;



    @BeforeAll @Test
    public static void Start() {
        System.out.println("Offer");
    }

    @BeforeEach
    @Test
    public void setUp() {
        // create organisational units
        humanResources = new OrganisationalUnit("Human Resources", 1000);
        management = new OrganisationalUnit("Management", 1000);
        // create organisational unit members
        member = new OrganisationalUnitMembers("Sammy101", "asdf", "salt","Human Resources");
        otherMember = new OrganisationalUnitMembers("Linax0x", "salt","asdf",
                "Management");
        // get the current date
        long millis = System.currentTimeMillis();
        date = new Date(millis);

    }

    // clear the buy and sell offer databases before each test
    // also clear assets from organisational units
    @AfterEach
    @Test
    public void clearDataBase() {
        BuyOffersDB.removeAllBuyOffers();
        SellOffersDB.removeAllSellOffers();
        humanResources.removeAllAssets();
        management.removeAllAssets();
    }

    // Return a string of all current market buy offers
    @Test
    public void returnMarketBuyOffers() {
        member.listBuyOrder("Table", 10, 5.45);
        member.listBuyOrder("Lamp", 1, 6);
        member.listBuyOrder("Computer", 2, 50.45);

        assertEquals("1\tTable\t10\t $5.45\tSammy101\tHuman Resources\t" + date + "\n" +
                        "2\tLamp\t1\t $6.0\tSammy101\tHuman Resources\t" + date + "\n" +
                        "3\tComputer\t2\t $50.45\tSammy101\tHuman Resources\t" + date,
                BuyOffersDB.getBuyOffersDB().toString(), "Failed to Retrieve Market Buy Offers");
    }

    // Return a string of all current market sell offers
    @Test
    public void returnMarketSellOffers() {
        member.listSellOrder("Drawing Tablet", 1, 105.41);
        member.listSellOrder("Calculator", 5, 20);
        member.listSellOrder("Adobe Photoshop Monthly Subscription", 10, 10);
        member.listSellOrder("Coffee Maker", 1, 65);
        assertEquals("1\tDrawing Tablet\t1\t $105.41\tSammy101\tHuman Resources\t" + date + "\n" +
                        "2\tCalculator\t5\t $20.0\tSammy101\tHuman Resources\t" + date + "\n" +
                        "3\tAdobe Photoshop Monthly Subscription\t10\t $10.0\tSammy101\tHuman Resources\t" + date + "\n" +
                        "4\tCoffee Maker\t1\t $65.0\tSammy101\tHuman Resources\t" + date,
                SellOffersDB.getSellOffersDB().toString(), "Failed to Retrieve Market Sell Offers");
    }

    // Return a string of all current market buy offers made by the user's organisation
    @Test
    public void returnOrgMarketBuyOffers() {
        member.listBuyOrder("Table", 10, 5.45);
        member.listBuyOrder("Lamp", 1, 6);
        member.listBuyOrder("Computer", 2, 50.45);
        otherMember.listBuyOrder("Fit Bit", 1, 30);
        otherMember.listBuyOrder("Jar of Cookies", 1, 4);
        // should only return buy offers from otherMember's org unit, and exclude offers made by member
        assertEquals("4\tFit Bit\t1\t $30.0\tLinax0x\tManagement\t" + date + "\n" +
                        "5\tJar of Cookies\t1\t $4.0\tLinax0x\tManagement\t" + date, otherMember.getOrgBuyOffers(),
                "Failed to Retrieve Organisational Unit Buy Offers");
    }

    // Return a string of all current market sell offers made by the user's organisation
    @Test
    public void returnOrgMarketSellOffers() {
        member.listSellOrder("Drawing Tablet", 1, 105.41);
        member.listSellOrder("Calculator", 5, 20);
        member.listSellOrder("Adobe Photoshop Monthly Subscription", 10, 10);
        member.listSellOrder("Coffee Maker", 1, 65);
        otherMember.listSellOrder("Herman Miller Embody Chair", 2, 600);
        otherMember.listSellOrder("Pencil Case", 1, 3.55);
        assertEquals("5\tHerman Miller Embody Chair\t2\t $600.0\tLinax0x\tManagement\t" + date + "\n" +
                "6\tPencil Case\t1\t $3.55\tLinax0x\tManagement\t" + date, otherMember.getOrgSellOffers());

    }
//
//    // test checking if there is a corresponding sell offer after creating a buy offer of equal price
    @Test
    public void returnMatchingSellOffer() {
        member.listSellOrder("Calculator", 1, 20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(1, BuyOffersDB.getBuyOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching sell offer");

    }

    // test checking if there is a corresponding sell offer after creating a buy offer of higher price
    @Test
    public void returnMatchingSellOfferLowerPrice() {
        member.listSellOrder("Calculator", 1, 18);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(1, BuyOffersDB.getBuyOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching sell offer");
    }

    // test returning lowest matching sell offer, given two sell offers
    @Test
    public void returnLowestPricedSellOffer() {
        member.listSellOrder("Calculator", 1, 18);
        member.listSellOrder("Calculator", 1, 17);
        member.listSellOrder("Calculator", 1, 20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(2, BuyOffersDB.getBuyOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching sell offer");
    }

    // test to see if getting a matching sell offer fails when the sell offer is a requesting a different asset
    @Test
    public void returnNoMatchingAssetSellOffer() {
        member.listSellOrder("Table", 1, 20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(0, BuyOffersDB.getBuyOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to check that there were no matching sell offers for the item");

    }


    // test to see if getting a matching sell offer fails when the sell offer is higher priced
    @Test
    public void returnNoMatchingPricedSellOffer() {
        member.listSellOrder("Table", 1, 25);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(0, BuyOffersDB.getBuyOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to check that there were no matching sell offers for the item");

    }

    // testing return a buy order ID which matches a sell order of equal price
    @Test
    public void returnMatchingBuyOrder() {
        member.listBuyOrder("iPad", 1, 100);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(1, SellOffersDB.getSellOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching buy offer");
    }

    // test returning a buy order ID which matches a sell order of lower price
    @Test
    public void returnMatchingHigherPricedBuyOrder() {
        member.listBuyOrder("iPad", 1, 110);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(1, SellOffersDB.getSellOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching buy offer");
    }

    // test returning the buy order that is lowest priced, then by order it was created
    @Test
    public void returnLowestMatchingBuyOrder() {
        member.listBuyOrder("iPad", 1, 110);
        member.listBuyOrder("iPad", 1, 120);
        member.listBuyOrder("iPad", 1, 105);
        member.listBuyOrder("iPad", 1, 130);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(1, SellOffersDB.getSellOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Failed to return matching buy offer");
    }

    // test not returning an buy offer with a different asset
    @Test
    public void returnNoMatchingAssetBuyOrder() {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(0, SellOffersDB.getSellOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Returned a matching buy offer, but not meant to");
    }

    // test not returning a buy offer with a lower price than the sell offer
    @Test
    public void returnNoMatchingPriceBuyOrder() {
        member.listBuyOrder("Fit Bit", 1, 90);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(0, SellOffersDB.getSellOffersDB().getOffer(1).getMatchedPriceOffer(),
                "Returned a matching buy offer, but not meant to");
    }

    /**
     * Resolving matching sell offers to a created newly buy offer
     */
    // test reducing the quantities of matching buy and sell offers, when equal quantities
    // expect both offers to be removed
    // testing a private method
    @Test
    public void reduceBuyToSellOfferEqualQuantities()  {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1));
    }

    // test reducing the quantities of matching buy and sell offers, when the buy offer quantity is greater
    // expect sell offer to be removed, but buy offer has a reduced quantity
    // testing a private method
    @Test
    public void reduceGreaterBuyToSellQuantities()  {
        member.listBuyOrder("Fit Bit", 3, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                BuyOffersDB.getBuyOffersDB().getOffer(1).getQuantity() == 2);

    }

    // test reducing the quantities of matching buy and sell offers, when the buy offer quantity is greater
    // expect sell offer to be removed, but buy offer has a reduced quantity
    // testing a private method
    @Test
    public void reduceLowerBuyToSellQuantities()  {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 3, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                SellOffersDB.getSellOffersDB().getOffer(1).getQuantity() == 2);
    }

    // test reducing the quantities with multiple sell offers, with more sell quantity than buy
    @Test
    public void reduceBuyToMultipleSellQuantities() {
        member.listBuyOrder("Fit Bit", 3, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                !SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(2) &&
                BuyOffersDB.getBuyOffersDB().getOffer(1).getQuantity() == 1);
    }

    // test reducing the quantities with multiple sell offers, with more total sell quantity than buy
    @Test
    public void  reduceBuyToMultipleGreaterSellQuantities()  {
        member.listBuyOrder("Fit Bit", 3, 100);
        otherMember.listSellOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Coffee Machine", 2, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                !SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                SellOffersDB.getSellOffersDB().getOffer(2).getQuantity() == 1);
    }

    /**
     * Resolving matching buy offers to a newly created sell offer
     */
    // test reducing the quantity of an equal amount of sell and buy offers
    @Test
    public void reduceSellToBuyOfferEqualQuantities() {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1));
    }

    // test reducing the quantities of the sell and buy offer, when the quantity of buy offer is greater
    @Test
    public void reduceLowerSellToBuyOfferQuantities() {
        member.listBuyOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                BuyOffersDB.getBuyOffersDB().getOffer(1).getQuantity() == 1);
    }

    // test reducing the quantities of the sell and buy offer, when the quantity of the sell offer is greater
    @Test
    public void reduceGreaterSellToBuyOfferQuantities() {
        member.listBuyOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Fit Bit", 5, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer();
        assertTrue(!BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                SellOffersDB.getSellOffersDB().getOffer(1).getQuantity() == 3);
    }

    // test reducing the quantities with multiple buy orders, with the quantity ordered from buy offers being greater
    @Test
    public void reduceSellToMultipleBuyQuantities() {
        member.listBuyOrder("Fit Bit", 2, 100);
        member.listBuyOrder("Fit Bit", 2, 105);
        member.listBuyOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Fit Bit", 5, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(2) &&
                BuyOffersDB.getBuyOffersDB().getOffer(3).getQuantity() == 1);
    }

    // test reducing the quantities with multiple buy orders, with the quantity ordered from buy offers being less
    @Test
    public void reduceSellToMultipleGreaterBuyQuantities() {
        member.listBuyOrder("Fit Bit", 1, 100);
        member.listBuyOrder("Fit Bit", 1, 105);
        member.listBuyOrder("Fit Bit", 1, 100);
        member.listBuyOrder("Fit Bit", 10, 90);
        otherMember.listSellOrder("Fit Bit", 5, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer();
        assertTrue(SellOffersDB.getSellOffersDB().getOffer(1).getQuantity() == 2 &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(2) &&
                !BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(3));
    }

    /**
     * Resolving trading assets - sell offers to a newly created buy offer
     * Reduce order quantities AND trade assets between buy and sell org
     */

    /*
        System.out.println("Buyer owns: " + management.getAssetsOwned());
        System.out.println("Seller owns: " + humanResources.getAssetsOwned());
        System.out.println("Buyer credits: " + management.getCredits());
        System.out.println("Seller credits: " + humanResources.getCredits());
     */

    // test trading assets between two organisations - equal quantity buy and sell offers
    @Test
    public void tradeAssetsEqualQuantity() throws Exception {
        // member is part of human resources - in this case is a seller
        // other member is part of management - in this case a buyer
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        member.listSellOrder("Fit Bit", 2, 100);

        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer(management, humanResources);

        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two organisations - more quantity buy offer than sell offer
    @Test
    public void tradeAssetsGreaterBuyOffer() throws Exception {
        // member is part of human resources - in this case is a seller
        // other member is part of management - in this case a buyer
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 10, 100);
        member.listSellOrder("Fit Bit", 2, 100);

        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer(management, humanResources);

        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two organisations - more quantity sell offer than buy offer
    @Test
    public void tradeAssetsGreaterSellOffer() throws Exception {
        // member is part of human resources - in this case is a seller
        // other member is part of management - in this case a buyer
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        member.listSellOrder("Fit Bit", 3, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer(management, humanResources);
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two organisations - with multiple sell offers
    @Test
    public void tradeAssetsMultipleSellOffers() throws Exception {
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        member.listSellOrder("Fit Bit", 2, 105);
        member.listSellOrder("Fit Bit", 1, 100);
        member.listSellOrder("Fit Bit", 1, 95);
        member.listSellOrder("Fit Bit", 1, 95);

        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer(management, humanResources);

        // 2 Fit Bits are bought and sold @95
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 810 &&
                humanResources.getCredits() == 1190);
    }


    /**
     * Resolving trading assets - sell offers to a newly created sell offer
     * Reduce order quantities AND trade assets between buy and sell org
     */
    @Test
    public void tradeAssetsSellToBuyOffer() throws Exception {
        // member - human resources = buyer
        // otherMember - management = seller
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        member.listSellOrder("Fit Bit", 2, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer(management, humanResources);
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two organisations when resolving a sell offer - more quantity buy offer than sell offer
    @Test
    public void tradeAssetsSellToGreaterBuyOffer() throws Exception {
        // member is part of human resources - in this case is a seller
        // other member is part of management - in this case a buyer
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 10, 100);
        member.listSellOrder("Fit Bit", 2, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer(management, humanResources);
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two organisations - more quantity sell offer than buy offer
    @Test
    public void tradeAssetsGreaterSellToBuyOffer() throws Exception {
        // member is part of human resources - in this case is a seller
        // other member is part of management - in this case a buyer
        humanResources.addAsset("Fit Bit", 3);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        member.listSellOrder("Fit Bit", 3, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer(management, humanResources);
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 5 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 800 &&
                humanResources.getCredits() == 1200);
    }

    // test trading assets between two orgs - with multiple buy offers at different prices
    @Test
    public void tradeAssetsSellToMultipleBuyOffers() throws Exception {
        humanResources.addAsset("Fit Bit", 5);
        management.addAsset("Fit Bit", 3);
        otherMember.listBuyOrder("Fit Bit", 2, 100);
        otherMember.listBuyOrder("Fit Bit", 2, 95);
        otherMember.listBuyOrder("Fit Bit", 2, 105);
        member.listSellOrder("Fit Bit", 4, 100);
        SellOffer sellOffer = SellOffersDB.getSellOffersDB().getOffer(1);
        sellOffer.resolveOffer(management, humanResources);
        // 4 Fit Bits are bought and sold @100
        assertTrue(management.getAssetsOwned().get("Fit Bit") == 7 &&
                humanResources.getAssetsOwned().get("Fit Bit") == 1 &&
                management.getCredits() == 600 &&
                humanResources.getCredits() == 1400);
    }
}