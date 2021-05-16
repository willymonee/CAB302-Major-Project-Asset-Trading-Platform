package ElectronicAssetTradingPlatform.Database.MockDBs;


import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;

import java.util.Iterator;
import java.util.Map;
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
     */
    private SellOffersDB() { }

    public TreeMap<Integer, SellOffer> getMarketSellOffers() {
        return MarketSellOffers;
    }

    /**
     * Retrieve the singleton/create the DB object
     */
    public static SellOffersDB getSellOffersDB() {
        if (sellOffersDB == null) {
            return new SellOffersDB();
        }
        else {
            return sellOffersDB;
        }
    }

    /**
     * Return the amount of sell offers in the DB
     */
    public int getSize() {
        return MarketSellOffers.size();
    }

    /**
     * Return a sell offer from the DB based on its ID
     */
    public SellOffer getOffer(int ID) {
        return MarketSellOffers.get(ID);
    }

    /**
     * Remove a sell offer based on its ID
     */
    public static void removeSellOffer(int ID) {
        MarketSellOffers.remove(ID);
    }

    /**
     * Remove all sell offers from the DB
     */
    public static void removeAllSellOffers() { MarketSellOffers.clear(); }

    /**
     * Add a sell offer to the DB
     */
    public static void addSellOffer(int ID, SellOffer offer) {
        MarketSellOffers.put(ID, offer);
    }


    @Override
    public String toString() {
        Iterator<Map.Entry<Integer, SellOffer>> entries = MarketSellOffers.entrySet().iterator();
        String MarketOffers = "";
        while (entries.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = entries.next();
            MarketOffers +=  String.format(entry.getValue().toString());
            if (entries.hasNext()) {
                MarketOffers += "\n";
            }
        }
        return MarketOffers;
    }



    /**
     * Retrieve the sell offers created by an organisation given the org's name
     */
    public String getOrgSellOffers(String orgName) {
        Iterator<Map.Entry<Integer, SellOffer>> entries = MarketSellOffers.entrySet().iterator();
        String OrgMarketOffers = "";
        while (entries.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = entries.next();
            if (entry.getValue().getUnitName() == orgName) {
                OrgMarketOffers += entry.getValue().toString();
                if (entries.hasNext()) {
                    OrgMarketOffers += "\n";
                }
            }
        }
        return OrgMarketOffers;
    }
}
