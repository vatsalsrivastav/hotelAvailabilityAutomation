package mainCode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.*;
import java.util.Properties;

public class SearchHotels {
	
	/*This function is used to get the date ahead
	  by 'n' number of days from the current date.*/
	public static String getDateAhead(int n){
		LocalDate today = LocalDate.now(); 				//Getting today's date.
		String date = "" + today.plusDays(n);			//Finding date 'n' days from today.
		return date;									//Returning 'n' days ahead date.
	}
	
	/*This method is used to read the destination city from the properties 
	  file and return it to the query field.*/
	public static String getDestination() throws IOException{
		
		//Loading property file to get the destination location.
		Properties prop = new Properties();
		String propLocation = System.getProperty("user.dir") + "/mainCode/config.properties";
		FileInputStream ipt = new FileInputStream(propLocation);
		prop.load(ipt);
		ipt.close();
		
		//Returning the destination city after reading from properties file.
		return prop.getProperty("destination");
	}
	
	/*This function is used to input the values in the required fields
	  according to the search requirements and click on search button.*/
	public void searchHotels(WebDriver driver) throws Exception{
		
		//Adding WebDriverWait element of 15 seconds wait time.
		WebDriverWait wait = new WebDriverWait(driver, 15);
		
		//Opening Trivago Hotel Booking Home Page.
		driver.get("https://www.trivago.in/"); 
		
		//Expected title of the homepage.
		String expectedTitle = "trivago.in - Compare hotel prices worldwide";
		
		//Actual title received after opening the homepage.
		String actualTitle = driver.getTitle();
		
		//Verifying if the correct page is opened.
		if(actualTitle.equalsIgnoreCase(expectedTitle)){
			System.out.println("HomePage Successfully Opened.");
		}
		
		//Explicitly waiting to click Okay button when the cookie warning pops up.
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button#onetrust-accept-btn-handler")));
		Thread.sleep(500);			//Giving some time for cookie dialog to appear completely.
		
		//Getting the WebElement of the Okay button on the cookie warning pop up.
		WebElement cookieOk = driver.findElement(By.cssSelector("button#onetrust-accept-btn-handler"));
		
		cookieOk.click();			//Clicking Okay button on cookie warning.
		
		Thread.sleep(500);			//Giving some time for cookie dialog to disappear completely.
		
		//Finding destination city to search hotels in.
		String destination = getDestination();
		
		//Finding Destination search box and entering destination in it received from properties file.
		driver.findElement(By.id("querytext")).sendKeys(destination);
		
		//Printing destination city name.
		System.out.println("\nDestination City: " + destination);
		
		//XPath of a heading text element to create a diversion click.
		String headDivert = "//span[text() = 'Find your ideal hotel and compare prices from different websites']";
		
		//Clicking on diversion element.
		driver.findElement(By.xpath(headDivert)).click();
		
		//Explicitly waiting for the Check-In date button to be available.
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@key = 'checkInButton']")));
				
		Thread.sleep(500);		//Giving some time for Check-In date dialog to open before selecting date.
		
		//XPath of desired Check-In date which is a week after the current date.
		String checkIn = "//td[@class = 'cal-day-wrap']/time[@datetime = '" + getDateAhead(7) +"']";
		
		//Try-catch block to select the Check-In date from the date dialog box.
		try{
			driver.findElement(By.xpath("//button[@key = 'checkInButton']")).click();
			Thread.sleep(500);
			WebElement checkInDate = driver.findElement(By.xpath(checkIn));
			Thread.sleep(500);
			checkInDate.click();
		}
		catch(Exception e){
			driver.findElement(By.xpath(headDivert)).click();
			driver.findElement(By.xpath("//button[@key = 'checkInButton']")).click();
			Thread.sleep(500);
			driver.findElement(By.cssSelector("button.cal-btn-prev")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(checkIn)).click();
		}
		
		//Printing Check-In Date
		System.out.println("\nCheck-In Date: " + getDateAhead(7));
		
		//Clicking on diversion element.
		driver.findElement(By.xpath(headDivert)).click();
		Thread.sleep(500);		//Giving some time for Check-Out date dialog to open before selecting date.
		
		//XPath of desired Check-Out date which is 2 days after the Check-In date.
		String checkOut = "//td[@class = 'cal-day-wrap']/time[@datetime = '" + getDateAhead(9) +"']";
		
		//Try-catch block to select the Check-Out date from the date dialog box.
		try{
			driver.findElement(By.xpath("//button[@key = 'checkOutButton']")).click();
			Thread.sleep(500);
			WebElement checkOutDate = driver.findElement(By.xpath(checkOut));
			Thread.sleep(500);
			checkOutDate.click();
		}
		catch(Exception e){
			driver.findElement(By.xpath(headDivert)).click();
			driver.findElement(By.xpath("//button[@key = 'checkOutButton']")).click();
			Thread.sleep(500);
			driver.findElement(By.cssSelector("button.cal-btn-next")).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(checkOut)).click();
		}
		
		//Printing Check-Out Date
		System.out.println("Check-Out Date: " + getDateAhead(9));
				
		Thread.sleep(500);		//Giving some time for Check-Out date dialog to close.
		
		//Setting number of guests to 1 adult instead of 2 adults.
		driver.findElement(By.xpath("//button[@data-role = 'removeAdult']")).click();
		
		Thread.sleep(500);		//Giving some time for guests dialog to open.
		
		//Clicking on apply button for the number of guests.
		driver.findElement(By.xpath("//button[@data-role = 'applyConfigBtn']")).click();
		
		Thread.sleep(500);		//Giving some time for guests dialog to close.
		
		//Clicking on Search button to search all the hotels in Mumbai.
		driver.findElement(By.xpath("//button[@data-qa = 'search-button']")).click();
		
		//Explicitly waiting for the first result page to load before handing over driver control.
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-qa = 'champion-deal']")));
		
		//Verifying if Hotels in Mumbai are searched successfully
		String actPage = driver.getTitle();
		String expPage = "Mumbai Hotels | Find & compare great deals on trivago";
		if(actPage.equalsIgnoreCase(expPage))
			System.out.println("\nHotels Page Loaded Successfully.");
	}
}
