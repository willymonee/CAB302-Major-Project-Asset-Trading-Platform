package ElectronicAssetTradingPlatform.AssetTrading;

/**
 * Factory class to create an Asset Object
 */
public class AssetFactory {

    /**
     * Create an asset object given an assetName
     * @param assetName     The name of the asset to be created within the Factory
     * @return
     */
    public static Asset CreateAsset(String assetName) {
        return new Asset(assetName);
    }

}
