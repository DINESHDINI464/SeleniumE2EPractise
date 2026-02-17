package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int retryCount = 0; // no.of retries
	private int maxretryCount = 1; // Max retries

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxretryCount) {
			retryCount++;
			return true; // Retry the count

		}
		return false;
	}
}
