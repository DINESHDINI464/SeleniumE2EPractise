package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass {

	@Test
	public void dummyTest() {
		//ExtentManager.startTest("DummyTest1 Test"); --This has been implemented in ITestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed -title not matched";

		System.out.println("Test Pass --Title matched");
		//ExtentManager.logSkip("This case is skipped");
		throw new SkipException("Skipping the test as part of testing");

	}
}
