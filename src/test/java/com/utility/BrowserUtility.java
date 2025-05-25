package com.utility;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.constants.Browser;

public abstract class BrowserUtility {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	Logger logger = LoggerUtility.getLogger(this.getClass());

	public WebDriver getDriver() {
		return driver.get();
	}

	public BrowserUtility(WebDriver driver) {
		super();
		this.driver.set(driver);
	}

	public BrowserUtility(String browserName) {
		logger.info("Launching browser for " + browserName);
		if (browserName.equalsIgnoreCase("chrome")) {

			driver.set(new ChromeDriver());
		} else if (browserName.equalsIgnoreCase("edge")) {

			driver.set(new EdgeDriver());
		} else {
			logger.error("Invalid browser!! please select chrome or edge only!!");
			System.err.println("Invalid browser!! please select chrome or edge only!!");
		}
	}

	public BrowserUtility(Browser browserName) {
		logger.info("Launching browser for " + browserName);
		if (browserName == Browser.CHROME) {
			driver.set(new ChromeDriver());
		} else if (browserName == Browser.EDGE) {
			driver.set(new EdgeDriver());
		} else if (browserName == Browser.FIREFOX) {
			driver.set(new FirefoxDriver());
		}

	}

	public BrowserUtility(Browser browserName, boolean isHaeadless) {
		logger.info("Launching browser for " + browserName);

		if (browserName == Browser.CHROME) {
			if (isHaeadless) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless=old");
				options.addArguments("--window-size=1920,1080");
				driver.set(new ChromeDriver(options));
			} else {
				driver.set(new ChromeDriver());
			}

		} else if (browserName == Browser.EDGE) {
			if (isHaeadless) {
				EdgeOptions options = new EdgeOptions();
				options.addArguments("--headless=old");
				options.addArguments("disable-gpu");
				driver.set(new EdgeDriver(options));
			} else {
				driver.set(new EdgeDriver());
			}
		} else if (browserName == Browser.FIREFOX) {
			if (isHaeadless) {
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--headless=old");
				driver.set(new FirefoxDriver(options));
			} else {
				driver.set(new FirefoxDriver());
			}

		}

	}

	public void goToWebsite(String url) {
		logger.info("Visiting the website" + url);
		driver.get().get(url);
	}

	public void maximizeWindow() {
		logger.info("Maximize the browser window");
		driver.get().manage().window().maximize();
	}

	public void clickOn(By locator) {
		logger.info("Finding element with the locator" + locator);
		WebElement element = driver.get().findElement(locator);
		logger.info("Element found and now performing click");
		element.click();
	}

	public void enterText(By locator, String textToEnter) {
		logger.info("Finding element with the locator" + locator);
		WebElement element = driver.get().findElement(locator);
		logger.info("Element found and now enter text" + textToEnter);
		element.sendKeys(textToEnter);
	}

	public String getVisibleText(By locator) {
		logger.info("Finding element with the locator" + locator);
		WebElement element = driver.get().findElement(locator);
		logger.info("Element found and now returning the visible " + element.getText());
		return element.getText();
	}

	public String takeScreenshot(String name) {
		if (driver.get() == null) {
			logger.error("WebDriver is null — cannot take screenshot.");
			return null;
		}

		TakesScreenshot screenshot = (TakesScreenshot) driver.get();
		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String path = System.getProperty("user.dir") + "/screenshots/" + name + "-" + timestamp + ".png";

		File destFile = new File(path);
		try {
			destFile.getParentFile().mkdirs(); // Ensure folder exists
			FileUtils.copyFile(srcFile, destFile);
			logger.info("Screenshot saved at: " + path);
		} catch (IOException e) {
			logger.error("Screenshot saving failed: " + e.getMessage(), e);
		}

		return path;
	}

}
