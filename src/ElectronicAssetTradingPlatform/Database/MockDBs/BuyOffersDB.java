package ElectronicAssetTradingPlatform.Database.MockDBs;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;



import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Mock database class for market buy offers DEPRECATED
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


    /**
     * Retrieve the singleton/create the DB object
     */
    public static BuyOffersDB getBuyOffersDB() {
        if (buyOffersDB == null) {
            return new BuyOffersDB();
        }
        else {
            return buyOffersDB;
        }
    }

    /**
     * Get the amount of offers in the DB
     */
    public int getSize() {
        return MarketBuyOffers.size();
    }

    /**
     * Retrieve a buy offer from the DB
     */
    public BuyOffer getOffer(int ID) {
        return MarketBuyOffers.get(ID);
    }


    /**
     * Insert a buy offer into the DB
     */
    public static void addBuyOffer(int ID, BuyOffer offer) {

        MarketBuyOffers.put(ID, offer);

    }

    /**
     * Remove an offer from the DB
     */
    public static void removeBuyOffer(int ID) {
        MarketBuyOffers.remove(ID);
    }

    /**
     * Remove all offers from the DB
     */
    public static void removeAllBuyOffers() { MarketBuyOffers.clear(); }



    @Override
    public String toString() {
        Iterator<Map.Entry<Integer, BuyOffer>> entries = MarketBuyOffers.entrySet().iterator();
        String MarketOffers = "";
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            MarketOffers += entry.getValue().toString();
            if (entries.hasNext()) {
                MarketOffers += "\n";
            }
        }
        return MarketOffers;
    }


    /**
     * Retrieve buy offers made by a particular organisation
     */
    public String getOrgBuyOffers(String org) {
        Iterator<Map.Entry<Integer, BuyOffer>> entries = MarketBuyOffers.entrySet().iterator();
        String OrgMarketOffers = "";
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            if (entry.getValue().getUnitName() == org) {
                OrgMarketOffers += entry.getValue().toString();
                if (entries.hasNext()) {
                    OrgMarketOffers += "\n";
                }
            }
        }
        return OrgMarketOffers;
    }
}
