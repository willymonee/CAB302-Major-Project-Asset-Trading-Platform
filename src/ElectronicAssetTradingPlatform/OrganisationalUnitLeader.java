package ElectronicAssetTradingPlatform;

/**
 * Class for the organisational unit leaders
 */
public class OrganisationalUnitLeader extends OrganisationalUnitMembers{
    /**
     * Constructor used to set ID to organisational unit to user
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     * @param organisationalUnitID int ID matching the organisational unit
     *                             the organisational member is a part of
     *
     */
    public OrganisationalUnitLeader(String username, String password, int organisationalUnitID) {
        super(username, password, organisationalUnitID);

    }
}
