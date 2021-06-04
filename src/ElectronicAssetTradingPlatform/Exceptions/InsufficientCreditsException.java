package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when trying to make a buy offer with insufficient credits
 */
public class InsufficientCreditsException extends Exception {
    public InsufficientCreditsException(String message) {
        super(message);
    }
}
