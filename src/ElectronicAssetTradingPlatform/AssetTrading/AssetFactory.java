package ElectronicAssetTradingPlatform.AssetTrading;

/**
 * A factory class to create static Asset objects
 */
public class AssetFactory {
    /**
     * Constructor for creating a static Asset object provided an asset name is given
     *
     * @param assetName A string name of the asset to be created
     *
     * @return The Asset object created
     */
    public static Asset CreateAsset(String assetName) {
        return new Asset(assetName);
    }
}
