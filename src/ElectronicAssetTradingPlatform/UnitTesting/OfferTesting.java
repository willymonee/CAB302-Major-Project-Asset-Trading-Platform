package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
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
        System.out.println(BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().get(1).displayOffer());
    }


}
