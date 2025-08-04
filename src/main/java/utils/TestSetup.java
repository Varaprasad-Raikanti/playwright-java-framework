package utils;

import org.testng.SkipException;

import com.microsoft.playwright.Page;

public class TestSetup {

    public static void initializePlaywrightWithConfig(String app) {
        ConfigReader.loadProperties(app);

        String browser = ConfigReader.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = "chromium"; // default
        }

        PlaywrightManager.initialize(browser);
    }
    
 // TestSetup.java or TestInitializer.java
    public static Page initializePageAndNavigate() {
        Page page = PlaywrightManager.getPage();
        if (page == null) throw new SkipException("Playwright page is null — skipping test.");

        String url = ConfigReader.getProperty("url");
        if (url == null || url.isEmpty()) throw new RuntimeException("URL not found in config file");

        page.navigate(url);
        page.waitForLoadState(); // Wait for full load
        return page;
    }

    
    
    
}
