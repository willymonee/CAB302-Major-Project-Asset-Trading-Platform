package ElectronicAssetTradingPlatform.AssetTrading;


import ElectronicAssetTradingPlatform.Database.MarketSellOffers;

import java.util.Date;

public class SellOffer extends Offer{
    
    private int orderID;
    private Date datePlaced;

    /**
     * Constructor for trade offer
     *
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public SellOffer(String asset, int quantity, float pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
    }

    public int createUniqueID() {
        if (MarketSellOffers.MarketSellOffers.size() == 0) {
            return 1;
        }
        return MarketSellOffers.MarketSellOffers.lastKey() + 1;
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
        long millis = System.currentTimeMillis();
        datePlaced = new Date(millis);
    }
}
