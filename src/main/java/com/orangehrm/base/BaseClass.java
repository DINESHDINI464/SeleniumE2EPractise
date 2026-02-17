package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>(); // ThreadLocal is a Java class used to ensure
																		// thread safety when running tests in parallel
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	protected ThreadLocal<SoftAssert> softAssert=ThreadLocal.withInitial(SoftAssert::new);
	
	//Getter method for softAssert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
		//Start the extent report 
		//ExtentManager.getReporter(); --This has been implemented in ITestListener

	}

	@SuppressWarnings("deprecation")
	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("settings up webdriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configBroswer();
		staticWait(2);
		logger.info("WebDriver initialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a Error message");
		logger.debug("This is a Debug message");
		logger.fatal("This is a Fatal message");
		logger.warn("This is a Waring message");

		// Initialize the actionDriver only once(Singleton Design Framework)
		/*
		 * if (actionDriver == null) { 
		 * actionDriver = new ActionDriver(driver);
		 * logger.info( "ActionDriver instance is created, initialized from base class " + Thread.currentThread().getId()); }
		 */

//initialize ActionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread 4rm baseclass: "+Thread.currentThread().getId());
	
	}

	/*
	 * Initialize the WebDriver based on the browser defined in config.properties
	 * file
	 */
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			//driver = new ChromeDriver();
			driver.set(new ChromeDriver()); //New changes as per ThreadLocal class
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());//New changes as per ThreadLocal class
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver()); //New changes as per ThreadLocal class
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created");

		} else {
			throw new IllegalArgumentException("Browser not supported" + browser);
		}
	}

	/*
	 * Configure browser settings such as implicit wait, maximize browser, navigate
	 * the url
	 */
	private void configBroswer() {
		// Implicit wait
		int implicitwait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));
		// Maximize the driver
		getDriver().manage().window().maximize();

		// Navigate the url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("fail to navigate to the url:" + e.getMessage());
		}

	}
	/*
	 * // Driver getter method public WebDriver getDriver() { return driver; }
	 * 
	 */

	// Getter method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

	// Getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		BaseClass.driver = driver;
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;

	}

	@AfterMethod
	public void tearDown() {
		// quit the browser
		if (getDriver() != null) {

			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Fail to quit the driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
		//driver = null;
		//actionDriver = null;
		//ExtentManager.endTest(); --This has been implemented in ITestListener
	}

	// static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}