package ElectronicAssetTradingPlatform.AssetTrading;

import java.util.HashMap;

public class UnitFactory {

    public enum EditCreditType {
        addCredits, removeCredits
    }

    public enum EditAssetType {
        addAssets, removeAssets
    }

    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits) {
        return new OrganisationalUnit(unitName, credits);
    }

    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits, HashMap<String, Integer> assets) {
        return new OrganisationalUnit(unitName, credits, assets);
    }
}
