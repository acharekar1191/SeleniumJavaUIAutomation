package com.qa.opencart.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.JavaScriptUtil;

public class SearchPage {
	
	private WebDriver driver;
	private ElementUtil eu;
	private JavaScriptUtil jsUtil;
	
    private By searchResult = By.xpath("//p[contains(@class, 'css-yg30e6')]");
    			
	public SearchPage(WebDriver driver) {
		this.driver = driver;
		eu = new ElementUtil(driver);
		jsUtil = new JavaScriptUtil(driver);
	}
	
	public By addToCartBtnXpath(String productName) {
		return By.xpath("//p[normalize-space()='"+productName+"']"
				+ "/ancestor::div[contains(@class,'css-sycj1h')]/descendant::button[text()='Add to cart']");
	}
	
	public By productPriceXpath(String productName){
		return By.xpath("//p[normalize-space()='"+productName+"']"
				+ "/ancestor::div[contains(@class,'css-sycj1h')]/descendant::p[contains(@class,'css-uickyz')]");
	}

	public List<String> getSearchResultList() {
		List<WebElement> elements = eu.waitForVisibilityOfElements(searchResult, 5);
		System.out.println("Size :" +elements.size());
		List<String> productsList = new ArrayList<String>();	
		for(WebElement e : elements) {
			String text = e.getText().trim();
			if(!text.isEmpty()) {
				productsList.add(text);		
			}
		}
		return productsList;
	}
	
	public int getSearchResultCount() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    List<WebElement> elems = new WebDriverWait(driver, Duration.ofSeconds(5))
	            .until(ExpectedConditions.refreshed(
	                ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResult)
	            ));
	    return elems.size();
	}	
	
	
	public String getPriceOfTheProduct(String productName) {
		return eu.getElement(productPriceXpath(productName)).getText();
	}
	
	public int getRatingOfTheProduct(String productName) {
		List<WebElement> starIcons = driver.findElements(By.xpath("//p[normalize-space()='"+productName+"']/ancestor::div[contains(@class,'css-sycj1h')]"
				+ "/descendant::span[contains(@class,'MuiRating-decimal')]"
				+ "[.//*[local-name()='svg' and @data-testid='StarIcon']]"));	
		return starIcons.size();
	}
	
	//p[normalize-space()='Tan Leatherette Weekender Duffle']/ancestor::div[contains(@class,'css-sycj1h')]/descendant::span[contains(@class,'MuiRating-decimal')][.//*[local-name()='svg' and @data-testid='StarIcon']]
	public By sizeDrodownXpath(String productName) {
		return By.xpath("//p[normalize-space()='"+productName+"']"
				+ "/ancestor::div[contains(@class,'css-sycj1h')]/descendant::select[@id='uncontrolled-native']");
	}
	
	public List<String> getSizesAvailable(String productName) {	
		List<String> sizes = new ArrayList<>();
		Select select = new Select(eu.getElement(sizeDrodownXpath(productName)));
		List<WebElement> options = select.getOptions();
		for(WebElement e : options) {
			String text = e.getText();
			sizes.add(text);
		}
		return sizes;		
	}
	
	public By sizeChartBtnXpath(String productName) {
		return By.xpath("//p[normalize-space()='"+productName+"']/ancestor::div[contains(@class,'css-sycj1h')]"
				+ "/descendant::button[text()='Size chart']");
	}
	
	public By sizeChartHeaderXpath() {
		return By.xpath("//p[normalize-space()=\"Shoe's Size Chart\"]");
	}
	
	public boolean isChartVisible() {
		return eu.isElementDisplayed(sizeChartHeaderXpath());
	}
	
	public void clickSizeChart(String productName) {
		eu.getElement(sizeChartBtnXpath(productName)).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isAddToCartBtnEnabled(String productName) {
		return eu.getElement(addToCartBtnXpath(productName)).isEnabled();
	}
	
	public void selectSize(String productName,String value) {
		eu.selectByVisibleText(sizeDrodownXpath(productName), value);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public CartPage clickAddToCartButton(String productName) {
		eu.doClick(addToCartBtnXpath(productName));
		jsUtil.scrollPageUp();
		return new CartPage(driver);
	}
	
	public void closeSizeChartUsingEsc() {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public CartPage getCart() {
        return new CartPage(driver);
    }
	
	
}
