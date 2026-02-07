package com.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.LoggerManager;

public class HomePage extends ActionDriver{
	private static final Logger log = LoggerManager.getLoggerInstance(HomePage.class);
	
	private By adminTabXpath = By.xpath("//span[text()='Admin']");
	private By bannerLocClassName = By.className("oxd-brand-banner");
	private By userIDClassName = By.className("oxd-userdropdown-tab");
	private By logoutBtnXpath = By.xpath("//a[contains(text(), 'Logout')]");
	
	// constructor
//	public HomePage(WebDriver driver) {
//		this.actionDriver = new ActionDriver(driver);
//	}
	
	// constructor
	public HomePage(WebDriver driver) {
	}
	
	// method to verify admin tag is visible
	public boolean isAdminTabVisible() {
		log.info("Checking if admin tab is visible");
		return isDisplayed(adminTabXpath);
	}
	
	// method to check is logo is displayed
	public boolean isLogoDisplayed() {
		log.info("Checking if OrangeHRM logo is displayed correctly.");
		return isDisplayed(bannerLocClassName);
	}
	
	// method to log out
	public void logout() {
		log.info("Performing out opreation");
		log.info("Clicking on logout button");
		click(userIDClassName);
		click(logoutBtnXpath);
	}
}
