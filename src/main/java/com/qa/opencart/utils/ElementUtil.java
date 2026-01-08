package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.factory.DriverFactory;

public class ElementUtil {
	
	//Never create WebDriver instance as static since we cannot achieve parallel execution .
		//Also when object is created then we can pass driver instance and use it overall the execution.
		private WebDriver driver;
		private JavaScriptUtil jsUtil;
		
		//Constructor.
		public ElementUtil(WebDriver driver) {
			this.driver = driver;
			jsUtil = new JavaScriptUtil(driver);
		}
		
		/**
		 * Whenever getElement is called that time on screen you can see the locator is highlighted 
		 * @param locator
		 * @return
		 */
		
		
		public WebElement getElement(By locator) {
			WebElement element = driver.findElement(locator);
			if(Boolean.parseBoolean(DriverFactory.highlight)){
				jsUtil.flash(element);
			}
			return element;
		}
		
		public void doSendKeys(By locator,String text) {
			getElement(locator).sendKeys(text);
		}
		
		public String getText(By locator) {
			return getElement(locator).getText(); 
		}
		
		public void doClick(By locator) {
			getElement(locator).click();
		}
		
		public List<WebElement> getElements(By locator) {
			return driver.findElements(locator);
		}
		
		public List<String> getAllElementsText(By locator){
			List<WebElement> elements = waitForVisibilityOfElements(locator, 5);
			System.out.println("Size :" +elements.size());
			List<String> textList = new ArrayList<String>();
			
			for(WebElement e : elements) {
				String text = e.getText().trim();
				if(!text.isEmpty()) {
					textList.add(text);		
				}
			}
			return textList;
		}
		
		/**************Wait utils ********************/
		
		public WebElement waitForVisibilityOfElement(By Locator, int timeout) {
			return new WebDriverWait(driver, Duration.ofSeconds(timeout))
			.until(ExpectedConditions.visibilityOfElementLocated(Locator));		
		}
		
		public List<WebElement> waitForVisibilityOfElements(By Locator, int timeout) {
			return new WebDriverWait(driver, Duration.ofSeconds(timeout))
			.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Locator));		
		}
		
		public WebElement waitForElementToBeClickable(By Locator, int timeout) {
			return new WebDriverWait(driver, Duration.ofSeconds(timeout))
			.until(ExpectedConditions.elementToBeClickable(Locator));
		}
		
		public String waitForUrlToBe(String expectedUrl, int timeout) {
			try {
				 new WebDriverWait(driver, Duration.ofSeconds(timeout))
						.until(ExpectedConditions.urlToBe(expectedUrl));
			} catch (TimeoutException e) {
				System.out.println("WARNING → URL did not match " +expectedUrl );
			}
			return driver.getCurrentUrl();
		}
		
		public String waitForUrlContains(String urlFraction,int timeout) {
			try {
				new WebDriverWait(driver, Duration.ofSeconds(timeout))
						.until(ExpectedConditions.urlContains(urlFraction));				
			} catch (TimeoutException e) {
				System.out.println("WARNING → URL did not contain: " + urlFraction);
			}
			return driver.getCurrentUrl();
		}
		
		public boolean titleContains(String titleFraction,int timeout) {
			try {
				return new WebDriverWait(driver, Duration.ofSeconds(timeout))
						.until(ExpectedConditions.titleContains(titleFraction));
			} catch (TimeoutException e) {
				return false;
			}
		}
		
		public String waitForTitleIs(String expected, int timeout) {
			try {
				new WebDriverWait(driver, Duration.ofSeconds(timeout))
						.until(ExpectedConditions.titleIs(expected));
			} catch (TimeoutException e) {
				System.out.println("WARNING → Title did not match " +expected);
			}
			return driver.getTitle();
		}
		
		//Waits for Alert
		public Alert waitForAlert(int timeout) {
			return new WebDriverWait(driver,Duration.ofSeconds(timeout))
			.until(ExpectedConditions.alertIsPresent());
		}
		
		public void acceptAlert(int timeout) {
			waitForAlert(timeout).accept();
		}
		
		public void dissmissAlert(int timeout) {
			waitForAlert(timeout).dismiss();
		}
		
		public String getTextFromAlert(int timeout) {
			return waitForAlert(timeout).getText();
		}
		
		
		//DropDown Utils
		public void selectByVisibleText(By locator,String text) {
			Select select = new Select(getElement(locator));
			select.selectByVisibleText(text);
		}
		
		public void selectByIndex(By locator,int index) {
			Select select = new Select(getElement(locator));
			select.selectByIndex(index);
		}
		
		public void selectByValue(By locator,String value) {
			Select select = new Select(getElement(locator));
			select.selectByValue(value);
		}
		
		public String firstSelectedOption(By locator) {
			Select select = new Select(getElement(locator));
			WebElement el = select.getFirstSelectedOption();
			return el.getText();
		}
		
		public List<String> getDropdownOptions(By locator){
			Select select = new Select(getElement(locator));
			List<WebElement> optionsList = select.getOptions();
			List<String> dropDownOptionsList = new ArrayList<String>();
			
			for(WebElement e : optionsList) {
				String text = e.getText().trim();
				if(!text.isEmpty()) {
					dropDownOptionsList.add(text);
				}
			}
			return dropDownOptionsList;		
		}
		
		//Select value from DropDown without using Select methods.
		public void doSelectValueFromDropDown(By locator,String value) {
			Select select = new Select(getElement(locator));
			List<WebElement> optionsList = select.getOptions();
			for(WebElement e:optionsList) {
				String text = e.getText();
				if(text.equals(value)) {
					e.click();
					break;
				}
			}
		}
		
		
		/**************Action Utils ********************/
		
		public void moveToSubMenu(By menu, By submenu1) {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(menu)).perform();
			getElement(submenu1).click();		
		}
		
		public void moveToSubMenu(By menu, By submenu1, By submenu2) {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(menu)).perform();
			action.moveToElement(getElement(submenu1)).perform();	
			getElement(submenu2).click();		
		}
		
		public void moveToSubMenu(By menu, By submenu1, By submenu2,By submenu3) {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(menu)).perform();
			action.moveToElement(getElement(submenu1)).perform();	
			action.moveToElement(getElement(submenu2)).perform();
			getElement(submenu3).click();		
		}
		
		//Sending text using Actions class object
		public void doActionsSendkeys(By locator,String text) {
			Actions act = new Actions(driver);
			act.sendKeys(getElement(locator),text).perform();
		}
		
		//Click on element using Actions class object
		public void doActionsClick(By locator) {
			Actions act = new Actions(driver);
			act.click(getElement(locator)).perform();
		}
		
		public boolean isElementDisplayed(By locator) {
			return getElement(locator).isDisplayed();
		}
		
		public void doClearInputBox(By locator) {
			getElement(locator).clear();
		}
		
		public String getDynamicUsername(String base) {
			return base + "_" + System.currentTimeMillis();
		}
		
		public void clearInputField(By locator) {
		    WebElement element = waitForVisibilityOfElement(locator, 5);
		    element.click();
		    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
		        element.sendKeys(Keys.COMMAND + "a");
		    } else {
		        element.sendKeys(Keys.CONTROL + "a");
		    }
		    element.sendKeys(Keys.DELETE);
		}
		
		public String waitForToastMessage(By toastLocator, String expectedText, int timeout) {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		    wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(
		            toastLocator, expectedText));

		    return driver.findElement(toastLocator).getText().trim();
		}
		
		public boolean waitForTextToBePresent(By locator, String expectedText, int timeout) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
		    } catch (TimeoutException e) {
		        return false;
		    }
		}

}
