package ElectronicAssetTradingPlatform;

import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * Asset class creates an asset that belongs to an organisation that can stored, sold or bought
 *
 */
public class Asset {
    private int assetID;
    private String assetName;
    private TreeMap<Integer, String> TradingPlatformAssets = new TreeMap<>();

    /**
     * Constructor to initialise an asset with asset ID and name
     * Adds the asset into the database if it does not exist yet
     *
     * @param assetName string for asset name
     */
    public Asset(String assetName) throws Exception {
        if (TradingPlatformAssets.containsValue(assetName)) {
            throw new Exception("An asset with the same name already exists in the database!");
        }
        try {
            assetID = TradingPlatformAssets.lastKey() + 1;
            this.assetName = assetName;
            TradingPlatformAssets.put(assetID, assetName);
        }
        catch (NoSuchElementException e) {
            assetID = 1;
            this.assetName = assetName;
            TradingPlatformAssets.put(assetID, assetName);
        }
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

