package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

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
        getOffersFromDB();
        MarketplaceDataSource.getInstance().updateOfferQuantity(quantity, ID);
    }

    /**
     * Retrieve the most recently placed offer's ID from the database
     * @return int of the most recently placed offer ID
     */
    public int getPlacedOfferID() {
        return MarketplaceDataSource.getInstance().getPlacedOfferID();
    }

    /**
     * Checks if two strings are the same organisational unit name
     * @return true if they are the same name, otherwise false
     */
    protected boolean sameOrgUnitName(String unitNameA, String unitNameB) {
        return unitNameA.equals(unitNameB);
    }
}
