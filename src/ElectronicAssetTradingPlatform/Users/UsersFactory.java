package ElectronicAssetTradingPlatform.Users;

/**
 * Class to create new users of a specific type
 */
public class UsersFactory {
    /**
     * Available user types
     */
    public enum UserType {
        ITAdmin, OrganisationalUnitLeader, OrganisationalUnitMembers, SystemsAdmin
    }

    /**
     * Creates a new user from the inputs
     * @param username Username of the user
     * @param password Hashed password of the user
     * @param salt Salt used in hasing the password
     * @param unitName Name of the organisational unit the user belongs to
     * @param type User type to be created
     * @return A new user of a specific type
     */
    public static User CreateUser(String username, String password, String salt, String unitName, UserType type) {
        return switch (type) {
            case ITAdmin -> new ITAdmin(username, password, salt);
            case OrganisationalUnitLeader -> new OrganisationalUnitLeader(username, password, salt, unitName);
            case OrganisationalUnitMembers -> new OrganisationalUnitMembers(username, password, salt, unitName);
            case SystemsAdmin -> new SystemsAdmin(username, password, salt);
        };
    }
}
