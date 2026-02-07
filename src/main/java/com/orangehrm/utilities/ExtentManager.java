package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static final Logger log = LoggerManager.getLoggerInstance(ExtentManager.class);
	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();
	private static Map<Long, WebDriver> webDriverMap = new HashMap<>();
	
	public static ExtentReports getReporter() {
		if(extent == null) {
			String reprotPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReports/test_report.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reprotPath);
			spark.config().setReportName("OrangeHRM Automation test report.");
			spark.config().setDocumentTitle("OrangeHRM test Report");
			
			extent = new ExtentReports();
			extent.attachReporter(spark);
			// Adding system information
			extent.setSystemInfo("Operating system", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}
	
	public static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		extentTestThread.set(extentTest);
		return extentTest;
	}
	
	// End a test
	public static void endTest() {
		getReporter().flush();
	}
	
	// get current thread test
	public static ExtentTest getTest() {
		return extentTestThread.get();
	}
	
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if(currentTest != null) {
			return currentTest.getModel().getName();
		}
		else {
			return "No test active on this thread";
		}
	}
	
	// Method to log messages
	public static void log(String message) {
		getTest().info(message);
	}
	
	// log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		
		// screenshot method
		attachScreenshotToReport(driver, screenshotMessage);
	}
	
	// method to log a test failure
	public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().fail(logMessage);
	}
	
	// method to log a test skip
	public static void logSkip(String logMessage) {
		getTest().skip(logMessage);
	}
	
	// method to take a screenshot that contain time and date in file.
	public static String takeScreenshot(WebDriver driver, String scrShotName) {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// format the date and time so that we can add it to screenshot name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String destiNationPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + scrShotName + timeStamp + ".png";
		
		// copy file to destination path
		File destination = new File(destiNationPath);
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Exception while copying screenshot: " + e.getMessage());
		}
		
		// convert to Base64
		String base64Format = convertToBase64Format(destination);
		return base64Format;
	}
	
	// method to convert image to base64 format
	public static String convertToBase64Format(File screenShot) {
		String base64Format ="";
		
		try {
			// read the content to byte string
			byte[] fileContent = FileUtils.readFileToByteArray(screenShot);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Exception converting screen shot to base64 format: " + e.getMessage());
		}
		return base64Format;
	}
	
	// method to attach the screenshot
	public static void attachScreenshotToReport(WebDriver driver, String message) {
		try {
			String base64Screenshot = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot to report");
			log.error("Failed to attach screenshot to report: " + e.getMessage());
		}	
	}
	
	// register the WebDriver for current thread
	public static void registerDriver(WebDriver driver) {
		webDriverMap.put(Thread.currentThread().getId(), driver);
	}
}

