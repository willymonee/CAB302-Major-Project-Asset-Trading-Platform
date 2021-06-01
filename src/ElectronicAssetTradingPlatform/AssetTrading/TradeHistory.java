package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;

public class TradeHistory {
    String assetName;
    int tradedQuantity;
    float price;
    java.sql.Date dateFulfilled;
    String creatorUsername;
    OrganisationalUnit unitOfTrader;


    /**
     * Constructor for tracking trade history of a org unit
     * @param assetName             Name of the asset traded
     * @param tradedQuantity        Number of asset traded
     * @param price                 Price of the asset traded
     * @param dateFulfilled         Date the trade was completed
     * @param creatorUsername       Name of the buy/sell listing creator
     * @param unitOfTrader          Unit the asset was bought from or sold to
     */

    // TODO: THIS IS IN FORMAT OF THE TABLE'S REQUIRED INFORMATION,
    //  Can change OrganisationalUnit unitOfTrader to String, and dateFulfilled
    //  will either be SQL Date or Java UTIL Date,
    //  althought structure however u want to, GL!

    public TradeHistory(String assetName, int tradedQuantity, float price, java.sql.Date dateFulfilled, String creatorUsername, OrganisationalUnit unitOfTrader ) {
        this.assetName = assetName;
        this.tradedQuantity = tradedQuantity;
        this.price = price;
        this.dateFulfilled = dateFulfilled;
        this.creatorUsername = creatorUsername;
        this.unitOfTrader = unitOfTrader;

    }

    public String getAssetName() {
        return assetName;
    }

    public int getTradedQuantity() {
        return tradedQuantity;
    }

    public float getPrice() {
        return price;
    }

    public java.sql.Date getDateFulfilled() {
        return dateFulfilled;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public OrganisationalUnit getunitOfTrader() {
        return unitOfTrader;
    }
}
