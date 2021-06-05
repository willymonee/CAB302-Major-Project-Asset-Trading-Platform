package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An abstract class that is used to help create trade offers to buy/sell assets
 */
public abstract class Offer implements Serializable {
    private String assetName;
    private int quantity;
    private double pricePerUnit;
    private String username;
    private String organisationalUnitName;
    private int orderID;
    protected static final int NO_MATCHING_OFFERS = 0;
    protected final int FULLY_RESOLVED = 2;
    protected final int PARTIALLY_RESOLVED = 1;
    protected final int NOT_RESOLVED = 0;

    /**
     * Constructor for trade offer
     * @param assetName Name of the asset to be bought or sold
     * @param quantity Quantity of asset
     * @param pricePerUnit Price of the asset
     * @param username The username of the user who made the offer
     * @param organisationalUnitName The name of the organisation whose assets and credits will be affected
     */
    public Offer(String assetName, int quantity, double pricePerUnit, String username, String organisationalUnitName)   {
        if (pricePerUnit <= 0 ) {
            throw new IllegalArgumentException("Price needs to be greater than 0");
        }
        if (quantity <= 0 ) {
            throw new IllegalArgumentException("Quantity needs to be greater than 0");
        }
        this.assetName = assetName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.username = username;
        this.organisationalUnitName = organisationalUnitName;
    }

    /**
     * Overloaded Constructor for trade offer - used when retrieving offer from DB
     * @param orderID                The ID of the offer
     * @param assetName                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public Offer(int orderID, String assetName, int quantity, double pricePerUnit, String username, String organisationalUnitName)   {
        if (pricePerUnit <= 0 ) {
            throw new IllegalArgumentException("Price needs to be greater than 0");
        }
        if (quantity <= 0 ) {
            throw new IllegalArgumentException("Quantity needs to be greater than 0");
        }
        this.assetName = assetName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.username = username;
        this.organisationalUnitName = organisationalUnitName;
        this.orderID = orderID;
    }

    /**
     * If there is a matched offer deduct or add appropriate amount of credits from the organisational unit's involved
     * Also add/remove the correct amount of assets bought or sold to the organisational units
     * Also update the offer's quantities or alternatively delete the offer when it has been fully resolved
     * Repeat this process until the offer has been fully resolved or there are no matching offers
     *
     * @return NOT_RESOLVED if no trades occured, PARTIALLY resolved if part of the offer was resolved, and
     *      * FULLY_RESOLVED if theoffer is fully resolved
     */
    public abstract int resolveOffer();

    // Concrete methods
    /**
     * @return the offer as a string containing its ID, asset name, quantity, price, offer creator and unit name
     */
    @Override
    public String toString() {
        return  getAssetName() + "\t" + getQuantity()+ "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName();
    }

    /**
     * Getter for the offer's ID
     * @return the offer's ID as an int
     */
    public int getOfferID() {
        return this.orderID;
    }

    /**
     * Setter for the offer's ID
     * @param ID takes an integer and set's the offer's ID to that
     */
    public void setOfferID(int ID) {
        this.orderID = ID;
    }

    /**
     * Setter for the quantity field
     */
    protected void setQuantity(int newQuantity) {
            this.quantity = newQuantity;
        }

    /**
     * Getter for the quantity field
     *
     * @return The quantity of the asset
     */
    public int getQuantity() {
        return quantity;
    }


    /**
     * Getter for the price per unit field
     *
     * @return The price per unit of the asset
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Getter for the username field
     *
     * @return The username of the user who made the offer
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the organisational unit name field
     *
     * @return The organisational unit name whose assets and credits will be affected
     */
    public String getUnitName() {
        return organisationalUnitName;
    }

    /**
     * Getter for the assetname field
     *
     * @return The assetname of the order
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Check if an offer's asset name is the same as another offer's
     */
    protected boolean sameAssetName(BuyOffer buyOffer, SellOffer sellOffer) {
        String buyOfferAssetName = buyOffer.getAssetName();
        String sellOfferAssetName = sellOffer.getAssetName();
        return buyOfferAssetName.equals(sellOfferAssetName);
    }

    /**
     * Check if there is a matching offer
     * @return true if there is a matching ID, false if the ID is equal to 0
     */
    protected boolean isMatching(int matchingID) {
        return matchingID != NO_MATCHING_OFFERS;
    }

    /**
     * Checks if the buy organisation who listed the buy offer owns the particular asset yet (has a field in
     * organisational_unit_assets table in the DB)
     *
     * @param buyOffer the buy offer to get the organisational unit from
     * @return true if the org has a field otherwise false
     */
    protected boolean orgOwnsAsset(BuyOffer buyOffer) {
        NetworkDataSource dataSource = new NetworkDataSource();
        dataSource.run();
        try {
            HashMap<String, Integer> orgAssets = dataSource.getAssets(buyOffer.getUnitName());
            Iterator orgAssetsIter = orgAssets.entrySet().iterator();
            while (orgAssetsIter.hasNext()) {
                Map.Entry element = (Map.Entry) orgAssetsIter.next();
                if (element.getKey().equals(buyOffer.getAssetName())) {
                    return true;
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
