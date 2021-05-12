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
        // loop until there is no matching offer OR this.quantity == 0
        while(checkMatchedOffer() != 0 && this.getQuantity() > 0) {
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
            SellOffer matchingSellOffer = SellOffersDB.getSellOffersDB().getOffer(matchingID);
            // if the quantity of buy and sell offers are equal remove them both from the DB
            if (this.getQuantity() == matchingSellOffer.getQuantity()) {
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                matchingSellOffer.setQuantity(0);
            }
            // if the quantity of buy offer is greater than the sell offer, remove the sell offer from DB
            // and reduce the quantity of the buy offer
            else if (this.getQuantity() > matchingSellOffer.getQuantity()) {
                this.setQuantity(this.getQuantity() - matchingSellOffer.getQuantity());
                // update the database with new quantity
                BuyOffersDB.addBuyOffer(this.getOfferID(), this);
                matchingSellOffer.setQuantity(0);
                SellOffersDB.removeSellOffer(matchingID);
            }
            // if the quantity of buy offers is less than the sell offers, remove the buy offer from DB
            // and reduce the quantity of the sell offer
            else {
                matchingSellOffer.setQuantity(matchingSellOffer.getQuantity() - this.getQuantity());
                SellOffersDB.addSellOffer(matchingID, matchingSellOffer);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
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
