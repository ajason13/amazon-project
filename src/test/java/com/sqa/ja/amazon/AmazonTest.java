package com.sqa.ja.amazon;

import java.util.concurrent.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;
import org.testng.*;
import org.testng.annotations.*;

public class AmazonTest {

	// Variables: Driver to drive test
	private WebDriver driver;

	// Keep track of the base domain of testing site
	private String baseUrl;

	WebDriverWait wait;

	@DataProvider
	public Object[][] dp() {
		return new Object[][] { new Object[] { "dog treats", "5", 113.7 } };
	}

	@BeforeClass(enabled = true)
	public void setUpChrome() throws Exception {
		// Set system property to use Chrome driver
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		// Setup the driver to use Chrome
		this.driver = new ChromeDriver();
		// Set the base URL for this test
		this.baseUrl = "https://www.amazon.com/";
		// Set an implicit wait of 30 second (If pass 30 seconds to find
		// element, fail test case)
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Maximize the window
		this.driver.manage().window().maximize();
		this.wait = new WebDriverWait(this.driver, 10);
	}

	@BeforeClass(enabled = false)
	public void setUpFirefox() throws Exception {
		// Setup the driver to use Firefox
		this.driver = new FirefoxDriver();
		// Set the base URL for this test
		this.baseUrl = "https://www.amazon.com/";
		// Set an implicit wait of 30 second (If pass 30 seconds to find
		// element, fail test case)
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Maximize the window
		this.driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(this.driver, 10);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		// Quit all instances of the bowser
		this.driver.quit();
		// Close method would close all instances
	}

	@Test(dataProvider = "dp")
	public void testDogTreatCase01(String itemName, String quantity, double expectedPrice) throws Exception {
		// Go to base url of domain of site being tested
		this.driver.get(this.baseUrl + "/ref=nav_logo");
		// this.driver.findElement(By.id("twotabsearchtextbox")).clear();
		// Capture the search field in a WebElement
		WebElement searchField = this.driver.findElement(By.id("twotabsearchtextbox"));
		// Clear the search field
		searchField.clear();
		// this.driver.findElement(By.id("twotabsearchtextbox")).sendKeys("dog
		// treat");
		// Type text into the search field
		searchField.sendKeys(itemName);
		// this.driver.findElement(By.cssSelector("input.nav-input")).click();
		// Capture the search button in a WebElement and click on it
		WebElement searchButton = this.driver.findElement(By.cssSelector("input.nav-input"));
		searchButton.click();
		// Alternatively submit the form for searching
		// searchField.submit();
		// Click on first item in results
		WebElement resultItem = this.driver.findElement(By.xpath("//li[@id='result_0']/div/div[4]/div/a/h2"));
		resultItem.click();
		// Choose option to buy one time as a guest user
		this.driver.findElement(By.xpath("//div[@id='oneTimeBuyBox']/div/div/a/i")).click();
		// Set the quantity to 3
		new Select(this.driver.findElement(By.id("quantity"))).selectByVisibleText(quantity);
		// Click on the add to cart button
		this.driver.findElement(By.id("add-to-cart-button")).click();
		// Click on the view cart button
		// WebElement viewCart =
		// this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hlb-view-cart-announce")));
		// viewCart.click();
		this.driver.findElement(By.id("hlb-view-cart-announce")).click();
		WebElement priceElement = this.driver.findElement(By.cssSelector("span#sc-subtotal-amount-activecart span"));
		String priceString = priceElement.getText().trim().replace("$", "");
		double actualPrice = Double.parseDouble(priceString);
		Assert.assertEquals(actualPrice, expectedPrice);
		// Delete the item from your cart
		this.driver.findElement(By.name("submit.delete.C2F8NMEOBSVVVF")).click();
	}
}
