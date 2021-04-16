package ElectronicAssetTradingPlatform;

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

    public void isSold() {

    }

    public void isBought() {

    }
}
