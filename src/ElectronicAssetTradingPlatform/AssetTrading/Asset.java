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
     * A constructor to initialise an Asset object.
     *
     * @param assetName A string name of the initialised asset object
     */
    public Asset(String assetName)  {
        this.assetName = assetName;
    }

    /**
     *  A getter function to retrieve and return the name of the asset.
     *
     * @return The name of the asset
     */
    public String getAssetName() {
        return this.assetName;
    }

    /**
     * Edit the name of an asset given the new name for the asset's name to be changed to.
     *
     * @param newName A string name for the asset's name to be changed to
     */
    public void editAssetName(String newName) {
        this.assetName = newName;
    }

}


