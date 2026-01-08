package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

public class LoginPage {
	
	private WebDriver driver;
	private ElementUtil eu;
	
	//1. Always maintain private By locators.
	private By username = By.id("username");
	private By password = By.id("password");
	private By loginBtn = By.xpath("//button[text()='Login to QKart']");
	private By registerNowLink = By.linkText("Register now");
	private By registerTitle = By.xpath("//h2[text()='Register']");
	private By loginPageHeader = By.xpath("//h2[text()='Login']");
	private By loginSuccessMsg = By.xpath("//div[@id='notistack-snackbar']");
	private By logoutBtn = By.xpath("//button[text()='Logout']");
	private By invalidLoginErrMsg = By.xpath("//div[@id='notistack-snackbar']");
	//Password is incorrect
	
	//2. Public constructor to initialise driver.
	public LoginPage(WebDriver driver){
		this.driver = driver;
		eu = new ElementUtil(driver);
	}
	
	private String lastRegisteredUsername;

	public String getLastRegisteredUsername() {
	    return lastRegisteredUsername;
	}
	
	public void setLastRegisteredUsername(String name) {
		this.lastRegisteredUsername = name;
	}
	
	public String getLoginPageIdentifier() {
		return eu.getText(loginPageHeader);
	}
	
	//3. public page actions
	public String getLoginPageTitle() {
		return eu.waitForTitleIs(Constants.LOGIN_PAGE_TITLE, 5);
	}
	
	public String getLoginPageUrl() {
		return eu.waitForUrlContains(Constants.LOGIN_PAGE_URL_FRACTION, 5);
	}
	
	public HomePage doLogin(String email,String pwd) {
		eu.waitForVisibilityOfElement(username, 5).sendKeys(email);
		eu.doSendKeys(password, pwd);
		eu.doClick(loginBtn);
		return new HomePage(driver);
	}
	
	public void doInvalidLogin(String email,String pwd) {
		eu.clearInputField(username);
		eu.doSendKeys(username, email);
		eu.clearInputField(password);
		eu.doSendKeys(password, pwd);
		eu.doClick(loginBtn);	
	}
	
	public boolean isRegisterNowLinkExist() {
		return eu.isElementDisplayed(registerNowLink);
	}
	
	public RegistrationPage navigateToRegisterPageFromLoginPage() {
		if (!isRegisterNowLinkExist()) {
	        throw new RuntimeException("Register link is not present on Login Page!");
	    }
	    eu.doClick(registerNowLink);
	    // Wait for Registration page to load
	    eu.waitForVisibilityOfElement(registerTitle, 5);
	    return new RegistrationPage(driver);
	}
	

	public boolean isLoginSuccess() {
		return eu.getText(loginSuccessMsg).trim().equals(Constants.LOGIN_SUCCESS_MSG);
	}
	
	public boolean isLogoutBtnVisible() {
		return eu.isElementDisplayed(logoutBtn);
	}
	
	public void logout() {
		eu.doClick(logoutBtn);
	}
	
	public String getLoginToastMessage(String expectedMsg) {
	    return eu.waitForToastMessage(invalidLoginErrMsg, expectedMsg, 5);
	}
}
