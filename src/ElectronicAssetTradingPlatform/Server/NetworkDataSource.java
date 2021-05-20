package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.Users.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class NetworkDataSource {
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    /**
     * Query the database through the server
     */
    private static Object sendCommand(NetworkCommands command, Object param) {
        try {
            // Initialise
            Socket socket = new Socket(HOSTNAME, PORT);

            try (ObjectOutputStream objectOutputStream =
                            new ObjectOutputStream(socket.getOutputStream())) {
                // Write the command type and param
                objectOutputStream.writeObject(command);
                objectOutputStream.writeObject(param);

                // Separate output and input streams
                objectOutputStream.flush();

                // Get output
                try (ObjectInputStream objectInputStream =
                                new ObjectInputStream(socket.getInputStream())) {
                    // Read the object
                    return objectInputStream.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Print the exception, but no need for a fatal error
            // if the connection with the server happens to be down
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends command for server to get user
     * Returns queried user
     *  If an error string was sent instead, throw the error
     */
    public static User retrieveUser(String username) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.RETRIEVE_USER, username);

        try {
            return (User) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Sends command for server to store user
     * Returns error message
     */
    public static String storeUser(User user) {
        return (String) sendCommand(NetworkCommands.STORE_USER, user);
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(String message) { super(message); }
    }
}
