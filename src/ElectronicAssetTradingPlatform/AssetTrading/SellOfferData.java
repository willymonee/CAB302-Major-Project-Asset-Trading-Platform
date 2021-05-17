package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SellOfferData {
    private static TreeMap<Integer, SellOffer> MarketSellOffers = new TreeMap<>();

    // local map
    private static SellOfferData sellOfferData = null;

    // database connectivity
    private static MarketplaceDataSource marketplaceDataSource = new MarketplaceDataSource();

    /**
     * Constructor to initialise the single AssetCollection object. This is a private constructor to restrict further
     * creation of it in other classes
     */
    private SellOfferData() { }

    public TreeMap<Integer, SellOffer> getMarketSellOffers() {
        return MarketSellOffers;
    }

    /**
     * Retrieve the singleton/create the DB object
     */
    public static SellOfferData getSellOffersData() {
        if (sellOfferData == null) {
            return new SellOfferData();
        }
        else {
            return sellOfferData;
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
    public static void addSellOffer(SellOffer offer) {
        marketplaceDataSource.insertSellOffer(offer);
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
