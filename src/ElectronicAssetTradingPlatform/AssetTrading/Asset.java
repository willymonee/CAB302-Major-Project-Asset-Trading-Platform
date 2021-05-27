package ElectronicAssetTradingPlatform.AssetTrading;

/**
 * The 'Asset' class provides goods or services owned by organisational units which can be used to trade.
 * This is achieved through the creation of the 'Asset' object by IT Admins, which adds an asset to the system.
 * It is important to note that the organisational units DO NOT own the 'Asset' object.
 * To add an asset to an organisational unit, an EXISTING Asset object's name is placed into a Map
 * containing the name of the asset as the key and the quantity owned as the value.
 */
public class Asset {
    private String assetName;


    /**
     * Constructor to initialise an asset with asset ID and name
     * Adds the asset into the database if it does not exist yet
     *
     * @param assetName string for asset name
     */
    public Asset(String assetName)  {
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
     *  Retrieve the asset's name
     *
     * @return string name of asset
     */
    public String getAssetName() {
        return this.assetName;
    }

    public void editAssetName(String name) {
        this.assetName = name;
    }



}


