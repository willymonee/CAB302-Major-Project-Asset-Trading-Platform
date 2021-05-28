package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.AssetTrading.SellOffer;
import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeMap;

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
     * Query the database through the server - No param
     */
    private Object sendCommand(NetworkCommands command) {
        try {
            // Write the command type and param
            outputStream.writeObject(command);

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
     * Query the database through the server - 2 parameters
     */
    private Object sendCommand(NetworkCommands command, Object param, Object param2) {
        try {
            // Write the command type and param
            outputStream.writeObject(command);
            outputStream.writeObject(param);
            outputStream.writeObject(param2);

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
     * Query the database through the server - 3 parameters
     */
    private Object sendCommand(NetworkCommands command, Object param, Object param2, Object param3) {
        try {
            // Write the command type and param
            outputStream.writeObject(command);
            outputStream.writeObject(param);
            outputStream.writeObject(param2);
            outputStream.writeObject(param3);

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
    public String editUser(User user) { return (String) sendCommand(NetworkCommands.EDIT_USER, user); }

    /**
     * Sends command for server to change user password
     * Returns error message
     */
    public String editPassword(User thisUser) {
        String str = (String) sendCommand(NetworkCommands.EDIT_PASSWORD, thisUser);
        return str;
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

    /**
     * Sends command to server to retrieve all buy offers from the database
     */
    public TreeMap<Integer, BuyOffer> getBuyOffers() {
        Object out = sendCommand(NetworkCommands.GET_BUY_OFFERS);
        return (TreeMap<Integer, BuyOffer>) out;
    }

    /**
     * Sends command to server to retrieve all sell offers from the database
     */
    public TreeMap<Integer, SellOffer> getSellOffers() {
        Object out = sendCommand(NetworkCommands.GET_SELL_OFFERS);
        return (TreeMap<Integer, SellOffer>) out;
    }

    /** Sends command to server to retrieve the ID of the most recently placed offer from the DB
     *
     */
    public int getPlacedOffer() {
        Object out = sendCommand(NetworkCommands.GET_PLACED_OFFER);
        return (int) out;
    }

    /**
     * Sends command to update the quantity of a selected offer in the DB
     */
    public String updateOfferQuantity(int newQuantity, int offerID) {
        Object out = sendCommand(NetworkCommands.UPDATE_OFFER, newQuantity, offerID);
        return (String) out;
    }

    /**
     * Sends command to update the credits an organisation unit has
     */
    public String editCredits(double credits, String orgName) {
        return (String) sendCommand(NetworkCommands.UPDATE_CREDITS, credits,orgName);
    }

    /**
     * Sends command to update the assets of an organisational unit
     */
    public String editAssets(int quantity, String orgName, String assetName) {
        return (String) sendCommand(NetworkCommands.UPDATE_ASSETS, quantity, orgName, assetName );
    }

    /**
     * Sends command to get the credits of an organisational unit
     * Returns unit credits
     *  If an error string was sent instead, throw the error
     */
    public float getCredits(String unitName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.GET_UNIT_CREDIT, unitName);

        try {
            return (float) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Sends command to get the assets of an organisational unit
     * Returns unit assets
     *  If an error string was sent instead, throw the error
     */
    public HashMap<String, Integer> getAssets(String unitName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.GET_UNIT_ASSETS, unitName);

        try {
            return (HashMap<String, Integer>) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }
}
