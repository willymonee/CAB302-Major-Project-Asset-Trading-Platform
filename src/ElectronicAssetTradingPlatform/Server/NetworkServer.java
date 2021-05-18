package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.Database.UsersDataSource;
import ElectronicAssetTradingPlatform.Users.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServer {
    private static final int PORT = 10000;
    private static final int SOCKET_TIMEOUT = 100;
    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Returns the port the server is configured to use
     *
     * @return The port number
     */
    public static int getPort() {
        return PORT;
    }

    /**
     * Starts the server running on the default port
     */
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(SOCKET_TIMEOUT);
            // The server is no longer running
            while (running.get()) {
                try {
                    Socket socket = serverSocket.accept();
                    handleConnection(socket);
                } catch (SocketTimeoutException ignored) {
                    // Do nothing. A timeout is normal- we just want the socket to
                    // occasionally timeout so we can check if the server is still running
                } catch (IOException | ClassNotFoundException e) {
                    // We will report an IOException by printing the stack trace, but we
                    // will not shut down the server. An IOException can happen due to a
                    // client malfunction (or malicious client)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // If we get an error starting up, show an error dialog then exit
            e.printStackTrace();
            System.exit(1);
        }

        // Close down the server
        System.exit(0);
    }

    /**
     * Handles the connection received from ServerSocket
     * @param socket The socket used to communicate with the currently connected client
     */
    private void handleConnection(Socket socket) throws IOException, ClassNotFoundException {
        try (
                ObjectInputStream objectInputStream =
                        new ObjectInputStream(socket.getInputStream())
        ) {
            String command = (String) objectInputStream.readObject();
            switch (command) {
                case NetworkDataSource.RETRIEVE -> {
                    try (
                            ObjectOutputStream objectOutputStream =
                                    new ObjectOutputStream(socket.getOutputStream())
                    ) {
                        objectOutputStream.writeObject(UsersDataSource.getInstance().getUser("willymon"));
                    } catch (SQLException | User.UserTypeException e) {
                        e.printStackTrace();
                    }
                }
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
