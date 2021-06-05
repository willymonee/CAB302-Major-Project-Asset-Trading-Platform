package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Retrieves and inserts data relating to sell offers to/from the database via network.
 * Also contains methods which operate on the data retrieved from the database
 * Singleton class
 */
public class SellOfferData extends OfferData {
    private static TreeMap<Integer, SellOffer> MarketSellOffers = new TreeMap<>();
    private NetworkDataSource dataSource;


    /**
     * Constructor to initialise the single SellOfferData object - protected to suppress unauthorised calls
     */
    private SellOfferData() {
        dataSource = new NetworkDataSource();
        dataSource.run();
    }

    /**
     * SellOfferDataHolder is loaded on the first execution of SellOfferData.getInstance() or the first access to
     * SellOfferData.INSTANCE, not before
     */
    private static class SellOfferDataHolder {
        private final static SellOfferData INSTANCE = new SellOfferData();
    }

    /**
     * Retrieve the INSTANCE of SellOfferData
     */
    public static SellOfferData getInstance() {
        return SellOfferDataHolder.INSTANCE;
    }

    /**
     * Retrieve market sell offers from the database and insert them into the TreeMap
     */
    protected void getOffersFromDB()  {
        TreeMap<Integer, SellOffer> sellOffers = dataSource.getSellOffers();
        MarketSellOffers.clear();
        // add all retrieved offers from the db into the treemap
        for (Map.Entry<Integer, SellOffer> sellOffer : sellOffers.entrySet()) {
            SellOffer nextOffer = sellOffer.getValue();
            MarketSellOffers.put(nextOffer.getOfferID(), nextOffer);
        }
    }

    /**
     * Update SellOfferData's MarketSellOffers field and then return it
     * @return TreeMap of the current market sell orders
     */
    public TreeMap<Integer, SellOffer> getMarketSellOffers()  {
        getOffersFromDB();
        return MarketSellOffers;
    }

    /**
     * Checks if sell offer with that ID exists
     * @return true if the offer exists, false if the offer is no longer there e.g. if it was removed
     */
    @Override
    public boolean offerExists(int ID)  {
        getOffersFromDB();
        return MarketSellOffers.containsKey(ID);
    }

    /**
     * @param ID of the sell offer to be retrieved
     * @return Sell offer from the DB based on its ID
     */
    public SellOffer getOffer(int ID)  {
        getOffersFromDB();
        return MarketSellOffers.get(ID);
    }

    /**
     * Add a sell offer to the DB
     * @param offer to be added
     */
    public void addSellOffer(SellOffer offer) {
        dataSource.addSellOffer(offer);
    }


    /**
     * Retrieve all market sell offers as a string
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
     * Retrieve all sell offers from a particular organisational unit
     * @param unitName Selected organisational unit
     * @return TreeMap containing the ID and SellOffers of an organisational unit
     */
    public TreeMap<Integer, SellOffer> getOrgOffersMap(String unitName)  {
        getOffersFromDB();
        TreeMap<Integer, SellOffer> orgOffers = new TreeMap<>();
        // add all offers where the offer unit name is the same as the queried unit name
        for (Map.Entry<Integer, SellOffer> sellOffer : MarketSellOffers.entrySet()) {
            String sellOfferUnitName = sellOffer.getValue().getUnitName();
            if (sameOrgUnitName(unitName, sellOfferUnitName)) {
                orgOffers.put(sellOffer.getKey(), sellOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Retrieve all sell offers for a particular asset
     *
     * @param assetName of queried asset
     * @return the sell offers of a queried asset as a treemap
     */
    public TreeMap<Integer, SellOffer> getAssetOffers(String assetName) {
        getOffersFromDB();
        TreeMap<Integer, SellOffer> orgOffers = new TreeMap<>();
        for (Map.Entry<Integer, SellOffer> sellOffer : MarketSellOffers.entrySet()) {
            String sellOfferAssetName = sellOffer.getValue().getAssetName();
            if (sameOrgUnitName(assetName, sellOfferAssetName)) {
                orgOffers.put(sellOffer.getKey(), sellOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Return the price of the lowest priced sell offer for a particular asset
     *
     * @param assetName of queried asset
     * @return the price of the lowest priced sell offer for a particular asset as a double
     */
    public double getLowestPricedSellOffer(String assetName) {
        TreeMap<Integer, SellOffer> assetOffers = getAssetOffers(assetName);
        Iterator<Map.Entry<Integer, SellOffer>> sellOffersIter = assetOffers.entrySet().iterator();
        double lowestPrice = 0;
        // set the first sell offer as the lowest price
        if (sellOffersIter.hasNext()) {
            lowestPrice = sellOffersIter.next().getValue().getPricePerUnit();
        }
        // iterate through the rest looking for lower prices
        while (sellOffersIter.hasNext()) {
            double nextPrice = sellOffersIter.next().getValue().getPricePerUnit();
            if (nextPrice < lowestPrice) {
                lowestPrice = nextPrice;
            }
        }
        return lowestPrice;
    }

    /**
     * Calculate the total quantity for sale in sell offers for a particular asset
     * @param assetName of queried asset
     * @return the total quantity for sale for a particular asset
     */
    public int assetQuantity(String assetName) {
        TreeMap<Integer, SellOffer> assetOffers = getAssetOffers(assetName);
        int quantity = 0;
        // sum the quantities of offers with a particular asset name
        for (Map.Entry<Integer, SellOffer> integerSellOfferEntry : assetOffers.entrySet()) {
            int offerQuantity = integerSellOfferEntry.getValue().getQuantity();
            quantity += offerQuantity;
        }
        return quantity;
    }

    /**
     * Calculate the total quantity for sale in sell offers for a particular asset for a particular organisational unit
     *
     *  @param unitName of queried unit
     *  @param assetName of queried asset
     *  @return the total quantity for sale for a particular asset
     */
    public int quantityAssetInSellOffer(String unitName, String assetName) {
        int quantity = 0;
        TreeMap<Integer, SellOffer> orgOffersMap = getOrgOffersMap(unitName);
        // sum the quantities of offers with a particular asset name and unit name
        for (Map.Entry<Integer, SellOffer> sellOffer : orgOffersMap.entrySet()) {
            String sellOfferAssetName = sellOffer.getValue().getAssetName();
            if (sameOrgUnitName(assetName, sellOfferAssetName)) {
                quantity += sellOffer.getValue().getQuantity();
            }
        }
        return quantity;
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
