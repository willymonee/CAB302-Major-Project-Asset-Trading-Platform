package ElectronicAssetTradingPlatform.AssetTrading;

/**
 * Class 
 */
public class AssetFactory {

    public static Asset CreateAsset(String assetName) {
        return new Asset(assetName);
    }

}
