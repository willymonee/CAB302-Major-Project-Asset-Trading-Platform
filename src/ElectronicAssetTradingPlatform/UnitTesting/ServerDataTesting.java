package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import ElectronicAssetTradingPlatform.Users.ITAdmin;
import ElectronicAssetTradingPlatform.Users.OrganisationalUnitMembers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class ServerDataTesting {

    @Test
    public void testGetUser() throws SQLException, NetworkDataSource.DatabaseException {
        OrganisationalUnitMembers user = (OrganisationalUnitMembers) NetworkDataSource.retrieveUser("willymon");

        System.out.println("Gotten: " + user.getUsername() + user.getPassword() + user.getUserType() + user.getUnitCredits());
    }

    @Test
    public void testStoreUser() throws NetworkDataSource.DatabaseException {
        ITAdmin userStore = new ITAdmin("name", "pass", "salt");
        System.out.println(NetworkDataSource.storeUser(userStore));

        ITAdmin user = (ITAdmin) NetworkDataSource.retrieveUser("name");
        assertEquals("name", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("ITAdmin", user.getUserType());
    }

    @Test
    public void testInvalidGetUser() {
        assertThrows(NetworkDataSource.DatabaseException.class, () -> NetworkDataSource.retrieveUser("a"));
    }

    // When testing this - remove the ITAdmin and run this test only
    @Test
    public void testThreadedServer() throws InterruptedException {
        ThreadedServerRunnable test1 = new ThreadedServerRunnable();
        ThreadedServerRunnable test2 = new ThreadedServerRunnable();
        test1.start();
        test2.start();
        Thread.sleep(5000);
        test1.finish();
        test2.finish();
        test1.join();
        test2.join();
    }

    private static class ThreadedServerRunnable extends Thread {
        private volatile boolean stopFlag;

        public ThreadedServerRunnable() { super(); }

        public void run() {
            stopFlag = false;
            while (!stopFlag) {
                try {
                    ITAdmin userStore = new ITAdmin("name", "pass", "salt");
                    System.out.println("Stored: " + NetworkDataSource.storeUser(userStore));

                    OrganisationalUnitMembers user = (OrganisationalUnitMembers) NetworkDataSource.retrieveUser("willymon");
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
