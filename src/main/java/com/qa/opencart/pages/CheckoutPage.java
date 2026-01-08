package com.qa.opencart.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;

public class CheckoutPage {
	
	private WebDriver driver;
	private ElementUtil eu;
    private By itemsInCart = By.xpath("//div[@class='cart MuiBox-root css-0']/div[@class='MuiBox-root css-0']");
	
	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
	}

	
	
	

}
