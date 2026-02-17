package com.orangehrm.actiondriver;

import java.time.Duration;
//import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	// protected static Properties prop;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitwait = Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitwait));
		logger.info("WebDriver instance created from ActionDriver class");
	}

	// Method to be click element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element: " + elementDescription);
			logger.info("clicked an element---->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("unable to click element: " + elementDescription);
			ExtentManager.logFailure(BaseClass.getDriver(), " unable to click element: ",
					elementDescription + "_Unable to click");
			logger.error("Unable to click an element");
		}
	}

	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on" + getElementDescription(by) + "----> " + value);
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("unable to enter text:" + e.getMessage());
		}

	}

	// Method to get text from an input filed
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Unable to get text from an input filed:" + e.getMessage());
			return "";
		}
	}

	// Method to compare two texts
	public boolean compareText(By by, String exceptedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (exceptedText.equals(actualText)) {
				applyBorder(by,"green");
				logger.info("Texts are matching: " + actualText + " equals: " + exceptedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Tests verified successfully! " + actualText + " equals " + exceptedText);
				return true;
			} else {
				applyBorder(by,"red");
				logger.error("Texts are not matching: " + actualText + " not equals: " + exceptedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Compare Text",
						"Tests comparision failed! " + actualText + " not equals " + exceptedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("unable to compare text:" + e.getMessage());
		}
		return false;
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			logger.info("Element is displayed" + getElementDescription(by));
			ExtentManager.logStep("Element is displayed" + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ",
					"Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			applyBorder(by,"red");
			logger.error("Element not displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Element is not displayed " + getElementDescription(by));
			return false;
		}
	}

	// Method for an pageload wait
	public void pageLoadWait(By by) {
		try {
			waitForElementToBeVisible(by);
			wait.until(webdriver -> ((JavascriptExecutor) webdriver).executeScript("return document.readyState")
					.equals("complete"));
			logger.info("Page load successfully");
		} catch (Exception e) {
			logger.error("Not able to load page:" + e.getMessage());
		}

	}

	// Method to scroll to an Element
	public void scrollToElement(By by) {
		try {
			applyBorder(by,"green");
			waitForElementToBeClickable(by);
			WebElement element = driver.findElement(by);
			new Actions(driver).scrollToElement(element).click().perform();
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("Not able to scroll:" + e.getMessage());
		}
	}

	// Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("element is not clickable:" + e.getMessage());
		}
	}

//wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible: " + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid null pointer exception
		if (driver == null)
			return "driver is null";
		if (locator == null)
			return "Locator is null";

		// find the element using the locator
		WebElement element = driver.findElement(locator);

		// Get element attributes
		String name = element.getDomAttribute("name");
		String id = element.getDomAttribute("id");
		String text = element.getText();
		String className = element.getDomAttribute("class");
		String placeHolder = element.getDomAttribute("placeholder");

		try {
			// Return the description based on element attributes
			if (isNotEmpty(name)) {
				return "Element with name:" + name;
			} else if (isNotEmpty(className)) {
				return "Element with className:" + className;
			} else if (isNotEmpty(id)) {
				return "Element with id:" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text:" + truncate(text, 50);
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placholder:" + placeHolder;
			}
		} catch (Exception e) {
			logger.error("unable to describe the element" + e.getMessage());
		}
		return null;

	}

	// Utility method to check string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + ".............";

	}

	// utility method to border an element
	public void applyBorder(By by, String color) {

		try {
			// locate the element
			WebElement element = driver.findElement(by);
			// Apply the border so write java script
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Apply the border with color " +color+ " to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Fail to apply the border to an element " + getElementDescription(by), e);
		}

	}
	
	public void navigateBack() {
		driver.navigate().back();
	}

}
