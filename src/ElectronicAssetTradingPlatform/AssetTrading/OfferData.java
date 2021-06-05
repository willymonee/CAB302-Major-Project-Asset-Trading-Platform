package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;


/**
 * Abstract class for BuyOfferData and SellOfferData
 * Describes and implements methods related to inserting and retrieving offers, and manipulating retrieved offer data
 */
public abstract class OfferData {
    /**
     * Retrieve the respective offers from the database
     */
    protected abstract void getOffersFromDB();

    /**
     * Checks if offer with that ID exists
     * @param ID given the offer's ID, check if that ID exists in the database
     */
    public abstract boolean offerExists(int ID);

    /**
     * Return a string of an organisational unit's offers based.
     * @param unitName the organisational unit whose offers will be returned
     * @return String of offers
     */
    public abstract String getOrgOffers(String unitName);

    /**
     * Calculate the total quantity requested/for sale for a particular offer
     * @param assetName of queried asset
     * @return the total quantity requested for a particular asset
     */
    public abstract int assetQuantity(String assetName);

    /**
     * Remove an offer from the DB
     */
    public void removeOffer(int ID) {
        NetworkDataSource dataSource = new NetworkDataSource();
        dataSource.run();
        dataSource.removeOffer(ID);
    }

    /**
     * Given a quantity and offer, edit the offer's quantity by that amount
     * @param quantity is the amount you want to change the offer by
     * @param ID is the ID of the offer you want to change the quantity of
     */
    public void updateOfferQuantity(int quantity, int ID) {
        NetworkDataSource dataSource = new NetworkDataSource();
        dataSource.run();
        dataSource.updateOfferQuantity(quantity,ID);
    }

    /**
     * Retrieve the most recently placed offer's ID from the database
     * @return int of the most recently placed offer ID
     */
    public int getPlacedOfferID() {
        NetworkDataSource dataSource = new NetworkDataSource();
        dataSource.run();
        return dataSource.getPlacedOffer();
    }

    /**
     * Checks if two strings are the same organisational unit name
     * @return true if they are the same name, otherwise false
     */
    protected boolean sameOrgUnitName(String unitNameA, String unitNameB) {
        return unitNameA.equals(unitNameB);
    }

    /**
     * Checks if two strings are the same asset
     * @return true if they are the same name, otherwise false
     */
    protected boolean sameAssetName(String assetNameA, String assetNameB) {
        return assetNameA.equals(assetNameB);
    }
}
