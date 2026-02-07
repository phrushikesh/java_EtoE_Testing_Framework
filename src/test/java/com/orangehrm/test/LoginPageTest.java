package com.orangehrm.test;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.LoggerManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	private static final Logger log = LoggerManager.getLoggerInstance(LoginPageTest.class);
	
	@BeforeMethod
	public void setupPages() {
		log.info("Constructor for LoginPageTest called");
		this.loginPage = new LoginPage(getDriver());
		this.homePage = new HomePage(getDriver());
	}
	
	@Test(enabled=false)
	public void test001_verfiyValidLogin() {
		log.info("TestCase002: Verify valid credentials login");
		// perform login
		log.info("Performing login action using valid credentials");
		loginPage.login("Admin", "admin123");
		
		// step 7: verify ADMIN tab on home page
		log.info("Checking if admin tab is visible or not");
		boolean isAdminTabVisible = homePage.isAdminTabVisible();
		assert isAdminTabVisible: "Test failed: Admin tab not visible";
		log.info("Admin tab is visible");
		
		// step 8: perform logout operation
		log.info("Performing log out operation.");
		homePage.logout();
		
		// check if landed on login page
		assert loginPage.getTitle().equals("Orang11eHRM"): "Test Failed: Title not equal";
		log.info("Returned back on login page and title confirmed.");
	}
	
	@Test
	public void test002_invalidCredentials() {
		log.info("TestCase003: Verify login with invalid credentials.");
		// perform login with invalid credentials
		log.info("Performing login operation with invalid credentials");
		loginPage.login("admin", "admin");
		
		// check if invalid login message is displayed
		log.info("Checking if the error message is displayed on login page.");
		assert loginPage.isErrorMessageDisplayed(): "[FAIL]: Invalid password message not displayed";
		log.info("Error message displayed");
		
		// Checking error message
		log.info("Checking if correct error message is displayed on the screen.");
		assert loginPage.getErrorMessageText().equals("Invalid credentials"): "[FAIL]: Error message incorrect";
		log.info("Correct error messaeg displayed.");
		
		// checking if still on login page
		log.info("Checking title of current page to verify still present on login page.");
		assert loginPage.getTitle().equals("OrangeHRM"): "[FAIL]: Not on login page";
		log.info("Successfully verified login page title.");
	}
}
