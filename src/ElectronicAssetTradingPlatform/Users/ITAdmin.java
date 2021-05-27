package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.Asset;

import java.security.SecureRandom;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * ITAdmin class which extends the user class. This class is for the IT administration team
 * giving them privileges, allowing them to do tasks such as creating and managing new
 * organisational units, assets and the amount of credits for an organisational unit.
 */
public class ITAdmin extends User {
    private static final SecureRandom rng = new SecureRandom();
    private static final char[] characters = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
    private static final int PWD_SIZE = 8;

    /**
     * Constructor for ITAdmin class to login with administration access levels
     *
     * @param username string identifier used to login
     * @param password string matched with username identifier used to login
     */
    public ITAdmin(String username, String password, String salt) {
        super(username, password, salt);
        this.userType = UsersFactory.UserType.ITAdmin.toString();
    }

    /**
     * Create a new organisational unit, assign user/s to it, and update the database [M]
     *
     * @param name string name of the organisational unit
     * @param credits float credits to be initially assigned to the unit
     */
    public OrganisationalUnit createOrganisationalUnit(String name, float credits) throws EmptyFieldException {
        checkInputEmpty(name);

        return new OrganisationalUnit(name, credits);

        // createOrganisationalUnit method
        // Create new org unit ID
        // Assign member/s with the new unit ID (or create new user containing the org unit ID)
    }

    /**
     * Add or remove the number of credits an organisational unit owns manually [M]
     * Prints a success or error message
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param credits int new amou  nt of credits to edit for the organisational unit
     */
    public OrganisationalUnit addOrganisationalUnitCredits(OrganisationalUnit unitName, float credits) throws Exception {
        checkInputEmpty(unitName.getUnitName()); // these might be redundant

        unitName.addCredits(credits); // passed by value

        return unitName;

        // TODO: add exceptions for non existent unitName, ADD implementation for confirmation when GUI is implemented


    }

    /**
     * Add or remove the number of credits an organisational unit owns manually [M]
     * Prints a success or error message
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param credits int new amou  nt of credits to edit for the organisational unit
     */
    public OrganisationalUnit removeOrganisationalUnitCredits(OrganisationalUnit unitName, float credits) throws Exception {
        checkInputEmpty(unitName.getUnitName()); // these might be redundant

        unitName.removeCredits(credits); // passed by value

        return unitName;

        // TODO: add exceptions for non existent unitName, ADD implementation for confirmation when GUI is implemented


    }

    /**
     * Edit the assets or quantity of asset an organisational unit owns [M]
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param assetName Asset type to be edited
     * @param quantity int quantity of the asset to be changed
     */
    public OrganisationalUnit addOrganisationalUnitAssets(OrganisationalUnit unitName, String assetName, int quantity) throws Exception {
        checkInputEmpty(unitName.getUnitName()); // these might be redundant
        checkInputEmpty(assetName);

        unitName.addAsset(assetName, quantity);

        return unitName;

    }

    /**
     * Edit the assets or quantity of asset an organisational unit owns [M]
     *
     * @param unitName string organisational unit name that owns the assets that are to be edited
     * @param assetName Asset type to be edited
     * @param quantity int quantity of the asset to be changed
     */
    public OrganisationalUnit removeOrganisationalUnitAssets(OrganisationalUnit unitName, String assetName, int quantity) throws Exception {
        checkInputEmpty(unitName.getUnitName()); // these might be redundant
        checkInputEmpty(assetName);

        unitName.removeAsset(assetName, quantity);

        return unitName;

    }

    /**
     * Creates a new user [M]
     *
     * @param name string for name of new user
     * @param unitName string organisational unit name for new user to be associated with
     * @param userType user type for new user's access level
     * @return the newly created user object
     */
    public User createUser(String name, String unitName, String userType) throws UserTypeException, EmptyFieldException {
        // Check valid parameters
        checkInputEmpty(name);
        checkInputEmpty(userType);

        // Create password - length 8
        // Hash password
        byte[] saltBytes = Hashing.newRngBytes(Hashing.SALT_SIZE);
        String passwordRaw = newRngText(PWD_SIZE);
        byte[] passwordBytes = Hashing.createHash(saltBytes, passwordRaw);

        // Convert to string
        String salt = Hashing.bytesToString(saltBytes);
        String password = Hashing.bytesToString(passwordBytes);

        // Display the raw password for the admin to copy
        JTextArea text = new JTextArea("Please copy this password down: " + passwordRaw);
        text.setBackground(null);
        JOptionPane.showMessageDialog(null, text);

        // Try get type
        UsersFactory.UserType type = null;
        try {
            type = UsersFactory.UserType.valueOf(userType);
        }
        catch (IllegalArgumentException e) {
            throw new User.UserTypeException("Invalid user type");
        }

        return UsersFactory.CreateUser(name, password, salt, unitName, type);
    }

    /**
     * Create a new asset type and add it to the database [M]
     *
     * @param name string name of the asset type to be added to the database
     */
    public Asset createNewAsset(String name) throws EmptyFieldException {
        checkInputEmpty(name);

        return new Asset(name);

    }

    /**
     * Choose to edit the user's user type and organisational unit [C]
     * @param username the new username the user will have
     * @param userType the new user type the user will be
     * @param unitName the organisational unit that the user will be part of
     * @return
     */
    public String[] editUser(String username, String userType, String unitName) throws EmptyFieldException, SQLException, UserTypeException {
        // Check valid input
        checkInputEmpty(username);
        checkInputEmpty(userType);

        // Checks complete - query to update db
        // Clear unit name if IT/SysAdmin
        try {
            switch (UsersFactory.UserType.valueOf(userType)) {
                case ITAdmin, SystemsAdmin -> UsersDataSource.getInstance().editUser(username, userType, null);
                case OrganisationalUnitMembers, OrganisationalUnitLeader -> UsersDataSource.getInstance().editUser(username, userType, unitName);
                default -> throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new UserTypeException("Invalid user type");
        }
        return new String[0];
    }

    /**
     * Edit an organisational unit's name [C]
     *
     * @param unitName the current name the organisational unit has
     * @param newName the new name for the organisational unit
     */
    public OrganisationalUnit editOrganisationalUnitName(OrganisationalUnit unitName, String newName) throws EmptyFieldException {
        checkInputEmpty(unitName.getUnitName()); // these might be redundant
        checkInputEmpty(newName);

        unitName.editName(newName);

        return unitName;

    }

    /**
     * Edit an asset's name [C]
     *
     * @param assetName the current name the asset has
     * @param newName the new name for the asset
     */
    public Asset editAssetName(Asset assetName, String newName) throws EmptyFieldException {
        checkInputEmpty(assetName.getAssetName()); // these might be redundant
        checkInputEmpty(newName);

        assetName.editAssetName(newName);

        return assetName;
    }



    private String newRngText(int length) {
        if (length == 0) throw new IndexOutOfBoundsException("Length cannot be 0");

        StringBuilder password = new StringBuilder();

        for (int i = 0; i <= length; i++) {
            password.append(characters[rng.nextInt(characters.length)]);
        }

        return password.toString();
    }
}
