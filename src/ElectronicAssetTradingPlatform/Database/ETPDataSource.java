package ElectronicAssetTradingPlatform.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class ETPDataSource {
    // DB Instance
    private Connection connection;
    // Create Table Queries
    private static final String marketplaceTable =
            "CREATE TABLE IF NOT EXISTS Marketplace ("
                    + "Offer_ID INTEGER UNIQUE,"
                    + "Buy_or_Sell TEXT CHECK (buy_or_sell IN ('b', 's')),"
                    + "Unit_ID INTEGER NOT NULL,"
                    + "User_ID INTEGER NOT NULL,"
                    + "Asset_type_ID NOT NULL,"
                    + "Price_per_unit FLOAT NOT NULL,"
                    + "PRIMARY KEY(Offer_ID AUTOINCREMENT)"
                    + ");";

    private static final String marketplaceHistoryTable =
            "CREATE TABLE IF NOT EXISTS Marketplace_history ("
                    + "Trade_ID INTEGER UNIQUE,"
                    + "Buyer_ID INTEGER NOT NULL,"
                    + "Seller_ID INTEGER NOT NULL,"
                    + "Asset_type_ID INTEGER NOT NULL,"
                    + "Price_per_unit FLOAT NOT NULL,"
                    + "Quantity INTEGER NOT NULL,"
                    + "Date_fulfilled TEXT NOT NULL,"
                    + "PRIMARY KEY(Trade_ID AUTOINCREMENT)"
                    + ");";

    private static final String userAccountsTable =
            "CREATE TABLE IF NOT EXISTS User_Accounts ("
                + "User_ID INTEGER UNIQUE,"
                + "Username VARCHAR(60) UNIQUE NOT NULL,"
                + "Password_hash VARCHAR(100) NOT NULL,"
                + "Salt VARCHAR(60) NOT NULL,"
                + "User_Type VARCHAR(60) NOT NULL,"
                + "Unit_ID INTEGER,"
                + "PRIMARY KEY(User_ID AUTOINCREMENT)"
                + ");";

    private static final String assetTypesTable =
            "CREATE TABLE IF NOT EXISTS Asset_Types ("
                + "Type_ID INTEGER UNIQUE,"
                + "Name VARCHAR(80) UNIQUE NOT NULL,"
                + "PRIMARY KEY(Type_ID AUTOINCREMENT)"
                + ");";

    private static final String orgUnitsTable =
            "CREATE TABLE IF NOT EXISTS Organisational_Units ("
                + "Unit_ID INTEGER UNIQUE,"
                + "Name VARCHAR(80) UNIQUE NOT NULL,"
                + "Credits FLOAT,"
                + "PRIMARY KEY(Unit_ID AUTOINCREMENT)"
                + ");";

    private static final String orgUnitAssetsTable =
            "CREATE TABLE IF NOT EXISTS Organisational_Unit_Assets ("
                + "Unit_ID INTEGER NOT NULL,"
                + "Asset_ID INTEGER NOT NULL,"
                + "Asset_Quantity INTEGER NOT NULL"
                + ");";


    /**
     * Constructor to build the Database Schema
     */
    public ETPDataSource() {
        connection = DBConnectivity.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(marketplaceTable);
            st.execute(marketplaceHistoryTable);
            st.execute(userAccountsTable);
            st.execute(assetTypesTable);
            st.execute(orgUnitsTable);
            st.execute(orgUnitAssetsTable);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
