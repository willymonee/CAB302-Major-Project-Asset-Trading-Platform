package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when an inputed user type is invalid
 */
public class UserTypeException extends Exception {
    public UserTypeException(String message) {
        super(message);
    }
}
