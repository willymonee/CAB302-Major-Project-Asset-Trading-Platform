package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import org.junit.jupiter.api.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class OfferTesting {

    // declare a member object
    OrganisationalUnitMembers member;
    OrganisationalUnitMembers otherMember;
    Date date;

    @BeforeEach @Test
    public void setUp() {
         // create an organisational unit member
         member = new OrganisationalUnitMembers("Sammy101", "asdf", "Human Resources");
         otherMember = new OrganisationalUnitMembers("Linax0x", "asdf",
                "Management");
         // get the current date
        long millis = System.currentTimeMillis();
        date = new Date(millis);
    }

    // clear the buy and sell offer databases before each test
    @AfterEach @Test
    public void clearDataBase() {
        BuyOffersDB.removeAllBuyOffers();
        SellOffersDB.removeAllSellOffers();
    }

    // Creating a buy order
    @Test
    public void createBuyOrder() {
        member.listBuyOrder("Table", 10, 5.45);
        assertEquals("1\tTable\t10\t $5.45\tSammy101\tHuman Resources\t" + date,
                BuyOffersDB.getBuyOffersDB().getOffer(1).toString(), "Listing Buy Offer Failed");
    }

    // Remove a buy order
    @Test
    public void removeBuyOrder() {
        member.listBuyOrder("Table", 10, 5.45);
        member.removeBuyOffer(1);
        assertEquals(0, BuyOffersDB.getBuyOffersDB().getSize(), "Removing Buy Offer Failed");
    }

    // Creating a sell offer
    @Test
    public void createSellOffer() {
        member.listSellOrder("Chair",2,5.41);
        assertEquals("1\tChair\t2\t $5.41\tSammy101\tHuman Resources\t" + date,
                SellOffersDB.getSellOffersDB().getOffer(1).toString(), "Listing Sell Offer Failed");
    }

    // Removing a sell offer
    @Test
    public void removeSellOffer() {
        member.listSellOrder("Chair",2,5.41);
        member.removeSellOffer(1);
        assertEquals(0, SellOffersDB.getSellOffersDB().getSize());
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
        member.listSellOrder("Drawing Tablet",1,105.41);
        member.listSellOrder("Calculator",5,20);
        member.listSellOrder("Adobe Photoshop Monthly Subscription",10,10);
        member.listSellOrder("Coffee Maker",1,65);
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
        member.listSellOrder("Drawing Tablet",1,105.41);
        member.listSellOrder("Calculator",5,20);
        member.listSellOrder("Adobe Photoshop Monthly Subscription",10,10);
        member.listSellOrder("Coffee Maker",1,65);
        OrganisationalUnitMembers otherMember = new OrganisationalUnitMembers("Linax0x", "asdf",
                "Management");
        otherMember.listSellOrder("Herman Miller Embody Chair", 2, 600);
        otherMember.listSellOrder("Pencil Case", 1, 3.55);
        assertEquals("5\tHerman Miller Embody Chair\t2\t $600.0\tLinax0x\tManagement\t" + date + "\n" +
                "6\tPencil Case\t1\t $3.55\tLinax0x\tManagement\t" + date, otherMember.getOrgSellOffers());

    }

    // test checking if there is a corresponding sell offer after creating a buy offer of equal price
    @Test
    public void checkMatchingSellOffer() {
        member.listSellOrder("Calculator",1,20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(1, BuyOffersDB.getBuyOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching sell offer");

    }

    // test checking if there is a corresponding sell offer after creating a buy offer of equal price
    @Test
    public void checkMatchingSellOfferLowerPrice() {
        member.listSellOrder("Calculator",1,18);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(1, BuyOffersDB.getBuyOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching sell offer");
    }

    // test returning lowest matching sell offer, given two sell offers
    @Test
    public void returnLowestPricedSellOffer() {
        member.listSellOrder("Calculator",1,18);
        member.listSellOrder("Calculator",1,17);
        member.listSellOrder("Calculator",1,20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(2, BuyOffersDB.getBuyOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching sell offer");

    }

    // test checking if there is no corresponding sell offer with the same asset name
    @Test
    public void checkNoMatchingSellOfferAssetName() {
        member.listSellOrder("Table", 1, 20);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(0,BuyOffersDB.getBuyOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to check that there were no matching sell offers for the item");

    }

    // test checking if there is no corresponding sell offer with equal or lower price
    @Test
    public void checkNoMatchingSellOfferAssetPrice() {
        member.listSellOrder("Table", 1, 25);
        otherMember.listBuyOrder("Calculator", 1, 20);
        assertEquals(0,BuyOffersDB.getBuyOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to check that there were no matching sell offers for the item");

    }

    // testing return a buy order ID which matches a sell order of equal price
    @Test
    public void returnMatchingBuyOrder() {
        member.listBuyOrder("iPad", 1, 100);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(1, SellOffersDB.getSellOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching buy offer");
    }

    // testing return a buy order ID which matches a sell order of equal price
    @Test
    public void returnMatchingBuyOrderHigherPrice() {
        member.listBuyOrder("iPad", 1, 110);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(1, SellOffersDB.getSellOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching buy offer");
    }

    // testing returning the buy order with the lowest price, but equal or above the sell order,
    // with other buy orders above the sell offer's price
    @Test
    public void returnLowestMatchingBuyOrder() {
        member.listBuyOrder("iPad", 1, 110);
        member.listBuyOrder("iPad", 1, 120);
        member.listBuyOrder("iPad", 1, 105);
        member.listBuyOrder("iPad", 1, 130);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(3, SellOffersDB.getSellOffersDB().getOffer(1).checkMatchedOffer(),
                "Failed to return matching buy offer");
    }

    // testing not returning a buy offer with a different asset type
    @Test
    public void returnNoMatchingBuyOrderAsset() {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(0, SellOffersDB.getSellOffersDB().getOffer(1).checkMatchedOffer(),
                "Returned a matching buy offer, but not meant to");
    }

    // test not returning a buy offer with a lower price than the sell offer
    @Test
    public void returnNoMatchingBuyOrderPrice() {
        member.listBuyOrder("Fit Bit", 1, 90);
        otherMember.listSellOrder("iPad", 1, 100);
        assertEquals(0, SellOffersDB.getSellOffersDB().getOffer(1).checkMatchedOffer(),
                "Returned a matching buy offer, but not meant to");
    }

    // test reducing the quantities of matching buy and sell offers, when equal quantities
    // expect both offers to be removed
    // testing a private method
    @Test
    public void reduceEqualOfferQuantities() {
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
    public void reduceGreaterBuyOfferQuantities() {
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
    public void reduceGreaterSellOfferQuantities() {
        member.listBuyOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 3, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                SellOffersDB.getSellOffersDB().getOffer(1).getQuantity() == 2);
    }

    // test reducing the quantities with multiple sell offers, with more sell quantity than buy
    @Test
    public void reduceQuantityMultipleSellOffers() {
        member.listBuyOrder("Fit Bit", 3, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        otherMember.listSellOrder("Fit Bit", 1, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                !SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(2) &&
                BuyOffersDB.getBuyOffersDB().getOffer(1).getQuantity() == 1);
    }

    // test reducing the quantities with multiple sell offers, with more buy quantity than sell
    @Test
    public void reduceQuantityMultipleSellOffersGreaterQuantity() {
        member.listBuyOrder("Fit Bit", 3, 100);
        otherMember.listSellOrder("Fit Bit", 2, 100);
        otherMember.listSellOrder("Fit Bit", 2, 100);
        BuyOffer buyOffer = BuyOffersDB.getBuyOffersDB().getOffer(1);
        buyOffer.resolveOffer();
        assertTrue(!BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().containsKey(1) &&
                !SellOffersDB.getSellOffersDB().getMarketSellOffers().containsKey(1) &&
                SellOffersDB.getSellOffersDB().getOffer(2).getQuantity() == 1);
    }


    // test adding a sell offer with an asset name that has not been registered


    // test retrieving market buy offers which is empty --> doesn't seem to cause errors but maybe throw an exception
    @Test
    public void returnEmptyMarketBuyOffers() {
        // System.out.println(BuyOffersDB.getBuyOffersDB().toString());
    }

    // test retrieving market sell offers which is empty




}
