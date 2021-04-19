package ElectronicAssetTradingPlatform.Backend;

import ElectronicAssetTradingPlatform.Backend.Offer;

/**
 * An extension of the offer class, only available to the server to resolve offers with
 */
public class ResolveOffer {
    /**
     * Compare the newly created offer with existing buy and sell orders and
     * resolve matching ones e.g. if the order is a sell order check existing buy orders,
     * if the order is a buy order check existing sell orders).
     *
     * @param offer The offer to be checked with the system
     * @return Returns the matching order ID if there is a match, returns 0 otherwise.
     */
    public int checkMatchedOffer(Offer offer) {
        return 0;
    }

    /**
     * Deduct appropriate amount of credits from the organisational unit based on the pricePerUnit
     * and the quantity bought, as well as add the correct amount of assets bought
     *
     * @param offer the offer in which needs credits to be deducted and assets to be added
     */
    public void buyOfferResolve(Offer offer) {
        // buyOfferResolve method
    }

    /**
     * Add the appropriate amount of credits to the organisational unit based on the pricePerUnit
     * and quantity sold, as well as remove the correct amount of assets sold
     *
     * @param offer the offer in which needs credits to be added and assets to be deducted
     */
    public void sellOfferResolve(Offer offer) {
        // sellOfferResolve
    }
}
