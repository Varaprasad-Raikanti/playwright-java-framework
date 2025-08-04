package utils;

import com.microsoft.playwright.*;

public class PlaywrightManager {

    private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static ThreadLocal<Page> page = new ThreadLocal<>();

    public static void initialize(String browserName) {
        Playwright pw = Playwright.create();
        playwright.set(pw);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Boolean.parseBoolean(ConfigReader.getProperty("headless")));

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser.set(pw.chromium().launch(options));
                break;
            case "firefox":
                browser.set(pw.firefox().launch(options));
                break;
            case "webkit":
                browser.set(pw.webkit().launch(options));
                break;
            case "chrome":
                browser.set(pw.chromium().launch(options.setChannel("chrome")));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        context.set(browser.get().newContext());
        page.set(context.get().newPage());

        System.out.println("Playwright initialized with browser: " + browserName);
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getContext() {
        return context.get();
    }

    public static void tearDown() {
        if (context.get() != null) context.get().close();
        if (browser.get() != null) browser.get().close();
        if (playwright.get() != null) playwright.get().close();

        playwright.remove();
        browser.remove();
        context.remove();
        page.remove();

        System.out.println("Playwright resources cleaned up.");
    }
}
