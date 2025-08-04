package utils;

public class TestSetup {

    public static void initializePlaywrightWithConfig(String app) {
        ConfigReader.loadProperties(app);

        String browser = ConfigReader.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = "chromium"; // default
        }

        PlaywrightManager.initialize(browser);
    }
}
