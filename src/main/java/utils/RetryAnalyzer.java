package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int currentRetry = 0;
	private static final int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {

		if (currentRetry < maxRetryCount) {
			currentRetry++;

			System.out.println("Retrying Test: " + result.getName() + " | Retry Count: " + currentRetry);

			return true;
		}

		return false;
	}
}