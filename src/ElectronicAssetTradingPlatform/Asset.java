package ElectronicAssetTradingPlatform;

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
    public Asset(String assetName) {
        if (TradingPlatformAssets.size() == 0) {
            this.assetID = 1;
        }
        this.assetID = TradingPlatformAssets.lastKey() + 1;
        this.assetName = assetName;
        TradingPlatformAssets.put(this.assetID, assetName);
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


    /**
     * Checks if the asset is in the list of created assets
     *
     * @return returns false if the asset isn't currently in the TreeMap otherwise true
     */
    public boolean checkAssetExists(String name) {
        return TradingPlatformAssets.containsValue(name);
    }
}

