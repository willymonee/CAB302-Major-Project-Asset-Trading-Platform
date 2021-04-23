package ElectronicAssetTradingPlatform.Miscellaneous;

public class BuyOffer extends Offer{

    /**
     * Constructor for trade offer
     *
     * @param asset                Name of the asset to be bought or sold
     * @param quantity             Quantity of asset
     * @param pricePerUnit         Price of the asset
     * @param organisationalUnitID The ID of the organisation whose assets and credits will be affected
     * @param userID               The ID of the user who made the offer
     */
    public BuyOffer(Asset asset, int quantity, float pricePerUnit, int organisationalUnitID, int userID) {
        super(asset, quantity, pricePerUnit, organisationalUnitID, userID);
    }
}
