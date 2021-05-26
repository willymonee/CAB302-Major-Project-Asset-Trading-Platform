package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.AssetTrading.BuyOffer;
import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

public class ServerDataTesting {
    static NetworkDataSource data;
    static NetworkDataSource data2;

    @BeforeAll
    @Test
    public static void initConnection() {
        data = new NetworkDataSource();
        data.run();
        data2 = new NetworkDataSource();
        data2.run();
    }

//    @Test
//    public void testGetUser() throws SQLException, NetworkDataSource.DatabaseException {
//        OrganisationalUnitMembers user = (OrganisationalUnitMembers) data.retrieveUser("willymon");
//
//        System.out.println("Gotten: " + user.getUsername() + user.getPassword() + user.getUserType() + user.getUnitCredits());
//    }
//
//    @Test
//    public void testStoreUser() throws NetworkDataSource.DatabaseException {
//        ITAdmin userStore = new ITAdmin("name", "pass", "salt");
//        System.out.println(data.storeUser(userStore));
//
//        ITAdmin user = (ITAdmin) data.retrieveUser("name");
//        assertEquals("name", user.getUsername());
//        assertEquals("pass", user.getPassword());
//        assertEquals("ITAdmin", user.getUserType());
//    }
//
//    @Test
//    public void testInvalidGetUser() {
//        assertThrows(NetworkDataSource.DatabaseException.class, () -> data.retrieveUser("joe1920c"));
//    }

    @Test
    public void testAddBuyOffer() {
        BuyOffer buyOffer = new BuyOffer("iPhone 10", 3, 25, "willymon", "Human Resources");
        System.out.println(data.addBuyOffer(buyOffer));
    }

    // When testing this - remove the ITAdmin and run this test only
    @Test
    public void testThreadedServer() throws InterruptedException {
//        ThreadedServerRunnable test1 = new ThreadedServerRunnable(data);
//        ThreadedServerRunnable test2 = new ThreadedServerRunnable(data2);
//        test1.start();
//        test2.start();
//        Thread.sleep(5000);
//        test1.finish();
//        test2.finish();
//        test1.join();
//        test2.join();
    }

    private static class ThreadedServerRunnable extends Thread {
        private volatile boolean stopFlag;
        private volatile NetworkDataSource data;

        public ThreadedServerRunnable(NetworkDataSource dataSource) {
            super();
            data = dataSource;
        }

        public void run() {
            stopFlag = false;
            while (!stopFlag) {
                try {
                    ITAdmin userStore = new ITAdmin("name", "pass", "salt");
                    System.out.println("Stored: " + data.storeUser(userStore));

                    OrganisationalUnitMembers user = (OrganisationalUnitMembers) data.retrieveUser("willymon");
                    System.out.println("Gotten: " + user.getUsername() + user.getPassword());
                } catch (NetworkDataSource.DatabaseException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void finish() {
            stopFlag = true;
        }
    }
}
