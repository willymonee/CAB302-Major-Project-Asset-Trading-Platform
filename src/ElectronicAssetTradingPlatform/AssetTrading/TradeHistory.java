package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketplaceHistoryDataSource;

import java.io.Serializable;

public class TradeHistory implements Serializable {
    String buyOrSell;
    String assetName;
    int tradedQuantity;
    float price;
    float total;
    String dateFulfilled;
    String creatorUsername;
    String unitNameOfTrader;


    /**
     * Constructor for tracking trade history of a org unit
     * @param buyOrSell             Distinguishes whether the Unit gained or lost an asset (via the trade)
     * @param assetName             Name of the asset traded
     * @param tradedQuantity        Number of asset traded
     * @param price                 Price of the asset traded
     * @param total                 Total price of credits traded throughout this trade
     * @param dateFulfilled         Date the trade was completed
     * @param unitNameOfTrader      Unit the asset was bought from or sold to
     */



    public TradeHistory(String buyOrSell, String assetName, int tradedQuantity, float price, float total, String dateFulfilled, String unitNameOfTrader ) {
        this.buyOrSell = buyOrSell;
        this.assetName = assetName;
        this.tradedQuantity = tradedQuantity;
        this.price = price;
        this.total = total;
        this.dateFulfilled = dateFulfilled;
        this.unitNameOfTrader = unitNameOfTrader;

    }

    public String getBuyOrSell() { return buyOrSell; }

    public String getAssetName() {
        return assetName;
    }

    public int getTradedQuantity() {
        return tradedQuantity;
    }

    public float getPrice() {
        return price;
    }

    public float getTotal() { return total; }

    public String getDateFulfilled() {
        return dateFulfilled;
    }

    public String getunitNameOfTrader() {
        return unitNameOfTrader;
    }
}
