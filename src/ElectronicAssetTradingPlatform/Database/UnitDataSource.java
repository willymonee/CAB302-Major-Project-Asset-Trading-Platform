package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helper Class for any DataSource class that requires to fetch the organisational unit name and/or ID.
 */
public class UnitDataSource {
    private static final String GET_UNIT_NAME = "SELECT Name FROM Organisational_Units WHERE Unit_ID =?";
    private static final String GET_UNIT_ID = "SELECT Unit_ID FROM Organisational_Units WHERE Name =?";
    private static final String GET_USER_ID = "SELECT USER_ID FROM User_Accounts WHERE Username =?";
    private static final String GET_USERNAME = "SELECT Username FROM User_Accounts WHERE User_ID =?";
    // delete this statement later just here for testing
    private static final String GET_ASSET_NAME = "SELECT Name FROM Asset_Types WHERE Type_ID =?";
    private static final String GET_ASSET_ID = "SELECT Type_ID FROM Asset_Types WHERE Name=?";
    private static final String UPDATE_CREDITS = "UPDATE Organisational_Units SET Credits= Credits + ? WHERE Name=?";
    private static final String UPDATE_ASSETS = "UPDATE Organisational_Unit_Assets SET Asset_Quantity= Asset_Quantity + ? WHERE Unit_ID=? AND Asset_ID=?";


    PreparedStatement getUnitNameQuery;
    PreparedStatement getUnitIDQuery;
    PreparedStatement getUserIDQuery;
    PreparedStatement getUserNameQuery;
    //delete this statement later
    PreparedStatement getAssetNameQuery;
    PreparedStatement getAssetIDQuery;
    PreparedStatement updateUnitCredits;
    PreparedStatement updateUnitAssets;

    private Connection connection;

    public UnitDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            // ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY to allow resultSets to be used multiple times
            getUnitNameQuery = connection.prepareStatement(GET_UNIT_NAME, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            getUnitIDQuery = connection.prepareStatement(GET_UNIT_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            getUserIDQuery = connection.prepareStatement(GET_USER_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            getUserNameQuery = connection.prepareStatement(GET_USERNAME, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            getAssetNameQuery = connection.prepareStatement(GET_ASSET_NAME, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            getAssetIDQuery = connection.prepareStatement(GET_ASSET_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            updateUnitCredits = connection.prepareStatement(UPDATE_CREDITS, ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            updateUnitAssets = connection.prepareStatement(UPDATE_ASSETS, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String executeGetUnitName(int unitID) throws SQLException {
        // Prepare
        getUnitNameQuery.setInt(1, unitID);

        // Result
        ResultSet rs = null;
        String unitName;
        try {
            rs = getUnitNameQuery.executeQuery();
            rs.next();
            unitName = rs.getString("Name");
        } finally {
            if (rs != null) rs.close();
        }

        // Return
        return unitName;
    }

    public void updateUnitCredits(float credits, String orgName) {
        try {
            updateUnitCredits.setFloat(1, credits);
            updateUnitCredits.setString(2, orgName);
            updateUnitCredits.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateUnitCredits(float credits, int ID) {
        try {
            updateUnitCredits.setFloat(1, credits);
            updateUnitCredits.setInt(2, ID);
            updateUnitCredits.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateUnitAssets(int quantity, String unitName, String assetName ) {
        try {
            updateUnitAssets.setInt(1, quantity);
            int unitID = Integer.parseInt(executeGetUnitID(unitName));
            updateUnitAssets.setInt(2, unitID);
            int assetID = executeGetAssetID(assetName);
            updateUnitAssets.setInt(3, assetID);
            updateUnitAssets.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateUnitAssets(int quantity, int unitID,int assetID ) {
        try {
            updateUnitAssets.setInt(1, quantity);
            updateUnitAssets.setInt(2, unitID);
            updateUnitAssets.setInt(3, assetID);
            updateUnitAssets.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String executeGetUnitID(String unitName) throws SQLException {
        // Prepare
        getUnitIDQuery.setString(1, unitName);
        // Result
        ResultSet rs = null;
        String unitID;
        try {
            rs = getUnitIDQuery.executeQuery();
            if (rs.isClosed()) throw new SQLException("Unit not found: " + unitName);
            rs.next();
            unitID = rs.getString("Unit_ID");
        }
        finally {
            if (rs != null) rs.close();
        }

        // Return
        return unitID;
    }

    public String executeGetUserID(String username) throws SQLException{
        getUserIDQuery.setString(1, username);
        ResultSet rs = null;
        String userID;
        try {
            rs = getUserIDQuery.executeQuery();
            rs.next();
            userID = rs.getString("User_ID");
        } finally {
            if (rs != null) rs.close();
        }
        return userID;
    }

    // get user's username from their ID
    public String executeGetUsername(int userID) throws SQLException{

        ResultSet rs = null;
        String username;
        try {
            getUserNameQuery.setInt(1, userID);
            rs = getUserNameQuery.executeQuery();
            rs.next();
            username = rs.getString("Username");
        } finally {
            if (rs != null) rs.close();
        }
        return username;
    }

    // get asset name from asset ID
    public String executeGetAssetName(int assetID) throws SQLException {

        String assetName;
        ResultSet rs = null;
        try {
            getAssetNameQuery.setInt(1, assetID);
            rs = getAssetNameQuery.executeQuery();
            rs.next();
            assetName = rs.getString("Name");
        } finally {
            if (rs != null) rs.close();
        }
        return assetName;
    }

    // get asset name from asset ID
    public int executeGetAssetID(String assetName) throws SQLException {
        int assetID;
        getAssetIDQuery.setString(1, assetName);
        try (ResultSet rs = getAssetIDQuery.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                throw new SQLException("Asset: " + assetName + " is not in the system");
            }
            rs.next();
            assetID = rs.getInt("Type_ID");
        }
        return assetID;
    }


}