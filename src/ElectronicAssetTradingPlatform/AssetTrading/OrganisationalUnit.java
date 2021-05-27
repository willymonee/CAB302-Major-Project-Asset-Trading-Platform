package ElectronicAssetTradingPlatform.AssetTrading;


import java.util.Map;
import java.util.TreeMap;

/**
 * Class for the organisational units which are individual departments in the organisation e.g. Human Resources or
 * Management or Public Relations etc.
 * These are the organisational units in which the users will belong in, and will use their unit's assets and credits
 * to perform exchanges on this platform
 */

public class OrganisationalUnit {
    private String name;
    private double credits;
    private Map<String,Integer> assetsOwned;

    /**
     * Constructor for an organisational unit
     *
     * @param name The organisational unit's name
     * @param credits The number of credits the organisational unit starts with/currently owns when created
     */
    public OrganisationalUnit(String name, float credits) {
        this.name = name;
        this.credits = credits;
        assetsOwned = new TreeMap<>();
    }

    /**
     * Given an integer of credits, add this onto the existing amount of credits an organisational unit owns
     * However, cannot reduce credits by more than the organisational unit owns (can't go negative)
     * This is the base helper function for all other methods elsewhere which add/subtract organisational unit credits
     * [M]
     *
     * @param credits float amount of credits to add (positive int) or remove (negative int)
     *
     */
    public void addCredits(float credits){
        this.credits += credits;
    }

    /**
     * Given an integer of credits, add this onto the existing amount of credits an organisational unit owns
     * However, cannot reduce credits by more than the organisational unit owns (can't go negative)
     * This is the base helper function for all other methods elsewhere which add/subtract organisational unit credits
     * [M]
     *
     * @param credits float amount of credits to add (positive int) or remove (negative int)
     *
     * @throws Exception exception handling so that net credits cannot be less than zero
     */
    public void removeCredits(float credits) throws Exception {
        if (this.credits + credits < 0) {
            throw new Exception("Cannot remove more credits than there actually are!");
        }
        else {
            this.credits -= credits;
        }
    }

    /**
     * Given an existing asset object (has been created by IT admins)
     * and a quantity of the asset in integers, add the asset name as the key and quantity as
     * the value into the organisationalUnitAssets' TreeMap collection IF the organisation does not own any amount of
     * the asset yet.
     * Otherwise, add onto the existing quantity value in the TreeMap
     * [M]
     *
     * @param assetName Asset object which an organisational unit owns/going to own
     * @param quantityToAdd Number of that particular asset to be added (must be greater than 0)
     *
     */
    public void addAsset(String assetName, int quantityToAdd) {
        assetsOwned.put(assetName, assetsOwned.getOrDefault(assetName, 0) + quantityToAdd);
    }

    /**
     * Given an existing asset object (has been created by IT admins) and a quantity of the asset in integers,
     * reduce the quantity of the asset owned in the organisationalUnitAssets' TreeMap collection, where the asset name
     * is the Key and quantity is the Value.
     * [M]
     *
     *
     * The asset name should be unique and the quantity removed should not reduce the asset below zero
     *
     * @param assetName String asset which an organisational unit owns
     * @param quantityToRemove Number of that particular asset to be removed (must be less than number owned currently)
     *
     * @throws Exception // not enough assets owned to be removed
     * @throws Exception // organisational unit does not have the asset (cannot remove asset that is not owned)
     */

    public void removeAsset(String assetName, int quantityToRemove) throws Exception { // TODO: MAKE EXCEPTION FILE FOR THIS
        int currentQuantity;

        try {
            currentQuantity = assetsOwned.get(assetName);

        }
        catch (NullPointerException NPE) {
            throw new Exception("ASSET DOES NOT EXIST HELLO? "); // TODO: THROW NEW EXCEPTION FOR THIS FROM MADE EXCEPTIONS
        }

        if (currentQuantity > quantityToRemove) {
            assetsOwned.put(assetName, currentQuantity - quantityToRemove);
        }
        else if (quantityToRemove == currentQuantity) {
            assetsOwned.remove(assetName);
        }
        else {
            throw new Exception("Cannot remove more assets that owned!"); // TODO ADD EXCEPTION WHEN MADE
        }

    }

    public void editName(String name) {
        this.name = name;
    }

    public void removeAllAssets() {
        assetsOwned.clear();
    }

    public double getCredits() {
        return credits;
    }

    public String getUnitName() {
        return name;
    }

    /**
     * Getter for the assetsOwned field
     * @return A map of the asset_name and quantity for this unit's assets
     */
    public Map<String, Integer> getAssetsOwned() {
        return assetsOwned;
    }

}

