package com.qa.opencart.tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

public class LoginPageNegativeTest extends BaseTest{
	
	@BeforeClass
	public void loginPageSetup() {
		loginPage = homePage.navigateToLoginPageFromHomePage();
	}
	
	@DataProvider
	public Object[][] getInvalidCreds(){
		return new Object[][] {
			{"kalyani11","test1234",Constants.INCORRECT_PASSWORD_MSG},
			{"","",Constants.EMPTY_USERNAME_MSG},
			{"","test@123",Constants.EMPTY_USERNAME_MSG},
			{"hello123","",Constants.EMPTY_PASSWORD_MSG},
			{"notexist123","test@123",Constants.USERNAME_NOT_EXISTS_MSG}
		};
	}
	
	@Test(dataProvider = "getInvalidCreds")
	public void loginWithInvalidCredentials(String uname,String pwd,String expectedMsg) {
		loginPage.doInvalidLogin(uname, pwd);
		String actualMsg = loginPage.getLoginToastMessage(expectedMsg);
		assertEquals(actualMsg, expectedMsg, "Login validation message missmatch");
	}

}
