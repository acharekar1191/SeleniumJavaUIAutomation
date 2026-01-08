package com.qa.opencart.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.CartPage;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.RegistrationPage;
import com.qa.opencart.pages.SearchPage;

public class BaseTest{
	
	public DriverFactory df;
	public WebDriver driver;
	public Properties prop;
	public LoginPage loginPage;
	public HomePage homePage;
	public SearchPage searchPage;
	public RegistrationPage registerPage;
	public CartPage cartPage;
	public SoftAssert softAssert;
	
	
	@BeforeTest
	public void setup() {
		df = new DriverFactory();
		prop = df.init_prop();
		driver = df.init_driver(prop);
		homePage = new HomePage(driver);
		softAssert = new SoftAssert();
	}
	
	@AfterTest
	public void teardown() {
		driver.quit();
	}

}
