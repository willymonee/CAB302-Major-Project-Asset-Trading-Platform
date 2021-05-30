package ElectronicAssetTradingPlatform.Exceptions;

/**
 * Exception for when an error occurs while using the database, or when an exception occurs while interacting with the server
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
}
