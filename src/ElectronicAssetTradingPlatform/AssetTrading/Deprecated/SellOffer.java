package ElectronicAssetTradingPlatform.AssetTrading.Deprecated;

import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.Offer;
import ElectronicAssetTradingPlatform.AssetTrading.Deprecated.OrganisationalUnit;
import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;

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


    /**
     * Getter for the sell offer's ID
     *
     * @return the sell offer's ID
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
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity() + "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }

    /**
     * Creates a unique ID for the sell offer
     *
     * @return unique ID as an int
     */
    @Override
    protected int createUniqueID() {
        if (SellOffersDB.getSellOffersDB().getMarketSellOffers().size() == 0) {
            return 1;
        }
        return SellOffersDB.getSellOffersDB().getMarketSellOffers().lastKey() + 1;
    }

    /**
     * Returns an array list of all buy offers which are offering the same asset (asset name's are the same)
     *
     * @return Array List of matching Buy Offers
     */
    private ArrayList<BuyOffer> matchingBuyOffers() {
        ArrayList<BuyOffer> matchingBuyOffers = new ArrayList<>();
        TreeMap<Integer, BuyOffer> buyOfferMap = BuyOffersDB.getBuyOffersDB().getMarketBuyOffers();
        Iterator<Map.Entry<Integer, BuyOffer>> entries = buyOfferMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, BuyOffer> entry = entries.next();
            if (entry.getValue().getAssetName().equals(this.getAssetName())) {
                BuyOffer matchingOffer = entry.getValue();
                matchingBuyOffers.add(matchingOffer);
            }
        }
        return matchingBuyOffers;
    }

    /**
     * Takes the matching buy offers and finds a buy offer which is equally or higher priced than the sell offer.
     * As long as the buy offer is equally or higher priced, it will be matched and returned (a higher priced buy offer
     * does not have priority because the trades occur at the sell price).
     * Thus, the returned buy offer will the oldest buy offer which is equally or higher priced
     * <p>
     * EVENTUALLY TURN TO A PRIVATE METHOD ONCE TESTING IS COMPLETE
     *
     * @return int of the buy offer, int >= 0
     */
    @Override
    public int getMatchedPriceOffer() {
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
        } else {
            return 0;
        }
        return 0;
    }


    /**
     * TEMPORARY FUNCTION FOR TESTING DELETE LATER
     * This takes a matching buy offer ID and compares it to the buy offer
     * Then it reduces the 'quantities' of both offers
     */
    private void tradeAssetsAndReduceOrders(int matchingID) {
        if (matchingID != 0) {
            BuyOffer matchingBuyOffer = BuyOffersDB.getBuyOffersDB().getOffer(matchingID);
            // if the quantity of buy and sell offers are equal remove them both from the DB
            if (this.getQuantity() == matchingBuyOffer.getQuantity()) {
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                matchingBuyOffer.setQuantity(0);
            }
            // if the quantity specified by the buy offer is greater than the sell offer, remove the sell offer from DB
            // and reduce the quantity of the buy offer by the quantity of the sell offer
            else if (this.getQuantity() > matchingBuyOffer.getQuantity()) {
                this.setQuantity(this.getQuantity() - matchingBuyOffer.getQuantity());
                // update the database with new quantity
                SellOffersDB.addSellOffer(this.getOfferID(), this);
                matchingBuyOffer.setQuantity(0);
                BuyOffersDB.removeBuyOffer(matchingID);
            }
            // if the quantity specified by the buy offers is less than the sell offers, remove the buy offer from DB
            // and reduce the quantity of the sell offer by the quantity of the buy offer
            else {
                matchingBuyOffer.setQuantity(matchingBuyOffer.getQuantity() - this.getQuantity());
                BuyOffersDB.addBuyOffer(matchingID, matchingBuyOffer);
                SellOffersDB.removeSellOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }


    /**
     * ACTUAL FUNCTION FOR TRADING ASSETS
     * This takes a matching buy offer ID and compares it to the sell offer
     * Then it reduces the 'quantities' of both offers
     * It also adds/removes assets and credits from each organisational unit based on the offers
     *
     * @param matchingID The ID of the matching buy order, int >= 0
     * @param buyOrg     The buyer's Organisational Unit to add assets and remove credits from
     * @param sellOrg    The seller's Organisational Unit to remove assets and add credits to
     */
    // overridden, function which also exchanges assets between buyer and seller
    private void tradeAssetsAndReduceOrders(int matchingID, OrganisationalUnit buyOrg, OrganisationalUnit sellOrg) throws Exception {
        if (matchingID != 0) {
            BuyOffer matchingBuyOffer = BuyOffersDB.getBuyOffersDB().getOffer(matchingID);
            double sellersPrice = this.getPricePerUnit();
            // if the quantity of buy and sell offers are equal remove them both from the DB
            if (this.getQuantity() == matchingBuyOffer.getQuantity()) {
                int quantityTraded = this.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double) quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double) quantityTraded));
                // edit quantities of orders
                SellOffersDB.removeSellOffer(matchingID);
                this.setQuantity(0);
                BuyOffersDB.removeBuyOffer(this.getOfferID());
                matchingBuyOffer.setQuantity(0);
            }
            // if the quantity of buy offer is greater than the sell offer, remove the sell offer from DB
            // and reduce the quantity of the buy offer
            else if (this.getQuantity() > matchingBuyOffer.getQuantity()) {
                int quantityTraded = matchingBuyOffer.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double) quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double) quantityTraded));
                // edit quantities of orders
                this.setQuantity(this.getQuantity() - matchingBuyOffer.getQuantity());
                SellOffersDB.addSellOffer(this.getOfferID(), this);
                matchingBuyOffer.setQuantity(0);
                BuyOffersDB.removeBuyOffer(matchingID);
            }
            // if the quantity of buy offers is less than the sell offers, remove the buy offer from DB
            // and reduce the quantity of the sell offer
            else {
                int quantityTraded = this.getQuantity();
                // exchange assets and credits
                buyOrg.addAsset(this.getAssetName(), quantityTraded);
                sellOrg.removeAsset(this.getAssetName(), quantityTraded);
                sellOrg.editCredits(sellersPrice * (double) quantityTraded);
                buyOrg.editCredits(-(sellersPrice * (double) quantityTraded));
                // edit quantities of orders
                matchingBuyOffer.setQuantity(matchingBuyOffer.getQuantity() - this.getQuantity());
                BuyOffersDB.addBuyOffer(matchingID, matchingBuyOffer);
                SellOffersDB.removeSellOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }


    /**
     * TEMP FUNCTION FOR RESOLVING OFFERS for testing because stuff might break otherwise
     * While there is a matching sell offer and the buy offer has not been resolved
     * Perform a trade between a matching sell offer and buy offer - this one only reduces quantities of offers
     * Does not trade assets
     */
    public void resolveOffer() {
        // loop until there is no matching offer OR this.quantity == 0
        while (getMatchedPriceOffer() != 0 && this.getQuantity() > 0) {
            int matchingID = getMatchedPriceOffer();
            // reduce the quantities of matching buy and sell offers + deleting offers if they've been fully resolved
            tradeAssetsAndReduceOrders(matchingID);
            // sell offer is fully resolved
            if (this.getQuantity() <= 0) {
                long millis = System.currentTimeMillis();
                this.dateResolved = new Date(millis);
            }
        }
    }

    /**
     * ACTUAL FUNCTION FOR RESOLVING OFFERS
     * While there is a matching sell offer and the buy offer has not been resolved
     * Perform a trade between a matching sell offer and buy offer which reduces order quantities
     * and exchanges assets and credits
     */
    public void resolveOffer(OrganisationalUnit buyer, OrganisationalUnit seller) throws Exception {
        // loop until there is no matching offer OR this.quantity == 0
        while (getMatchedPriceOffer() != 0 && this.getQuantity() > 0) {
            int matchingID = getMatchedPriceOffer();
            // reduce the quantities of matching buy and sell offers + deleting offers if they've been fully resolved
            // and trade assets
            tradeAssetsAndReduceOrders(matchingID, buyer, seller);
            // sell offer is fully resolved
            if (this.getQuantity() <= 0) {
                long millis = System.currentTimeMillis();
                this.dateResolved = new Date(millis);
            }
        }
    }
}