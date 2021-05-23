package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import com.sun.source.tree.Tree;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Retrieves and inserts data relating to buy offers to/from the database.
 * Singleton class
 */

public class BuyOfferData extends OfferData {
    private static TreeMap<Integer, BuyOffer> MarketBuyOffers = new TreeMap<>();

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
        TreeMap<Integer, BuyOffer> buyOffers = MarketplaceDataSource.getInstance().getBuyOffers();
        for (Map.Entry<Integer, BuyOffer> buyOffer : buyOffers.entrySet()) {
            BuyOffer nextOffer = buyOffer.getValue();
            MarketBuyOffers.put(nextOffer.getOfferID(), nextOffer);
        }
    }

    /**
     * Retrieve a buy offer from the DB
     */
    public BuyOffer getOffer(int ID) {
        getOffersFromDB();
        return MarketBuyOffers.get(ID);
    }

    /**
     * Checks if offer with that ID exists
     */
    public boolean offerExists(int ID) {
        getOffersFromDB();
        return MarketBuyOffers.containsKey(ID);
    }


    /**
     * Insert a buy offer into the DB
     */
    public static void addOffer(BuyOffer offer)  {
        MarketplaceDataSource.getInstance().insertBuyOffer(offer);
    }

    /**
     * Remove an offer from the DB
     */
    public static void removeOffer(int ID) {
        MarketBuyOffers.remove(ID);
        MarketplaceDataSource.getInstance().removeOffer(ID);
    }


    public TreeMap<Integer, BuyOffer> getMarketBuyOffers() {
        getOffersFromDB();
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
     * Return the buy offers of an organisational unit as a TreeMap
     */
    private TreeMap<Integer, BuyOffer> getOrgOffersMap(String unitName) {
        getOffersFromDB();
        TreeMap<Integer, BuyOffer> orgOffers = new TreeMap<>();
        for (Map.Entry<Integer, BuyOffer> buyOffer : MarketBuyOffers.entrySet()) {
            if (buyOffer.getValue().getUnitName().equals(unitName)) {
                orgOffers.put(buyOffer.getKey(), buyOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Retrieve buy offers made by a particular organisation as a string
     */
    public String getOrgOffers(String unitName) {
        TreeMap<Integer, BuyOffer> orgOffers = getOrgOffersMap(unitName);
        Iterator<Map.Entry<Integer, BuyOffer>> buyOffersIter = orgOffers.entrySet().iterator();
        StringBuilder OrgMarketOffers = new StringBuilder();
        OrgMarketOffers.append(unitName).append("'s Buy Offers: \n");
        while (buyOffersIter.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = buyOffersIter.next();
            OrgMarketOffers.append(entry.getValue().toString());
            if (buyOffersIter.hasNext()) {
                OrgMarketOffers.append("\n");
            }
        }
        return OrgMarketOffers.toString();
    }
}
