package com.qa.opencart.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

public class CheckoutPage {
	
	private WebDriver driver;
	private ElementUtil eu;
    private By itemsInCart = By.xpath("//div[@class='cart MuiBox-root css-0']/div[@class='MuiBox-root css-0']");
    private By addNewAddressBtn = By.id("add-new-btn");
    private By shippingHeader = By.xpath("//h4[text()='Shipping']");
    private By paymentHeader = By.xpath("//h4[text()='Payment']");
    private By addressBox = By.xpath("//textarea[@placeholder='Enter your complete address']");
    private By addBtn = By.xpath("//button[text()='Add']");
    private By cancelBtn = By.xpath("//button[text()='Cancel']");
    private By placeHolderBtn = By.xpath("//button[text()='PLACE ORDER']");
    private By errPopup = By.id("notistack-snackbar");
    private By orderTotal = By.xpath("//div[@data-testid='cart-total']");
    private By grandTotal = By.xpath("//div[contains(@class,'css-1suk1xd')]/p[2]");
    private By addressRadioBtn = By.xpath("//input[@name='address']");
    private By deleteAddress = By.xpath("//p[text()=' Delete ']");
    
	
	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
	}

	public boolean validateCurrentUrl() {
		return driver.getCurrentUrl().contains(Constants.CHECKOUT_PAGE_URL_FRACTION);
	}
	
	public void addNewAddress() {
		eu.waitForElementToBeClickable(addNewAddressBtn, 5).click();
		eu.doSendKeys(addressBox, Constants.ADDRESS1);
		eu.doClick(addBtn);
	}
	
	public void selectTheAddress() {
		eu.doClick(addressRadioBtn);
	}
	
	public void validateProduct() {
		
	}
	
	public void clickPlaceOrder() {
		eu.waitForElementToBeClickable(placeHolderBtn, 5).click();		
	}
	
	public boolean isOrderPlaced() {
	    return eu.waitForTextToBePresent(errPopup,"Order placed successfully!",10);
	}
	
	public boolean validateOrderSuccessPageUrl() {
		return driver.getCurrentUrl().contains(Constants.THANKS_PAGE_URL_FRACTION);
	}

}
