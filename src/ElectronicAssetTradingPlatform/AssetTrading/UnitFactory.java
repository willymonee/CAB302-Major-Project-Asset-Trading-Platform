package ElectronicAssetTradingPlatform.AssetTrading;

import java.util.HashMap;

/**
 * Factory class to create a new Organisational Unit Object
 */
public class UnitFactory {

    /**
     * Options (in GUI) to edit a unit's credits
     */
    public enum EditCreditType {
        addCredits, removeCredits
    }

    /**
     * Options (in GUI) to edit a unit's assets
     */
    public enum EditAssetType {
        addAssets, removeAssets
    }

    /**
     * Creates a new Organisational Unit Object with credits
     * @param unitName      Name of the Organisational Unit
     * @param credits       Initialised credits for the Organisational Unit
     *                      being created
     * @return              OrganisationalUnit Object
     */
    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits) {
        return new OrganisationalUnit(unitName, credits);
    }

    /**
     * Creates a new Organisational Unit Object with credits and existing assets
     * @param unitName      Name of the Organisational Unit
     * @param credits       Initialised credits for the Organisational Unit
     *                      being created
     * @param assets        List of assets the Organisational Unit owns
     * @return              OrganisationalUnit Object
     */
    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits, HashMap<String, Integer> assets) {
        return new OrganisationalUnit(unitName, credits, assets);
    }
}
