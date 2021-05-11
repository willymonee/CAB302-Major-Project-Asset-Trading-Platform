package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitLeader;
import ElectronicAssetTradingPlatform.Users.SystemsAdmin;
import ElectronicAssetTradingPlatform.Users.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class ITAdminTesting {
    // Exception codes: https://sqlite.org/rescode.html
    private static final int CONSTRAINT_EXCEPTION_CODE = 19;

    ITAdmin itAdmin;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        ETPDataSource etp = new ETPDataSource();
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

    }

    // Create new users tests
    @Test
    public void invalidName() {
        assertThrows(Exception.class, () -> itAdmin.createUser("", "", "ITAdmin"));
        assertThrows(Exception.class, () -> itAdmin.createUser(" ", "", "ITAdmin"));
        assertThrows(Exception.class, () -> itAdmin.createUser(null, "", "ITAdmin"));
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

        assertEquals(itAdmin.createUser("newSysAdmin2", "asdf", "ITAdmin").getClass(), ITAdmin.class);
    }
    @Test
    public void validOrgLeader() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader"));

        assertEquals(itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader").getClass(), OrganisationalUnitLeader.class);
    }
    @Test
    public void validOrgMember() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newMember", "unit1", "OrganisationalUnitMembers"));

        assertEquals(itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader").getClass(), OrganisationalUnitLeader.class);
    }
    @Test
    public void validSystemsAdmin() throws Exception {
        assertDoesNotThrow(() -> {
            itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin");
            itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin");
        });

        assertEquals(itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin").getClass(), SystemsAdmin.class);
    }
    @Test
    public void notDuplicatePassword() throws Exception {
        ITAdmin itAdmin1 = new ITAdmin("adminGuy1", "pass123", "salt");
        ITAdmin itAdmin2 = new ITAdmin("adminGuy2", "pass123", "salt");

        String user1 = itAdmin1.createUser("bob1", "", "ITAdmin").getPassword();
        String user2 = itAdmin2.createUser("bob2", "", "ITAdmin").getPassword();
        String user3 = itAdmin2.createUser("bob3", "", "ITAdmin").getPassword();
        String user4 = itAdmin1.createUser("bob4", "", "ITAdmin").getPassword();

        assertNotEquals(user1, user2);
        assertNotEquals(user2, user3);
        assertNotEquals(user1, user4);
    }

    // Edit user tests
    @Test
    public void editUser() throws Exception {
        itAdmin.editUser("user1", "OrganisationalUnitLeader", "Unit1");
    }

    // Users Data Source test
    @Test
    public void insertLeader() {
        try {
            UsersDataSource db = new UsersDataSource();
            User user = itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader");
            db.insertUser(user);

            User dbUser = db.getUser("newLeader");

            System.out.println(user.getUsername());
            System.out.println(dbUser.getUsername());

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("Incorrect user inputs: maybe they already exist?");
            } else {
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
                e.printStackTrace();
            }
        }
        catch (User.UserTypeException | User.EmptyFieldException e) {
            e.printStackTrace();
            assert false;
        }
    }
    @Test
    public void insertSysAdmin() {
        try {
            UsersDataSource db = new UsersDataSource();
            User user = itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin");
            db.insertUser(user);

            User dbUser = db.getUser("newSysAdmin1");

            System.out.println(user.getUsername());
            System.out.println(dbUser.getUsername());

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("Incorrect user inputs: maybe they already exist?");
            } else {
                e.printStackTrace();
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
            }
        }
        catch (User.UserTypeException | User.EmptyFieldException e) {
            e.printStackTrace();
            assert false;
        }
    }
    @Test
    public void insertITAdmin() {
        try {
            UsersDataSource db = new UsersDataSource();
            User user = itAdmin.createUser("newITAdmin1", "", "ITAdmin");
            db.insertUser(user);

            User dbUser = db.getUser("newITAdmin1");

            System.out.println(user.getUsername());
            System.out.println(dbUser.getUsername());

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("Incorrect user inputs: maybe they already exist?");
            } else {
                e.printStackTrace();
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
            }
        }
        catch (User.UserTypeException | User.EmptyFieldException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
