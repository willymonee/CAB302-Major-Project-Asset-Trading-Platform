package ElectronicAssetTradingPlatform.Database;


import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;

import java.util.TreeMap;

/**
 * Mock database class for market sell offers
 */
public class SellOffersDB {
    private static TreeMap<Integer, SellOffer> MarketSellOffers = new TreeMap<>();

    // instantiate DB as null
    private static SellOffersDB sellOffersDB = null;

    /**
     * Constructor to initialise the single AssetCollection object. This is a private constructor to restrict further
     * creation of it in other classes
     *
     */
    private SellOffersDB() { }

    public TreeMap<Integer, SellOffer> getMarketSellOffers() {
        return MarketSellOffers;
    }

    public int getSize() {
        return MarketSellOffers.size();
    }

    public SellOffer getOffer(int ID) {
        return MarketSellOffers.get(ID);
    }


    public static void addSellOffer(int ID, SellOffer offer) {
        MarketSellOffers.put(ID, offer);
    }

    public static void removeSellOffer(int ID) {
        MarketSellOffers.remove(ID);
    }


    public static SellOffersDB getSellOffersDB() {
        if (sellOffersDB == null) {
            return new SellOffersDB();
        }
        else {
            return sellOffersDB;
        }
    }
}
