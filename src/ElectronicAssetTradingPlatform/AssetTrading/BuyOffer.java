package ElectronicAssetTradingPlatform.AssetTrading;

import ElectronicAssetTradingPlatform.Database.BuyOffersDB;
import ElectronicAssetTradingPlatform.Database.SellOffersDB;

import java.util.*;

import java.sql.Date;

public class BuyOffer extends Offer{
    private int orderID;
    private Date dateResolved;


    /**
     * Constructor for trade offer
     *  @param asset                  Name of the asset to be bought or sold
     * @param quantity               Quantity of asset
     * @param pricePerUnit           Price of the asset
     * @param username               The ID of the user who made the offer
     * @param organisationalUnitName The ID of the organisation whose assets and credits will be affected
     */
    public BuyOffer(String asset, int quantity, double pricePerUnit, String username, String organisationalUnitName) {
        super(asset, quantity, pricePerUnit, username, organisationalUnitName);
        this.orderID = createUniqueID();
    }



    @Override
    public int getOfferID() {
        return this.orderID;

    }

    @Override
    public String toString() {
        return this.orderID + "\t" + getAssetName() + "\t" + getQuantity()+ "\t $"
                + getPricePerUnit() + "\t" + getUsername() + "\t" + getUnitName() + "\t" + getDatePlaced();
    }



    @Override
    protected int createUniqueID() {
        if (BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().size() == 0) {
            return 1;
        }
        return BuyOffersDB.getBuyOffersDB().getMarketBuyOffers().lastKey() + 1;
    }

    @Override
    public void resolveOffer() {
        // Implement resolve offer, use checkMatchedOffer() also?
        long millis = System.currentTimeMillis();
        this.dateResolved = new Date(millis);
    }

    // function to return sell offers with matching asset name
    private ArrayList<SellOffer> matchingSellOffers() {
        ArrayList<SellOffer> matchingSellOffers = new ArrayList<>();

        TreeMap<Integer, SellOffer> sellOfferMap = SellOffersDB.getSellOffersDB().getMarketSellOffers();
        Iterator<Map.Entry<Integer, SellOffer>> entries = sellOfferMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, SellOffer> entry = entries.next();
            if(entry.getValue().getAssetName() == this.getAssetName()) {
                SellOffer matchingOffer = entry.getValue();
                System.out.println(entry.getValue().getAssetName());
                matchingSellOffers.add(matchingOffer);
            }
        }
        return matchingSellOffers;
    }

    @Override
    public int checkMatchedOffer() {
        ArrayList<SellOffer> matchingSellOffers = matchingSellOffers();
        double buyOfferPrice = getPricePerUnit();
        // assign the first sell offer as the lowest price


        // convert map into entry set to iterate over
        Iterator<SellOffer> iter = matchingSellOffers.iterator();
        SellOffer lowestSellOffer;
        double lowestSellPrice;
        if (iter.hasNext()) {
            lowestSellOffer = iter.next();
            lowestSellPrice = lowestSellOffer.getPricePerUnit();
            while(iter.hasNext()) {
                SellOffer newOffer = iter.next();
                if (newOffer.getPricePerUnit() < lowestSellPrice) {
                    lowestSellPrice = newOffer.getPricePerUnit();
                    lowestSellOffer = newOffer;
                }

                else if ((newOffer.getPricePerUnit() == lowestSellPrice) &&
                        (newOffer.getDatePlaced().compareTo(lowestSellOffer.getDatePlaced()) > 0)) {
                    lowestSellPrice = newOffer.getPricePerUnit();
                    lowestSellOffer = newOffer;
                }
            }
            if (lowestSellPrice <= buyOfferPrice) {
                return 1;
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }




    }


}
