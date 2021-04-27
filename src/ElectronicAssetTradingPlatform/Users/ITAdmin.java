package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Database.AssetCollection;
import ElectronicAssetTradingPlatform.Miscellaneous.OrganisationalUnit;

/**
 * ITAdmin class which extends the user class. This class is for the IT administration team
 * giving them privileges, allowing them to do tasks such as creating and managing new
 * organisational units, assets and the amount of credits for an organisational unit.
 */
public class ITAdmin extends User {

    /**
     * Constructor for ITAdmin class to login with administration access levels
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     */
    public ITAdmin(String username, String password) {
        super(username, password);
    }

    /**
     * Create a new organisational unit, assign user/s to it, and update the database [M]
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
     * Add or remove the number of credits an organisational unit owns manually [M]
     * Prints a success or error message
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param credits int new amount of credits to edit for the organisational unit
     */
    public void editOrganisationalUnitCredits(String unitName, float credits) throws Exception {
        //organisationalUnit.editCredits(credits);
    }

    /**
     * Edit the assets or quantity of asset an organisational unit owns [M]
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param assetName Asset type to be edited
     * @param quantity int quantity of the asset to be changed
     */
    public void editOrganisationalUnitAssets(String unitName, String assetName, int quantity) throws Exception {
        // Edit or add asset to unit
        // Should check the type exists within the db
        //organisationalUnit.addAsset(assetName, quantity);
    }

    /**
     * Creates a new user [M]
     *
     * @param name string for name of new user
     * @param unitName string organisational unit name for new user to be associated with
     * @param userType user type for new user's access level
     */
    public void createUser(String name, int unitName, User userType) {
        // createUser method
    }

    /**
     * Create a new asset type and add it to the database [M]
     *
     * @param name string name of the asset type to be added to the database
     */
    // NOTE IT IS BEST FOR THE ID TO BE AUTOMATICALLY CREATED IN THE ASSET OR ASSET COLLECTION OBJECTS
    public void createNewAsset(String name) throws Exception{
        // Add parsed asset name to db
        AssetCollection.addAssetToCollection(name);
    }

    /**
     * Choose to edit the user's username, user type and organisational unit [C]
     *
     * @param username the new username the use will have
     * @param userType the new user type the user will be
     * @param organisationalUnit the new organisational unit that the user will be part of
     */
    public void editUser(String username, User userType, OrganisationalUnit organisationalUnit ) {

    }

    /**
     * Edit an organisational unit's name [C]
     *
     * @param currentName the current name the organisational unit has
     * @param newName the new name for the organisational unit
     */
    public void editOrganisationalUnitName(String currentName, String newName) {

    }

    /**
     * Edit an asset's name [C]
     *
     * @param currentName the current name the asset has
     * @param newName the new name for the asset
     */
    public void editAssetName(String currentName, String newName) {

    }
}
