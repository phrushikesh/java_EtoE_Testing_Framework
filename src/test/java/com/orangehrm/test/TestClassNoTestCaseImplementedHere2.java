package com.orangehrm.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class TestClassNoTestCaseImplementedHere2 extends BaseClass {

	@Test
	public void ClickLoginBtn2() {
		String title = "OrangeHRM";
		String actualTitle = driver.getTitle();
		
		assert actualTitle.equals(title) : "Test failed: Title not matching";
		System.out.println("Test passed");
	}
}
