package ElectronicAssetTradingPlatform.Database;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitDataSource {
    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID = ?";
    private static final String GET_UNIT_ID = "SELECT Unit_ID FROM Organisational_Units WHERE Name = ?";
    private static final String GET_USER_ID = "SELECT USER_ID FROM User_Accounts WHERE Username = ?";

    private static final String EDIT_UNIT_CREDITS = "UPDATE Organisational_Units SET Credits = ? WHERE name = ?";
    private static final String EDIT_UNIT_ASSETS = "UPDATE Organisational_Unit_Assets SET Asset_Quantity = ? WHERE Unit_ID = ?, Asset_ID = ?";
    private static final String GET_ASSET_ID = "SELECT Type_ID FROM Asset_Types WHERE Name = ?";
    private static final String EDIT_UNIT_NAME = "UPDATE Organisational_Units SET Name = ? WHERE Name = ?";
    private static final String EDIT_ASSET_NAME = "UPDATE Asset_Types SET Name = ? WHERE Name = ?";

    PreparedStatement getUnitNameQuery;
    PreparedStatement getUnitIDQuery;
    PreparedStatement getUserIDQuery;
    PreparedStatement editUnitCreditsQuery;
    PreparedStatement editUnitAssetsQuery;
    PreparedStatement getAssetIDQuery;
    PreparedStatement editUnitNameQuery;
    PreparedStatement editAssetNameQuery;

    private Connection connection;

    public UnitDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME);
            getUnitIDQuery = connection.prepareStatement(GET_UNIT_ID);

            editUnitCreditsQuery = connection.prepareStatement(EDIT_UNIT_CREDITS);
            editUnitAssetsQuery = connection.prepareStatement(EDIT_UNIT_ASSETS);
            getAssetIDQuery = connection.prepareStatement(GET_ASSET_ID);
            editUnitNameQuery = connection.prepareStatement(EDIT_UNIT_NAME);
            editAssetNameQuery = connection.prepareStatement(EDIT_ASSET_NAME);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public String executeGetUnitName(String unitID) throws SQLException {
        // Prepare
        getUnitNameQuery.setString(1, unitID);

        // Result
        ResultSet rs = null;
        String unitName;
        try {
            rs = getUnitNameQuery.executeQuery();
            unitName = rs.getString("Name");
        } finally {
            if (rs != null) rs.close();
        }

        // Return
        return unitName;
    }

    public String executeGetUnitID(String unitName) throws SQLException {
        // Prepare
        getUnitIDQuery.setString(1, unitName);

        // Result
        ResultSet rs = null;
        String unitID;
        try {
            rs = getUnitIDQuery.executeQuery();
            unitID = rs.getString("Unit_ID");
        } finally {
            if (rs != null) rs.close();
        }

        // Return
        return unitID;
    }

    public String executeGetUserID(String username) throws SQLException{
        getUserIDQuery.setString(2, username);
        ResultSet rs = null;
        String userID = null;
        try {
            rs = getUserIDQuery.executeQuery();
            rs.getString("User_ID");
        } finally {
            if (rs != null) rs.close();
        }

        return userID;
    }

    public String executeGetAssetID(String assetName) throws SQLException {

        getAssetIDQuery.setString(1, assetName);

        ResultSet rs = null;
        String assetID;
        try {
            rs = getAssetIDQuery.executeQuery();
            assetID = rs.getString("Asset_ID");
        } finally {
            if (rs != null) rs.close();
        }

        return assetID;
    }

    public void editUnitCredits(String unitName, float credits) throws SQLException {
        // edit org unit credits
        editUnitCreditsQuery.setFloat(1, credits); // sets the unit credits to this param
        editUnitCreditsQuery.setString(2, unitName); // sets credits to above statement if unit name matches

        editUnitCreditsQuery.execute();
    }

    public void editUnitAssets(String unitName, String assetName, int quantity) throws SQLException {
        // edit unit assets
        String unitID = executeGetUnitID(unitName); // get associated unit id from param name
        editUnitAssetsQuery.setString(2, unitID);

        String assetID = executeGetAssetID(assetName); // get associated asset id from param name
        editUnitAssetsQuery.setString(3, assetID);

        editUnitAssetsQuery.setInt(1, quantity); // set new quantity of assets for unit

        editUnitAssetsQuery.execute();

    }

    public void editUnitName(String currentName, String newName) throws SQLException {
        // edit unit name
        editUnitNameQuery.setString(1, newName); // set new name in DB
        editUnitNameQuery.setString(2, currentName); // look for old name of unit to change

        editUnitNameQuery.execute();
    }

    public void editAssetName(String currentName, String newName) throws SQLException {
        // edit asset name
        editAssetNameQuery.setString(1, newName); // set new name in DB
        editAssetNameQuery.setString(2, currentName); // look for old name of asset to change

        editAssetNameQuery.execute();

    }
}
