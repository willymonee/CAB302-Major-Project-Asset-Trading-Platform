package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class OrganisationalUnitMembersTesting {

    /*
    DELETE FROM User_Accounts;
    INSERT INTO Organisational_Units (Name, Credits) VALUES ("unit1", "5");
     */

    OrganisationalUnitMembers member;
    UsersDataSource db;

    @BeforeEach
    @Test
    public void setUpITAdmin() {
        // Recreate db
        ETPDataSource etp = new ETPDataSource();
        try {
            db = new UsersDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // create an organisational unit member
        try {
            member = new OrganisationalUnitMembers("member1", "pass123", "salt", "unit1");
            db.insertUser(member);
        } catch (SQLException ignore) {
        }
    }

    // Test get unit credits
    @Test
    public void getYourUnitCredit() throws SQLException {
        float res = member.getUnitCredits();
        assertEquals(5.0, res);
    }

    @AfterClass
    public void close() throws SQLException {
        db.close();
    }
}
