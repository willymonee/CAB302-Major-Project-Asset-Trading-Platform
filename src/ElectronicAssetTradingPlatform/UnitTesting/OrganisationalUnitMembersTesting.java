package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.HashMap;

public class OrganisationalUnitMembersTesting {

    /*
    INSERT INTO Organisational_Units (Name, Credits) VALUES ('unit1', 5), ('unit2', 6);
    INSERT INTO Asset_Types (name) VALUES ('Asset1'), ('Asset2'), ('Asset3');
    INSERT INTO Organisational_Unit_Assets (Unit_ID, Asset_ID, Asset_Quantity) VALUES (1, 1, 5), (2, 2, 3), (1, 3, 6);
     */

    OrganisationalUnitMembers member;
    static UsersDataSource db;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        // Recreate db
        ETPDataSource etp = new ETPDataSource();
        db = UsersDataSource.getInstance();
        // create an organisational unit member
        try {
            member = new OrganisationalUnitMembers("member1", "pass123", "salt", "unit1");
            db.insertUser(member);
        } catch (SQLException ignore) {
        }
    }

    // Test get unit credits
    @Test
    public void getCredit() throws SQLException {
        float res = member.getUnitCredits();
        assertEquals(5.0, res);
    }

    // Test get unit assets
    @Test
    public void getAssets() throws SQLException {
        HashMap<String, Integer> res = member.getUnitAssets();
        HashMap<String, Integer> test = new HashMap<>();
        test.put("Asset1", 5);
        test.put("Asset3", 6);
        assertEquals(test, res);
    }

    @AfterAll
    public static void close() throws SQLException {
        db.close();
    }
}
