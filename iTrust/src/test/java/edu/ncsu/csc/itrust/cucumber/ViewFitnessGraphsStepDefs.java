package edu.ncsu.csc.itrust.cucumber;

import java.util.Calendar;
import org.openqa.selenium.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ViewFitnessGraphsStepDefs {
	
	/** Selenium WebDriver used to navigate for tests */
	iTrustDriver driver;
	
	/**
	 * Constructor that takes a Selenium WebDriver to do the navigation for the tests
	 * @param driver a WebDriver to do the navigation for the tests
	 */
	public ViewFitnessGraphsStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
	}
	
	@Given("^I am at the iTrust login screen$")
	public void atLogin() {
		//log out of any potential logged in user and go to login screen
		driver.loadPage("/iTrust/logout.jsp");
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/forwardUser.jsp")); //make sure we are at the login screen
	}
	
	@Given("^I have logged in with MID (.+) and password (.+)$")
	public void login(String mid, String pwd) {
		// Make sure we are at the login screen
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/forwardUser.jsp"));
				
		//login with the given mid and password
		driver.findElement(By.name("j_username")).sendKeys(mid);;
		driver.findElement(By.name("j_password")).sendKeys(pwd);;
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		//Check if we logged in correctly
		if (driver.getTitle().equals("iTrust - Login"))
			Assert.fail("Error logging in, user not in database?");
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
	}
	
	@Given("^I have pulled up the patient calendar for patient with name (.+)$")
	public void toFitnessCalendar(String name) {		
		// Make sure we're at the home page
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
		
		try {
			// Navigate to Patient Info -> Patient Fitness Data
			driver.findElement(By.linkText("Patient Fitness Data")).click();
			
			// Make sure we are at the select a patient screen
			Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
			
			// Enter patient MID and search
			driver.findElement(By.cssSelector("input[name=\"FIRST_NAME\"]")).sendKeys(name);
			driver.findElement(By.cssSelector("input[value=\"User Search\"]")).click();
			
			// Select patient
			driver.findElement(By.id("selectPatient1")).submit();
			
			// Make sure that the calendar appears
			Assert.assertTrue(driver.findElements(By.id("calendarTable")).size() > 0);
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I click the button to view summary reports$") 
	public void viewSummaryReports() {
		try {
			// Click view summary report button
			driver.findElement(By.linkText("View Summary Report")).click();
			Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/fitnessGraphs.xhtml"));
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I enter start date (\\S+) and end date (\\S+)$")
	public void enterStartEndDate(String day1, String day2) {
		System.out.println(driver.getCurrentUrl());
		driver.findElement(By.id("j_idt21:start-date")).sendKeys(day1);
		driver.findElement(By.id("j_idt21:end-date")).sendKeys(day2);
	}
	
	@When("^I change the default dates to be the past month$")
	public void goBackOneMonth() {
		// Build start and end date strings
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = 1;
		int year = cal.get(Calendar.YEAR);
		String startDate = String.format("%02d/%02d/%04d", Math.abs((month - 1) % 12), day, (month == 0) ? (year - 1) : year);
		String endDate = String.format("%02d/%02d/%04d", month, day, year);
		
		try {
			// Send strings to start and end date text fields
			driver.findElement(By.id("j_idt21:start-date")).sendKeys(startDate);
			driver.findElement(By.id("j_idt21:end-date")).sendKeys(endDate);
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I click generate$")
	public void generateGraphs() {
		try {
			// Click the generate button
			driver.findElement(By.linkText("Generate")).click();
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^I can see three graphs of daily step counts, daily deltas, and weekly average step counts$")
	public void checkGraphs() {
		// Make sure we're at the right page]
		System.out.println(driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp/fitnessGraphs.xhtml#"));
		
		//Access the graph divs, if graphs aren't displayed then no such element exception will be thrown
		try {
			driver.findElement(By.cssSelector("#daily-chart"));
			driver.findElement(By.cssSelector("#delta-chart"));
			driver.findElement(By.cssSelector("#weekly-avg-chart"));
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^the graphs are changed to reflect the past month$")
	public void checkUpdatedGraphs() {
		// Make sure we're at the right page
		Assert.assertTrue(driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp/fitnessGraphs.xhtml#"));
		
		// Find/check graphs
		try {
			driver.findElement(By.cssSelector("#daily-chart"));
			driver.findElement(By.cssSelector("#delta-chart"));
			driver.findElement(By.cssSelector("#weekly-avg-chart"));
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I change the defaults dates to start on (\\d+) and end on (\\d+)$")
	public void invalidDatesInCalendar(int day1, int day2)
	{
		// Build start and end date strings
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String startDate = String.format("%02d/%02d/%04d", month, day1, year);
		String endDate = String.format("%02d/%02d/%04d", month, day2, year);
		
		try {
			// Send strings to start and end date text fields
			driver.findElement(By.id("j_idt21:start-date")).sendKeys(startDate);
			driver.findElement(By.id("j_idt21:end-date")).sendKeys(endDate);
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^I get an error message about invalid dates$")
	public void checkForInvalidDatesErrorMessage()
	{
		Assert.assertTrue(driver.getPageSource().contains("Enter dates in YYYY-MM-DD format."));
	}			
	
}