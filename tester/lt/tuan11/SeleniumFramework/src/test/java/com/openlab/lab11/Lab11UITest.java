package com.openlab.lab11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import java.time.Duration;

public class Lab11UITest extends SeleniumBase {

    @Test
    void exampleUiTest() {
        setUp();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            // Simple placeholder: navigate to a known page and check title if available
            driver.get("https://example.com");
            String title = driver.getTitle();
            assertTrue(title.contains("Example"));
        } finally {
            tearDown();
        }
    }
}
