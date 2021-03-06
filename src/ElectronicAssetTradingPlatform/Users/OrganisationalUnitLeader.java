package ElectronicAssetTradingPlatform.Users;

/**
 * Class for the organisational unit leaders
 * Organisanational unit leaders are part of the organisational unit, however they have additional capabilities over
 * ordinary members.
 */
public class OrganisationalUnitLeader extends OrganisationalUnitMembers {
    /**
     * Constructor used to set ID to organisational unit to user
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     * @param unitName string name of the organisational unit the member is a part of
     *
     */
    public OrganisationalUnitLeader(String username, String password, String salt, String unitName) {
        super(username, password, salt, unitName);
        this.userType = UsersFactory.UserType.OrganisationalUnitLeader.toString();
    }

    /**
     *im gunna
     * @param memberName Username of the member to be kicked
     */
    public void kickMember(String memberName) {

    }
}
