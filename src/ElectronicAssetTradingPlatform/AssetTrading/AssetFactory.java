package ElectronicAssetTradingPlatform.AssetTrading;

public class AssetFactory {

    public static Asset CreateAsset(String assetName) {
        return new Asset(assetName);
    }

}
