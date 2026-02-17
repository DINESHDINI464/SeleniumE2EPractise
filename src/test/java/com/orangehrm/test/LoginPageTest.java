package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod

	public void setupPages() {

		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	@Test(dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void verifyLoginValidTest(String username, String password) {
		//ExtentManager.startTest("valid login test:"); --This has been implemented in ITestListener
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page by entering username & password");
		loginpage.login(username, password);
		ExtentManager.logStep("verifying admin tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin tab should be visible after successful login");
		ExtentManager.logStep("validation successful");
		homepage.logout();
		ExtentManager.logStep("Logged out successfully");
		staticWait(2); // extends from BaseClass.java
	}

	@SuppressWarnings("deprecation")
	@Test(dataProvider="inValidLoginData", dataProviderClass=DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		// ExtentManager.startTest("Invalid login test:"); --This has been implemented in ITestListener
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to login page by entering username & password");
		loginpage.login(username,password);
		String expectedmesssage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedmesssage), "Test Failed:Invalid error message");
		ExtentManager.logStep("validation successful");
		ExtentManager.logStep("Logged out successfully!");
		staticWait(2);
	}
}
