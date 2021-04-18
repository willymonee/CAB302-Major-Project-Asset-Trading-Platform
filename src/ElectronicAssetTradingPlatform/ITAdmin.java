package ElectronicAssetTradingPlatform;

public class ITAdmin extends User {

    public ITAdmin(String username, String password) {
        super(username, password);
    }

    /**
     * Create a new organisational unit, assign user/s to it, and update the database
     *
     * @param Name string name of the organisational unit
     * @param credits float credits to be initially assigned to the unit
     */
    public void createOrganisationalUnit(String Name, float credits) {
        // createOrganisationalUnit method
        // Create new org unit ID
        // Assign member/s with the new unit ID (or create new user containing the org unit ID)
    }

    /**
     * Add or remove the number of credits an organisational unit owns manually
     * Prints a success or error message
     *
     * @param organisationalUnit int ID of organisational unit to edit the credits for
     * @param credits int new amount of credits to edit for the organisational unit
     */
    public void editOrganisationalUnitCredits(OrganisationalUnit organisationalUnit, float credits) throws Exception {
        organisationalUnit.editCredits(credits);
    }

    /**
     * Edit the assets or quantity of asset an organisational unit owns
     *
     * @param organisationalUnit int ID of the organisational unit that owns the assets that are to be edited
     * @param assetName Asset type to be edited
     * @param quantity int quantity of the asset to be changed
     */
    public void editOrganisationalUnitAssets(OrganisationalUnit organisationalUnit, String assetName, int quantity) throws Exception {
        // Edit or add asset to unit
        // Should check the type exists within the db
        organisationalUnit.addAsset(assetName, quantity);
    }

    /**
     * Creates a new user
     *
     * @param name string for name of new user
     * @param organisationalUnitID int ID for new user to be associated with
     * @param userType user type for new user's access level
     */
    public void createUser(String name, int organisationalUnitID, User userType) {
        // createUser method
    }

    /**
     * Create a new asset type and add it to the database
     *
     * @param name string name of the asset type to be added to the database
     */
    // NOTE IT IS BEST FOR THE ID TO BE AUTOMATICALLY CREATED IN THE ASSET OR ASSET COLLECTION OBJECTS
    public void createNewAsset(String name, int ID) throws Exception{
        // Add parsed asset name to db
        AssetCollection.getAssetCollection().addAsset(name, ID);
    }
}
