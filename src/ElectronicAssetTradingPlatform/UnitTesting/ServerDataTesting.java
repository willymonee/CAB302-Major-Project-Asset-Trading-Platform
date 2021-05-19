package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class ServerDataTesting {

    @Test
    public void testGetUser() throws SQLException {
        OrganisationalUnitMembers user = (OrganisationalUnitMembers) NetworkDataSource.retrieveUser("willymon");

        System.out.println("Gotten: " + user.getUsername() + user.getPassword() + user.getUserType() + user.getUnitCredits());
    }

    @Test
    public void testStoreUser() {
        ITAdmin userStore = new ITAdmin("name", "pass", "salt");
        System.out.println(NetworkDataSource.storeUser(userStore));

        ITAdmin user = (ITAdmin) NetworkDataSource.retrieveUser("name");
        System.out.println("Stored: " + user.getUsername() + user.getPassword() + user.getUserType());
    }
}
