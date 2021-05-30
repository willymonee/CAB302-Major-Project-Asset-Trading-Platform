package ElectronicAssetTradingPlatform.AssetTrading;

import java.util.Date;

public class TradeHistory {
    int tradeID;
    int buyerID;
    int sellerID;
    int assetID;
    float price;
    int quantity;
    float total;
    Date dateFulfilled;

    /**
     * Constructor for trade history
     * @param tradeID           ID for the trade completed
     * @param buyerID           ID of the buyer given a particular asset
     * @param sellerID          ID of the seller given a particular asset
     * @param assetID           ID of the asset traded
     * @param price             Price the asset was sold/bought at
     * @param quantity          Quantity of the asset traded
     * @param dateFulfilled     Date the trade was completed
     */
    public TradeHistory(int tradeID, int buyerID, int sellerID, int assetID, float price, int quantity, Date dateFulfilled) {
        this.tradeID = tradeID;
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.assetID = assetID;
        this.price = price;
        this.quantity = quantity;
        this.dateFulfilled = dateFulfilled;
        total = quantity * price;
        this.quantity = quantity;
        this.price = price;
        float total = quantity * price;
    }

    public int getTradeID() {
        return tradeID;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public int getAssetID() {
        return assetID;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotal() {
        return total;
    }

    public Date getDateFulfilled() {
        return dateFulfilled;
    }

    // methods to get BuyerUsername, SellerUsername, AssetName
    // method to get row data regarding the org unit GUI table
}
