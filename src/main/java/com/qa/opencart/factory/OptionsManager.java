package com.qa.opencart.factory;

import java.util.Properties;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class OptionsManager {
	private Properties prop;
	private ChromeOptions co;
	private FirefoxOptions fo;
	
	public OptionsManager(Properties prop) {
		this.prop = prop;
	}
	
	public ChromeOptions getChromeOptions() {
		co = new ChromeOptions();
		if(isHeadless()) {
			co.addArguments("--headless=new");
			co.addArguments("--window-size=1920,1080");
		}
		if(Boolean.parseBoolean(prop.getProperty("incognito"))) {
			co.addArguments("--incognito");
		}
		return co;
	}

	public FirefoxOptions getFireFoxOptions() {
		fo = new FirefoxOptions();
		if(isHeadless()) {
			fo.addArguments("--headless");
			fo.addArguments("--width=1920");
			fo.addArguments("--height=1080");
		}
		if(Boolean.parseBoolean(prop.getProperty("incognito"))) {
			fo.addArguments("--incognito");
		}
		return fo;
	}

	/**
	 * -Dheadless system property (used by CI) takes precedence over the
	 * env config file's headless value, so CI can force headless without
	 * changing the local dev default.
	 */
	private boolean isHeadless() {
		String override = System.getProperty("headless");
		if(override != null) {
			return Boolean.parseBoolean(override);
		}
		return Boolean.parseBoolean(prop.getProperty("headless"));
	}

}
