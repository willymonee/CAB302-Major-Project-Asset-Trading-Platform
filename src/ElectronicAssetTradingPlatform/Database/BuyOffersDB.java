package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;


import java.util.TreeMap;

/**
 * Mock database class for market buy offers
 */
public class BuyOffersDB {
    private static TreeMap<Integer, BuyOffer> MarketBuyOffers = new TreeMap<>();

    // instantiate DB as null
    private static BuyOffersDB buyOffersDB = null;

    /**
     * Constructor to initialise the single AssetCollection object. This is a private constructor to restrict further
     * creation of it in other classes
     *
     */
    private BuyOffersDB() { }

    public TreeMap<Integer, BuyOffer> getMarketBuyOffers() {
        return MarketBuyOffers;
    }

    public int getSize() {
        return MarketBuyOffers.size();
    }



    public static void addBuyOffer(int ID, BuyOffer offer) {
        MarketBuyOffers.put(ID, offer);
    }

    public static void removeBuyOffer(int ID) {
        MarketBuyOffers.remove(ID);
    }


    public static BuyOffersDB getBuyOffersDB() {
        if (buyOffersDB == null) {
            return new BuyOffersDB();
        }
        else {
            return buyOffersDB;
        }
    }

}
