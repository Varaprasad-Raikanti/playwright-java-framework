package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import utils.AllureUtils;
import utils.ConfigReader;

public class TestBase {

	protected Playwright playwright;
	protected Browser browser;
	protected BrowserContext context;
	protected Page page;

	@BeforeSuite(alwaysRun = true)
	public void setupAllureEnv() {
		String appName = System.getProperty("appName", "Drybar");
		AllureUtils.writeEnvToAllure(appName);
	}

	@BeforeMethod
	public void setup() {
		playwright = Playwright.create();
		String browserName = ConfigReader.getProperty("browser");
		boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
		String baseUrl = ConfigReader.getProperty("base.url");

		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(isHeadless);

		switch (browserName.toLowerCase()) {
		case "chromium":
			browser = playwright.chromium().launch(options);
			break;
		case "firefox":
			browser = playwright.firefox().launch(options);
			break;
		case "webkit":
			browser = playwright.webkit().launch(options);
			break;
		case "chrome":
			options.setChannel("chrome");
			browser = playwright.chromium().launch(options);
			break;
		default:
			throw new RuntimeException("Unsupported browser: " + browserName);
		}

		context = browser.newContext();
		page = context.newPage();
		page.navigate(baseUrl);
	}

	@AfterMethod
	public void tearDown() {
		if (playwright != null) {
			playwright.close();
		}
	}

	public Page getPage() {
		return page;
	}
}
