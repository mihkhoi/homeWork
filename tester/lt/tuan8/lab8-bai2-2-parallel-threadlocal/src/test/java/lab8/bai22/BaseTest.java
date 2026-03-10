package lab8.bai22;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public
class BaseTest {

    @Parameters({"browser"})
        @BeforeMethod public void setUp(String browser) {
        DriverFactory.initDriver(browser);
        System.out.println("Khoi tao driver cho thread: " + Thread.currentThread().getId());
    }

    @AfterMethod public void tearDown() {
        System.out.println("Dong driver cho thread: " + Thread.currentThread().getId());
        DriverFactory.quitDriver();
    }
}
