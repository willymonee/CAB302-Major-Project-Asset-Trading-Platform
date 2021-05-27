package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when an error occurs while using the database
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
}
