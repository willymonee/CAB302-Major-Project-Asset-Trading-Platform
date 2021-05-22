package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceDataSource;

public abstract class OfferData {
    protected abstract void getOffersFromDB();

    public void updateOfferQuantity(int quantity, int ID) {
        getOffersFromDB();
        MarketplaceDataSource.getInstance().updateOfferQuantity(quantity, ID);
    }

    /**
     * Retrieve the most recently placed offer's ID from the database
     *
     * @return int of the most recently placed offer ID
     */
    public int getPlacedOfferID() {
        return MarketplaceDataSource.getInstance().getPlacedOfferID();
    }
}
