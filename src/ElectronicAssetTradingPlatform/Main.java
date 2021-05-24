package ElectronicAssetTradingPlatform;

import ElectronicAssetTradingPlatform.GUI.GUI;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	    // Starts with login
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkDataSource net = new NetworkDataSource();
                net.run();
                new GUI(net);
            }
        });
    }
}
