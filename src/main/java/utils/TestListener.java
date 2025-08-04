package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Object testInstance = result.getInstance();

            // Get private 'page' field from current test class (not superclass!)
            Field pageField = testInstance.getClass().getDeclaredField("page");
            pageField.setAccessible(true);
            Page page = (Page) pageField.get(testInstance);

            if (page != null) {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment("Failure Screenshot - " + result.getName(), "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
                System.out.println(" Screenshot attached to Allure.");
            } else {
                System.out.println("Page is null. Screenshot not taken.");
            }

        } catch (NoSuchFieldException nsfe) {
            System.out.println("'page' field not found in test class: " + nsfe.getMessage());
        } catch (Exception e) {
            System.out.println(" Screenshot capture failed: " + e.getMessage());
        }
    }
}
