package ElectronicAssetTradingPlatform.GUI;

import ElectronicAssetTradingPlatform.Database.ETPDataSource;
import ElectronicAssetTradingPlatform.Server.NetworkServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerGUI extends JFrame {
    private JTextField serverInfo;
    private JButton shutdownBtn;

    private static NetworkServer server;

    public ServerGUI() {
        // Initialise
        Container contentPane = this.getContentPane();

        serverInfo = new JTextField("Server open on port: " + server.getPort());
        serverInfo.setEditable(false);
        serverInfo.setHorizontalAlignment(JTextField.CENTER);
        shutdownBtn = new JButton("Shutdown server");

        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(serverInfo, BorderLayout.CENTER);
        contentPane.add(shutdownBtn, BorderLayout.SOUTH);

        // add listeners to interactive components
        addWindowListener(new ClosingListener());
        shutdownBtn.addActionListener(new ButtonListener());

        // decorate the frame and make it visible
        setTitle("Login");
        setMinimumSize(new Dimension(250, 120));
        pack();
        setVisible(true);
    }

    /**
     * Handles events on the UI
     */
    private class ButtonListener implements ActionListener {
        /**
         * If shutdown button pressed, close server
         */
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == shutdownBtn) {
                server.shutdown();
            }
        }
    }

    /**
     * Closes the server
     */
    private class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            server.shutdown();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create db if not there
                ETPDataSource etp = new ETPDataSource();

                // Start server
                server = new NetworkServer();
                Thread serverThread = new Thread(() -> {
                    try {
                        server.start();
                    } catch (IOException e) {
                        // In the case of an exception, show an error message and terminate
                        e.printStackTrace();
                        System.exit(1);
                    }
                });
                serverThread.start();

                // Create GUI
                new ServerGUI();
            }
        });

    }
}
