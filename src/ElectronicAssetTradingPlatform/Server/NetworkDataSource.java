package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Exceptions.DatabaseException;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * The client-side class for accessing the server, to access the database through the server
 */
public class NetworkDataSource extends Thread {

    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    /**
     * Initialise connection
     */
    @Override
    public void run() {
        HashMap<String, String> file = ReadConfig.readConfigFile();

        try {
            String HOSTNAME = ReadConfig.getHostname(file);
            int PORT = ReadConfig.getPort(file);
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
     * Query the database through the server with 1 param
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
     * If an error string was sent instead, throw the error
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
        return (String) sendCommand(NetworkCommands.EDIT_PASSWORD, thisUser);
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
        try {
            return (TreeMap<Integer, BuyOffer>) out;
        } catch (ClassCastException e) {
            e.printStackTrace();
            TreeMap<Integer, BuyOffer> empty = new TreeMap<>();
            return empty;
        }
    }

    /**
     * Sends command to server to retrieve all sell offers from the database
     */
    public TreeMap<Integer, SellOffer> getSellOffers()  {
        Object out = sendCommand(NetworkCommands.GET_SELL_OFFERS);
        try {
            return (TreeMap<Integer, SellOffer>) out;
        } catch (ClassCastException e) {
            e.printStackTrace();
            TreeMap<Integer, SellOffer> empty = new TreeMap<>();
            return empty;
        }
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
     * @param credits to increase or decrease by
     * @param orgName to edit credits for
     */
    public String editCredits(double credits, String orgName) {
        return (String) sendCommand(NetworkCommands.UPDATE_CREDITS, credits,orgName);
    }

    /**
     * Sends command to update the assets of an organisational unit
     * @param quantity to increase/decrease by
     * @param orgName whose asset's quantity will be affectd
     * @param assetName to increase the quantity of
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

    /**
     * Sends command to add price history for
     * a successfully traded asset
     */
    public String addAssetHistory(BuyOffer buyOffer, SellOffer sellOffer, int quantity) {
        return (String) sendCommand(NetworkCommands.ADD_HISTORY, buyOffer, sellOffer, quantity);
    }

    /**
     * Send a command through the network to store the organisational unit in the database
     *
     * @param orgUnit The OrganisationalUnit object to store in the database
     *
     * @return String
     */
    public String storeOrgUnit(OrganisationalUnit orgUnit) {
        return (String) sendCommand(NetworkCommands.STORE_ORG_UNIT, orgUnit);
    }

    /**
     * Store the given asset by sending a command through the network to keep the data in the database
     *
     * @param asset The Asset object to store in the database
     *
     * @return String
     */
    public String storeAsset(Asset asset) {
        return (String) sendCommand(NetworkCommands.STORE_ASSET, asset);
    }

    /**
     * Sends command to get the buy/sell trade history
     * given an asset name from the Marketplace_history table in the database
     * Stores the date the traded was completed and price the asset
     * was traded at into an List<List<>>
     */
    public List<List<Object>> getAssetHistory(String assetName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.GET_ASSET_HISTORY, assetName);
        try {
            return (List<List<Object>>) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Send a command through the network to retrieve a queried organisational unit in the database given the unit name
     *
     * @param unitName A string of the unit name to be retrieved
     *
     * @return The organisational unit queried
     *
     * @throws DatabaseException Catch any database exceptions that may occur
     */
    public OrganisationalUnit retrieveOrgUnit(String unitName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.RETRIEVE_ORG_UNIT, unitName);

        try {
            return (OrganisationalUnit) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Set the a particular organisational unit's credits to a particular value
     * Different from editUnitCredits as this method sets the credit value, rather than increasing or decreasing
     * the existing credit value
     *
     * @param orgUnit whose credits will be set
     * @param newCredits value to be set to
     *
     */
    public String setOrgUnitCredits(OrganisationalUnit orgUnit, float newCredits) {
        return (String) sendCommand(NetworkCommands.SET_ORG_UNIT_CREDITS, orgUnit, newCredits);
    }

    /**
     * Set the a particular organisational unit's assets to a particular value
     * Different from editUnitAssets as this method sets the asset value, rather than increasing or decreasing
     * the existing asset value
     *
     * @param orgUnit whose assetss will be set
     * @param assetName to set
     * @param newQuantity to be set to
     *
     */
    public String setOrgUnitAssets(OrganisationalUnit orgUnit, String assetName, int newQuantity) {
        return (String) sendCommand(NetworkCommands.SET_ORG_UNIT_ASSETS, orgUnit, assetName, newQuantity);
    }

    /**
     * Gets all the assets in the database
     * @return List of asset names
     * @throws DatabaseException throws any exceptions occurred while the server was handling the request
     */
    public ArrayList<String> retrieveAllAssets() throws DatabaseException {
        Object out = sendCommand(NetworkCommands.GET_ALL_ASSETS);

        try {
            return (ArrayList<String>) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Gets all the members of a given unit in the database
     * @param unitName The input String unit name
     * @return List of String arrays [...][0]: is the username, [...][1]: is the user type
     * @throws DatabaseException throws any exceptions occurred while the server was handling the request
     */
    public ArrayList<String[]> retrieveAllMembers(String unitName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.GET_ALL_MEMBERS, unitName);

        try {
            return (ArrayList<String[]>) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Edit the organisational unit name by sending a command through the network to the database given the
     * OrganisationalUnit object and the unit's old name
     *
     * @param orgUnit The OrganisationalUnit object in which the name has been changed
     * @param oldUnitName A string of the unit's old name
     *
     * @return String
     */
    public String editOrgUnitName(OrganisationalUnit orgUnit, String oldUnitName) {
        return (String) sendCommand(NetworkCommands.EDIT_ORG_UNIT_NAME, orgUnit, oldUnitName);
    }

    /**
     * Retrieve the asset from the database given an Asset object
     *
     * @param assetName The string name of the asset to retrieve
     *
     * @return The queried Asset object
     *
     * @throws DatabaseException Catch any database exceptions that may occur
     */
    public Asset retrieveAsset(String assetName) throws DatabaseException {
        Object out = sendCommand(NetworkCommands.RETRIEVE_ASSET, assetName);

        try {
            return (Asset) out;
        }
        catch (ClassCastException e) {
            throw new DatabaseException((String) out);
        }
    }

    /**
     * Sends command through to network to edit the asset name in the database
     *
     * @param asset The Asset object in which the name has already been changed
     * @param oldAssetName A string of the asset's previous name
     *
     * @return String
     */
    public String editAssetName(Asset asset, String oldAssetName) {
        return (String) sendCommand(NetworkCommands.EDIT_ASSET_NAME, asset, oldAssetName);
    }

    /**
     * Sends command for the network to fetch the trade history
     * Given a Unit ID to query for (from the Marketplace_history database table)
     */
    public TreeMap<Integer, TradeHistory> getUnitTradeHistory(String unitName) throws LessThanZeroException {
        Object out = sendCommand(NetworkCommands.GET_UNIT_TRADEHISTORY, unitName);
        return (TreeMap<Integer, TradeHistory>) out;
    }
}
