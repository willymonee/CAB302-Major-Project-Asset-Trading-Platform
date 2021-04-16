package ElectronicAssetTradingPlatform;

public class ITAdmin extends User {

    public ITAdmin(String username, String password) {
        super(username, password);
    }

    /**
     * Create a new                                               this needs parameters
     *
     */
    private void createOrganisationalUnit() {
        // createOrganisationalUnit method

    }

    /**
     * Edit the number of credits an organisational unit owns manually
     *
     * @param organisationalUnit int ID of organisational unit to edit the credits for
     * @param credits int new amount of credits to edit for the organisational unit
     */
    private void addOrganisationalUnitCredits(OrganisationalUnit organisationalUnit, int credits) {
        organisationalUnit.editCredits(credits);
    }

    /**
     * Creates a new user
     *
     * @param name string for name of new user
     * @param organisationalUnitID int ID for new user to be associated with
     * @param userType user type for new user's access level
     */
    private void createUser(String name, int organisationalUnitID, User userType) {
        // createUser method
    }

    /**
     * Create a new asset and add to database
     *
     * @param assetType asset
     * @param assetName string name of asset to add
     * @param organisationalUnitID int ID of organisational unit the new asset is assigned to
     */
    private void insertAsset(Asset assetType, String assetName, int organisationalUnitID) {

    }
}
