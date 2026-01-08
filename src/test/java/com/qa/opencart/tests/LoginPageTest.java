package com.qa.opencart.tests;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class LoginPageTest extends BaseTest{
	
	/**
	 * Always initialize properties values inside test method or beforeClass, do not initialise at class level. 
	 * class-level variables run before TestNG lifecycle
	 * BaseTest is not initialized yet, thats why prop is null.
	 */
	
	String uname,pwd;
	
	@BeforeClass
	public void loginPageSetup() {
		uname = prop.getProperty("username");
		pwd = prop.getProperty("password");

		loginPage = homePage.navigateToLoginPageFromHomePage();
	}
	
	@Test(priority = 1)
	@Description("Verify Login Header is present on the Login Page")
	public void loginPageHeaderTest() {
		Assert.assertEquals(loginPage.getLoginPageIdentifier(), Constants.LOGIN_PAGE_HEADER);
	}
	
	@Test(priority = 2)
	public void loginPageUrlTest() {
		String actualUrl = loginPage.getLoginPageUrl();
		Assert.assertTrue(actualUrl.contains(Constants.LOGIN_PAGE_URL_FRACTION));
	}
	
	@Step("Verify Register Link is present on the login page")
	@Test(priority = 3)
	public void isRegisterNowLinkExistTest() {
		assertTrue(loginPage.isRegisterNowLinkExist());
	}
	
	@Step("Login to the application with valid credentials")
	@Test(priority = 4)
	public void loginTest() {	
		homePage = loginPage.doLogin(uname, pwd);
		assertTrue(loginPage.isLoginSuccess());
		assertTrue(homePage.isUserLoggedIn(uname));
		assertTrue(loginPage.isLogoutBtnVisible());
	}

}
