package com.qa.opencart.tests;



import static org.testng.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.Constants;

public class CheckoutPageTest extends BaseTest{
	String productName,seachKeyword;
	
	public static final Logger log = LogManager.getLogger(CheckoutPageTest.class);
	
	@BeforeClass
	public void checkoutPageSetup() {
		loginPage = homePage.navigateToLoginPageFromHomePage();
		homePage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		productName = prop.getProperty("productName");
		seachKeyword = prop.getProperty("seachKeyword");
	}
	
	@BeforeMethod
	public void ensureCartIsEmpty() {
	    cartPage = homePage.getCart();
	    if(!cartPage.isCartEmpty()) {
	    	cartPage.resetProductQuantity(productName, 0); //cleans the cart
	    }
	}
	
	
	@Test
	public void singleProductCheckoutTest() {
		searchPage = homePage.searchProduct(seachKeyword);
		
		cartPage = searchPage.clickAddToCartButton(productName);
		assertTrue(cartPage.isProductPresentInCart(productName));
		
		checkoutPage = cartPage.clickCheckout();	
		checkoutPage.addNewAddressIfNotExists(Constants.ADDRESS2);
		checkoutPage.selectTheAddress(Constants.ADDRESS2);
		checkoutPage.clickPlaceOrder();
		
		Assert.assertTrue(checkoutPage.isOrderPlaced());
		Assert.assertTrue(checkoutPage.validateOrderSuccessPageUrl());
	}
	
}
