package ElectronicAssetTradingPlatform.Users;

import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Exceptions.MissingAssetException;
import ElectronicAssetTradingPlatform.Exceptions.UserTypeException;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.AssetTrading.Asset;

import java.security.SecureRandom;
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
     * Create a new organisational unit given a name and an amount of credits to be initialised with the organisational
     * unit.
     *
     * @param name A string name for the newly to be created organisational unit
     * @param credits A float amount of credits to be initialised with the new organisational unit
     *
     * @return A newly created OrganisationalUnit object
     */
    public OrganisationalUnit createOrganisationalUnit(String name, float credits) {
        return new OrganisationalUnit(name, credits);
    }

    /**
     * Add additional credits to an existing organisational unit given the unit object and an amount of credits to add.
     *
     * @param unitName An OrganisationalUnit object of the organisational unit for credits to be added to
     * @param credits A float amount of credits to be added to the organisational unit
     *
     * @return The OrganisationalUnit object where the credits have been added
     */
    public OrganisationalUnit addOrganisationalUnitCredits(OrganisationalUnit unitName, float credits) {
        unitName.addCredits(credits);
        return unitName;
    }

    /**
     * Remove a certain amount of credits from an existing organisational unit given the unit object and credits to be
     * removed.
     *
     * @param unitName An OrganisationalUnit object of the organisational unit for the credits to be removed
     * @param credits A float amount of credits to remove from the organisational unit
     *
     * @return The OrganisationalUnit object where the credits have been removed
     */
    public OrganisationalUnit removeOrganisationalUnitCredits(OrganisationalUnit unitName, float credits)
            throws LessThanZeroException {
        unitName.removeCredits(credits);
        return unitName;
    }

    /**
     * Add additional amounts of assets to an organisational unit given the unit object, the asset name to add
     * quantities to, and the amount of extra assets to add.
     *
     * @param unitName An OrganisationalUnit object of the organisational unit whose asset belongs to
     * @param assetName A string name of the asset to which the extra quantities will be added
     * @param quantity An integer amount of extra assets to be added
     *
     * @return The OrganisationalUnit object in which the assets have been added to
     */
    public OrganisationalUnit addOrganisationalUnitAssets(OrganisationalUnit unitName, String assetName, int quantity) {
        unitName.addAsset(assetName, quantity);
        return unitName;
    }

    /**
     * Remove an amount of assets from an organisational unit given the unit object, the asset name for quantities to
     * be removed, and the amount of assets to be removed.
     *
     * @param unitName An OrganisationalUnit object of the organisational unit the asset belongs to
     * @param assetName A string name of the asset amounts to be removed
     * @param quantity An integer amount of assets to be removed
     *
     * @return The OrganisationalUnit object in which the assets have been removed from
     */
    public OrganisationalUnit removeOrganisationalUnitAssets(OrganisationalUnit unitName, String assetName,
                                                             int quantity) throws MissingAssetException,
                                                                                  LessThanZeroException {
        unitName.removeAsset(assetName, quantity);
        return unitName;
    }

    /**
     * Create a new asset given the name of the asset
     *
     * @param name A string name of the newly to be created asset
     *
     * @return A newly created Asset object
     */
    public Asset createNewAsset(String name) {
        return new Asset(name);
    }

    /**
     * Edit the name of the organisational unit given the original OrganisationalUnit object and the new name for it.
     *
     * @param unitName An OrganisationalUnit object of the unit to be name changed
     * @param newName A string name for the organisational unit name to be changed to
     *
     * @return The newly name changed OrganisationalUnit object
     */
    public OrganisationalUnit editOrganisationalUnitName(OrganisationalUnit unitName, String newName) {
        unitName.editName(newName);
        return unitName;
    }

    /**
     * Edit the name of an asset given the Asset object and the new name for the asset to be changed to.
     *
     * @param assetName An Asset object for the name to be changed
     * @param newName A string name for the asset to be changed to
     *
     * @return The Asset object in which the name has been changed
     */
    public Asset editAssetName(Asset assetName, String newName) {
        assetName.editAssetName(newName);
        return assetName;
    }

    /**
     * Creates a new user [M]
     *
     * @param name string for name of new user
     * @param unitName string organisational unit name for new user to be associated with
     * @param userType user type for new user's access level
     * @return [0]: the newly created user object, [1]: the generated raw password
     * @throws UserTypeException Throws exception when an inputed user type is invalid
     */
    public Object[] createUser(String name, String unitName, String userType) throws UserTypeException {
        // Create password - length 8
        // Hash password
        byte[] saltBytes = Hashing.newRngBytes(Hashing.SALT_SIZE);
        String passwordRaw = newRngText(PWD_SIZE);
        byte[] passwordBytes = Hashing.createHash(saltBytes, passwordRaw);

        // Convert to string
        String salt = Hashing.bytesToString(saltBytes);
        String password = Hashing.bytesToString(passwordBytes);

        // Try get type
        UsersFactory.UserType type;
        try {
            type = UsersFactory.UserType.valueOf(userType);
        }
        catch (IllegalArgumentException e) {
            throw new UserTypeException("Invalid user type");
        }

        return new Object[] {UsersFactory.CreateUser(name, password, salt, unitName, type), passwordRaw};
    }

    /**
     * Edit a user's user type and organisational unit [C]
     * @param user the user to be edited
     * @param userType the new user type the user will be
     * @param unitName the organisational unit that the user will be part of
     * @return The user with the edited details
     * @throws UserTypeException Throws exception when an inputed user type is invalid
     */
    public User editUser(User user, String userType, String unitName) throws UserTypeException {
        // Clear unit name if IT/SysAdmin
        try {
            UsersFactory.UserType type = UsersFactory.UserType.valueOf(userType);
            switch (type) {
                case ITAdmin, SystemsAdmin -> {
                    return UsersFactory.CreateUser(user.getUsername(), user.getPassword(), user.getSalt(), null, type);
                }
                case OrganisationalUnitMembers, OrganisationalUnitLeader -> {
                    return UsersFactory.CreateUser(user.getUsername(), user.getPassword(), user.getSalt(), unitName, type);
                }
                default -> throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new UserTypeException("Invalid user type");
        }
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
