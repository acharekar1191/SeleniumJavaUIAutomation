package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {
	
	public WebDriver driver;
	public Properties prop;
	public FileInputStream fis;
	public static String highlight;
	public OptionsManager optionsManager;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	
	public static final Logger log = LogManager.getLogger(DriverFactory.class);
	
	/**
	 * This method is used to initialize the browser based on browsername.
	 * This method will take care of local and remote execution
	 * @param browserName
	 * @return
	 */
	
	//Pass By Reference
	public WebDriver init_driver(Properties prop) {
		String browserName = prop.getProperty("browser").trim();
		highlight = prop.getProperty("highlight").trim();
		optionsManager = new OptionsManager(prop);
		log.info("Running tests on Browser : "+browserName);
		
		if(browserName.equalsIgnoreCase("chrome")) {
			//driver = new ChromeDriver(optionsManager.getChromeOptions());
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			
		}else if(browserName.equalsIgnoreCase("firefox")) {
			//driver = new FirefoxDriver(optionsManager.getFireFoxOptions());
			tlDriver.set(new FirefoxDriver(optionsManager.getFireFoxOptions()));
			
		}else if(browserName.equalsIgnoreCase("safari")) {
			//driver = new SafariDriver();
			tlDriver.set(new SafariDriver());
		}else {
			log.info("Please enter the right browser: "+browserName);
		}	
		
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		getDriver().get(prop.getProperty("url").trim());
		
		return getDriver();
	}
	
	
	/**
	 * This method is used to initialize properties on the basis of given env.
	 * @return
	 */
	public Properties init_prop() {
		prop = new Properties();
		String filePath;
	//mvn clean install -Denv="qa" > at runtime env value will be fetched and as per that properties file will be loaded.
	//mvn clean install   >> without env value also we can run mvn command to compile and run the tests.	
		String envName = System.getProperty("env");
		log.info("Running on Env : "+envName);
		
		if (envName == null) {
			log.info("No env is specified so executing tests on QA.");
			filePath = "./src/test/resources/config/qa.config.properties";
			try {
				fis = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				switch (envName.toLowerCase()) {
				case "qa":
					filePath = "./src/test/resources/config/qa.config.properties";
					fis = new FileInputStream(filePath);
					break;
				case "stage":
					filePath = "./src/test/resources/config/stage.config.properties";
					fis = new FileInputStream(filePath);
					break;
				case "uat":
					filePath = "./src/test/resources/config/uat.config.properties";
					fis = new FileInputStream(filePath);
					break;
				case "prod":
					filePath = "./src/test/resources/config/prod.config.properties";
					fis = new FileInputStream(filePath);
					break;
				default:
					log.info("Please pass the correct env : " + envName);
					break;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;		
	}
	
	/**
	 * This will return the thread local copy of the WebDriver(driver).
	 * @return
	 */	
	public static WebDriver getDriver()
	{
		return tlDriver.get();
	}
	
	/**
	 * This method will take the screenshot and return the path of the file to showcase in the report.
	 * @param testName
	 * @return
	 */
	public static String takeScreenshot(String testName) {
        String path = System.getProperty("user.dir")
                + "/screenshots/" +getTimeStamp()+"_"+testName + ".png";
        File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        File dest = new File(path);
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
	
	private static String getTimeStamp() {
        return new SimpleDateFormat("yy_MM_dd_HH_mm")
                .format(new Date());
    }

	
}
