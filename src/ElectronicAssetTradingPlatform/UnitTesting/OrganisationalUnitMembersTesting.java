package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.AssetTrading.OrganisationalUnit;
import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Testing for the organisational unit member functionality
 */
public class OrganisationalUnitMembersTesting {

    /*
    INSERT INTO Organisational_Units (Name, Credits) VALUES ('unit1', 5), ('unit2', 6);
    INSERT INTO Asset_Types (name) VALUES ('Asset1'), ('Asset2'), ('Asset3');
    INSERT INTO Organisational_Unit_Assets (Unit_ID, Asset_ID, Asset_Quantity) VALUES (1, 1, 5), (2, 2, 3), (1, 3, 6);
     */

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
        try {
            // Pre-test inserts
            dbUnit.insertOrgUnit(new OrganisationalUnit("unit1", 5));
            dbUnit.insertOrgUnit(new OrganisationalUnit("unit2", 6));
        } catch (SQLException ignore) {
        }
    }

    // Test get unit credits
    @Test
    public void getCredit() throws SQLException {
        float res = db.getUnitCredits("unit1");
        assertEquals(5.0, res);
    }

    // Test get unit assets
    @Test
    public void getAssets() throws SQLException {
        try {
            dbUnit.insertAsset(new Asset("Asset1"));
            dbUnit.insertAsset(new Asset("Asset2"));
            dbUnit.insertAsset(new Asset("Asset3"));
            dbUnit.editOrgUnitAssets("unit1", "Asset1", 5);
            dbUnit.editOrgUnitAssets("unit2", "Asset2", 7);
            dbUnit.editOrgUnitAssets("unit1", "Asset3", 6);
        } catch (SQLException | LessThanZeroException ignore) {
        }
        HashMap<String, Integer> res = db.getUnitAssets("unit1");
        HashMap<String, Integer> test = new HashMap<>();
        test.put("Asset1", 5);
        test.put("Asset3", 6);
        assertEquals(test, res);
    }
}
