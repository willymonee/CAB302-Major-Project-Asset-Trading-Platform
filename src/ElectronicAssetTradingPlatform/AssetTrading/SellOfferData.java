package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SellOfferData {
    private static TreeMap<Integer, SellOffer> MarketSellOffers = new TreeMap<>();

    // database connectivity
    private static MarketplaceDataSource marketplaceDataSource = new MarketplaceDataSource();

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
        TreeMap<Integer, SellOffer> sellOffers = marketplaceDataSource.getSellOffers();
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
     * Retrieve the sell offers created by an organisation given the org's name
     * TODO - not implemented yet
     */
    public String getOrgSellOffers(String orgName) {
        Iterator<Map.Entry<Integer, SellOffer>> entries = MarketSellOffers.entrySet().iterator();
        StringBuilder OrgMarketOffers = new StringBuilder();
        while (entries.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = entries.next();
            if (entry.getValue().getUnitName().equals(orgName)) {
                OrgMarketOffers.append(entry.getValue().toString());
                if (entries.hasNext()) {
                    OrgMarketOffers.append("\n");
                }
            }
        }
        return OrgMarketOffers.toString();
    }
}
