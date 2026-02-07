package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.LoggerManager;

public abstract class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	private static final Logger log = LoggerManager.getLoggerInstance(ActionDriver.class);


	public ActionDriver(){
		this.driver = BaseClass.getDriver();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(getExplicitWaitTime()));
		log.info("Constructor of action driver class");
	}
	
	private int getExplicitWaitTime(){
		return Integer.parseInt(BaseClass.getProperties().getProperty("explicitWait"));
	}

	// wait for element to be clickable
	private void waitUntilElementClickable(By elementLocator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
		} catch (Exception e) {
			log.error("element is not clickable, error: " + e.getMessage());
		}
	}

	// wait for element to be visible
	private void waitUntilElementVisible(By elementLocator) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
		} catch (Exception e) {
			log.error("element is not visible, error: " + e.getMessage());
		}
	}
	
	// wait for page to load completely
	public void waitForPageLoad(int timeoutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeoutInSec))
		    .until(driver -> ((JavascriptExecutor) driver)
		        .executeScript("return document.readyState")
		        .equals("complete"));
			log.info("Page loaded successfully.");
		}
		catch(Exception e) {
			log.error("Page did not load, exception: " + e.getMessage());
		}
	}

	// method to click element
	public void click(By by) {
		String desc = getElementDescription(by);
		try {
			waitUntilElementClickable(by);
			driver.findElement(by).click();
			log.info("Clicked on: " + desc);
		} catch (Exception e) {
			log.error("Unable to click element, error: " + e.getMessage());
		}
	}

	public void enterText(By elementLocator, String text) {
		String desc = getElementDescription(elementLocator);
		try {
			waitUntilElementVisible(elementLocator);
			WebElement ele = driver.findElement(elementLocator);
			ele.clear();
			ele.sendKeys(text);
			log.info("Entered text on " + desc + ": " + text);
		} catch (Exception e) {
			log.error("Unable to enter text, error: " + e.getMessage());
		}
	}

	// get text from element
	public String getText(By elementLocator) {
		try {
			waitUntilElementVisible(elementLocator);
			return driver.findElement(elementLocator).getText();
		} catch (Exception e) {
			log.error("Could not get text from element, error: " + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitUntilElementVisible(by);
			String actualText = driver.findElement(by).getText();
			if (actualText.equals(expectedText)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Error while comparing texts, error: " + e.getMessage());
			return false;
		}
	}

	// check if element is displayed
	public boolean isDisplayed(By eleLocator) {
		String desc = getElementDescription(eleLocator);
		try {
			waitUntilElementVisible(eleLocator);
			log.info("Displayed on DOM: " + desc);
			return driver.findElement(eleLocator).isDisplayed();
		} catch (Exception e) {
			log.error("Element is not displayed, error: " + e.getMessage());
			return false;
		}
	}

	// scroll to element
	public void scrollToElement(By eleLocator) {
		try {
		waitUntilElementVisible(eleLocator);
		WebElement ele = driver.findElement(eleLocator);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView(true);", ele);
		log.info("Scrolled successfully to element with locator: " + eleLocator);
		}
		catch(Exception e) {
			log.error("Exception occured while executing js, error: " + e.getMessage());
		}
	}
	
	// get title of the page
	public String getTitle() {
		waitForPageLoad(10);
		return driver.getTitle();
	}
	
	//method to get description of element based on its locator
	public String getElementDescription(By eleLocator) {
		if(driver==null) {
			return "driver is null";
		}
		if(eleLocator==null) return "Locator is null";
		
		// find the element using locator
		WebElement ele = driver.findElement(eleLocator);
		
		// get element description
		try {
		String name = ele.getDomAttribute("name");
		String id = ele.getDomAttribute("id");
		String text = ele.getText();
		String className = ele.getDomAttribute("class");
		String placeHolder = ele.getDomAttribute("placeholder");
		
		if(isNotNullOrEmpty(name)) return "Element with name: " + name;
		else if(isNotNullOrEmpty(id)) return "Element with id: " + id;
		else if(isNotNullOrEmpty(text)) return "Element with text: " + text;
		else if(isNotNullOrEmpty(className)) return "Element with class name: " + className;
		else if(isNotNullOrEmpty(placeHolder)) return "Element with placeholder: " + placeHolder;
		}
		catch(Exception e) {
			log.error("Exception while getting description of element, error: " + e.getMessage());
		}
		// return description based on element attributes
		return "Unable to describe element";
	}
	
	public boolean isNotNullOrEmpty(String inpString) {
		return inpString!=null && !inpString.isEmpty();
	}
}
