package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actiondriver;

	// Define locators using By class

	private By userNameFiled = By.cssSelector("[name='username']");
	private By passwordFiled = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[text()=' Login ']");
	private By errormessage = By.xpath("//p[text()='Invalid credentials']");

	//Initialize the ActionDriver object by passing WebDriver instance
/*	public LoginPage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);

	}*/
	
	public LoginPage(WebDriver driver) {
		this.actiondriver=BaseClass.getActionDriver();
	}

//Method to perform login 
	public void login(String username, String password) {

		actiondriver.enterText(userNameFiled, username);
		actiondriver.enterText(passwordFiled, password);
		actiondriver.click(loginButton);
	}

	// Method to check if the error message is displayed
	public boolean isErrorMesssageDisplayed() {
		return actiondriver.isDisplayed(errormessage);
	}

	// Method to get the text from error message
	public String getErrorMessageText() {
		return actiondriver.getText(errormessage);
	}

	// verify if error is correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actiondriver.compareText(errormessage, expectedError);
	}

}
