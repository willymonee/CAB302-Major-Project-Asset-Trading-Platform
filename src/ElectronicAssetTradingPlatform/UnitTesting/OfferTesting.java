package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import org.junit.jupiter.api.*;

import java.util.TreeMap;

public class OfferTesting {

    // declare a member object
    OrganisationalUnitMembers member;

    @BeforeEach @Test
    public void setUpMember() {
        // create an organisational unit member
         member = new OrganisationalUnitMembers("Sammy101", "asdf", "Human Resources");

    }

    // Creating a buy order
    @Test
    public void createBuyOrder() {
        member.listBuyOrder("Table", 10, 5.45);
        System.out.println(BuyOffersDB.getBuyOffersDB().getSize());
        System.out.println(BuyOffersDB.getBuyOffersDB().getOffer(1).displayOffer()); // maybe make an offer to string method
    }

    // Remove a buy order
    @Test
    public void removeBuyOrder() {
        member.removeBuyOffer(1);
        System.out.println(BuyOffersDB.getBuyOffersDB().getSize());
    }

    // Creating a sell offer
    @Test
    public void createSellOffer() {
        member.listSellOrder("Table",2,5.41);
        System.out.println(SellOffersDB.getSellOffersDB().getSize());
        System.out.println(SellOffersDB.getSellOffersDB().getOffer(1).displayOffer());

    }

    // Removing a sell offer
    @Test
    public void removeSellOffer() {
        member.removeSellOffer(1);
        System.out.println(SellOffersDB.getSellOffersDB().getSize());
    }


}
