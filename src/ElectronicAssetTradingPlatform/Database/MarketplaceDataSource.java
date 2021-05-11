package ElectronicAssetTradingPlatform.Database;

import ElectronicAssetTradingPlatform.AssetTrading.Asset;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Database.UnitDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketplaceDataSource extends UnitDataSource{
    private static final String INSERT_BUYOFFER = "INSERT INTO Marketplace (Trade_ID, Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_Unit)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String INSERT_SELLOFFER = "INSERT INTO Marketplace (Trade_ID, Buy_or_Sell, "
            + "Unit_ID, User_ID, Asset_type_ID, Price_per_Unit)"
            + "VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_BUYOFFERS = "SELECT * FROM Marketplace WHERE Buy_or_Sell=b?";
    private static final String GET_SELLOFFERS = "SELECT * FROM Marketplace WHERE Buy_or_Sell=s?";
    private static final String RESOLVE_OFFER = "DELETE FROM Marketplace WHERE Trade_ID=?";

    private PreparedStatement insertBuyOffer;
    private PreparedStatement insertSellOffer;
    private PreparedStatement getBuyOffers;
    private PreparedStatement getSellOffers;
    private PreparedStatement resolveOffer;

    private Connection connection;

    public MarketplaceDataSource() {
        try {
            connection = DBConnectivity.getInstance();
            insertBuyOffer = connection.prepareStatement(INSERT_BUYOFFER);
            insertSellOffer = connection.prepareStatement(INSERT_SELLOFFER);
            getBuyOffers = connection.prepareStatement(GET_BUYOFFERS);
            getSellOffers = connection.prepareStatement(GET_SELLOFFERS);
            resolveOffer = connection.prepareStatement(RESOLVE_OFFER);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void insertBuyOffer(User user, Asset asset, String assetPrice) {
        try {
            insertBuyOffer.setString(2, "b");
            // Get Unit ID
            if (user.getClass() == OrganisationalUnitMembers.class) {
                String id = executeGetUnitID(((OrganisationalUnitMembers) user).getUnitName());
                insertBuyOffer.setString(3, id);
            }
            //insertBuyOffer.setString(4, user); // user id
            insertBuyOffer.setString(5, asset.getAssetName());
            insertBuyOffer.setString(6, assetPrice);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}