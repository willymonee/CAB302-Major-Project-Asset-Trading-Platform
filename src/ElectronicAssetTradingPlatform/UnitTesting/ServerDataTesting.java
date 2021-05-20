package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class ServerDataTesting {

    @Test
    public void testGetUser() throws SQLException, NetworkDataSource.DatabaseException {
        OrganisationalUnitMembers user = (OrganisationalUnitMembers) NetworkDataSource.retrieveUser("willymon");

        System.out.println("Gotten: " + user.getUsername() + user.getPassword() + user.getUserType() + user.getUnitCredits());
    }

    @Test
    public void testStoreUser() throws NetworkDataSource.DatabaseException {
        ITAdmin userStore = new ITAdmin("name", "pass", "salt");
        System.out.println(NetworkDataSource.storeUser(userStore));

        ITAdmin user = (ITAdmin) NetworkDataSource.retrieveUser("name");
        assertEquals("name", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("ITAdmin", user.getUserType());
    }

    @Test
    public void testInvalidGetUser() {
        assertThrows(NetworkDataSource.DatabaseException.class, () -> NetworkDataSource.retrieveUser("a"));
    }
}
