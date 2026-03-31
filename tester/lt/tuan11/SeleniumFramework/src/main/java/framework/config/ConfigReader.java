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
            System.out.println("[ConfigReader] Environment: " + env);
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy file config: " + filePath, e);
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

    /**
     * Đọc username - ưu tiên GitHub Secrets
     */
    public String getUsername() {
        String envUsername = System.getenv("APP_USERNAME");
        if (envUsername != null && !envUsername.isBlank()) {
            return envUsername;
        }
        String configUsername = props.getProperty("app.username");
        if (configUsername != null && !configUsername.isBlank()) {
            return configUsername;
        }
        throw new RuntimeException("APP_USERNAME not found. Set GitHub Secret or config.properties");
    }

    /**
     * Đọc password - ưu tiên GitHub Secrets
     */
    public String getPassword() {
        String envPassword = System.getenv("APP_PASSWORD");
        if (envPassword != null && !envPassword.isBlank()) {
            return envPassword;
        }
        String configPassword = props.getProperty("app.password");
        if (configPassword != null && !configPassword.isBlank()) {
            return configPassword;
        }
        throw new RuntimeException("APP_PASSWORD not found. Set GitHub Secret or config.properties");
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
