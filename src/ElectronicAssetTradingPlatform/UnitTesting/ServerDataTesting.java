package ElectronicAssetTradingPlatform.UnitTesting;

import ElectronicAssetTradingPlatform.Server.NetworkDataSource;
import org.junit.jupiter.api.*;

public class ServerDataTesting {

    NetworkDataSource dataSource;

    @BeforeEach
    @Test
    public void setUpDataSource() {
        dataSource = new NetworkDataSource();
    }

    @Test
    public void testGetUser() {
        dataSource.retrieveUser();
    }
}
