package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.Users.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class NetworkDataSource extends Thread {

    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    /**
     * Initialise connection
     */
    @Override
    public void run() {
        final String HOSTNAME = "127.0.0.1";
        final int PORT = 10000;

        try {
            socket = new Socket(HOSTNAME, PORT);

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("hi");
            System.out.println(outputStream);
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("hi2");
            System.out.println(inputStream);
        } catch (IOException e) {
            // If the networkExercise.server connection fails, we're going to throw exceptions
            // whenever the application actually tries to query anything.
            // But it wasn't written to handle this, so make sure your
            // networkExercise.server is running beforehand!
            e.printStackTrace();
            System.out.println("Failed to connect to networkExercise.server");
            System.out.println(socket.toString());
        }
    }

    /**
     * Query the database through the server
     */
    private Object sendCommand(NetworkCommands command, Object param) {
        try {
            // Write the command type and param
            outputStream.writeObject(command);
            outputStream.writeObject(param);

            // Separate output and input streams
            outputStream.flush();

            // Get output
            // Read the object
            return inputStream.readObject();
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
    public User retrieveUser(String username) throws DatabaseException {
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
    public String storeUser(User user) {
        return (String) sendCommand(NetworkCommands.STORE_USER, user);
    }

    public class DatabaseException extends Exception {
        public DatabaseException(String message) { super(message); }
    }
}
