package com.orangehrm.test;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.LoggerManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	private static final Logger log = LoggerManager.getLoggerInstance(HomePageTest.class);
	
	@BeforeMethod
	public void setupPages() {
		log.info("HomePageTest constructor called.");
		this.loginPage = new LoginPage(getDriver());
		this.homePage = new HomePage(getDriver());
	}
	
	@Test
	public void verifyOrangeHRMLogo() {
		// perform login
		log.info("TestCase001: verifyOrangeHRMLogo   ");
		log.info("Performing login operation.");
		loginPage.login("Admin", "admin123");
		
		// check if logo present
		log.info("Checking if logo of OrangeHRM is displayed or not");
		assert homePage.isLogoDisplayed(): "[FAIL]: Logo not displayed.";
	}
	
}
