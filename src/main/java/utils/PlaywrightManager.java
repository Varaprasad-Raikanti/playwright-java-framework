package utils;

import java.util.Arrays;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightManager {

	private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
	private static ThreadLocal<Browser> browser = new ThreadLocal<>();
	private static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
	private static ThreadLocal<Page> page = new ThreadLocal<>();

	public static void initialize(String browserName) {
		Playwright pw = Playwright.create();
		playwright.set(pw);

		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
				.setHeadless(Boolean.parseBoolean(ConfigReader.getProperty("headless")))
				.setArgs(Arrays.asList("--start-maximized")); 
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

		// ✅ Pass null viewport → use full screen size
		context.set(browser.get().newContext(new Browser.NewContextOptions().setViewportSize(null)));
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
		String keepBrowserOpen = ConfigReader.getProperty("keepBrowserOpen");

		if (keepBrowserOpen != null && keepBrowserOpen.equalsIgnoreCase("true")) {
			System.out.println("Browser left open for debugging (per config).");

			// ✅ Detach Playwright references so JVM can exit cleanly
			playwright.remove();
			browser.remove();
			context.remove();
			page.remove();

			return; // don't close actual browser
		}

		// Normal cleanup when keepBrowserOpen = false
		if (context.get() != null)
			context.get().close();
		if (browser.get() != null)
			browser.get().close();
		if (playwright.get() != null)
			playwright.get().close();

		playwright.remove();
		browser.remove();
		context.remove();
		page.remove();

		System.out.println("Playwright resources cleaned up.");
	}

}
