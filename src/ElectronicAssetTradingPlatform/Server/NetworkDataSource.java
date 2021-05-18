package ElectronicAssetTradingPlatform.Server;

import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;
import ElectronicAssetTradingPlatform.Users.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class NetworkDataSource {
    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 10000;

    public static final String RETRIEVE = "Retrieve";

    private User user;

    /**
     * Retrieves the contents of the addressbook data file from the server
     */
    public void retrieveUser() {
        try {
            Socket socket = new Socket(HOSTNAME, PORT);

            try (
                    ObjectOutputStream objectOutputStream =
                            new ObjectOutputStream(socket.getOutputStream())
            ) {
                objectOutputStream.writeObject(RETRIEVE);

                // This flush is important. The ObjectOutputStream writes some
                // data when it is first created to initialise the stream of
                // objects, and the ObjectInputStream expects to read that
                // data when it is constructed. In practice, when we have a read call
                // followed by a write call (or vice-versa) it is important to flush
                // the first stream before trying to read/write from/to the second.
                // Otherwise, the data in the ObjectOutputStream may not have actually
                // been sent to the server by this point. Because the server will not
                // write anything to the client until it has received the first object,
                // this will result in both server and client waiting for data from each
                // other
                objectOutputStream.flush();

                try (
                        ObjectInputStream objectInputStream =
                                new ObjectInputStream(socket.getInputStream())
                ) {
                    user = (User) objectInputStream.readObject();
                    System.out.println("Gotten: " + user.getUsername() + user.getPassword() + user.getUserType() + ((OrganisationalUnitMembers)user).getUnitCredits());
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            // Print the exception, but no need for a fatal error
            // if the connection with the server happens to be down
            e.printStackTrace();
        }
    }
}
