package utils;

import java.io.ByteArrayInputStream;

import com.microsoft.playwright.Page;

import io.qameta.allure.Allure;

public class ScreenshotUtils {

	public static void attachScreenshot(Page page, String status, String testName) {
		try {
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
			Allure.addAttachment(status + " Screenshot - " + testName, "image/png",
					new ByteArrayInputStream(screenshot), ".png");
		} catch (Exception e) {
			System.out.println("❌ Screenshot capture failed: " + e.getMessage());
		}
	}
}
