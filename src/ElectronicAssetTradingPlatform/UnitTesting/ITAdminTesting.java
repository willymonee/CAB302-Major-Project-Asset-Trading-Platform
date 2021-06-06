package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Exceptions.MissingAssetException;
import ElectronicAssetTradingPlatform.Exceptions.UserTypeException;
import ElectronicAssetTradingPlatform.Passwords.Hashing;
import ElectronicAssetTradingPlatform.Users.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

/**
 * Testing for the ITAdmin user functionality
 */
public class ITAdminTesting {
    // Exception codes: https://sqlite.org/rescode.html
    private static final int CONSTRAINT_EXCEPTION_CODE = 19;
    /*
    DELETE FROM User_Accounts;
    INSERT INTO Organisational_Units (Name, Credits) VALUES ("unit1", "5");
    INSERT INTO Organisational_Units (Name, Credits) VALUES ("unit2", "6");
     */

    ITAdmin itAdmin;
    OrganisationalUnit orgUnit;
    OrganisationalUnit uneditedOrgUnit;
    Asset asset;
    Asset uneditedAsset;
    static UsersDataSource db;
    static UnitDataSource dbUnit;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        // Recreate db
        ETPDataSource etp = new ETPDataSource();
        db = UsersDataSource.getInstance();
        dbUnit = UnitDataSource.getInstance();
        // create an organisational unit member
        itAdmin = new ITAdmin("adminGuy", "pass123", "salt");

        // Pre-test inserts
        try {
            dbUnit.insertOrgUnit(new OrganisationalUnit("unit1", 5));
            dbUnit.insertOrgUnit(new OrganisationalUnit("unit2", 6));
        } catch (SQLException ignore) {
        }
    }

    @BeforeEach
    @Test
    public void setUpOrgUnit() {
        uneditedOrgUnit = new OrganisationalUnit("orgUnit01", 100);
        orgUnit = new OrganisationalUnit("orgUnit01", 100);

        orgUnit.addAsset("asset01", 10);
    }

    @BeforeEach
    @Test
    public void setupAsset() {
        uneditedAsset = new Asset("asset01");
        asset = new Asset("asset01");
    }

    // Create new users tests
    @Test
    public void invalidUserType() {
        assertThrows(UserTypeException.class, () -> itAdmin.createUser("bob", "", "asd"));
    }
    @Test
    public void validITAdmin() throws Exception {
        assertDoesNotThrow(() -> {
            itAdmin.createUser("newITAdmin1", "", "ITAdmin");
            itAdmin.createUser("newSysAdmin2", "asdf", "ITAdmin");
        });

        assertEquals("ITAdmin", ((User)itAdmin.createUser("newSysAdmin2", "asdf", "ITAdmin")[0]).getUserType());
    }
    @Test
    public void validOrgLeader() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader"));

        assertEquals("OrganisationalUnitLeader", ((User)itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader")[0]).getUserType());
    }
    @Test
    public void validOrgMember() throws Exception {
        assertDoesNotThrow(() -> itAdmin.createUser("newMember", "unit1", "OrganisationalUnitMembers"));

        assertEquals("OrganisationalUnitLeader", ((User)itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader")[0]).getUserType());
    }
    @Test
    public void validSystemsAdmin() throws Exception {
        assertDoesNotThrow(() -> {
            itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin");
            itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin");
        });

        assertEquals("SystemsAdmin", ((User)itAdmin.createUser("newSysAdmin2", "asdf", "SystemsAdmin")[0]).getUserType());
    }
    @Test
    public void notDuplicatePassword() throws Exception {
        ITAdmin itAdmin1 = new ITAdmin("adminGuy1", "pass123", "salt");
        ITAdmin itAdmin2 = new ITAdmin("adminGuy2", "pass123", "salt");

        String user1 = ((User)itAdmin1.createUser("bob1", "", "ITAdmin")[0]).getPassword();
        String user2 = ((User)itAdmin2.createUser("bob2", "", "ITAdmin")[0]).getPassword();
        String user3 = ((User)itAdmin2.createUser("bob3", "", "ITAdmin")[0]).getPassword();
        String user4 = ((User)itAdmin1.createUser("bob4", "", "ITAdmin")[0]).getPassword();

        assertNotEquals(user1, user2);
        assertNotEquals(user2, user3);
        assertNotEquals(user1, user4);
    }

    // START DB TESTS
    @Test
    public void insertLeader() {
        assertDoesNotThrow(() -> {
            try {
                User user = (User) itAdmin.createUser("newLeader", "unit1", "OrganisationalUnitLeader")[0];
                db.insertUser(user);

                User dbUser = db.getUser("newLeader");

                assertEquals(user.getUsername(), dbUser.getUsername());

                db.editUser("newLeader","OrganisationalUnitMembers","unit1");
                User check = UsersDataSource.getInstance().getUser("newLeader");
                assertEquals("OrganisationalUnitMembers", check.getUserType());
                assertEquals("unit2", ((OrganisationalUnitMembers) check).getUnitName());
            } catch (SQLException e) {
                if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                    System.out.println("User maybe already exist? - insertLeader()");
                } else {
                    throw e;
                }
            }
        });
    }
    @Test
    public void insertSysAdmin() {
        assertDoesNotThrow(() -> {
            try {
                User user = (User) itAdmin.createUser("newSysAdmin1", "", "SystemsAdmin")[0];
                db.insertUser(user);

                User dbUser = db.getUser("newSysAdmin1");

                assertEquals(user.getUsername(), dbUser.getUsername());
            } catch (SQLException e) {
                if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                    System.out.println("User maybe already exist? - insertSysAdmin()");
                } else {
                    throw e;
                }
            }
        });
    }
    @Test
    public void insertITAdmin() {
        assertDoesNotThrow(() -> {
            try {
                User user = (User) itAdmin.createUser("newITAdmin1", "", "ITAdmin")[0];
                db.insertUser(user);

                User dbUser = db.getUser("newITAdmin1");

                assertEquals(user.getUsername(), dbUser.getUsername());

                UsersDataSource.getInstance().editUser("newITAdmin1", "SystemsAdmin", "");
                User check = UsersDataSource.getInstance().getUser("newITAdmin1");
                assertEquals("SystemsAdmin", check.getUserType());
            } catch (SQLException e) {
                if (e.getErrorCode() == CONSTRAINT_EXCEPTION_CODE) {
                    System.out.println("User maybe already exist? - insertITAdmin()");
                } else {
                    throw e;
                }
            }
        });
    }
    // END DB TESTS

    // Edit user tests
    @Test
    public void editMemberCheckUnitName() {
        assertDoesNotThrow(() -> {
            ITAdmin user = new ITAdmin("newLeader", "pass", "salt");
            OrganisationalUnitMembers out = (OrganisationalUnitMembers)itAdmin.editUser(user, "OrganisationalUnitMembers", "unit2");
            assertEquals("unit2", out.getUnitName());
        });

    }
    @Test
    public void editITAdminCheckUnitName() {
        assertDoesNotThrow(() -> {
            ITAdmin user = new ITAdmin("newITAdmin1", "pass", "salt");
            SystemsAdmin out = (SystemsAdmin) itAdmin.editUser(user, "SystemsAdmin", "unit1");
        });
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

    @Test
    public void createValidOrgUnit() {
        assertDoesNotThrow(() -> itAdmin.createOrganisationalUnit("newUnit01", 15));

        assertEquals(itAdmin.createOrganisationalUnit("newUnit01", 15).getClass(), OrganisationalUnit.class);
    }

    @Test
    public void createValidAsset() {
        assertDoesNotThrow(() -> itAdmin.createNewAsset("newAsset01"));

        assertEquals(itAdmin.createNewAsset("newAsset01").getClass(), Asset.class);
    }

    @Test
    public void addValidOrgUnitCredits() {
        itAdmin.addOrganisationalUnitCredits(orgUnit, 20);

        assertNotEquals(uneditedOrgUnit.getCredits(), orgUnit.getCredits());
    }

    @Test
    public void removeValidOrgUnitCredits() throws LessThanZeroException {
        itAdmin.removeOrganisationalUnitCredits(orgUnit, 20);

        assertNotEquals(uneditedOrgUnit.getCredits(), orgUnit.getCredits());
    }

    @Test
    public void removeMoreThanOwnedCredits() {
        assertThrows(LessThanZeroException.class, () -> itAdmin.removeOrganisationalUnitCredits(orgUnit, 120));
    }

    @Test
    public void addValidOrgUnitAssets() {
        itAdmin.addOrganisationalUnitAssets(orgUnit, "asset02", 5);

        assertNotEquals(uneditedOrgUnit.getAssetsOwned(), orgUnit.getAssetsOwned());
    }

    @Test
    public void removeValidOrgUnitAssets() throws MissingAssetException, LessThanZeroException {
        itAdmin.removeOrganisationalUnitAssets(orgUnit, "asset01", 5);

        assertNotEquals(uneditedOrgUnit.getAssetsOwned(), orgUnit.getAssetsOwned());
    }

    @Test
    public void removeMoreThanOwnedAssets(){
        assertThrows(LessThanZeroException.class, () -> itAdmin.removeOrganisationalUnitAssets(orgUnit, "asset01", 15));
    }

    @Test
    public void removeMissingAssets() {
        assertThrows(MissingAssetException.class, () -> itAdmin.removeOrganisationalUnitAssets(orgUnit, "asset02", 5));
    }

    @Test
    public void validEditOrgUnitName() {
        itAdmin.editOrganisationalUnitName(orgUnit, "newOrgUnitName");

        assertNotEquals(uneditedOrgUnit.getUnitName(), orgUnit.getUnitName());
    }

    @Test
    public void validEditAssetName() {
        itAdmin.editAssetName(asset, "newAssetName");

        assertNotEquals(uneditedAsset.getAssetName(), asset.getAssetName());
    }
}
