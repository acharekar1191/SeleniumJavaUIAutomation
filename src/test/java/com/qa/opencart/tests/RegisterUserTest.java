package com.qa.opencart.tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ExcelUtil;

public class RegisterUserTest extends BaseTest{
	
	String new_user,new_pwd;
	
	@BeforeClass
	public void registerPageSetup() {
		registerPage = homePage.navigateToRegisterPageFromHomePage();
		new_user = prop.getProperty("newuser");
		new_pwd = prop.getProperty("password");
	}	
	
	@Test(priority = 1)
	public void registerUrlTest() {
		String actualUrl = registerPage.getRegisterPageUrl();
		System.out.println("Actual URL : "+actualUrl);
		assertTrue(actualUrl.contains(Constants.REGISTER_PAGE_URL_FRACTION),"URL missmatch, Actual URL: "+actualUrl);			
	}
	
	@Test(priority = 2)
	public void registerPageHeaderTest() {
		String actualHeader = registerPage.getRegisterPageHeader();
		assertTrue(actualHeader.equals(Constants.REGISTER_PAGE_HEADER),"Expected header to contain: "+Constants.REGISTER_PAGE_HEADER
		+ " but found: "+actualHeader);
	}
	
	@Test(priority = 3)
	public void userRegistrationE2ETest() {
		loginPage = registerPage.performRegistration(new_user, new_pwd, true);
		String lastGeneratedUser = loginPage.getLastRegisteredUsername();
		boolean regSuccess = registerPage.isRegistrationSuccess();
		
		//Here hard assert since without reg we can't proceed for login.
		assertTrue(regSuccess, "Registration failed, cannot proceed with login");
		
		softAssert.assertTrue(loginPage.getLoginPageUrl().contains(Constants.LOGIN_PAGE_URL_FRACTION));	
		
		homePage = loginPage.doLogin(lastGeneratedUser, new_pwd);
		
		softAssert.assertTrue(homePage.isUserLoggedIn(lastGeneratedUser));
		softAssert.assertAll();		
	}
	
	

	/** Using Data Provider:
	 * 
	 * public Object[][] getDataForRegistration(){
		return new Object[][] {
			{"dp1_","test12345"},
			{"dp2_","test12345"},
		};	
	}*/
	
	/**
	 * Using Excel sheet in Data Provider.
	 * @return
	 */
	@DataProvider
	public Object[][] getDataForRegistration(){
		Object[][] data = ExcelUtil.getData("register");
		return data;
	}
	
	//@Test(priority = 4,dataProvider = "getDataForRegistration")
	public void userRegistrationE2ETestWithDataProvider(String dpName,String dpPwd) {
		loginPage = registerPage.performRegistration(dpName, dpPwd, true);
		String lastGeneratedUser = loginPage.getLastRegisteredUsername();
		boolean regSuccess = registerPage.isRegistrationSuccess();
		
		//Here hard assert since without reg we can't proceed for login.
		assertTrue(regSuccess, "Registration failed, cannot proceed with login");
		
		softAssert.assertTrue(loginPage.getLoginPageUrl().contains(Constants.LOGIN_PAGE_URL_FRACTION));	
		
		homePage = loginPage.doLogin(lastGeneratedUser, dpPwd);
		softAssert.assertTrue(homePage.isUserLoggedIn(lastGeneratedUser));
		loginPage.logout();
		homePage.navigateToRegisterPageFromHomePage();
		softAssert.assertAll();		
	}
}
