package ElectronicAssetTradingPlatform.Users;

public class UsersFactory {
    // Available user type enum
    public enum UserType {
        ITAdmin, OrganisationalUnitLeader, OrganisationalUnitMembers, SystemsAdmin
    }

    public static User CreateUser(String username, String password, String salt, String unitName, UserType type) {
        return switch (type) {
            case ITAdmin -> new ITAdmin(username, password, salt);
            case OrganisationalUnitLeader -> new OrganisationalUnitLeader(username, password, salt, unitName);
            case OrganisationalUnitMembers -> new OrganisationalUnitMembers(username, password, salt, unitName);
            case SystemsAdmin -> new SystemsAdmin(username, password, salt);
        };
    }
}
