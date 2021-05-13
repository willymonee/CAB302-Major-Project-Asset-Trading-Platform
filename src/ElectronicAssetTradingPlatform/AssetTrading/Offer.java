package ElectronicAssetTradingPlatform.AssetTrading;

import java.sql.Date;

/**
 * An abstract class that is used to help create trade offers to buy/sell assets
 */
public abstract class Offer {
    private String assetName;
    private int quantity;
    private double pricePerUnit;
    private String username;
    private String organisationalUnitName;
    private Date datePlaced;

    /**
     * Constructor for trade offer
     *  @param assetName Name of the asset to be bought or sold
     * @param quantity Quantity of asset
     * @param pricePerUnit Price of the asset
     * @param username The username of the user who made the offer
     * @param organisationalUnitName The name of the organisation whose assets and credits will be affected
     */
    public Offer(String assetName, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        this.assetName = assetName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.username = username;
        this.organisationalUnitName = organisationalUnitName;
        long millis = System.currentTimeMillis();
        this.datePlaced = new Date(millis);
    }

    // Abstract methods
    /**
     * Getter for Offer ID
     *
     * @return The particular offerID of an offer as an integer.
     */
    public abstract int getOfferID();

    /**
     * Create a unique ID for sell and buy offers. The IDs are separate between buy and sell offers e.g.
     * there can be a sell and buy offer with IDs == 1 at the same time
     *
     * @return A unique ID for either sell or buy offers as an integer.
     */
    protected abstract int createUniqueID();

    /**
     * Override the object and covert it into a string.
     *
     * @return The object in string format.
     */
    public abstract String toString();



    /**
     * Deduct or add appropriate amount of credits from the organisational unit based on the pricePerUnit
     * and the quantity bought or sold, as well as add the correct amount of assets bought or sold
     * Also delete the offer if all the requested assets have been sold or purchased, otherwise update the offer
     * [M]
     *
     */
    // public abstract void resolveOffer(OrganisationalUnit buyer, OrganisationalUnit seller);




    /**
     * Compare the newly created offer with existing buy and sell orders and
     * resolve matching ones e.g. if the order is a sell order check existing buy orders,
     * if the order is a buy order check existing sell orders).
     *
     * @return Returns the matching order ID if there is a match, returns 0 otherwise.
     */
    public abstract int checkMatchedOffer();


    // Concrete methods
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
    protected double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Getter for the username field
     *
     * @return The username of the user who made the offer
     */
    protected String getUsername() {
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
    protected String getAssetName() {
        return assetName;
    }

    /**
     * Getter for the datePlaced
     *
     * @return The date the order was place
     */
    protected Date getDatePlaced() {
        return datePlaced;
    }
}
