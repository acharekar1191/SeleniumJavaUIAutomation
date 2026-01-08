package com.qa.opencart.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;


public class RegistrationPage {
	
	private WebDriver driver;
	private ElementUtil eu;
	private By username = By.id("username");
	private By password = By.id("password");
	private By confirmPassword = By.id("confirmPassword");
	private By registerNowBtn = By.xpath("//button[text()='Register Now']");
	private By registerHeader = By.xpath("//h2[text()='Register']");
	private By regSuccessMsg = By.xpath("//div[@id='notistack-snackbar']");
	private By registerLink = By.linkText("Register now");
	
	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
	}
	
	public String getRegisterPageUrl() {
		return eu.waitForUrlContains(Constants.REGISTER_PAGE_URL_FRACTION, 5);
	}
	
	public String getRegisterPageHeader() {
		return eu.getText(registerHeader);
	}
	
	public boolean isRegisterPageHeaderExist() {
		return eu.isElementDisplayed(registerHeader);
	}
	
	public void clickRegisterNowLink() {
		if(eu.isElementDisplayed(registerLink)) {
			eu.doClick(registerLink);
		}
	}
	
	public LoginPage performRegistration(String uname,String pwd,boolean isUnameDynamic) {
		String newUsername;
		if(isUnameDynamic) {
			newUsername = eu.getDynamicUsername(uname);
		}else {
			newUsername = uname;
		}		
		eu.waitForVisibilityOfElement(username, 0).sendKeys(newUsername);
		eu.doSendKeys(password, pwd);
		eu.doSendKeys(confirmPassword, pwd);;
		eu.doClick(registerNowBtn);
		LoginPage lp = new LoginPage(driver);
		lp.setLastRegisteredUsername(newUsername);   //Setting up the newly generated username, so that in E2E flow we can use it.
		return lp;
	}
	
	public boolean isRegistrationSuccess() {
		return eu.getText(regSuccessMsg).trim().equals(Constants.REGISTER_SUCCESS_MSG);
	}

}
