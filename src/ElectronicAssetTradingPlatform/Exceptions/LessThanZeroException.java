package ElectronicAssetTradingPlatform.Exceptions;

/**
 * An exception class that throws an exception when a certain value cannot be less than zero
 */
public class LessThanZeroException extends Exception {
    public LessThanZeroException(String errorMessage) {
        super(errorMessage);
    }
}
