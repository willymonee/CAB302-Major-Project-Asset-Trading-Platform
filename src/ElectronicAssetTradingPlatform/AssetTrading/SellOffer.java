package ElectronicAssetTradingPlatform.AssetTrading;


import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SellOffer extends Offer {

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
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity() + "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }

    @Override
    public int getOfferID() {
        return this.orderID;
    }


    @Override
    public void resolveOffer() {
        // loop until there is no matching offer OR this.quantity == 0
        while (checkMatchedOffer() != 0 && this.getQuantity() > 0) {
            int matchingID = checkMatchedOffer();
            // reduce the quantities of matching buy and sell offers + deleting offers if they've been fully resolved
            reduceOrderQuantities(matchingID);
            // buy offer is fully resolved
            if (this.getQuantity() <= 0) {
                long millis = System.currentTimeMillis();
                this.dateResolved = new Date(millis);
            }
        }
    }

    private void reduceOrderQuantities(int matchingID) {
        if (matchingID != 0) {
            BuyOffer matchingBuyOffer = BuyOffersDB.getBuyOffersDB().getOffer(matchingID);
            // if the quantity of buy and sell offers are equal remove them both from the DB
            if (this.getQuantity() == matchingBuyOffer.getQuantity()) {
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                matchingBuyOffer.setQuantity(0);
            }
            // if the quantity of buy offer is greater than the sell offer, remove the sell offer from DB
            // and reduce the quantity of the buy offer
            else if (this.getQuantity() > matchingBuyOffer.getQuantity()) {
                this.setQuantity(this.getQuantity() - matchingBuyOffer.getQuantity());
                // update the database with new quantity
                SellOffersDB.addSellOffer(this.getOfferID(), this);
                matchingBuyOffer.setQuantity(0);
                BuyOffersDB.removeBuyOffer(matchingID);
            }
            // if the quantity of buy offers is less than the sell offers, remove the buy offer from DB
            // and reduce the quantity of the sell offer
            else {
                matchingBuyOffer.setQuantity(matchingBuyOffer.getQuantity() - this.getQuantity());
                BuyOffersDB.addBuyOffer(matchingID, matchingBuyOffer);
                SellOffersDB.removeSellOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }


    // return an arraylist of buy offers with the same asset name as the sell offer
    private ArrayList<BuyOffer> matchingBuyOffers() {
        ArrayList<BuyOffer> matchingBuyOffers = new ArrayList<>();
        TreeMap<Integer, BuyOffer> buyOfferMap = BuyOffersDB.getBuyOffersDB().getMarketBuyOffers();
        Iterator<Map.Entry<Integer, BuyOffer>> entries = buyOfferMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            if (entry.getValue().getAssetName() == this.getAssetName()) {
                BuyOffer matchingOffer = entry.getValue();
                matchingBuyOffers.add(matchingOffer);
            }
        }
        return matchingBuyOffers;
    }

    // return the matching buy offer's ID which was the oldest buy offer AND is equal or higher priced than
    // the sell offer
    @Override
    public int checkMatchedOffer() {
        ArrayList<BuyOffer> matchingBuyOffers = matchingBuyOffers();
        double sellOfferPrice = getPricePerUnit();
        // assign the first sell offer as the lowest price
        // convert map into entry set to iterate over
        Iterator<BuyOffer> iter = matchingBuyOffers.iterator();
        BuyOffer buyOffer;
        double buyOfferPrice;
        if (iter.hasNext()) {
            while (iter.hasNext()) {
                buyOffer = iter.next();
                buyOfferPrice = buyOffer.getPricePerUnit();
                if (buyOfferPrice >= sellOfferPrice) {
                    return buyOffer.getOfferID();
                }
            }
        }
        else {
            return 0;
        }
        return 0;
    }





    @Override
    protected int createUniqueID() {
        if (SellOffersDB.getSellOffersDB().getMarketSellOffers().size() == 0) {
            return 1;
        }
        return SellOffersDB.getSellOffersDB().getMarketSellOffers().lastKey() + 1;
    }
}
