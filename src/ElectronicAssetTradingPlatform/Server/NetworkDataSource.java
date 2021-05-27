package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.User;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

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
            try {
                socket = new Socket(HOSTNAME, PORT);
            } catch (ConnectException e) {
                // Alert user of connection failure
                JOptionPane.showMessageDialog(null, "Connection failed");
            }

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            // If the networkExercise.server connection fails, we're going to throw exceptions
            // whenever the application actually tries to query anything.
            // But it wasn't written to handle this, so make sure your
            // networkExercise.server is running beforehand!
            e.printStackTrace();
            System.out.println("Failed to connect to server");
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

    /**
     * Sends command for server to edit user
     * Returns error message
     */
    public String editUser(String username, String userType, String unitName) {
        String[] object = new String[]{username, userType, unitName};
        return (String) sendCommand(NetworkCommands.EDIT_USER, object);
    }

    /**
     * Sends command for the server to add a new buy offer
     */
    public String addBuyOffer(BuyOffer buyOffer) {
        return (String) sendCommand(NetworkCommands.ADD_BUY_OFFER, buyOffer);
    }

    /**
     * Sends command for the server to add a new buy offer
     */
    public String addSellOffer(SellOffer sellOffer)
    {
        return (String) sendCommand(NetworkCommands.ADD_SELL_OFFER, sellOffer);
    }

    /**
     * Sends command to server to remove a buy offer
     */
    public String removeOffer(int ID) {
        return (String) sendCommand(NetworkCommands.REMOVE_OFFER, ID);
    }

    public class DatabaseException extends Exception {
        public DatabaseException(String message) { super(message); }
    }
}
