package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import com.sun.source.tree.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Retrieves and inserts data relating to buy offers to/from the database.
 * Singleton class
 */
public class BuyOfferData {
    private static TreeMap<Integer, BuyOffer> MarketBuyOffers = new TreeMap<>();

    // database connectivity
    private static MarketplaceDataSource marketplaceDataSource = new MarketplaceDataSource();

    /**
     * Constructor to initialise the single BuyOfferData object - protected to suppress unauthorised calls
     */
    protected BuyOfferData() { }

    /**
     * BuyOfferDataHolder is loaded on the first execution of BuyOfferData.getInstance() or the first access to
     * BuyOfferData.INSTANCE, not before
     */
    private static class BuyOfferDataHolder {
        private final static BuyOfferData INSTANCE = new BuyOfferData();
    }

    /**
     * Retrieve the INSTANCE of BuyOfferData
     */
    public static BuyOfferData getInstance() {
        return BuyOfferDataHolder.INSTANCE;
    }

    /**
     * Retrieve market buy offers from the database and insert them into the TreeMap
     */
    protected void getOffersFromDB() {
        TreeMap<Integer, BuyOffer> buyOffers = marketplaceDataSource.getBuyOffers();
        for (Map.Entry<Integer, BuyOffer> buyOffer : buyOffers.entrySet()) {
            BuyOffer nextOffer = buyOffer.getValue();
            MarketBuyOffers.put(nextOffer.getOfferID(), nextOffer);
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
    public static void addOffer(BuyOffer offer) {
        marketplaceDataSource.insertBuyOffer(offer);
    }

    /**
     * Remove an offer from the DB
     */
    public static void removeOffer(int ID) {
        MarketBuyOffers.remove(ID);
    }

    /**
     * Remove all offers from the DB
     */
    public static void removeAllBuyOffers() { MarketBuyOffers.clear(); }

    public TreeMap<Integer, BuyOffer> getMarketBuyOffers() {
        return MarketBuyOffers;
    }


    /**
     * Update the market buy offers stored in BuyOfferData MarketBuyOffers field and return them all as a string
     * @return String of all market buy offers stored in BuyOfferData MarketBuyOffers field
     */
    @Override
    public String toString() {
        // update MarketBuyOffers field from database
        getOffersFromDB();
        Iterator<Map.Entry<Integer, BuyOffer>> buyOffersIter = MarketBuyOffers.entrySet().iterator();
        StringBuilder MarketOffers = new StringBuilder("Buy Offers: \n");
        while (buyOffersIter.hasNext()) {
            Map.Entry<Integer, BuyOffer> buyOffer = buyOffersIter.next();
            MarketOffers.append(buyOffer.getValue().toString());
            if (buyOffersIter.hasNext()) {
                MarketOffers.append("\n");
            }
        }
        return MarketOffers.toString();
    }


    /**
     * Retrieve buy offers made by a particular organisation
     * TODO - not implemented yet
     */
    public String getOrgOffers(String org) {
        Iterator<Map.Entry<Integer, BuyOffer>> entries = MarketBuyOffers.entrySet().iterator();
        StringBuilder OrgMarketOffers = new StringBuilder();
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            if (entry.getValue().getUnitName().equals(org)) {
                OrgMarketOffers.append(entry.getValue().toString());
                if (entries.hasNext()) {
                    OrgMarketOffers.append("\n");
                }
            }
        }
        return OrgMarketOffers.toString();
    }
}
