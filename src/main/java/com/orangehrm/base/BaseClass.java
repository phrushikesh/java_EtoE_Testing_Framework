// video time -: 4:06:39

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {
	protected static Properties prop;
	protected static WebDriver driver;
	public static final Logger log = LoggerManager.getLoggerInstance(BaseClass.class);

	@BeforeMethod
	public void setup() {
		log.info("Setup for " + this.getClass().getSimpleName() + " started.");
		log.info("Setting up webdriver for: " + this.getClass().getSimpleName());
		launchBrowser();
		configBrowser();
	}
	
	@BeforeSuite
	public void loadConfig() throws IOException {
		// load config.properties file
		prop = new Properties();

		FileInputStream fis = new FileInputStream(
				"E:\\code_base\\myCode\\Java\\.metadata\\java.selenium\\src\\main\\resources\\config.properties");
		prop.load(fis);
	}

	private void launchBrowser() {
		// initialize the webdriver on browser defined in properties file
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver(); // selenium manager - new feature of selenium - automatically download
											// configure and enstanciate driver
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else {
			log.error("Browser not correct");
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}
	}
	
	private void configBrowser() {
		// configure broweser settings, maximize the browser.
		// implicit wait
		int configTime = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configTime));

		// maximize driver
		driver.manage().window().maximize();
		
		// navigate to url
		String url = prop.getProperty("url");
		try {
			driver.get(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Can't naviaget to url, error: " + e.getMessage());
		}
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit(); // driver.close() only quit the active window only.
		}
		log.info("WebDriver instance closed.");
		driver=null;
	}
	
	// getter method for webdriver
	public static WebDriver getDriver() {
		if(driver==null) {
			log.info("WebDriver not initialize");
			throw new IllegalStateException("WebDriver not initialized");
		}
		return driver;
	}
	

	// driver setter method
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	// properties getter method
	public static Properties getProperties() {
		return prop;
	}
	
	// static wait for pause instead of thread.sleep()
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
  