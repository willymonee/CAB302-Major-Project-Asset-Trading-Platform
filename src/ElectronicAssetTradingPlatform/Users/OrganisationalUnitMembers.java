package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.MockDBs.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.MockDBs.SellOffersDB;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import java.sql.SQLException;
import java.util.HashMap;

/**
 *  OrganisationalUnitMembers class which extends the user class. This class is implemented
 *  for users who wish to trade their organisational unit's assets by connecting to the server
 *  and managing unit asset listings via their client side GUI.
 */
public class OrganisationalUnitMembers extends User {
    private final String organisationalUnitName;

    /**
     * Constructor used to set ID to organisational unit to user
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     * @param unitName string name of the organisational unit the member is a part of
     *
     */
    public OrganisationalUnitMembers(String username, String password, String salt, String unitName) {
        super(username, password, salt);
        this.userType = UsersFactory.UserType.OrganisationalUnitMembers.toString();
        this.organisationalUnitName = unitName;
    }

    /**
     * Display current sell offers made by the organisational unit [M]
     * @return String of the unit's sell offers
     */
    public String getOrgSellOffers() {

        return SellOfferData.getInstance().getOrgOffers(this.organisationalUnitName);

    }

    /**
     * Display current buy offers made by the organisational unit [M]
     * @return String of the unit's buy offers
     */
    public String getOrgBuyOffers() {
        return BuyOfferData.getInstance().getOrgOffers(this.organisationalUnitName);
    }

    /**
     * Set up buy order for an organisational unit using ones own
     * organisational unit's credits [M]
     * @param assetType asset name for buy order
     * @param quantity int amount of assets placed for buy order
     * @param price int price of asset requested for buy order
     */
    public void listBuyOrder(String assetType, int quantity, double price) {
        // create offer
        BuyOffer offer = new BuyOffer(assetType, quantity, price, this.getUsername(), this.organisationalUnitName);
        // add offer into database
        BuyOfferData.getInstance().addOffer(offer);
        // retrieve the buy offer's ID from the database and set the buy offer's ID
        int buyOfferID = BuyOfferData.getInstance().getPlacedOfferID();
       offer.setOfferID(buyOfferID);
       // look to resolve the offer
        offer.resolveOffer();
    }

    /**
     * Set up sell order for own organisational unit's asset using credits [M]
     *  @param assetType asset name for sell order
     * @param quantity int amount of assets placed for sell order
     * @param price int price of asset set for sell order
     */
    public void listSellOrder(String assetType, int quantity, double price) {
        SellOffer offer = new SellOffer(assetType, quantity, price, this.getUsername(), this.organisationalUnitName);
        // using the actual database
        SellOfferData.getInstance().addSellOffer(offer);
        // retrieve the sell offer's ID from the database and set the sell offer's ID
        int sellOfferID = SellOfferData.getInstance().getPlacedOfferID();
        offer.setOfferID(sellOfferID);
        // look to resolve the offer
        offer.resolveOffer();
    }

    // temp function for testing without resolving
    public void listSellOrderNoResolve(String assetType, int quantity, double price) {
        SellOffer offer = new SellOffer(assetType, quantity, price, this.getUsername(), this.organisationalUnitName);
        // using the actual database
        SellOfferData.getInstance().addSellOffer(offer);
        // retrieve the sell offer's ID from the database and set the sell offer's ID
        int sellOfferID = SellOfferData.getInstance().getPlacedOfferID();
        offer.setOfferID(sellOfferID);
    }

    // tesmp function for testing without resolving
    public void listBuyOrderNoResolve(String assetType, int quantity, double price) {
        // create offer
        BuyOffer offer = new BuyOffer(assetType, quantity, price, this.getUsername(), this.organisationalUnitName);
        // add offer into database
         BuyOfferData.getInstance().addOffer(offer);
        // retrieve the buy offer's ID from the database and set the buy offer's ID
        int buyOfferID = BuyOfferData.getInstance().getPlacedOfferID();
        offer.setOfferID(buyOfferID);
    }

    /**
     * Remove currently listed buy order provided ID of asset listing [M]
     *
     * @param listingID int ID of asset listing for removal
     */
    public void removeBuyOffer(int listingID) {
        BuyOfferData.getInstance().removeOffer(listingID);
    }

    /**
     * Remove currently listed sell order provided ID of asset listing [M]
     *
     * @param listingID int ID of asset listing for removal
     */
    public void removeSellOffer(int listingID) {
        SellOfferData.getInstance().removeOffer(listingID);
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

    /**
     * Gets the assets this user's organisational unit owns
     *
     * @return Returns the map of asset_name and quantity
     */
    public HashMap<String, Integer> getUnitAssets() throws SQLException {
        return UsersDataSource.getInstance().getUnitAssets(organisationalUnitName);
    }

    /**
     * Gets the amount of credits this user's organisational unit owns
     *
     * @return Returns the quantity of credits
     */
    public float getUnitCredits() throws SQLException {
        return UsersDataSource.getInstance().getUnitCredits(organisationalUnitName);
    }

    public String getUnitName() { return organisationalUnitName; }
}