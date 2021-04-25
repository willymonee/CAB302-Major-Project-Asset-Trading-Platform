package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Miscellaneous.Asset;
import ElectronicAssetTradingPlatform.Miscellaneous.Offer;

/**
 *  OrganisationalUnitMembers class which extends the user class. This class is implemented
 *  for users who wish to trade their organisational unit's assets by connecting to the server
 *  and managing unit asset listings via their client side GUI.
 */
public class OrganisationalUnitMembers extends User {
    private int organisationalUnitID;

    /**
     * Constructor used to set ID to organisational unit to user
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     * @param organisationalUnitID int ID matching the organisational unit
     *                             the organisational member is a part of
     *
     */
    public OrganisationalUnitMembers(String username, String password, int organisationalUnitID) {
        super(username, password);
        this.organisationalUnitID = organisationalUnitID;
    }

    /**
     * Display current sell offers made by the organisational unit [M]
     */
    public void getcurrentSellOffers() {

    }

    /**
     * Display current buy offers made by the organisational unit [M]
     */
    public void getcurrentBuyOffers() {

    }

    /**
     * Set up buy order for an organisational unit using ones own
     * organisational unit's credits [M]
     *
     * @param assetType asset name for buy order
     * @param quantity int amount of assets placed for buy order
     * @param price int price of asset requested for buy order
     */
    public void listBuyOrder(Asset assetType, int quantity, int price) {

    }

    /**
     * Set up sell order for own organisational unit's asset using credits [M]
     *
     * @param assetType asset name for sell order
     * @param quantity int amount of assets placed for sell order
     * @param price int price of asset set for sell order
     */
    public void listSellOrder(Asset assetType, int quantity, int price) {

    }

    /**
     * Remove currently listed buy/sell order provided ID of asset listing [M]
     *
     * @param listingID int ID of asset listing for removal
     */
    public void removeListing(int listingID) {

    }

    /**
     * Relist currently listed buy/sell offer for different amount and/or price [C]
     *
     * @param listingID int ID for asset listing to relist
     * @param quantity int amount of assets for listing to be relisted for
     * @param price int price of asset requested for listing to relist for
     */
    public void relist(int listingID, int quantity, int price) {

    }

    /**
     * Get unit history for own organisational unit [C]
     *
     * @param listingID int ID for listing to get the history of the
     *                  organisational unit
     */
    public void getUnitHistory(int listingID) {
        // getUnitHistory method
    }

    /**
     * Get all currently listed prices for specified asset name [S]
     *
     * @param assetType asset name to get asset price list for
     */
    public void getAssetPriceList(Asset assetType) {
        // getAssetPriceList method
    }

    /**
     * Create a graph of all prices offers were resolved at for the asset [C]
     *
     */
    public void assetPriceGraph() {
        // assetPriceListToGraph method
    }

}