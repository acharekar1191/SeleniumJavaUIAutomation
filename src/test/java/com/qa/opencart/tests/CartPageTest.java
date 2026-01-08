package com.qa.opencart.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

public class CartPageTest extends BaseTest{
	String productName,seachKeyword;
	
	@BeforeClass
	public void cartPageSetup() {
		loginPage = homePage.navigateToLoginPageFromHomePage();
		homePage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		productName = prop.getProperty("productName");
		seachKeyword = prop.getProperty("seachKeyword");
	}
	
	@BeforeMethod
	public void resetCart() {
		searchPage = homePage.searchProduct(productName);
		cartPage = searchPage.getCart();
		if(!cartPage.isCartEmpty()) { // Checking if cart does not have any items, before each test
			cartPage.ensureCartIsEmpty(productName);  //resets Qty to 0 so that product is removed from cart.
		}
	}
	
	@Test(description = "verifyProductAddedToCart and assert its details",priority = 0)
	public void addProductToCartTest() {
		searchPage = homePage.searchProduct(seachKeyword);
		searchPage.selectSize(productName, "8");
		cartPage = searchPage.clickAddToCartButton(productName);
		
		assertTrue(cartPage.isProductPresentInCart(productName));
		assertEquals(cartPage.getDisplayedQuantity(productName),1);
		assertEquals(cartPage.getProductPriceFromCart(productName), Constants.Roadster_price);
		assertEquals(cartPage.getOrderTotal(),Constants.Roadster_price);
		assertTrue(cartPage.isCheckOutBtnDisplayed());
		
	}
	
		@Test(description = "verifyUserCanIncreaseProductQuantity",priority = 1)
	public void addProductQtyTest() {
		searchPage = homePage.searchProduct(productName);
		cartPage = searchPage.clickAddToCartButton(productName);
		
		assertTrue(cartPage.isProductPresentInCart(productName));
		
		cartPage.setProductQuantity(productName, 2); //Updates qty to 2 using add
		int unitPrice = Integer.parseInt(cartPage.getProductPriceFromCart(productName).replace("$", ""));
		int expectedOrderTotal = unitPrice * 2;
		int actualOrderTotal = Integer.parseInt(cartPage.getOrderTotal().replace("$", ""));
		
		assertEquals(actualOrderTotal, expectedOrderTotal);
	}
		
}
