package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import static java.util.Collections.unmodifiableMap;

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
    protected void getOffersFromDB() {
        TreeMap<Integer, SellOffer> sellOffers = dataSource.getSellOffers();
        MarketSellOffers.clear();
        for (Map.Entry<Integer, SellOffer> sellOffer : sellOffers.entrySet()) {
            SellOffer nextOffer = sellOffer.getValue();
            MarketSellOffers.put(nextOffer.getOfferID(), nextOffer);
        }
    }

    /**
     * Update SellOfferData's MarketSellOffers field and then return it
     * @return Unmodifiable Map of the current market sell orders
     */
    public Map<Integer, SellOffer> getMarketSellOffers() {
        getOffersFromDB();
        return unmodifiableMap(MarketSellOffers) ;
    }

    /**
     * Checks if sell offer with that ID exists
     */
    @Override
    public boolean offerExists(int ID) {
        getOffersFromDB();
        return MarketSellOffers.containsKey(ID);
    }

    /**
     * Return a sell offer from the DB based on its ID
     */
    public SellOffer getOffer(int ID) {
        getOffersFromDB();
        return MarketSellOffers.get(ID);
    }


    /**
     * Add a sell offer to the DB
     */
    public void addSellOffer(SellOffer offer) {
        dataSource.addSellOffer(offer);
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
    public TreeMap<Integer, SellOffer> getOrgOffersMap(String unitName) {
        getOffersFromDB();
        TreeMap<Integer, SellOffer> orgOffers = new TreeMap<>();
        for (Map.Entry<Integer, SellOffer> sellOffer : MarketSellOffers.entrySet()) {
            String sellOfferUnitName = sellOffer.getValue().getUnitName();
            if (sameOrgUnitName(unitName, sellOfferUnitName)) {
                orgOffers.put(sellOffer.getKey(), sellOffer.getValue());
            }
        }
        return orgOffers;
    }

    /**
     * Return the sell offers of a particular asset as a TreeMap
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
     * Return the price of the highest priced buy offer for a particular asset
     */
    public double getLowestPricedSellOffer(String assetName) {
        TreeMap<Integer, SellOffer> assetOffers = getAssetOffers(assetName);
        Iterator<Map.Entry<Integer, SellOffer>> sellOffersIter = assetOffers.entrySet().iterator();
        double lowestPrice = 0;
        if (sellOffersIter.hasNext()) {
            lowestPrice = sellOffersIter.next().getValue().getPricePerUnit();
        }
        while (sellOffersIter.hasNext()) {
            double nextPrice = sellOffersIter.next().getValue().getPricePerUnit();
            if (nextPrice < lowestPrice) {
                lowestPrice = nextPrice;
            }
        }
        return lowestPrice;
    }

    /**
     * Return the quantity of an asset for sale
     */
    public int quantitySellOffersForAsset(String assetName) {
        TreeMap<Integer, SellOffer> assetOffers = getAssetOffers(assetName);
        int quantity = 0;
        Iterator<Map.Entry<Integer, SellOffer>> sellOffersIter = assetOffers.entrySet().iterator();
        while (sellOffersIter.hasNext()) {
            int offerQuantity = sellOffersIter.next().getValue().getQuantity();
            quantity += offerQuantity;
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
