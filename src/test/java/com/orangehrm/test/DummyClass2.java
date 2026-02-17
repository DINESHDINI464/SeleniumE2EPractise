package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {

	@Test
	public void dummyTest2() {
		//ExtentManager.startTest("DummyTest2 Test"); --This has been implemented in ITestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test fail -title not matched";

		System.out.println("Test Pass --Title matched");
		ExtentManager.logStep("validation successful");

	}
}
