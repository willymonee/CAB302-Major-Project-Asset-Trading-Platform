package ElectronicAssetTradingPlatform.AssetTrading;


import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SellOffer extends Offer{
    
    private int orderID;
    private Date dateResolved;

    /**
     * Constructor for trade offer
     *
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public SellOffer(String asset, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
        this.orderID = createUniqueID();
    }


    public int getSellOfferID() {
        return this.orderID;
    }

    @Override
    public String toString() {
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity()+ "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }

    @Override
    public int getOfferID() {
        return this.orderID;
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
        long millis = System.currentTimeMillis();
        dateResolved = new Date(millis);
    }


    // return an arraylist of buy offers with the same asset name as the sell offer
    private ArrayList<BuyOffer> matchingBuyOffers() {
        ArrayList<BuyOffer> matchingBuyOffers = new ArrayList<>();
        TreeMap<Integer, BuyOffer> buyOfferMap = BuyOffersDB.getBuyOffersDB().getMarketBuyOffers();
        Iterator<Map.Entry<Integer, BuyOffer>> entries = buyOfferMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            if(entry.getValue().getAssetName() == this.getAssetName()) {
                BuyOffer matchingOffer = entry.getValue();
                matchingBuyOffers.add(matchingOffer);
            }
        }
        return matchingBuyOffers;
    }

    // return the matching buy offer's ID which has the lowest price, but is equal or greater than the sell offer's price
    @Override
    public int checkMatchedOffer() {
        ArrayList<BuyOffer> matchingBuyOffers = matchingBuyOffers();
        double sellOfferPrice = getPricePerUnit();
        // assign the first sell offer as the lowest price
        // convert map into entry set to iterate over
        Iterator<BuyOffer> iter = matchingBuyOffers.iterator();
        BuyOffer lowestBuyOffer;
        double lowestBuyPrice;
        if (iter.hasNext()) {
            lowestBuyOffer = iter.next();
            lowestBuyPrice = lowestBuyOffer.getPricePerUnit();
            while(iter.hasNext()) {
                BuyOffer newOffer = iter.next();
                if (newOffer.getPricePerUnit() < lowestBuyPrice) {
                    lowestBuyPrice = newOffer.getPricePerUnit();
                    lowestBuyOffer = newOffer;
                }

                else if ((newOffer.getPricePerUnit() == lowestBuyPrice) &&
                        (newOffer.getDatePlaced().compareTo(lowestBuyOffer.getDatePlaced()) > 0)) {
                    lowestBuyPrice = newOffer.getPricePerUnit();
                    lowestBuyOffer = newOffer;
                }
            }
            // check if the lowest matching sell offer is equal or less than they buy offer's price
            if (lowestBuyPrice >= sellOfferPrice) {
                return lowestBuyOffer.getOfferID();
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }



    @Override
    protected int createUniqueID() {
        if (SellOffersDB.getSellOffersDB().getMarketSellOffers().size() == 0) {
            return 1;
        }
        return SellOffersDB.getSellOffersDB().getMarketSellOffers().lastKey() + 1;
    }
}
