package ElectronicAssetTradingPlatform;

/**
 * Asset class creates an asset that belongs to an organisation that can stored, sold or bought
 *
 */
public class Asset {
    private int assetID;
    private String assetName;

    /**
     * Constructor to initialise an asset with asset ID and name
     *
     * @param assetID int ID for the asset
     * @param assetName string for asset name
     */
    public Asset(int assetID, String assetName) {
        this.assetID = assetID;
        this.assetName = assetName;
    }

    /**
     * Check if the asset is listed for trade
     *
     * @return returns false if the asset isn't currently listed otherwise true if asset is listed
     */
    public boolean isListed() {
        return false;
    }

    /**
     *  Retrieve the asset name
     *
     * @return string name of asset
     */
    public String getAssetName() {
        return this.assetName;
    }
}
