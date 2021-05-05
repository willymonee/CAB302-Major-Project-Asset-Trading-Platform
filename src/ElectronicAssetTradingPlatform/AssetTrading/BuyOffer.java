package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.MarketBuyOffers;

import java.util.Date;

public class BuyOffer extends Offer{
    private int orderID;
    private Date dateResolved;


    /**
     * Constructor for trade offer
     *
     * @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public BuyOffer(String asset, int quantity, float pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
        this.orderID = createUniqueID();
    }


    public int createUniqueID() {
        if (MarketBuyOffers.MarketBuyOffers.size() == 0) {
            return 1;
        }
        return MarketBuyOffers.MarketBuyOffers.lastKey() + 1;
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
        long millis = System.currentTimeMillis();
        this.dateResolved = new Date(millis);
    }
}
