package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when any mandatory input is empty
 */
public class EmptyFieldException extends Exception {
    public EmptyFieldException(String message) {
        super(message);
    }
}
