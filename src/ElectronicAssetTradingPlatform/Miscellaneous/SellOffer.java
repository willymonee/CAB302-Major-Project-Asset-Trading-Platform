package ElectronicAssetTradingPlatform.Miscellaneous;

public class SellOffer extends Offer{
    /**
     * Constructor for trade offer
     *
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public SellOffer(Asset asset, int quantity, float pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
    }
}
