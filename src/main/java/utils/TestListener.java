package utils;


import com.microsoft.playwright.Page;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtils.log("❌ Test Failed: " + result.getName());

        Page page = PlaywrightManager.getPage();
        if (page != null) {
            ScreenshotUtils.attachScreenshot(page, "Failure Screenshot - " + result.getName());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtils.log("▶️ Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtils.log("✅ Test Passed: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtils.log("🧪 Test Suite Finished: " + context.getName());
    }

    // You can override other methods if needed (onTestSkipped, onStart, etc.)
}
