package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;
//import com.qa.opencart.utils.JavaScriptUtil;

public class CartPage {
	
	private WebDriver driver;
	private ElementUtil eu;
	//private JavaScriptUtil jsUtil;
	
	private By checkoutBtn = By.xpath("//button[text()='Checkout']");
	private By orderTotal = By.xpath("//div[@data-testid='cart-total']");
	private By emptyCart = By.xpath("//div[@class='cart MuiBox-root css-0']");
	
	public CartPage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
		//jsUtil = new JavaScriptUtil(driver);
		
	}
	
	public boolean isCheckOutBtnDisplayed() {
		return eu.isElementDisplayed(checkoutBtn);
	}
	
	public String getOrderTotal() {
		return eu.getText(orderTotal);
	}
	
	public By addQtyBtn(String productName) {
		return By.xpath("//div[normalize-space()='"+productName+"']/ancestor::div[@class='MuiBox-root css-zgtx0t']"
				+ "/descendant::button[.//*[local-name()='svg' and @data-testid='AddOutlinedIcon']]");
	}
	
	public By minusQtyBtn(String productName) {
		return By.xpath("//div[normalize-space()='"+productName+"']/ancestor::div[@class='MuiBox-root css-zgtx0t']"
				+ "/descendant::button[.//*[local-name()='svg' and @data-testid='RemoveOutlinedIcon']]");
	}
	
	public By productQtyXpath(String productName) {
		return By.xpath("//div[normalize-space()='"+productName+"']/ancestor::div[@class='MuiBox-root css-zgtx0t']"
				+ "/descendant::div[@data-testid='item-qty']");
	}

	public By productPriceInCart(String productName) {
		return By.xpath("//div[text()='"+productName+"']/ancestor::div[@class='MuiBox-root css-1gjj37g']"
				+ "/descendant::div[@class='MuiBox-root css-1wppqd7']");
	}
	
	/**
	 * Sets the product quantity to targetQty using '+' button
	 * @param productName
	 * @param targetQty
	 */
	public void setProductQuantity(String productName, int targetQty) {
		int currentQty = Integer.parseInt(eu.getElement(productQtyXpath(productName)).getText());	
		while(currentQty < targetQty) {
			eu.waitForElementToBeClickable(addQtyBtn(productName), 5).click();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentQty++;
		}
	}
	
	/***
	 * Resets product quantity to targetQty using '-' button
	 * @param productName
	 * @param targetQty
	 */
	public void resetProductQuantity(String productName, int targetQty) {
		int currentQty = Integer.parseInt(eu.getElement(productQtyXpath(productName)).getText());	
		while(currentQty > targetQty) {
			eu.waitForElementToBeClickable(minusQtyBtn(productName), 5).click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentQty--;
		}
	}
	
	public void ensureCartIsEmpty(String productName) {
		if(isProductPresentInCart(productName)) {
			resetProductQuantity(productName, 0); //Updates qty to 0 using minus
		}
	}
	
	public int getDisplayedQuantity(String productName) {
	    return Integer.parseInt(
	        eu.getElement(productQtyXpath(productName)).getText()
	    );
	}
	
	public boolean isProductPresentInCart(String productName) {
		return eu.isElementDisplayed(By.xpath("//div[text()='"+productName+"']"));
	}
	
	public String getProductPriceFromCart(String productName) {
		return eu.getText(productPriceInCart(productName));
	}
	
	public boolean isCartEmpty() {
		return eu.getElements(emptyCart).size()==0 ;
	}
	
	public CheckoutPage clickCheckout() {
		eu.doClick(checkoutBtn);
		return new CheckoutPage(driver);
	}
	
}
