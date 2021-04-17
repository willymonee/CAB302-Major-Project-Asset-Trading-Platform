package ElectronicAssetTradingPlatform;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class for the organisational units which are individual departments in the organisation e.g. Human Resources or
 * Management or Public Relations etc.
 * These are the organisational units in which the users will belong in, and will use their unit's assets and credits
 * to perform exchanges on this platform
 */

public class OrganisationalUnit {
    private int organisationalUnitID;
    private String organisationalUnitName;
    private int credits;
    private Map<String,Integer> organisationalUnitAssets = new TreeMap<>();

    /**
     * Constructor for an organisational unit
     *
     * @param ID ID of an organisational unit
     * @param Name The organisational unit's name
     * @param credits The number of credits the organisational unit starts with/currently owns when created
     */
    public OrganisationalUnit(int ID, String Name, int credits) {
        organisationalUnitID = ID;
        organisationalUnitName = Name;
        this.credits = credits;
    }

    /**
     * Given an integer of credits, add this onto the existing amount of credits an organisational unit owns
     * However, cannot reduce credits by more than the organisational unit owns (can't go negative)
     * This is the base helper function for all other methods elsewhere which add/subtract organisational unit credits
     *
     * @param credits int amount of credits to add (positive int) or remove (negative int)
     */
    public void editCredits(int credits) {
        if (credits < 0 && Math.abs(credits) > this.credits) {
            // throw an error
        }
        else {
            this.credits += credits;
        }
    }

    /**
     * Given an existing asset object (has been created by IT admins)
     * and a quantity of the asset in integers, add the asset name as the key and quantity as
     * the value into the organisationalUnitAssets' TreeMap collection IF the organisation does not own any amount of
     * the asset yet.
     * Otherwise, add onto the existing quantity value in the TreeMap
     *
     * @param asset Asset object which an organisational unit owns/going to own
     * @param quantity Number of that particular asset to be added (must be greater than 0)
     *
     */
    public void addAsset(Asset asset, Integer quantity) {
        String assetName = asset.getAssetName();
        // if the organisation already has any amount of the asset
        if (organisationalUnitAssets.containsKey(assetName)) {
            Integer currentQuantity = organisationalUnitAssets.get(assetName);
            Integer newQuantity = currentQuantity + quantity;
            organisationalUnitAssets.put(assetName, newQuantity);
        }
        // if the organisation does not currently have any amount of the asset
        else {
            organisationalUnitAssets.put(assetName, quantity);
        }
    }

    /**
     * Given an existing asset object (has been created by IT admins) and a quantity of the asset in integers,
     * reduce the quantity of the asset owned in the organisationalUnitAssets' TreeMap collection, where the asset name
     * is the Key and quantity is the Value.
     *
     *
     * The asset name should be unique and the quantity removed should not reduce the asset below zero
     *
     * @param asset Asset object which an organisational unit owns
     * @param quantity Number of that particular asset to be removed (must be less than number owned currently)
     *
     * @throws // not enough assets owned to be removed
     * @throws // organisational unit does not have the asset (cannot remove asset that is not owned)
     */
    public void removeAsset(Asset asset, Integer quantity) {
        String assetName = asset.getAssetName();
        Integer currentQuantity = organisationalUnitAssets.get(assetName);
        if (organisationalUnitAssets.containsKey(assetName)) {
            if (quantity <= currentQuantity) {
                Integer newQuantity = currentQuantity - quantity;
                organisationalUnitAssets.put(assetName, newQuantity);
            }
            else {
                // throw some error
            }
        }
        else {
            // throw some error
        }
    }
}
