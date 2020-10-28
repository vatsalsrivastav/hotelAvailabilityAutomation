package mainCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CountHotels {
	
	/*This method is used to remove the comma from the price and
	  then return it in form of an integer.*/
	public static int convertToInteger(String actPrice){
		String[] remComma = actPrice.split("[,]");					//Removing the comma from the price.
		
		int value = 0;
		
		if(remComma.length > 1)
			value = Integer.parseInt(remComma[0] + remComma[1]);	//Converting price with comma to integer value.
		else
			value = Integer.parseInt(actPrice);						//Converting price without comma to integer value.
		
		return value;
	}
	
	/*This method is used to get the minimum and maximum price values
	  to get the price range in which to search for hotels.*/
	public static int[] getPriceRange() throws IOException{
		
		//Loading property file to get the min and max price.
		Properties prop = new Properties();
		String propLocation = System.getProperty("user.dir") + "/mainCode/config.properties";
		FileInputStream ipt = new FileInputStream(propLocation);
		prop.load(ipt);
		ipt.close();
		
		int[] range = new int[2];
		range[0] = Integer.parseInt(prop.getProperty("minPrice"));		//Storing min price at index 0.
		range[1] = Integer.parseInt(prop.getProperty("maxPrice"));		//Storing max price at index 1.
		
		//Returning array of min and max price.
		return range;
	}
	
	/*This function is used to fetch the price list of all hotels from each landing page and
	  navigate to the other landing page as long as there exists another page of hotel details. 
	  The method compares and checks if the price of the hotel is in the given price range and 
	  increments the 'count' variable if it is between minimum and maximum price.*/
	public int countHotels(WebDriver driver) throws InterruptedException, IOException{
		
		//Wait object created with 15 seconds timeout.
		WebDriverWait wait = new WebDriverWait(driver, 15);		
		
		int count = 0;		//Initialization of 'count' variable.
		
		//XPath for 'Loading' animation.
		String loader = "//div[@class = 'ellipsis-loader ellipsis-loader--branded center-x']";
		
		//XPath for next page button.
		String next = "//button[@class = 'btn btn--pagination btn--small btn--page-arrow btn--next']";
		
		//XPath for the price list.
		String priceList = "//strong[@data-qa = 'recommended-price']";
		
		//XPath for the list of hotel names.
		String nameList = "//span[@data-qa = 'item-name']";
		
		//Waiting for the page to load.
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loader)));		
		
		//Storing WebElement for next page button to check if it exists.
		List<WebElement> nextButton = driver.findElements(By.xpath(next));
		
		//Fetching best prices of all hotels displayed on the landing page.
		List<WebElement> price = driver.findElements(By.xpath(priceList));
		
		//Fetching names of all hotels displayed on the landing page.
		List<WebElement> hotelName = driver.findElements(By.xpath(nameList));
		
		//To store the names of hotels and their price.
		Map<String, String> hotels = new LinkedHashMap<String, String>();
		
		//Getting the min and max price from properties file.
		int[] range = getPriceRange();							
		
		//Loop to find the number of hotels in the price range received from properties file.
		while(!nextButton.isEmpty()){
			for(int i = 0; i < price.size(); i++){
				/*Retrieving price of i-th hotel from the list after 
				  removing currency symbol.*/
				String actPrice = price.get(i).getText().substring(1);		
				
				int value = convertToInteger(actPrice);					//Calling method to convert price to integer.
				
				if((value >= range[0]) && (value <= range[1])){
					count++;											//Incrementing count for hotels in given price range.
					hotels.put(hotelName.get(i).getText() , actPrice);
				}
			}

			//Storing WebElement for next page button to check if it exists.
			nextButton = driver.findElements(By.xpath(next));
			
			//Clicking on next page button if it exists.
			if(!nextButton.isEmpty()){
				nextButton.get(0).click();
				
				//Waiting for the page to load.
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loader)));
			}
			
			//Fetching best prices of all hotels displayed on the landing page.
			price = driver.findElements(By.xpath(priceList));
			
			//Fetching names of all hotels displayed on the landing page.
			hotelName = driver.findElements(By.xpath(nameList));
		}
		
		//Printing the searched price range
		System.out.println("\nPrice Range: Rs." + range[0] + " to Rs." + range[1]);
		
		//Accessing CreateExcel module to generate the excel file with hotel names and prices.
		CreateExcel excel = new CreateExcel();
		excel.generateReport(hotels);
		
		//Returning the total number of hotels found in the given price range.
		return count;	
	}
}
