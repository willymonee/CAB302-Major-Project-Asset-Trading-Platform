package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;

import java.sql.SQLException;
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

    public void setOfferID(int ID) {
        this.orderID = ID;
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
     * This takes a matching sell offer ID and compares it to the buy offer
     * Then it reduces the 'quantities' of both offers
     */
    public void reduceMatchingOfferQuantities(int matchingID) {
        if (matchingID != 0) {
            SellOffer matchingSellOffer = SellOfferData.getInstance().getOffer(matchingID);
            int sellOfferQuantity = matchingSellOffer.getQuantity();
            int buyOfferQuantity = this.getQuantity();
            // if the quantity specified by matching buy and sell offers are equal remove them both from the DB
            if (buyOfferQuantity == sellOfferQuantity) {
                BuyOfferData.removeOffer(this.getOfferID());
                SellOfferData.removeOffer(matchingID);
                this.setQuantity(0);
            }
            else if (buyOfferQuantity > sellOfferQuantity) {
                int updatedBuyQuantity = buyOfferQuantity - sellOfferQuantity;
                BuyOfferData.getInstance().updateOfferQuantity(updatedBuyQuantity, this.getOfferID());
                SellOfferData.removeOffer(matchingID);
                this.setQuantity(updatedBuyQuantity);
            }
            else {
                int updatedSellQuantity = sellOfferQuantity - buyOfferQuantity;
                SellOfferData.getInstance().updateOfferQuantity(updatedSellQuantity, matchingID);
                BuyOfferData.removeOffer(this.getOfferID());
                this.setQuantity(0);
            }
        }
    }



    /**
     * Remove credits from the buy org and add credits the sell org
     */
    private void tradeCredits(double credit, String sellOrgName) {
        UnitDataSource unitDataSource = new UnitDataSource();
        // decrease credits of the buy org
        unitDataSource.updateUnitCredits((float)(-(credit)), this.getUnitName());
        // increase credits of the sell org
        unitDataSource.updateUnitCredits((float)credit, sellOrgName);
    }

    private void tradeAssets(int quantity, SellOffer sellOffer)  {
        UnitDataSource unitDataSource = new UnitDataSource();
        try {
            int buyOrgID = Integer.parseInt(unitDataSource.executeGetUnitID(this.getUnitName()));
            int assetID = unitDataSource.executeGetAssetID(this.getAssetName());
            int sellOrgID = Integer.parseInt(unitDataSource.executeGetUnitID(sellOffer.getUnitName()));
            unitDataSource.updateUnitAssets(quantity, buyOrgID, assetID);
            unitDataSource.updateUnitAssets(-(quantity), sellOrgID, assetID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Takes a matching sell offer ID and compares it to the buy offer
     * Then it exchanges the organisational unit assets
     * When resolving offer should be called before reduceMatchingOfferQuantities
     */
    private void tradeAssetsAndCredits(int matchingID)  {
        if (matchingID != 0) {
            SellOffer matchingSellOffer = SellOfferData.getInstance().getOffer(matchingID);
            int sellOfferQuantity = matchingSellOffer.getQuantity();
            int buyOfferQuantity = this.getQuantity();
            double creditsExchanged;
            int assetsExchanged;
            double sellersPrice = matchingSellOffer.getPricePerUnit();
            if (buyOfferQuantity > sellOfferQuantity) {
                creditsExchanged = sellersPrice * sellOfferQuantity;
                assetsExchanged = sellOfferQuantity;
            }
            else {
                assetsExchanged = buyOfferQuantity;
                creditsExchanged = sellersPrice * buyOfferQuantity;
            }
            tradeAssets(assetsExchanged, matchingSellOffer);
            tradeCredits(creditsExchanged, matchingSellOffer.getUnitName());
        }
    }

    /**
     * Resolve the offer
     */
    public void resolveOffer() {
        // loop until there is either no matching offer OR whilst the offer exists
        while (getMatchedPriceOffer() != 0 && BuyOfferData.getInstance().offerExists(this.getOfferID())) {
            int matchingID = getMatchedPriceOffer();
            // reduce the quantities of matching buy and sell offers + deleting offers if they've been fully resolved
            tradeAssetsAndCredits(matchingID);
            reduceMatchingOfferQuantities(matchingID);
            // check if offer has been fully resolved
            if (!BuyOfferData.getInstance().offerExists(this.getOfferID())) {
                // OFFER FULLY RESOLVED
            }
        }
    }
}
