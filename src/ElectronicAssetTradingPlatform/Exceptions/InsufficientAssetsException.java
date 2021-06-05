package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when trying to make a sell offer without enough assets
 */
public class InsufficientAssetsException extends Exception{
    public InsufficientAssetsException(String message) {
        super(message);
    }
}
