package utils;

import com.microsoft.playwright.*;

public class PlaywrightManager {

    private static Playwright playwright;
    private static Browser browserInstance;
    private static BrowserContext context;
    private static Page page;

    public static void initialize(String browserName) {
        if (playwright != null) {
            return; // Already initialized
        }

        playwright = Playwright.create();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Boolean.parseBoolean(ConfigReader.getProperty("headless")));

        switch (browserName.toLowerCase()) {
            case "chromium":
                browserInstance = playwright.chromium().launch(options);
                break;
            case "firefox":
                browserInstance = playwright.firefox().launch(options);
                break;
            case "webkit":
                browserInstance = playwright.webkit().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        context = browserInstance.newContext();
        page = context.newPage();
        System.out.println("Playwright initialized with browser: " + browserName);
    }

    public static Page getPage() {
        if (page == null) {
            throw new IllegalStateException("Page not initialized. Call initialize() first.");
        }
        return page;
    }

    public static void tearDown() {
        if (context != null) context.close();
        if (browserInstance != null) browserInstance.close();
        if (playwright != null) playwright.close();

        playwright = null;
        browserInstance = null;
        context = null;
        page = null;

        System.out.println("Playwright resources cleaned up.");
    }
}
