package ElectronicAssetTradingPlatform;

import java.util.HashSet;
import java.util.Map;

public class OrganisationalUnit {
    private int organisationalUnitID;
    private String organisationalUnitName;
    private int credits;
    private HashSet<Map<Asset,Integer>> organisationalUnitAssets;


    public OrganisationalUnit(int ID, String Name, int credits) {
        organisationalUnitID = ID;
        organisationalUnitName = Name;
        this.credits = credits;
    }

    /**
     * Edit the amount of credits an organisational unit owns - This is the base helper function for all other methods
     * which add organisational unit credits
     *
     * @param credits int new amount of credits to edit for the organisational unit
     */
    public void editCredits(int credits) {
        this.credits += credits;
    }


}
