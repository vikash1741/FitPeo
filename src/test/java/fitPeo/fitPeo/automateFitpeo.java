package fitPeo.fitPeo;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class automateFitpeo {
	WebDriver driver;
	String actualTextBoxValue;
	String updatedSlideValue;
	@BeforeMethod
	public void setUp() {
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();

		//1.	Navigate to the FitPeo Homepage:
		//○	Open the web browser and navigate to FitPeo Homepage.

		driver.get("https://www.fitpeo.com/");
		String actualTitle =  driver.getTitle();
		String expectedTitle = "Remote Patient Monitoring (RPM) - fitpeo.com";
		Assert.assertEquals(actualTitle, expectedTitle);
	}
	@Test
	public void fitPeoAutomation() throws InterruptedException {
		/*2.	Navigate to the Revenue Calculator Page:
			From the homepage, navigate to the Revenue Calculator Page.
		 */

		WebElement revCal=driver.findElement(By.cssSelector("[href='/revenue-calculator\']"));
		revCal.click();
		Thread.sleep(5000);
		String actualUrl =  driver.getCurrentUrl();
		String expectedUrl = "https://www.fitpeo.com/revenue-calculator";
		Assert.assertEquals(actualUrl, expectedUrl);


		/* 
		 * 3.	Scroll Down to the Slider section:
		   ○	Scroll down the page until the revenue calculator slider is visible.

		 */
		WebElement muiSliThub = driver.findElement(By.xpath("//span[starts-with(@class,'MuiSlider-thumb')]"));
		JavascriptExecutor js = (( JavascriptExecutor)driver);
		js.executeScript("arguments[0].scrollIntoView(true);", muiSliThub);


		/*
		 * 4.	Adjust the Slider:
		   ○	Adjust the slider to set its value to 820. we’ve highlighted the slider in red color, Once the slider
		    	is moved the bottom text field value should be updated to 820

		 */
		//
		int expectedvalue = 820;
		WebElement slider = driver.findElement(By.xpath("//span[starts-with(@class,'MuiSlider-thumb')]//input"));
		Actions move = new Actions(driver);
		Action action = (Action) move.dragAndDropBy(slider, 93, 0).build();
		action.perform();
		int si = Integer.parseInt(slider.getAttribute("value")) ;
		int re = 0;
		if(si != expectedvalue ) {
			re =expectedvalue - si;
		}
		for (int i = 1; i <= re ; i++) {
			slider.sendKeys(Keys.ARROW_RIGHT);
		}

		updatedSlideValue= slider.getAttribute("value");
		Assert.assertEquals(updatedSlideValue, String.valueOf(expectedvalue));

		WebElement textboxValue = driver.findElement(By.xpath("//input[@type='number']"));
		actualTextBoxValue=textboxValue.getAttribute("value");
		Assert.assertEquals(actualTextBoxValue, String.valueOf(expectedvalue)); 

		/*
		 * 5.	Update the Text Field:
			○	Click on the text field associated with the slider.
			○	Enter the value 560 in the text field. Now the slider also should change accordingly 

		 */
		for (int i = 1; i <= 3 ; i++) {
			textboxValue.sendKeys(Keys.BACK_SPACE);
		}
		textboxValue.sendKeys("560");

		actualTextBoxValue=textboxValue.getAttribute("value");
		Assert.assertEquals(actualTextBoxValue, "560");

		/*
		 * 6.	Validate Slider Value:
			○	Ensure that when the value 560 is entered in the text field, the slider's position is updated to reflect the value 560.

		 */
		updatedSlideValue= slider.getAttribute("value");
		Assert.assertEquals(updatedSlideValue, "560");

		/*
		 * 7.	Select CPT Codes:
				Scroll down further and select the checkboxes for CPT-99091, CPT-99453, CPT-99454, and CPT-99474.

		 */
		List<WebElement> cpt = driver.findElements(By.xpath("//p[contains(@class, 'unkt')]"));
		String[] Cpt = {"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"};

		// Iterate through each element
		for (int i = 0; i < cpt.size(); i++) {
			WebElement cptName = cpt.get(i);
			js.executeScript("arguments[0].scrollIntoView(true);", cptName);
			String cptCodeName = cptName.getText();
			// Check if the text contains any of the CPT codes
			for (String serCpt : Cpt ) {
				if (cptCodeName.equals(serCpt)) {
					// Correct the XPath to be relative to the current cptName element

					WebElement inputElement = driver.findElements(By.xpath("//p[contains(@class,'unkt')]//parent::div //input")).get(i);
					inputElement.click();
					break; // Exit the inner loop once a match is found
				}
			}          

		}

		/*
		 * 8.	Validate Total Recurring Reimbursement:
			   9.	Verify that the header displaying Total Recurring Reimbursement for all Patients Per Month: shows the value $110700.	

		 */

		js.executeScript("arguments[0].scrollIntoView(true);", textboxValue);
		for (int i = 1; i <= 3 ; i++) {
			textboxValue.sendKeys(Keys.BACK_SPACE);
		}
		textboxValue.sendKeys("820");

		WebElement totalRecRei = driver.findElement(By.xpath("(//p[contains(@class,'guk')])[4]/p"));
		js.executeScript("arguments[0].scrollIntoView(true);", totalRecRei);
		String totalRecuReim =totalRecRei.getText();
		Assert.assertEquals(totalRecuReim, "$110700");

	}

	@AfterMethod()
	public void tearDown() {
		driver.quit();
	}
}
