package utils;

import org.testng.Assert;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.qameta.allure.Allure;

public class waitUtils {

	// ✅ 1. Wait for page to fully load
	public static void waitUntilPageIsReady(Page page) {
		try {
			page.waitForLoadState(LoadState.LOAD);
			System.out.println("✅ Page fully loaded.");
		} catch (Exception e) {
			System.out.println("❌ Failed while waiting for page to load: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failuure", "waitUntilPageIsReady");
			Assert.fail("Page load failed");
		}
	}

	// ✅ 2. Wait for element to become visible
	public static void waitUntilElementVisible(Page page, String selector) {
		try {
			Locator element = page.locator(selector);
			element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
			System.out.println("✅ Element visible: " + selector);
		} catch (Exception e) {
			System.out.println("❌ Element not visible: " + selector + " - " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failuure", "waitUntilElementVisible");
			Assert.fail("Element not visible: " + selector);
		}
	}

	// ✅ 3. Wait for element to become attached to DOM
	public static void waitUntilElementAttached(Page page, String selector) {
		try {
			Locator element = page.locator(selector);
			element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
			System.out.println("✅ Element attached: " + selector);
		} catch (Exception e) {
			System.out.println("❌ Element not attached: " + selector + " - " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failuure", "waitUntilElementAttached");
			Assert.fail("Element not attached: " + selector);
		}
	}

	// ✅ 4. Wait for element to become detached from DOM (useful for loading screens
	// or modals)
	public static void waitUntilElementDetached(Page page, String selector) {
		try {
			Locator element = page.locator(selector);
			element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
			System.out.println("✅ Element detached: " + selector);
		} catch (Exception e) {
			System.out.println("❌ Element not detached: " + selector + " - " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failuure", "waitUntilElementDetached");
			Assert.fail("Element not detached: " + selector);
		}
	}

	// ✅ 5. Wait for element to be enabled and interactable

	public static void waitUntilElementEnabled(Page page, String selector) {
		try {
			Locator element = page.locator(selector);
			element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
			Assert.assertTrue(element.isEnabled(), "Element is not enabled: " + selector);
			System.out.println("✅ Element is enabled: " + selector);
		} catch (Exception e) {
			System.out.println("❌ Element not enabled: " + selector + " - " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failuure", "waitUntilElementEnabled");
			Assert.fail("Element not enabled: " + selector);
		}
	}

	public static void validateCurrentUrl(Page page, String expectedPartialUrl) {
		try {
			String currentUrl = page.url();
			System.out.println("✅ Current URL: " + currentUrl);

			if (!currentUrl.contains(expectedPartialUrl)) {
				System.out.println("❌ Expected URL not found in current URL.");
				Assert.fail("Current URL does not contain expected value. Expected to contain: " + expectedPartialUrl
						+ ", but found: " + currentUrl);
			} else {
				System.out.println("✅ URL validation passed. Contains: " + expectedPartialUrl);
			}

		} catch (Exception e) {
			System.out.println("❌ Exception while validating URL: " + e.getMessage());
			Assert.fail("Exception occurred during URL validation");
		}
	}

	public static void validatePageTitle(Page page, String expectedPartialUrl) {
		try {
			String currentTitle = page.title();
			System.out.println("✅ Current Page Title: " + currentTitle);

			if (!currentTitle.contains(expectedPartialUrl)) {
				System.out.println("❌ Expected Title not found in current Title.");
				Assert.fail("Current Title does not match to expected Title. Expected : " + expectedPartialUrl
						+ ", but found: " + currentTitle);
			} else {
				System.out.println("✅ Title validation passed : " + expectedPartialUrl);
			}

		} catch (Exception e) {
			System.out.println("❌ Exception while validating Title: " + e.getMessage());
			Assert.fail("Exception occurred during Title validation");
		}
	}

	public static void validatePageUrlAndTitleContains(Page page, String[] expectedUrlParts,
			String[] expectedTitleParts) {
		try {
			Allure.step("🔎 Validating URL and Page Title using 'contains' check");

			String actualUrl = page.url();
			String actualTitle = page.title();

			boolean urlMatched = false;
			for (String expectedPart : expectedUrlParts) {
				if (actualUrl.contains(expectedPart)) {
					urlMatched = true;
					break;
				}
			}

			boolean titleMatched = false;
			for (String expectedTitle : expectedTitleParts) {
				if (actualTitle.contains(expectedTitle)) {
					titleMatched = true;
					break;
				}
			}

			Assert.assertTrue(urlMatched, "❌ URL did not contain any expected patterns");
			Assert.assertTrue(titleMatched, "❌ Page title did not contain any expected patterns");

			System.out.println("✅ URL and Title contain expected values.");

		} catch (AssertionError ae) {
			ScreenshotUtils.attachScreenshot(page, "Failuure", "validatePageUrlAndTitleContains");
			System.out.println("❌ Assertion failed: " + ae.getMessage());
			throw ae;
		} catch (Exception e) {
			ScreenshotUtils.attachScreenshot(page, "Failuure", "validatePageUrlAndTitleContains");
			System.out.println("❌ Unexpected error during URL/Title contains validation: " + e.getMessage());
			Assert.fail("URL/Title contains validation failed: " + e.getMessage());
		}
	}
}