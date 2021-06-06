package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.TreeMap;

/**
 * Testing adding and removing buy and sell offers (using the DB) - for more rigorous testing see
 * @OfferTestingDeprecated
 */
public class MarketplaceTesting {
    @BeforeAll @Test
    public static void Start() {
        System.out.println("Marketplace");
    }

    @BeforeEach
    @Test
    public void setupMarketplace() {
        // remove all buy offers
        MarketplaceDataSource.getInstance().removeAllOffers();
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
}
