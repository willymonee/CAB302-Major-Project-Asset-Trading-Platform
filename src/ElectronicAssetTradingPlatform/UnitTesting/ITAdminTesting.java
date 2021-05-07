package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Users.ITAdmin;

import static org.junit.jupiter.api.Assertions.*;

import ElectronicAssetTradingPlatform.Users.OrganisationalUnitLeader;
import ElectronicAssetTradingPlatform.Users.SystemsAdmin;
import org.junit.jupiter.api.*;

public class ITAdminTesting {
    ITAdmin itAdmin;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123");

    }

    // Create new users tests
    @Test
    public void invalidName() {
        assertThrows(Exception.class, () -> {
            itAdmin.createUser("", "", "ITAdmin");
            itAdmin.createUser(" ", "", "ITAdmin");
            itAdmin.createUser(null, "", "ITAdmin");
        });
    }
    @Test
    public void invalidUnitName() {
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", "", "OrganisationalUnitLeader"));
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", " ", "OrganisationalUnitLeader"));
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", null, "OrganisationalUnitLeader"));
    }
    @Test
    public void invalidUserType() {
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", "", "asd"));
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", "", ""));
        assertThrows(Exception.class, () -> itAdmin.createUser("bob", "", null));
    }
    @Test
    public void validITAdmin() throws Exception {
        assertDoesNotThrow(() -> {
            itAdmin.createUser("newITAdmin1", "", "ITAdmin");
            itAdmin.createUser("newSysAdmin2", "asdf", "ITAdmin");
        });

        assertEquals(itAdmin.createUser("newSysAdmin2", "asdf", "ITAdmin")[0].getClass(), ITAdmin.class);
    }
    @Test
    public void validOrgLeader() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader"));

        assertEquals(itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader")[0].getClass(), OrganisationalUnitLeader.class);
    }
    @Test
    public void validOrgMember() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newMember", "unit1", "OrganisationalUnitMember"));

        assertEquals(itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader")[0].getClass(), OrganisationalUnitLeader.class);
    }
    @Test
    public void validSystemsAdmin() throws Exception {
        assertDoesNotThrow(() -> {
            itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin");
            itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin");
        });

        assertEquals(itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin")[0].getClass(), SystemsAdmin.class);
    }
    @Test
    public void notDuplicatePassword() throws Exception {
        ITAdmin itAdmin1 = new ITAdmin("adminGuy1", "pass123");
        ITAdmin itAdmin2 = new ITAdmin("adminGuy2", "pass123");


        // For now, use return password, but may need to change to something more secure


        String user1 = (String)itAdmin1.createUser("bob1", "", "ITAdmin")[1];
        String user2 = (String)itAdmin2.createUser("bob2", "", "ITAdmin")[1];
        String user3 = (String)itAdmin2.createUser("bob3", "", "ITAdmin")[1];
        String user4 = (String)itAdmin1.createUser("bob4", "", "ITAdmin")[1];

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);

        assertNotEquals(user1, user2);
        assertNotEquals(user2, user3);
        assertNotEquals(user1, user4);
    }
}
