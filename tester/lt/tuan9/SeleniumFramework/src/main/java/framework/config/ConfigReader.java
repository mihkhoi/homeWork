package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static ConfigReader instance;
    private final Properties props = new Properties();

    private ConfigReader() {
        String env = System.getProperty("env", "dev");
        String filePath = "src/test/resources/config-" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
            System.out.println("[ConfigReader] Dang dung moi truong: " + env);
        } catch (IOException e) {
            throw new RuntimeException("Khong tim thay file config: " + filePath, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public static synchronized void reset() {
        instance = null;
    }

    public String getBaseUrl() {
        return props.getProperty("base.url", "https://www.saucedemo.com");
    }

    public int getExplicitWait() {
        return Integer.parseInt(props.getProperty("explicit.wait", "15"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(props.getProperty("implicit.wait", "5"));
    }

    public String getScreenshotPath() {
        return props.getProperty("screenshot.path", "target/screenshots/");
    }

    public String getEdgeDriverPath() {
        return props.getProperty("edge.driver.path", "drivers/msedgedriver.exe");
    }
}
