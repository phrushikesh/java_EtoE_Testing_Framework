package com.orangehrm.pages;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.LoggerManager;

public class LoginPage extends ActionDriver{
	private static final Logger log = LoggerManager.getLoggerInstance(LoginPage.class);
	
	// Define locators using By class
	private By usernameFieldByName = By.name("username");
	private By passwordFiledCssSelector = By.cssSelector("input[name='password']");
	private By loginBtnXpath = By.xpath("//button[contains(@class, 'orangehrm-login-button')]");
	private By invalidPasswrodXpath = By.xpath("//p[contains(., 'Invalid credentials')]");
	
	public LoginPage(WebDriver driver) {
	}

	// method to perform login action
	public void login(String username, String passwrod) {
		log.info("Performing login operation");
		enterText(usernameFieldByName, username);
		enterText(passwordFiledCssSelector, passwrod);
		click(loginBtnXpath);
	}
	
	// method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		log.info("Validating an error message is displayed or not.");
		return isDisplayed(invalidPasswrodXpath);
	}
	
	// method to get text from error message
	public String getErrorMessageText() {
		log.info("Getting error message text");
		return getText(invalidPasswrodXpath);
	}
	
	// verify invalid passwrod message is correct
	public boolean isInvalidPasswordTextCorrect() {
		log.info("Checking error message text");
		return compareText(invalidPasswrodXpath, "Invalid credentials");
	}
	
	// method to get the title of the page
	public String getCurrPageTitle() {
		log.info("Getting current page title.");
		waitForPageLoad(5);
		return getTitle();
	}
}
