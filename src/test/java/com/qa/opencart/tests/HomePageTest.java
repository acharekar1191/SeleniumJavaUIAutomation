package com.qa.opencart.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

public class HomePageTest extends BaseTest{
	
	@Test(priority = 0)
	public void appLogoValidationTest() {
		assertTrue(homePage.isAppLogoPresent());
	}
	
	@Test(priority = 1)
	public void homePageUrlTest() {
		String url = homePage.getHomePageUrl();
		assertTrue(url.equals(Constants.HOME_PAGE_URL));
	}
	
	@Test(priority = 2)
	public void searchBoxValidationTest() {
		assertTrue(homePage.isSearchBoxPresent());
	}
	
	@Test(priority = 3)
	public void footerListTest() {
		List<String> actualList = homePage.getFooterList();
		assertEquals(actualList, Constants.FOOTER_LISTS);
	}
	
	@Test(priority = 4)
	public void registerBtnValidationTest() {
		assertTrue(homePage.isRegisterBtnPresent());
	}
	
	@Test(priority = 5)
	public void loginBtnValidationTest() {
		assertTrue(homePage.isLoginBtnPresent());
	}
	
}
