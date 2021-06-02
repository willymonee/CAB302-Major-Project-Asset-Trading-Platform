package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.AssetTrading.*;
import ElectronicAssetTradingPlatform.Database.*;
import ElectronicAssetTradingPlatform.Exceptions.LessThanZeroException;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;
import ElectronicAssetTradingPlatform.Exceptions.UserTypeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The server-side class for creating connections, and interacting with the database
 */
public class NetworkServer {
    /**
     * this is the timeout inbetween accepting clients, not reading from the socket itself.
     */
    private static final int SOCKET_ACCEPT_TIMEOUT = 100;

    /**
     * This timeout is used for the actual clients.
     */
    private static final int SOCKET_READ_TIMEOUT = 5000;

    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * The connection to the database where everything is stored.
     */
    private Connection database;

    /**
     * Port of the server
     */
    private int PORT;

    /**
     * @return The port number of the server
     */
    public int getPort() {return PORT;}


    // Exception codes: https://sqlite.org/rescode.html
    private static final int UNIQUE_CONSTRAINT_EXCEPTION_CODE = 19;

    /**
     * Starts the server running on the default port
     */
    public void start() throws IOException {
        // Connect to the database
        database = DBConnectivity.getInstance();

        HashMap<String, String> file = ReadConfig.readConfigFile();
        PORT = ReadConfig.getPort(file);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(SOCKET_ACCEPT_TIMEOUT);
            // The server is running
            while (running.get()) {
                try {
                    final Socket socket = serverSocket.accept();
                    socket.setSoTimeout(SOCKET_READ_TIMEOUT);

                    // We have a new connection from a client. Use a runnable and thread to handle
                    // the client. The lambda here wraps the functional interface (Runnable).
                    final Thread clientThread = new Thread(() -> handleConnection(socket));
                    clientThread.start();
                } catch (SocketTimeoutException ignored) {
                    // Do nothing. A timeout is normal- we just want the socket to
                    // occasionally timeout so we can check if the server is still running
                } catch (Exception e) {
                    // We will report other exceptions by printing the stack trace, but we
                    // will not shut down the networkExercise.server. A exception can happen due to a
                    // client malfunction (or malicious client)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // If we get an error starting up, show an error dialog then exit
            e.printStackTrace();
            System.exit(1);
        }

        // The server is no longer running

        // Close down the server and db
        try {
            DBConnectivity.getInstance().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Handles the connection received from ServerSocket
     * @param socket The socket used to communicate with the currently connected client
     */
    private void handleConnection(Socket socket) {
        try {

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                try {
                    // Reads Command and parameter object
                    NetworkCommands command = (NetworkCommands) objectInputStream.readObject();
                    handleCommand(command, objectInputStream, objectOutputStream, socket);
                } catch (SocketTimeoutException e) {
                    /*
                     * We catch SocketTimeoutExceptions, because that just means the client hasn't sent
                     * any new requests. We don't want to disconnect them otherwise. Another way to
                     * check if they're "still there would be with ping/pong commands.
                     */
                } catch (SQLException e) {
                    e.printStackTrace();
                    if (e.getErrorCode() == UNIQUE_CONSTRAINT_EXCEPTION_CODE)
                        objectOutputStream.writeObject("It already exists.");
                    else objectOutputStream.writeObject("It could not be found: " + e.getMessage());
                } catch (UserTypeException e) {
                    e.printStackTrace();
                } catch (LessThanZeroException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            System.out.println(String.format("Connection %s closed", socket.toString()));
        }
    }

    /**
     * Execute the command
     * @param command NetworkCommand from input stream
     * @param socket socket for the client
     * @param objectInputStream input stream to read objects from
     * @param objectOutputStream output stream to write objects to
     * @throws IOException if the client has disconnected
     * @throws ClassNotFoundException if the client sends an invalid object
     * @throws UserTypeException if the user is an invalid user type for the specified command
     */
    private void handleCommand (NetworkCommands command, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket)
            throws IOException, ClassNotFoundException, SQLException, UserTypeException, LessThanZeroException {
        /*
         * Remember this is happening on separate threads for each client. Therefore access to the database
         * must be thread-safe in some way. The easiest way to achieve thread safety is to just put a giant
         * lock around all database operations, in this case with a synchronized block on the database object.
         */
        switch (command) {
            // Execute db queries based on command
            case RETRIEVE_USER -> {
                // Get input
                String username = (String) objectInputStream.readObject();

                User out;
                synchronized (database) {
                    out = UsersDataSource.getInstance().getUser(username);

                    // Write output
                    objectOutputStream.writeObject(out);
                    System.out.println("Wrote to socket: " + socket.toString() + out);
                }
                objectOutputStream.flush();
            }
            case STORE_USER -> {
                // Get input
                User user = (User) objectInputStream.readObject();

                synchronized (database) {
                    // Save to db
                    UsersDataSource.getInstance().insertUser(user);

                    // Write success output
                    objectOutputStream.writeObject("Added user.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case EDIT_USER -> {
                // Get input
                User user = (User) objectInputStream.readObject();

                synchronized (database) {
                    String unitName = null;
                    try {
                        unitName = ((OrganisationalUnitMembers) user).getUnitName();
                    } catch (ClassCastException ignored) {}

                    // Save to db
                    UsersDataSource.getInstance().editUser(user.getUsername(), user.getUserType(), unitName);

                    // Write success output
                    objectOutputStream.writeObject("Edited user.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case ADD_BUY_OFFER -> {
                // Get input
                BuyOffer offer =  (BuyOffer) objectInputStream.readObject();
                synchronized (database) {
                    // add offer to the DB
                    MarketplaceDataSource.getInstance().insertBuyOffer(offer);
                    // write success output
                    objectOutputStream.writeObject("Added buy offer: " + offer);
                }
                objectOutputStream.flush();
                System.out.println("Wrote to socket: " + socket.toString());

            }
            case ADD_SELL_OFFER -> {
                SellOffer offer = (SellOffer) objectInputStream.readObject();
                synchronized (database) {
                    // add offer to the DB
                    MarketplaceDataSource.getInstance().insertSellOffer(offer);
                    // write success output
                    objectOutputStream.writeObject("Added buy offer: " + offer);
                }
                objectOutputStream.flush();
                System.out.println("Wrote to socket: " + socket.toString());
            }
            case REMOVE_OFFER -> {
                int ID = (int) objectInputStream.readObject();
                synchronized (database) {
                    // remove offer from DB
                    MarketplaceDataSource.getInstance().removeOffer(ID);
                    objectOutputStream.writeObject("Removed offer");
                }
                objectOutputStream.flush();
                System.out.println("Removed offer on behalf of client");
            }
            case GET_BUY_OFFERS -> {
                synchronized (database) {
                    final TreeMap<Integer, BuyOffer> buyOffers = MarketplaceDataSource.getInstance().getBuyOffers();
                    objectOutputStream.writeObject(buyOffers);
                }
                objectOutputStream.flush();
                System.out.println("Retrieved buy offers and sent to client" + socket.toString());
            }
            case GET_SELL_OFFERS -> {
                synchronized (database) {
                    objectOutputStream.writeObject(MarketplaceDataSource.getInstance().getSellOffers());
                }
                objectOutputStream.flush();
                System.out.println("Retrieved sell offers and sent to client" + socket.toString());
            }
            case GET_PLACED_OFFER -> {
                synchronized (database) {
                    objectOutputStream.writeObject(MarketplaceDataSource.getInstance().getPlacedOfferID());
                }
                objectOutputStream.flush();
                System.out.println("Retrieved placed offer ID and sent to client");
            }
            case UPDATE_OFFER -> {
                int newQuantity = (int) objectInputStream.readObject();
                int ID = (int) objectInputStream.readObject();
                synchronized (database) {
                    MarketplaceDataSource.getInstance().open();
                    MarketplaceDataSource.getInstance().updateOfferQuantity(newQuantity ,ID);
                    objectOutputStream.writeObject("Updated offer quantity");
                }
                objectOutputStream.flush();
                System.out.println("Updated offer quantity on behalf of client");
            }
            case EDIT_PASSWORD -> {
                // Get input
                User user = (User) objectInputStream.readObject();

                synchronized (database) {
                    // Save to db
                    UsersDataSource.getInstance().editUserPassword(user.getUsername(), user.getPassword(), user.getSalt());

                    // Write success output
                    objectOutputStream.writeObject("Password has changed.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case UPDATE_CREDITS -> {
                double credits = (double) objectInputStream.readObject();
                String orgName = (String) objectInputStream.readObject();
                synchronized (database) {
                    UnitDataSource unitDataSource = new UnitDataSource();
                    unitDataSource.updateUnitCredits((float) credits, orgName);
                    objectOutputStream.writeObject("Updated unit credits");
                }
                objectOutputStream.flush();

                System.out.println("Updated unit credits");
            }
            case UPDATE_ASSETS -> {
                int quantity = (int) objectInputStream.readObject();
                String orgName = (String) objectInputStream.readObject();
                String assetName = (String) objectInputStream.readObject();
                synchronized (database) {
                    UnitDataSource unitDataSource = new UnitDataSource();
                    unitDataSource.updateUnitAssets(quantity, orgName, assetName);
                    objectOutputStream.writeObject("Updated org assets");
                }
                objectOutputStream.flush();
                System.out.println("Updated org assets");
            }
            case GET_UNIT_CREDIT -> {
                String unitName = (String) objectInputStream.readObject();
                synchronized (database) {
                    float credits = UsersDataSource.getInstance().getUnitCredits(unitName);
                    objectOutputStream.writeObject(credits);
                }
                objectOutputStream.flush();
            }
            case GET_UNIT_ASSETS -> {
                String unitName = (String) objectInputStream.readObject();
                synchronized (database) {
                    HashMap<String, Integer> credits = UsersDataSource.getInstance().getUnitAssets(unitName);
                    objectOutputStream.writeObject(credits);
                }
                objectOutputStream.flush();
            }
            case ADD_HISTORY -> {
                BuyOffer buyOffer = (BuyOffer) objectInputStream.readObject();
                SellOffer sellOffer = (SellOffer) objectInputStream.readObject();
                int quantity = (int) objectInputStream.readObject();
                synchronized (database) {
                    // add to history
                    MarketplaceHistoryDataSource.getInstance().insertCompletedTrade(buyOffer, sellOffer, quantity);

                    // write output
                    objectOutputStream.writeObject("Added history for asset: " + buyOffer.getAssetName());
                }
                objectOutputStream.flush();
                System.out.println("Wrote to socket: " + socket.toString());
            }
            case STORE_ORG_UNIT -> {
                // Get input
                OrganisationalUnit orgUnit = (OrganisationalUnit) objectInputStream.readObject();

                synchronized (database) {
                    // Save to db
                    UnitDataSource.getInstance().insertOrgUnit(orgUnit);

                    // Write success output
                    objectOutputStream.writeObject("Added organisational unit.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case STORE_ASSET -> {
                // Get input
                Asset asset = (Asset) objectInputStream.readObject();

                synchronized (database) {
                    // Save to db
                    UnitDataSource.getInstance().insertAsset(asset);

                    // Write success output
                    objectOutputStream.writeObject("Added asset.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case GET_ASSET_HISTORY -> {
                String assetName = (String) objectInputStream.readObject();
                synchronized (database) {
                    List<List<Object>> assetPriceHistory = MarketplaceHistoryDataSource.getInstance().getAssetPriceHistory(assetName);
                    objectOutputStream.writeObject(assetPriceHistory);
                }
                objectOutputStream.flush();
            }
            case RETRIEVE_ORG_UNIT -> {
                // Get input
                String unitName = (String) objectInputStream.readObject();

                OrganisationalUnit out;
                synchronized (database) {
                    out = UnitDataSource.getInstance().getOrgUnit(unitName);

                    // Write output
                    objectOutputStream.writeObject(out);
                    System.out.println("Wrote to socket: " + socket.toString() + out);
                }
                objectOutputStream.flush();
            }
            case EDIT_ORG_UNIT_CREDITS -> {
                // Get input
                OrganisationalUnit orgUnit = (OrganisationalUnit) objectInputStream.readObject();

                synchronized (database) {
//                    String unitName = null;
//                    try {
//                        unitName = ((OrganisationalUnitMembers) orgUnit).getUnitName();
//                    } catch (ClassCastException ignored) {}

                    // Save to db
                    UnitDataSource.getInstance().editOrgUnitCredits(orgUnit.getUnitName(), orgUnit.getCredits());

                    // Write success output
                    objectOutputStream.writeObject("Edited Organisational Unit Credits.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case EDIT_ORG_UNIT_ASSETS -> {
                // Get input
                OrganisationalUnit orgUnit = (OrganisationalUnit) objectInputStream.readObject();
                String assetName = (String) objectInputStream.readObject();


                synchronized (database) {
//                    String unitName = null;
//                    try {
//                        unitName = ((OrganisationalUnitMembers) orgUnit).getUnitName();
//                    } catch (ClassCastException ignored) {}

                    // Save to db
                    UnitDataSource.getInstance().editOrgUnitAssets(orgUnit.getUnitName(), assetName, orgUnit.getAssetQuantity(assetName));

                    // Write success output
                    objectOutputStream.writeObject("Edited Organisational Unit Assets.");
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case GET_ALL_ASSETS -> {
                synchronized (database) {
                    ArrayList<String> list = UsersDataSource.getInstance().getAllAssets();

                    // Write output
                    objectOutputStream.writeObject(list);
                    System.out.println("Wrote to socket: " + socket.toString());
                }
                objectOutputStream.flush();
            }
            case GET_ALL_MEMBERS -> {
                String unitName = (String) objectInputStream.readObject();

                synchronized (database) {
                    ArrayList<String[]> list = UsersDataSource.getInstance().getAllMembers(unitName);

                    // Write output
                    objectOutputStream.writeObject(list);
                    System.out.println("Wrote to socket: " + socket.toString());
                }

                objectOutputStream.flush();
            }
        }
    }

    /**
     * Requests the server to shut down
     */
    public void shutdown() {
        // Shut the server down
        running.set(false);
    }

}
