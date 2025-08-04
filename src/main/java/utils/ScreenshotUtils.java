package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class ScreenshotUtils {

    public static void attachScreenshot(Page page, String testName) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("Failure Screenshot - " + testName, "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            System.out.println("❌ Screenshot capture failed: " + e.getMessage());
        }
    }
}
