package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import org.junit.jupiter.api.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class OfferTesting {

    // declare a member object
    OrganisationalUnitMembers member;
    Date date;

    @BeforeEach @Test
    public void setUp() {
         // create an organisational unit member
         member = new OrganisationalUnitMembers("Sammy101", "asdf", "Human Resources");
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

        OrganisationalUnitMembers otherMember = new OrganisationalUnitMembers("Linax0x", "asdf",
                "Management");
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
        System.out.println(otherMember.getOrgSellOffers());
        assertEquals("5\tHerman Miller Embody Chair\t2\t $600.0\tLinax0x\tManagement\t" + date + "\n" +
                "6\tPencil Case\t1\t $3.55\tLinax0x\tManagement\t" + date, otherMember.getOrgSellOffers());

    }

    // test adding a buy offer with an asset name that has not been registered


    // test adding a sell offer with an asset name that has not been registered


    // test retrieving market buy offers which is empty --> doesn't seem to cause errors but maybe throw an exception
    @Test
    public void returnEmptyMarketBuyOffers() {
        System.out.println(BuyOffersDB.getBuyOffersDB().toString());
    }

    // test retrieving market sell offers which is empty




}
