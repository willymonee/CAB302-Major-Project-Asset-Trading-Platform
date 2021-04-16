package ElectronicAssetTradingPlatform;

/**
 *
 */
public class OrganisationalUnitMembers extends User {
    private int organisationalUnitID;

    /**
     * Constructor used to set ID to organisational unit to user
     *
     * @param organisationalUnitID int ID matching the organisational unit
     *                             the organisational member is a part of
     *
     */
    public OrganisationalUnitMembers(String username, String password, int organisationalUnitID) {
        super(username, password);
        this.organisationalUnitID = organisationalUnitID;
    }

    /**
     * Connect organisational unit member to the server using the supplied
     * configuration file
     *
     */
    private void connectDB() {
        // connectDB method
    }

    /**
     * Display current offers made by the organisational member's belonging
     * organisational unit
     */
    private void getUnitOffers() {
        // getUnitOffers method
    }

    /**
     * Set up buy order for an organisational unit using ones own
     * organisational unit's credits
     *
     * @param assetType asset name for buy order
     * @param quantity int amount of assets placed for buy order
     * @param price int price of asset requested for buy order
     */
    private void listBuyOrder(Asset assetType, int quantity, int price) {

    }

    /**
     * Set up sell order for own organisational unit's asset using credits
     *
     * @param assetType asset name for sell order
     * @param quantity int amount of assets placed for sell order
     * @param price int price of asset set for sell order
     */
    private void listSellOrder(Asset assetType, int quantity, int price) {

    }

    /**
     * Remove currently listed buy/sell order provided ID of asset listing
     *
     * @param listingID int ID of asset listing for removal
     */
    private void removeListing(int listingID) {

    }

    /**
     * Relist currently listed buy/sell offer for different amount and/or
     * price
     *
     * @param listingID int ID for asset listing to relist
     * @param quantity int amount of assets for listing to be relisted for
     * @param price int price of asset requested for listing to relist for
     */
    private void relist(int listingID, int quantity, int price) {

    }

    /**
     * Get unit history for own organisational unit
     *
     * @param isSell boolean                                  this needs to be int listingID, or Asset assetType, to confirm if sold yet with return type bool
     */
    private void getUnitHistory(boolean isSell) {

    }

    /**
     * Get all currently listed prices for specified asset name
     *
     * @param assetType asset name to get asset price list for
     */
    private void getAssetPriceList(Asset assetType) {

    }

    /**
     * Create a graph of all prices offers were resolved at for the asset
     *
     */
    private void assetPriceListToGraph() {

    }

}