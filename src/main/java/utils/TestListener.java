package utils;

import base.TestBase;
import com.microsoft.playwright.Page;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        try {
            // Get the "page" field using reflection
            java.lang.reflect.Field pageField = currentClass.getClass().getSuperclass().getDeclaredField("page");
            pageField.setAccessible(true);
            Page page = (Page) pageField.get(currentClass);

            if (page != null) {
                ScreenshotUtils.attachScreenshot(page, "Failure Screenshot - " + result.getName());
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to attach screenshot: " + e.getMessage());
        }
    }

    // You can optionally override other methods like onTestSuccess/onTestSkipped here
}
