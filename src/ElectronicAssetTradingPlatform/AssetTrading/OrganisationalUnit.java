package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Exceptions.MissingAssetException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for the organisational units which are individual departments in the organisation e.g. Human Resources or
 * Management or Public Relations etc.
 * These are the organisational units in which the users will belong in, and will use their unit's assets and credits
 * to perform exchanges on this platform.
 */
public class OrganisationalUnit implements Serializable {
    private String name;
    private float credits;
    private Map<String,Integer> assetsOwned;

    /**
     * Constructor for an organisational unit that sets the constructor's parameter's into the local storage.
     *
     * @param name A string name of the organisational unit to be initialised
     * @param credits A float amount of total credits the organisational unit is to be initialised with
     */
    public OrganisationalUnit(String name, float credits) {
        this.name = name;
        this.credits = credits;
        assetsOwned = new TreeMap<>();
    }

    /**
     * Constructor for an organisational unit that sets the constructor's parameter's into the local storage.
     *
     * @param name A string name of the organisational unit to be initialised
     * @param credits A float amount of total credits the organisational unit is to be initialised with
     * @param assets A hashmap collection of string and integers of the assets owned by the organisational unit
     */
    public OrganisationalUnit(String name, float credits, HashMap<String, Integer> assets) {
        this.name = name;
        this.credits = credits;
        assetsOwned = assets;
    }

    /**
     * Adds a given amount of credits onto the existing credits that the organisational unit already has.
     *
     * @param credits A float amount of credits to add to the existing credits of the organisational unit
     */
    public void addCredits(float credits){
        this.credits += credits;
    }

    /**
     * Removes a given amount of credits from the existing credits that the organisational unit owns.
     *
     * @param credits A float amount of credits to be removed from the organisational unit
     *
     * @throws LessThanZeroException An exception to handle the total amount of credits of the organisational unit
     *                               being less than zero disallowing the given amount of credits to be removed
     */
    public void removeCredits(float credits) throws LessThanZeroException {
        if (this.credits - credits < 0) {
            throw new LessThanZeroException("Cannot remove more credits than owned... ");
        }
        else {
            this.credits -= credits;
        }
    }

    /**
     * Given a name and an amount to add, an asset will be added to the organisational unit's assets owned collection.
     * If the organisational unit already owns the specified asset, the quantity to add will be added onto the
     * currently existing total amount of that asset, otherwise a new asset with the specified amount will be added.
     *
     * @param assetName A string name of the asset/s to be added or added to
     * @param amountToAdd An integer amount of assets to add to the asset
     */
    public void addAsset(String assetName, int amountToAdd) {
        assetsOwned.put(assetName, assetsOwned.getOrDefault(assetName, 0) + amountToAdd);
    }

    /**
     * Given a name and an amount to remove, the asset will be removed from the organisational unit's assets owned
     * collection. If the asset to be removed is not found to be owned by the organisational unit, then an exception
     * will be thrown detailing that. If the organisational unit does not own more assets than the amount of assets to
     * be removed, then an exception will be also be thrown detailing this. If the amount of assets to be removed is
     * equal to the amount the organisational unit currently owns, then the asset will be completely removed from the
     * organisational unit meaning they will no longer own any quantities of this asset.
     *
     * @param assetName A string name of the asset/s to be removed
     * @param amountToRemove An integer amount of assets to be removed from the asset or be completely removed
     *
     * @throws MissingAssetException An exception to handle the event that the queried asset name is not currently
     *                               owned by the organisational unit
     * @throws LessThanZeroException An exception to handle the removal of more assets than the organisational unit
     *                               currently owns
     */

    public void removeAsset(String assetName, int amountToRemove) throws MissingAssetException, LessThanZeroException {
        int currentQuantity;

        try {
            currentQuantity = assetsOwned.get(assetName);
        }
        catch (NullPointerException NPE) {
            throw new MissingAssetException("This does not exist or is currently not owned by this organisational " +
                                            "unit... ");
        }

        if (currentQuantity > amountToRemove) {
            assetsOwned.put(assetName, currentQuantity - amountToRemove);
        }
        else if (amountToRemove == currentQuantity) {
            assetsOwned.remove(assetName);
        }
        else {
            throw new LessThanZeroException("Cannot remove more assets that owned!"); // MAKE NEW EXCEPTION
        }

    }

    /**
     * A setter function to edit the name of the organisational unit.
     *
     * @param name A string name to change the organisational unit's name to
     */
    public void editName(String name) {
        this.name = name;
    }

    /**
     * A method to remove all the assets that the organisational unit currently owns.
     */
    public void removeAllAssets() {
        assetsOwned.clear();
    }

    /**
     * A getter function to retrieve and return the total amount of credits the organisational unit currently owns.
     *
     * @return The total amount of credits that the organisational unit owns
     */
    public float getCredits() {
        return credits;
    }

    /**
     * A getter function that retrieves and returns the name of the organisational unit.
     *
     * @return The name of the organisational unit
     */
    public String getUnitName() {
        return name;
    }

    /**
     * A getter function for retrieving and returning all the assets that the organisational unit currently owns.
     *
     * @return A map containing all the assets and their amounts in which this organisational unit currently owns
     */
    public Map<String, Integer> getAssetsOwned() {
        return assetsOwned;
    }

    /**
     * A getter function for retrieving and returning the amount of a specific singular asset given the name of the
     * asset to query.
     *
     * @param assetName A string name of the asset to return the amount of that asset
     *
     * @return Return the quantity amount of the asset queried
     */
    public int getAssetQuantity(String assetName) {
        if (assetsOwned.get(assetName) == null) {
            return 0;
        }
        else {
            return assetsOwned.get(assetName);
        }

    }

}

