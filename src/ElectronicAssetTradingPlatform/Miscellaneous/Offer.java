package ElectronicAssetTradingPlatform.Miscellaneous;

import java.time.LocalDate;

/**
 * An abstract class that is used to help create trade offers to buy/sell assets
 */
public abstract class Offer {
    private int orderID;
    private Asset asset;
    private int quantity;
    private float pricePerUnit;
    private int userID;
    private int organisationalUnitID;
    private LocalDate dateResolved;

    /**
     * Constructor for trade offer
     *
     * @param asset Name of the asset to be bought or sold
     * @param quantity Quantity of asset
     * @param pricePerUnit Price of the asset
     * @param userID The ID of the user who made the offer
     * @param organisationalUnitID The ID of the organisation whose assets and credits will be affected
     */
    public Offer(Asset asset, int quantity, float pricePerUnit, int organisationalUnitID, int userID) {
        this.orderID = 0; // Should be unique
        this.asset = asset;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.organisationalUnitID = organisationalUnitID;
        this.userID = userID;
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
     * Deduct appropriate amount of credits from the organisational unit based on the pricePerUnit
     * and the quantity bought, as well as add the correct amount of assets bought
     * Also delete the offer if all the requested assets have been sold, otherwise update the offer
     *
     */
    public void resolveBuyOffer() {
        // buyOfferResolve method
    }

    /**
     * Add the appropriate amount of credits to the organisational unit based on the pricePerUnit
     * and quantity sold, as well as remove the correct amount of assets sold
     * Also delete the offer if all the requested assets have been purchased
     *
     */
    public void resolveSellOffer() {
        // sellOfferResolve
    }
}
