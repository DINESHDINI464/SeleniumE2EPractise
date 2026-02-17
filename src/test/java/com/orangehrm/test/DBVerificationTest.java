package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod

	public void setupPages() {

		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider="empVerification", dataProviderClass=DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String empID,String empName) {
		
		SoftAssert softAssert=getSoftAssert();
		
		ExtentManager.logStep("Logging with Admin credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		ExtentManager.logStep("click on PIM tab");
		homePage.clickOnPIMTab();

		ExtentManager.logStep("search for employee");
		homePage.employeeSearch(empName);
		ExtentManager.logStep("Get the employee name from Db");
		String employee_id = empID;
//fetch the data into a map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		String empFirstName = employeeDetails.get("firstName");
		String empMiddleName = employeeDetails.get("middleName");
		String empLastName = employeeDetails.get("lastName");

		String empFirstAndMiddleName = (empFirstName+" "+empMiddleName).trim();

	/*	ExtentManager.logStep("verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName),
				"First and Middle name are not matching");
		
		ExtentManager.logStep("Click the browser back button");
		homePage.clickOnBackButton();*/

		ExtentManager.logStep("Verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmploeeLastName(empLastName));

		ExtentManager.logStep("DB validation completed");
		
		softAssert.assertAll();

	}

}
