package ElectronicAssetTradingPlatform.Miscellaneous;

import java.util.Date;

/**
 * An abstract class that is used to help create trade offers to buy/sell assets
 */
public abstract class Offer {
    private int orderID; // might not need, use SQL auto-increment (but it is the only unique value for this class)
    private Asset asset;
    private int quantity;
    private float pricePerUnit;
    private String username;
    private String organisationalUnitName;
    private Date dateResolved;

    /**
     * Constructor for trade offer
     *
     * @param asset Name of the asset to be bought or sold
     * @param quantity Quantity of asset
     * @param pricePerUnit Price of the asset
     * @param username The username of the user who made the offer
     * @param organisationalUnitName The name of the organisation whose assets and credits will be affected
     * @param dateResolved The date the offer was resolved
     */
    public Offer(Asset asset, int quantity, float pricePerUnit, String username, String organisationalUnitName, Date dateResolved) {
        this.orderID = 0; // Should be unique - might not need, use SQL auto-increment (but it is the only unique value for this class)
        this.asset = asset;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.username = username;
        this.organisationalUnitName = organisationalUnitName;
        this.dateResolved = dateResolved;
    }

    /**
     * Compare the newly created offer with existing buy and sell orders and
     * resolve matching ones e.g. if the order is a sell order check existing buy orders,
     * if the order is a buy order check existing sell orders).
     *
     * @return Returns the matching order ID if there is a match, returns 0 otherwise.
     */
    public int checkMatchedOffer() {
        return 0;
    }

    /**
     * Deduct or add appropriate amount of credits from the organisational unit based on the pricePerUnit
     * and the quantity bought or sold, as well as add the correct amount of assets bought or sold
     * Also delete the offer if all the requested assets have been sold or purchased, otherwise update the offer
     * [M]
     *
     */
    public abstract void resolveOffer(); // Implemented by either the sellOffer or buyOffer class

    /**
     * Getter for the quantity field
     *
     * @return The quantity of the asset
     */
    protected int getQuantity() {
        return quantity;
    }

    /**
     * Getter for the price per unit field
     *
     * @return The price per unit of the asset
     */
    protected float getPricePerUnit() {
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
    protected String getUnitName() {
        return organisationalUnitName;
    }
}
