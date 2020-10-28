package mainCode;

/*
 * Problem Title: Hotel Availability Automation
 *
 * Description:
 * Find the number of hotels available in Mumbai with price between  Rs. 2000 to Rs. 3000 for 1 day in next week.
 * 
 * Search for hotels online to stay in Mumbai for below details:
 * 	1. Stay will for 2 days in next week.
 * 	2. Hotel price should be between Rs. 2000 to Rs. 3000 for 1 day.
 * 
 * The following code has been written to solve the above mentioned problem statement. All requirements
 * mentioned have been taken care of to the best of my abilities. Please read the README.txt file before
 * executing the program on your machine.
 * 
 * @author		Vatsal Srivastav
 * @date		09/30/2020
 * @version		1.0
 * @website		Trivago.com
 */



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class HotelAvailabilityAutomation{
	
	static WebDriver driver = null;			//Declaring driver as a global static variable.
	
	/*This method is used to initialize the driver based on the
	  browser that the user chooses.*/
	public static void setupDriver() throws IOException{
		
		//To read values from the properties file.
		Properties prop = new Properties();
		String propLocation = System.getProperty("user.dir") + "/mainCode/config.properties";
		FileInputStream ipt = new FileInputStream(propLocation);
		prop.load(ipt);
		ipt.close();
		
		//To get the selected browser from the properties file.
		String optedBrowser = prop.getProperty("browser").toLowerCase();
		
		//This if block is used to initialize Firefox driver.
		if(optedBrowser.equals("firefox")){
			
			//Getting location of Firefox driver from properties file.
			String firefoxLocation = prop.getProperty("driverLocationFirefox");
			
			System.setProperty("webdriver.gecko.driver", firefoxLocation);
			
			driver = new FirefoxDriver();
						
			driver.manage().window().maximize();	//Command used to maximize browser window.
		}
		
		//This else if block is used to initialize Chrome driver.
		else if(optedBrowser.equals("chrome")){
			
			//Getting location of Chrome driver from properties file.
			String chromeLocation = prop.getProperty("driverLocationChrome");

			System.setProperty("webdriver.chrome.driver", chromeLocation);
			
			driver = new ChromeDriver();
						
			driver.manage().window().maximize();	//Command used to maximize browser window.
		}
	} 
	
	/*This is the main code to run and execute the entire program.*/
	public static void main(String args[]) throws Exception{

		//Initializing WebDriver based on user choice from properties file.
		setupDriver();
		
		//Accessing SearchHotels module to input values in search fields.
		SearchHotels search = new SearchHotels();
		search.searchHotels(driver);
		
		//Accessing CountHotels module to count the number of hotels in given price range.
		CountHotels cnt = new CountHotels();
		int noOfHotels = cnt.countHotels(driver);
		
		//Closing the driver after execution is completed.
		driver.quit();
		
		//Printing the total number of hotels found in the given price range on the console.
		System.out.println("\nTOTAL NUMBER OF HOTELS FOUND WITHIN THE GIVEN PRICE RANGE: " + noOfHotels);
	}
}