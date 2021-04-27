package ElectronicAssetTradingPlatform.Miscellaneous;

import java.time.LocalDate;

/**
 * An abstract class that is used to help create trade offers to buy/sell assets
 */
public abstract class Offer {
    private int orderID; // might not need, use SQL auto-increment
    private Asset asset;
    private int quantity;
    private float pricePerUnit;
    private String username;
    private String organisationalUnitName;
    private LocalDate dateResolved;

    /**
     * Constructor for trade offer
     *
     * @param asset Name of the asset to be bought or sold
     * @param quantity Quantity of asset
     * @param pricePerUnit Price of the asset
     * @param username The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public Offer(Asset asset, int quantity, float pricePerUnit, String username, String organisationalUnitName) {
        this.orderID = 0; // Should be unique - might not need, use SQL auto-increment
        this.asset = asset;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.organisationalUnitName = organisationalUnitName;
        this.username = username;
        this.dateResolved = null;
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
}
