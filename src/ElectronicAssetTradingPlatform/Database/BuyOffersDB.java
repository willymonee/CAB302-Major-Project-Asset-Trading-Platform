package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;


import java.util.Iterator;
import java.util.Map;
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

    public BuyOffer getOffer(int ID) {
        return MarketBuyOffers.get(ID);
    }


    public static void addBuyOffer(int ID, BuyOffer offer) {
        MarketBuyOffers.put(ID, offer);
    }

    public static void removeBuyOffer(int ID) {
        MarketBuyOffers.remove(ID);
    }

    public static void removeAllBuyOffers() { MarketBuyOffers.clear(); }


    public static BuyOffersDB getBuyOffersDB() {
        if (buyOffersDB == null) {
            return new BuyOffersDB();
        }
        else {
            return buyOffersDB;
        }
    }

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


    public String getOrgBuyOffers(String orgName) {
        Iterator<Map.Entry<Integer, BuyOffer>> entries = MarketBuyOffers.entrySet().iterator();
        String OrgMarketOffers = "";
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
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
