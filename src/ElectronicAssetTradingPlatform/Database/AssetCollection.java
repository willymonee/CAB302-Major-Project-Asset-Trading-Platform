package ElectronicAssetTradingPlatform.Database;

import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Map;
import ElectronicAssetTradingPlatform.Backend.Asset;

/**
 * AssetCollection class creates an object which contains a TreeMap field to store 'Asset' objects and their IDs
 * The methods in this class pertain to manipulating or interacting with either the TreeMap
 *
 */

public class AssetCollection {
    private static TreeMap<Integer, Asset> TradingPlatformAssets = new TreeMap<>();


    /**
     * Constructor to initialise the single AssetCollection object.
     *
     */
    public AssetCollection() { }



    /**
     * Create a new asset object and add it into the AssetCollection object's TreeMap as well as the database.
     * Primarily used by the IT admin user
     *
     */
    public static void addAssetToCollection(String name) {
        if(!checkAssetExists(name)) {
            int uniqueID = TradingPlatformAssets.lastKey() + 1;
            Asset asset = new Asset(name, uniqueID);
            TradingPlatformAssets.put(uniqueID, asset);
        }
        else {
            // throw error
        }
    }


    /**
     * Check if the asset is stored in the TradingPlatformAssets via assetName
     * Method creates an entry set out of the TreeMap and grabs each existing Asset and then its name
     * then compares it with the queried asset name
     *
     * @param queriedAssetName String is the name of the asset you want to check exists in the system
     *
     * @return returns true if the asset was found in the collection, false otherwise
     */
    public static boolean checkAssetExists(String queriedAssetName) {
        for (Map.Entry<Integer, Asset> entry: TradingPlatformAssets.entrySet()) {
            if (entry.getValue().getAssetName().equals(queriedAssetName)) {
                return true;
            }
        }
        return false;
    }
}
