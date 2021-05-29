package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception class for throwing an exception when trying to retrieve the name of asset that does not currently exist in
 * a java collection
 */
public class MissingAssetException extends Exception {
    public MissingAssetException(String errorMessage) {
        super(errorMessage);
    }
}
