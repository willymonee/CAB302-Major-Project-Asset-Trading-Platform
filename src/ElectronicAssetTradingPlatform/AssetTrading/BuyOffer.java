package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;

import java.util.ArrayList;
import java.util.Iterator;

import java.sql.Date;
import java.util.Map;
import java.util.TreeMap;

public class BuyOffer extends Offer{
    private int orderID;
    private Date dateResolved;


    /**
     * Constructor for trade offer
     *  @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public BuyOffer(String asset, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
        this.orderID = createUniqueID();
    }



    @Override
    public int getOfferID() {
        return this.orderID;

    }

    @Override
    public String toString() {
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity()+ "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }



    @Override
    protected int createUniqueID() {
        if (BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().size() == 0) {
            return 1;
        }
        return BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().lastKey() + 1;
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
        long millis = System.currentTimeMillis();
        this.dateResolved = new Date(millis);
    }

    // function to return sell offers with matching asset name
    private ArrayList<SellOffer> matchingSellOffers() {
        ArrayList<SellOffer> matchingSellOffers = new ArrayList<>();
        TreeMap<Integer, SellOffer> sellOfferMap = SellOffersDB.getSellOffersDB().getMarketSellOffers();
        Iterator<Map.Entry<Integer, SellOffer>> entries = sellOfferMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = entries.next();
            if(entry.getValue().getAssetName() == this.getAssetName()) {
                SellOffer matchingOffer = entry.getValue();
                matchingSellOffers.add(matchingOffer);
            }
        }
        return matchingSellOffers;
    }

    // return the lowest priced sell offer, which is also equal or lower priced than the buy offer
    @Override
    public int checkMatchedOffer() {
        ArrayList<SellOffer> matchingSellOffers = matchingSellOffers();
        double buyOfferPrice = getPricePerUnit();
        // assign the first sell offer as the lowest price
        // convert map into entry set to iterate over
        Iterator<SellOffer> iter = matchingSellOffers.iterator();
        SellOffer lowestSellOffer;
        double lowestSellPrice;
        if (iter.hasNext()) {
            lowestSellOffer = iter.next();
            lowestSellPrice = lowestSellOffer.getPricePerUnit();
            while(iter.hasNext()) {
                SellOffer newOffer = iter.next();
                if (newOffer.getPricePerUnit() < lowestSellPrice) {
                    lowestSellPrice = newOffer.getPricePerUnit();
                    lowestSellOffer = newOffer;
                }

                else if ((newOffer.getPricePerUnit() == lowestSellPrice) &&
                        (newOffer.getDatePlaced().compareTo(lowestSellOffer.getDatePlaced()) > 0)) {
                    lowestSellPrice = newOffer.getPricePerUnit();
                    lowestSellOffer = newOffer;
                }
            }
            // check if the lowest matching sell offer is equal or less than they buy offer's price
            if (lowestSellPrice <= buyOfferPrice) {
                return lowestSellOffer.getOfferID();
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }



}
