package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Server.NetworkServer;

import java.io.IOException;

public class ServerTester {
    public static void main(String[] args) {
        NetworkServer server = new NetworkServer();
        try {
            server.start();
        } catch (IOException e) {
            // In the case of an exception, show an error message and terminate
            e.printStackTrace();
            System.exit(1);
        }
    }
}
