package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Users.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class ITAdminTesting {
    // Exception codes: https://sqlite.org/rescode.html
    private static final int CONSTRAINT_EXCEPTION_CODE = 19;
    /*
    DELETE FROM User_Accounts;
    INSERT INTO Organisational_Units (Name, Credits) VALUES ("unit1", "5");
    INSERT INTO Organisational_Units (Name, Credits) VALUES ("unit2", "6");
     */

    ITAdmin itAdmin;
//    static UsersDataSource db;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        // Recreate db
//        ETPDataSource etp = new ETPDataSource();
//        db = UsersDataSource.getInstance();
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

    }

    // Create new users tests
    @Test
    public void emptyName() {
        assertThrows(User.EmptyFieldException.class, () -> itAdmin.createUser("", "", "ITAdmin"));
        assertThrows(User.EmptyFieldException.class, () -> itAdmin.createUser(" ", "", "ITAdmin"));
        assertThrows(User.EmptyFieldException.class, () -> itAdmin.createUser(null, "", "ITAdmin"));
    }
    @Test
    public void invalidUserType() {
        assertThrows(User.UserTypeException.class, () -> itAdmin.createUser("bob", "", "asd"));
        assertThrows(User.EmptyFieldException.class, () -> itAdmin.createUser("bob", "", ""));
        assertThrows(User.EmptyFieldException.class, () -> itAdmin.createUser("bob", "", null));
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
/*
    // START DB TESTS
    @Test
    public void insertLeader() {
        try {
            User user = itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader");
            db.insertUser(user);

            User dbUser = db.getUser("newLeader");

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("User maybe already exist? - insertLeader()");
            } else {
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
                e.printStackTrace();
                assert false;
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
            User user = itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin");
            db.insertUser(user);

            User dbUser = db.getUser("newSysAdmin1");

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("User maybe already exist? - insertSysAdmin()");
            } else {
                e.printStackTrace();
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
                assert false;
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
            User user = itAdmin.createUser("newITAdmin1", "", "ITAdmin");
            db.insertUser(user);

            User dbUser = db.getUser("newITAdmin1");

            assertEquals(user.getUsername(), dbUser.getUsername());
        }
        catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                System.out.println("User maybe already exist? - insertITAdmin()");
            } else {
                e.printStackTrace();
                System.out.println("Error with ITAdminTesting, will fix later");
                System.out.println("Error is likely due to db not updated with the changes I manually made to the tables. I had to delete the ETP..db file and run the DBTester again to get it right.");
                assert false;
            }
        }
        catch (User.UserTypeException | User.EmptyFieldException e) {
            e.printStackTrace();
            assert false;
        }
    }

    // Edit user tests
    @Test
    public void checkMemberEdit() throws User.UserTypeException, SQLException {
        User check = UsersDataSource.getInstance().getUser("newLeader");
        assertEquals("OrganisationalUnitMembers", check.getUserType());
        assertEquals("unit2", ((OrganisationalUnitMembers)check).getUnitName());
    }
    @Test
    public void checkITAdminEdit() throws SQLException, User.UserTypeException {
        User check = UsersDataSource.getInstance().getUser("newITAdmin1");
        assertEquals("SystemsAdmin", check.getUserType());
    }
    // END DB TESTS
*/
    // Edit user tests
    @Test
    public void editMemberCheckUnitName() {
        try {
            String[] out = itAdmin.editUser("newLeader", "OrganisationalUnitMembers", "unit2");
            assertNotNull(out[2]);
        } catch (User.EmptyFieldException | User.UserTypeException e) {
            e.printStackTrace();
            assert false;
        }

    }
    @Test
    public void editITAdminCheckUnitName() {
        try {
            String[] out = itAdmin.editUser("newITAdmin1", "SystemsAdmin", "unit1");
            assertNull(out[2]);
        } catch (User.EmptyFieldException | User.UserTypeException e) {
            e.printStackTrace();
            assert false;
        }
    }


    // Change password test
    @Test
    public void changePwd() {
        String pwdBefore = itAdmin.getPassword();
        String saltBefore = itAdmin.getSalt();

        // Change
        itAdmin.changePassword("newPassword");

        System.out.println("Salt (changePwd()): " + itAdmin.getSalt());

        // Check is changed
        assertNotEquals(pwdBefore, itAdmin.getPassword());
        assertNotEquals(saltBefore, itAdmin.getSalt());
    }
    @Test
    public void correctPwd() {
        // Change
        itAdmin.changePassword("newPassword");
        assertTrue(Hashing.compareHashPass(itAdmin.getSalt(), "newPassword", itAdmin.getPassword()));
    }
    @Test
    public void incorrectPwd() {
        // Change
        itAdmin.changePassword("newPassword");
        assertFalse(Hashing.compareHashPass(itAdmin.getSalt(), "newPassword1", itAdmin.getPassword()));
    }

//    @AfterAll
//    public static void dbClose() throws SQLException {
//        db.close();
//    }
}
