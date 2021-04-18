package ElectronicAssetTradingPlatform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.TreeMap;

// U ARE A SINGLETON
public class AssetCollection {
    private TreeMap<Integer, Asset> TradingPlatformAssets = new TreeMap<>();
    private static AssetCollection assetCollection = null;

    // a collection to contain all types of assets
    private AssetCollection() {
//        if (TradingPlatformAssets.containsValue(Asset)) {
//            throw new Exception("An asset with the same name already exists in the database!");
//        }
//        try {
//            assetID = TradingPlatformAssets.lastKey() + 1;
//            this.assetName = assetName;
//            TradingPlatformAssets.put(assetID, assetName);
//        }
//        catch (NoSuchElementException e) {
//            assetID = 1;
//            this.assetName = assetName;
//            TradingPlatformAssets.put(assetID, assetName);
//        }
    }

    public void addAsset(String name, Integer ID) {
        Asset asset = new Asset(name, ID);
        TradingPlatformAssets.put(ID, asset);
    }

    public static AssetCollection getAssetCollection() {
        if (assetCollection == null) {
            return new AssetCollection();
        }
        else {
            return assetCollection;
        }
    }

    public boolean checkAssetExists() {
        // TradingPlatformAssets.containsValue(Asset);
        return true;
    }
}
