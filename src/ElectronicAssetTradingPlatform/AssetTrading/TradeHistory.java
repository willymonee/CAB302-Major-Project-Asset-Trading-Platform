package ElectronicAssetTradingPlatform.AssetTrading;


import java.io.Serializable;

/**
 * Class for creating an TradeHistory object which is used to fetch history of any
 * trades completed via the MarketplaceHistoryDataSource in which this interacts with the
 * Marketplace_history database tables directly.
 */

public class TradeHistory implements Serializable {
    String buyOrSell;
    String assetName;
    int tradedQuantity;
    float price;
    float total;
    String dateFulfilled;
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

    /**
     * Get whether the trade was a Buy (+) or Sell (-)
     * @return    a String, + if The Org Unit bought an asset,
     *                      - if the Org Unit sold an asset
     */
    public String getBuyOrSell() { return buyOrSell; }

    /**
     * Get the Name of the asset that was traded
     * @return  String of the asset name
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Get the amount of quantity of the specific asset that was traded
     * @return  Int value of the quantity
     */
    public int getTradedQuantity() {
        return tradedQuantity;
    }

    /**
     * Get the price the asset was traded at
     * @return  Float, price the asset was traded at between both parties
     */
    public float getPrice() {
        return price;
    }

    /**
     * Get the total amount of credits the trade was settled at
     * @return  a float which amounts to the quantity traded * price asset was sold at
     */
    public float getTotal() { return total; }

    /**
     * Gets the date the trade was completed/ fulfilled
     * @return  A string of a date in: yyyy-MM-dd format
     */
    public String getDateFulfilled() {
        return dateFulfilled;
    }

    /**
     * Returns the Unit name of the trade party
     * @return  String of the trade party's unit name
     */
    public String getunitNameOfTrader() {
        return unitNameOfTrader;
    }
}
