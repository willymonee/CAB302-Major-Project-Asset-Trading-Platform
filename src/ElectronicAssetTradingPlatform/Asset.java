package ElectronicAssetTradingPlatform;

/**
 * Asset class creates an asset that belongs to an organisation that can stored, sold or bought
 *
 */
public class Asset {
    private int assetID;
    private String name;

    /**
     * Constructor to initialise an asset with asset ID and name
     *
     * @param assetID int ID for the asset
     * @param name string for asset name
     */
    public Asset(int assetID, String name) {
        this.assetID = assetID;
        this.name = name;
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
        return this.name;
    }
}
