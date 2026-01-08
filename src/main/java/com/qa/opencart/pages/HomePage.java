package com.qa.opencart.pages;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

public class HomePage {
	
	private WebDriver driver;
	private ElementUtil eu;
	
	private By searchBox = By.name("search");
	private By usernameLable = By.xpath("//p[@class='username-text']");
	private By logoutBtn = By.xpath("//button[text()='Logout']");
	private By logo = By.xpath("(//img[@alt='QKart-icon'])[1]");
	private By footerLinks = By.xpath("//p[@class='footer-text']/a");
	private By searchResult = By.xpath("//p[contains(@class, 'css-yg30e6')]");
	private By registerBtnFromHomePage = By.xpath("//button[text()='Register']");
	private By loginBtnFromHomePage = By.xpath("//button[text()='Login']");
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
	}
	
	public String getHomePageUrl() {
		return eu.waitForUrlToBe(Constants.HOME_PAGE_URL, 5);
	}
	
	public boolean isSearchBoxPresent() {
		return eu.isElementDisplayed(searchBox);
	}
	
	/**
	 * After clicking on search new searchResult page is appeared, so returning searchPage object.
	 * @param productName
	 * @return
	 */
	public SearchPage searchProduct(String productName) {
		if(isSearchBoxPresent()) {
			eu.doClearInputBox(searchBox);
			eu.doSendKeys(searchBox, productName);
			//explicit wait is not working getting stale ele exception so added Thread.sleep()
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 // wait for results to load
	        eu.waitForVisibilityOfElements(searchResult, 10);
			return new SearchPage(driver);
		}
		return null;
	}
	
	public boolean isAppLogoPresent() {
		return eu.isElementDisplayed(logo);
	}
	
	public boolean isUserLoggedIn(String uname) {
		try {
			String actual = eu.waitForVisibilityOfElement(usernameLable, 5)
					.getText()
					.trim();
			return actual.equals(uname.trim());
		}catch(TimeoutException te) {
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean isLogoutLinkPresent() {
		return eu.isElementDisplayed(logoutBtn);
	}
	
	public List<String> getFooterList() {		
		List<String> textList = eu.getAllElementsText(footerLinks);		
		return textList;
	}
	
	public boolean isRegisterBtnPresent() {
		return eu.isElementDisplayed(registerBtnFromHomePage);
	}
	
	public boolean isLoginBtnPresent() {
		return eu.isElementDisplayed(loginBtnFromHomePage);
	}
	
	public RegistrationPage navigateToRegisterPageFromHomePage() {
		eu.waitForElementToBeClickable(registerBtnFromHomePage, 5);
		eu.doClick(registerBtnFromHomePage);
		return new RegistrationPage(driver);
	}
	
	public LoginPage navigateToLoginPageFromHomePage() {
		eu.waitForVisibilityOfElement(loginBtnFromHomePage, 5).click();
		return new LoginPage(driver);
	}
	
	public CartPage getCart() {
		return new CartPage(driver);
	}

}
