package com.qa.opencart.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

public class SearchPageTest extends BaseTest{
	private String uname,pwd;
	private String productName;
	private String seachKeyword, commonSearchKey;
	
	@BeforeClass
	public void searchPageSetup() {
		uname = prop.getProperty("username");
		pwd = prop.getProperty("password");
		productName = prop.getProperty("productName");
		seachKeyword = prop.getProperty("seachKeyword");
		commonSearchKey = prop.getProperty("commonSearchKey");
		
		loginPage = homePage.navigateToLoginPageFromHomePage();
		homePage = loginPage.doLogin(uname, pwd); 
	}
		
	@Test(priority = 1)
	public void searchProductCountTest() {
		searchPage = homePage.searchProduct(commonSearchKey);  //When we trigger searchProduct() method searchPage object is created.
		int actualProductCount = searchPage.getSearchResultCount();
		assertEquals(actualProductCount, 2);
	}	
	
	@Test(priority = 2)
	public void searchProductListTest() {
		searchPage = homePage.searchProduct(commonSearchKey);
		List<String> list = searchPage.getSearchResultList();
		assertEquals(list, Constants.PRODUCT_LISTS_SHOES);
	}
	
	@Test(description = "To verify product details provided name of the product",priority = 3)
	public void productMetadataTest() {
		searchPage = homePage.searchProduct(seachKeyword);
		String productPrice = searchPage.getPriceOfTheProduct(productName);
		assertEquals(productPrice, Constants.Roadster_price);
		
		int rating = searchPage.getRatingOfTheProduct(productName);
		assertEquals(rating, 5);   // OR assertTrue(rating >= 4);
		
		List<String> sizesAvailable = searchPage.getSizesAvailable(productName);
		assertTrue(sizesAvailable.containsAll(Constants.expectedSizes));
		assertTrue(searchPage.isAddToCartBtnEnabled(productName));
		
		searchPage.clickSizeChart(productName);
		assertTrue(searchPage.isChartVisible());
		searchPage.closeSizeChartUsingEsc();
	}

}
