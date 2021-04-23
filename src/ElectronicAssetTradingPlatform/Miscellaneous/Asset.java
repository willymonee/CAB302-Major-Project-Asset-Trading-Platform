package ElectronicAssetTradingPlatform.Miscellaneous;

/**
 * Asset class creates an asset that belongs to an organisation that can stored, sold or bought
 *
 */
public class Asset {
    private int assetID;
    private String assetName;


    /**
     * Constructor to initialise an asset with asset ID and name
     * Adds the asset into the database if it does not exist yet
     *
     * @param assetName string for asset name
     */
    public Asset(String assetName, int ID)  {
        this.assetName = assetName;
        this.assetID = ID;
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
     *  Retrieve the asset's name
     *
     * @return string name of asset
     */
    public String getAssetName() {
        return this.assetName;
    }



}


