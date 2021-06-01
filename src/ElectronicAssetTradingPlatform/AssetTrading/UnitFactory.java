package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;

public class UnitFactory {

    public enum EditCreditType {
        addCredits, removeCredits
    }

    public static OrganisationalUnit CreateOrgUnit(String unitName, float credits) throws LessThanZeroException {

        return new OrganisationalUnit(unitName, credits);
    }
}
