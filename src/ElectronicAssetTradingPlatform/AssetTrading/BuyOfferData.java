package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Retrieves and inserts data relating to buy offers to/from the database via network.
 * Also contains methods which operate on the data retrieved from the database
 * Singleton class
 */
public class BuyOfferData extends OfferData {
    private static TreeMap<Integer, BuyOffer> MarketBuyOffers = new TreeMap<>();
    private NetworkDataSource dataSource;

    /**
     * Constructor to initialise the single BuyOfferData object - protected to suppress unauthorised calls
     */
    protected BuyOfferData() {
        dataSource = new NetworkDataSource();
        dataSource.run();
    }

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
     * Retrieve market buy offers from the database and insert them into the TreeMap to be used
     */
    protected void getOffersFromDB() {
        TreeMap<Integer, BuyOffer> buyOffers = dataSource.getBuyOffers();
        MarketBuyOffers.clear();
        // add all offers in
        for (Map.Entry<Integer, BuyOffer> buyOffer : buyOffers.entrySet()) {
            BuyOffer nextOffer = buyOffer.getValue();
            MarketBuyOffers.put(nextOffer.getOfferID(), nextOffer);
        }
    }

    /**
     * Return all market buy offers in a TreeMap
     * @return TreeMap of all market buy offers
     */
    public TreeMap<Integer, BuyOffer> getMarketBuyOffers() {
        getOffersFromDB();
        return MarketBuyOffers;
    }

    /**
     * Retrieve a buy offer from the DB
     * @param ID of the buy offer to be retrieved
     * @return selected BuyOffer object
     */
    public BuyOffer getOffer(int ID) {
        getOffersFromDB();
        return MarketBuyOffers.get(ID);
    }

    /**
     * Checks if buy offer with that ID exists
     * @return true if the offer exists, false if the offer is no longer there e.g. if it was removed
     */
    @Override
    public boolean offerExists(int ID) {
        getOffersFromDB();
        return MarketBuyOffers.containsKey(ID);
    }

    /**
     * Insert a buy offer into the DB
     * @param offer to be added
     */
    public void addBuyOffer(BuyOffer offer)  {
        dataSource.addBuyOffer(offer);
    }

    /**
     * Retrieve all market buy offers as a string
     * @return String of all market buy offers stored in BuyOfferData MarketBuyOffers field
     */
    @Override
    public String toString() {
        getOffersFromDB();
        Iterator<Map.Entry<Integer, BuyOffer>> buyOffersIter = MarketBuyOffers.entrySet().iterator();
        StringBuilder MarketOffers = new StringBuilder("Buy Offers: \n");
        // append all offers
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
     * Retrieve all offers from a particular organisational unit
     * @param unitName Selected organisational unit
     * @return TreeMap containing the ID and BuyOffers of an organisational unit
     */
    public TreeMap<Integer, BuyOffer> getOrgOffersMap(String unitName) {
        getOffersFromDB();
        TreeMap<Integer, BuyOffer> orgOffers = new TreeMap<>();
        // add all offers where the offer unit name is the same as the queried unit name
        for (Map.Entry<Integer, BuyOffer> buyOffer : MarketBuyOffers.entrySet()) {
            String buyOfferUnitName = buyOffer.getValue().getUnitName();
            if (sameOrgUnitName(unitName, buyOfferUnitName )) {
                orgOffers.put(buyOffer.getKey(), buyOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Calculate the amount of credits currently in use (in buy offers) for a particular unit
     *
     * @param unitName of the selected unit
     * @return quantity of credits being used in buy offer by the organisational unit as a double
     */
    public double creditsInBuyOffers(String unitName) {
        double creditsInUse = 0;
        TreeMap<Integer, BuyOffer> orgOffers = getOrgOffersMap(unitName);
        Iterator<Map.Entry<Integer, BuyOffer>> buyOffersIter = orgOffers.entrySet().iterator();
        // add the total credit value of each offer into creditsInUse
        while(buyOffersIter.hasNext()) {
            BuyOffer offer = buyOffersIter.next().getValue();
            creditsInUse += offer.getQuantity() * offer.getPricePerUnit();
        }
        return creditsInUse;
    }

    /**
     * Retrieve all buy offers for a particular asset
     *
     * @param assetName of queried asset
     * @return the buy offers of a queried asset as a treemap
     */
    public TreeMap<Integer, BuyOffer> getAssetOffers(String assetName) {
        getOffersFromDB();
        TreeMap<Integer, BuyOffer> assetOffers = new TreeMap<>();
        // add a buy offer if the offer's asset name is the same as the queried asset name
        for (Map.Entry<Integer, BuyOffer> buyOffer : MarketBuyOffers.entrySet()) {
            String buyOfferAssetName = buyOffer.getValue().getAssetName();
            if (sameAssetName(assetName, buyOfferAssetName )) {
                assetOffers.put(buyOffer.getKey(), buyOffer.getValue());
            }
        }
        return assetOffers;
    }

    /**
     * Find the price of the highest priced buy offer for a particular asset
     * @param assetName of queried asset
     * @return the price of the highest priced buy offer for a particular asset as a double
     */
    public double getHighestPrice(String assetName) {
        TreeMap<Integer, BuyOffer> assetOffers = getAssetOffers(assetName);
        Iterator<Map.Entry<Integer, BuyOffer>> buyOffersIter = assetOffers.entrySet().iterator();
        double highestPrice = 0;
        // set the first buy offer as the highest price
        if (buyOffersIter.hasNext()) {
            highestPrice = buyOffersIter.next().getValue().getPricePerUnit();
        }
        // iterate through the rest of the offers looking for higher prices
        while (buyOffersIter.hasNext()) {
            double nextPrice = buyOffersIter.next().getValue().getPricePerUnit();
            if (nextPrice > highestPrice) {
                highestPrice = nextPrice;
            }
        }
        return highestPrice;
    }

    /**
     * Calculate the total quantity requested in buy offers for a particular asset
     * @param assetName of queried asset
     * @return the total quantity requested for a particular asset
     */
    public int assetQuantity(String assetName) {
        TreeMap<Integer, BuyOffer> assetOffers = getAssetOffers(assetName);
        int quantity = 0;
        // sum the quantities of buy offers with a particular asset name
        for (Map.Entry<Integer, BuyOffer> integerBuyOfferEntry : assetOffers.entrySet()) {
            int offerQuantity = integerBuyOfferEntry.getValue().getQuantity();
            quantity += offerQuantity;
        }
        return quantity;
    }

    /**
     * Retrieve buy offers made by a particular organisation as a string
     */
    @Override
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
