package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

@Test
public class ApiTest {

	public void verifyGetUserApi() {
		SoftAssert softAssert=new SoftAssert();
	
		// Step1: Define API Endpoint
		String endpoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint:" + endpoint);

		// Step2: Send GET Request
		ExtentManager.logStep("Sending GET request to the API");
		Response response = ApiUtility.sendGetRequest(endpoint);

		// Step3: Validate Status code
		ExtentManager.logStep("Validating Response code the the API");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status code validation Failed!");
		}

		// Step4: validating user name
		ExtentManager.logStep("Validating the username field: ");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username validation passed!..");
		} else {
			ExtentManager.logFailureAPI("Username validation Failed");
		}

		// Step5: validating UserEmail
		ExtentManager.logStep("Validating the email field: ");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "UserEmail is not valid");
		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("userEmail validation passed!..");
		} else {
			ExtentManager.logFailureAPI("userEmail validation Failed");
		}
		softAssert.assertAll();

	}
}
