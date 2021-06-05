package ElectronicAssetTradingPlatform.AssetTrading;

import java.util.HashMap;

/**
 * A factory class to create static OrganisationalUnit objects.
 */
public class UnitFactory {
    /**
     * An enum class holding constants to help distinguish the edit type for credits.
     */
    public enum EditCreditType {
        addCredits, removeCredits
    }

    /**
     * An enum class holding constants to help distinguish the edit type for assets.
     */
    public enum EditAssetType {
        addAssets, removeAssets
    }

    /**
     * Constructor for creating a new static OrganisationalUnit object given the name for the unit, credits to be
     * initialised, and the hashmap of assets owned by the organisational unit.
     *
     * @param unitName A string name for the organisatioanl unit to be created
     * @param credits A float amount of credits to be initialised with the created organisational unit
     * @param assets A hashmap of string and integer values of the asset and its amount owned by the organisational unit
     *
     * @return The OrganisationalUnit object created
     */
    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits, HashMap<String, Integer> assets) {
        return new OrganisationalUnit(unitName, credits, assets);
    }
}
