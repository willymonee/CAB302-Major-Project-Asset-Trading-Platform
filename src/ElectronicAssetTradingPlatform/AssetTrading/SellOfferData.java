package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SellOfferData extends OfferData {
    private static TreeMap<Integer, SellOffer> MarketSellOffers = new TreeMap<>();

    // database connectivity
    // private static MarketplaceDataSource marketplaceDataSource = new MarketplaceDataSource();

    /**
     * Constructor to initialise the single BuyOfferData object - protected to suppress unauthorised calls
     */
    protected SellOfferData() { }

    /**
     * SellOfferDataHolder is loaded on the first execution of SellOfferData.getInstance() or the first access to
     * SellOfferData.INSTANCE, not before
     */
    private static class SellOfferDataHolder {
        private final static SellOfferData INSTANCE = new SellOfferData();
    }

    /**
     * Retrieve the INSTANCE of BuyOfferData
     */
    public static SellOfferData getInstance() {
        return SellOfferDataHolder.INSTANCE;
    }

    /**
     * Retrieve market buy offers from the database and insert them into the TreeMap
     */
    protected void getOffersFromDB() {
        TreeMap<Integer, SellOffer> sellOffers = MarketplaceDataSource.getInstance().getSellOffers();
        for (Map.Entry<Integer, SellOffer> sellOffer : sellOffers.entrySet()) {
            SellOffer nextOffer = sellOffer.getValue();
            MarketSellOffers.put(nextOffer.getOfferID(), nextOffer);
        }
    }

    /**
     * Update SellOfferData's MarketSellOffers field and then return it
     * @return TreeMap of the current market sell orders
     */
    public TreeMap<Integer, SellOffer> getMarketSellOffers() {
        getOffersFromDB();
        return MarketSellOffers;
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
        getOffersFromDB();
        return MarketSellOffers.get(ID);
    }

    /**
     * Remove an offer from the DB
     */
    public static void removeOffer(int ID) {
        MarketSellOffers.remove(ID);
        MarketplaceDataSource.getInstance().removeOffer(ID);
    }

    /**
     * Remove all sell offers from the DB
     */
    public static void removeAllSellOffers() { MarketSellOffers.clear(); }

    /**
     * Add a sell offer to the DB
     */
    public static void addSellOffer(SellOffer offer) {
        MarketplaceDataSource.getInstance().insertSellOffer(offer);
    }


    /**
     * Update the market sell offers stored in SellOfferData MarketSellOffers field and return them all as a string
     * @return String of all market sell offers stored in SellOfferData MarketSellOffers field
     */
    @Override
    public String toString() {
        getOffersFromDB();
        Iterator<Map.Entry<Integer, SellOffer>> sellOffersIter = MarketSellOffers.entrySet().iterator();
        StringBuilder MarketOffers = new StringBuilder("Sell Offers: \n");
        while (sellOffersIter.hasNext()) {
            Map.Entry<Integer, SellOffer> sellOffer = sellOffersIter.next();
            MarketOffers.append(sellOffer.getValue().toString());
            if (sellOffersIter.hasNext()) {
                MarketOffers.append("\n");
            }
        }
        return MarketOffers.toString();
    }

    /**
     * Return the sell offers of an organisational unit as a TreeMap
     */
    private TreeMap<Integer, SellOffer> getOrgOffersMap(String unitName) {
        getOffersFromDB();
        TreeMap<Integer, SellOffer> orgOffers = new TreeMap<>();
        for (Map.Entry<Integer, SellOffer> sellOffer : MarketSellOffers.entrySet()) {
            if (sellOffer.getValue().getUnitName().equals(unitName)) {
                orgOffers.put(sellOffer.getKey(), sellOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Retrieve the sell offers created by an organisation given the org's name and return it as a string
     */
    public String getOrgOffers(String unitName) {
        TreeMap<Integer, SellOffer> orgOffers = getOrgOffersMap(unitName);
        Iterator<Map.Entry<Integer, SellOffer>> sellOffersIter = orgOffers.entrySet().iterator();
        StringBuilder OrgMarketOffers = new StringBuilder();
        OrgMarketOffers.append(unitName).append("'s Sell Offers: \n");
        while (sellOffersIter.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = sellOffersIter.next();
            OrgMarketOffers.append(entry.getValue().toString());
            if (sellOffersIter.hasNext()) {
                OrgMarketOffers.append("\n");
            }
        }
        return OrgMarketOffers.toString();
    }
}
