package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;

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
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public BuyOffer(String asset, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
    }


    /**
     * Overloaded Constructor for trade offer - used when retrieving offer from DB
     * @param orderID                The ID of the offer
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public BuyOffer(int orderID, String asset, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
        this.orderID = orderID;
    }

    /**
     * Getter for the buy offer's ID
     *
     * @return the buy offer's ID
     */
    @Override
    public int getOfferID() {
        return this.orderID;
    }

    /**
     * Converts the buy offer to a string and returns it
     *
     * @return Buy Offer object as a string
     */
    @Override
    public String toString() {
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity()+ "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }

    /**
     * Creates a unique ID for the buy offer
     *
     * @return unique ID as an int
     */
    @Override
    protected int createUniqueID() {
        if (BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().size() == 0) {
            return 1;
        }
        return BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().lastKey() + 1;
    }

    /**
     * Returns an array list of all sell offers which are offering the same asset (asset name's are the same)
     *
     * @return Array List of matching Sell Offers
     */
    public ArrayList<SellOffer> getMatchingSellOffers() {
        ArrayList<SellOffer> matchingSellOffers = new ArrayList<>();
        // retrieve all sell offers from the database
        TreeMap<Integer, SellOffer> sellOfferMap = SellOfferData.getInstance().getMarketSellOffers();
        // compare the sell offer's asset name to the buy offer's asset name, adding those which match
        for (Map.Entry<Integer, SellOffer> entry : sellOfferMap.entrySet()) {
            if (entry.getValue().getAssetName().equals(this.getAssetName())) {
                SellOffer matchingOffer = entry.getValue();
                matchingSellOffers.add(matchingOffer);
            }
        }
        return matchingSellOffers;
    }


    /**
     * Takes the matching sell offers and finds the lowest priced sell offer which is equally or lower priced than
     * the buy offer.
     * If two sell offers are equally priced, the offer placed first has priority (this will be the offer queried first)
     * TODO turn to a private method after testing
     *
     * @return int of the sell offer OR 0 if no sell offer was found with a matching price and asset name
     */
    @Override
    public int getMatchedPriceOffer() {
        ArrayList<SellOffer> matchingSellOffers = getMatchingSellOffers();
        double buyOfferPrice = getPricePerUnit();
        // convert map into entry set to iterate over
        Iterator<SellOffer> sellOffersIter = matchingSellOffers.iterator();
        SellOffer lowestSellOffer;
        double lowestSellPrice;
        if (sellOffersIter.hasNext()) { // check if there are any sell offers with the same asset name
            // assign the lowest sell offer and price to the first sell offer
            lowestSellOffer = sellOffersIter.next();
            lowestSellPrice = lowestSellOffer.getPricePerUnit();
            // iterate through matching sell offers to see which has the lowest price, updating the lowestSellOffer
            // and lowestSellPrice if one is found
            while(sellOffersIter.hasNext()) {
                SellOffer newOffer = sellOffersIter.next();
                if (newOffer.getPricePerUnit() < lowestSellPrice) {
                    lowestSellPrice = newOffer.getPricePerUnit();
                    lowestSellOffer = newOffer;
                }
            }
            // return the sell offer's ID if the lowest matching sell offer is equal or less than they buy offer's price
            if (lowestSellPrice <= buyOfferPrice) {
                return lowestSellOffer.getOfferID();
            } else { // otherwise return 0
                return 0;
            }
        } else { // return 0 if there are no sell offers with the same asset name
            return 0;
        }
    }


    /**
     * TEMPORARY FUNCTION FOR TESTING DELETE LATER
     * This takes a matching sell offer ID and compares it to the buy offer
     * Then it reduces the 'quantities' of both offers
     */
    private void tradeAssetsAndReduceOrders(int matchingID) {
        if (matchingID != 0) {
            SellOffer matchingSellOffer = SellOffersDB.getSellOffersDB().getOffer(matchingID);
            // if the quantity specified by the buy and sell offers are equal remove them both from the DB
            if (this.getQuantity() == matchingSellOffer.getQuantity()) {
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                matchingSellOffer.setQuantity(0);
            }
            // if the quantity specified by the buy offer is greater than the sell offer, remove the sell offer from DB
            // and reduce the quantity of the buy offer by the quantity of the sell offer
            else if (this.getQuantity() > matchingSellOffer.getQuantity()) {
                int quantityTraded = matchingSellOffer.getQuantity();
                this.setQuantity(this.getQuantity() - quantityTraded);
                BuyOffersDB.addBuyOffer(this.getOfferID(), this);
                matchingSellOffer.setQuantity(0);
                SellOffersDB.removeSellOffer(matchingID);
            }
            // if the quantity specified by the buy offers is less than the sell offers, remove the buy offer from DB
            // and reduce the quantity of the sell offer by the quantity of the buy offer
            else {
                matchingSellOffer.setQuantity(matchingSellOffer.getQuantity() - this.getQuantity());
                SellOffersDB.addSellOffer(matchingID, matchingSellOffer);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }


    /**
     * ACTUAL FUNCTION FOR TRADING ASSETS
     * This takes a matching sell offer ID and compares it to the buy offer
     * Then it reduces the 'quantities' of both offers
     * It also adds/removes assets and credits from each organisational unit based on the offers
     *
     * @param matchingID The ID of the matching sell offer, int >= 0
     */
    private void tradeAssetsAndReduceOrders(int matchingID, OrganisationalUnit buyOrg, OrganisationalUnit sellOrg) throws Exception {
        if (matchingID != 0) {
            SellOffer matchingSellOffer = SellOffersDB.getSellOffersDB().getOffer(matchingID);
            double sellersPrice = matchingSellOffer.getPricePerUnit();
            // if the quantity of buy and sell offers are equal
            if (this.getQuantity() == matchingSellOffer.getQuantity()) {
                int quantityTraded = this.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double)quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double)quantityTraded));
                // edit quantities of orders
                matchingSellOffer.setQuantity(0);
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());

            }
            // if the quantity of buy offer is greater than the sell offer
            else if (this.getQuantity() > matchingSellOffer.getQuantity()) {
                int quantityTraded = matchingSellOffer.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double)quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double)quantityTraded));
                // edit quantities of orders
                this.setQuantity(this.getQuantity() - quantityTraded);
                BuyOffersDB.addBuyOffer(this.getOfferID(), this);
                matchingSellOffer.setQuantity(0);
                SellOffersDB.removeSellOffer(matchingID);
            }
            // if the quantity of buy offers is less than the sell offers
            else {
                int quantityTraded = this.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double)quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double)quantityTraded));
                // edit quantities of orders
                matchingSellOffer.setQuantity(matchingSellOffer.getQuantity() - this.getQuantity());
                SellOffersDB.addSellOffer(matchingID, matchingSellOffer);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }


    /**
     * ACTUAL FUNCTION FOR RESOLVING OFFERS
     * While there is a matching sell offer and the buy offer has not been resolved
     * Perform a trade between a matching sell offer and buy offer
     *
     * @param buyer The buyer's Organisational Unit to add assets and remove credits from
     * @param seller The seller's Organisational Unit to remove assets and add credits to
     */
    public void resolveOffer(OrganisationalUnit buyer, OrganisationalUnit seller) throws Exception {
        // loop until there is no matching offer or the buy offer has been fully resolved
        while(getMatchedPriceOffer() != 0 && this.getQuantity() > 0) {
            // get the matching sell offer and perform the trade
            int matchingID = getMatchedPriceOffer();
            tradeAssetsAndReduceOrders(matchingID, buyer, seller);
            // buy offer is fully resolved TODO add it somewhere else like resolved offers
            if (this.getQuantity() <= 0) {
                long millis = System.currentTimeMillis();
                this.dateResolved = new Date(millis);
            }
        }
    }

    /**
     * TEMP FUNCTION FOR RESOLVING OFFERS for testing because stuff might break otherwise
     * While there is a matching sell offer and the buy offer has not been resolved
     * Perform a trade between a matching sell offer and buy offer
     *
     */
    public void resolveOffer() {
        // loop until there is no matching offer OR this.quantity == 0
        while(getMatchedPriceOffer() != 0 && this.getQuantity() > 0) {
            int matchingID = getMatchedPriceOffer();
            // reduce the quantities of matching buy and sell offers + deleting offers if they've been fully resolved
            tradeAssetsAndReduceOrders(matchingID);
            // buy offer is fully resolved
            if (this.getQuantity() <= 0) {
                long millis = System.currentTimeMillis();
                this.dateResolved = new Date(millis);
            }
        }
    }

}
