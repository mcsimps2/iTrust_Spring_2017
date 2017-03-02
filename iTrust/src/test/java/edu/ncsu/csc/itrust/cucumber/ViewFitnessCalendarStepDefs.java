package edu.ncsu.csc.itrust.cucumber;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;

public class ViewFitnessCalendarStepDefs {
	private iTrustDriver driver;
	FitnessInfo randomInfo;
	FitnessInfoMySQL fisql;
	int dayOfTheMonth;
	WebElement dayInCalendar;
	String date;
	
	public ViewFitnessCalendarStepDefs(iTrustDriver driver)
	{
		this.driver = driver;
		fisql = new FitnessInfoMySQL(ConverterDAO.getDataSource());
	}
	
	@Given("^I have logged in as HCP (.+) with password (.+)$")
	public void login(String mid, String pwd)
	{
		//login with the given mid and password
		driver.findElement(By.name("j_username")).sendKeys(mid);;
		driver.findElement(By.name("j_password")).sendKeys(pwd);;
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		//Check if we logged in correctly
		if (driver.getTitle().equals("iTrust - Login")) {
			Assert.fail("Error logging in, user not in database?");
		}
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
	}
	
	@Given("^I have navigated to Patient Info -> Patient Fitness Data$")
	public void navigateToPatientFitnessData()
	{
		//Find the link to patient fitness data and click it
		try
		{
			driver.findElement(By.linkText("Patient Fitness Data")).click();
		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not click on Patient Fitness Data link");
		}
		//Make sure we are at the select a patient screen
		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@When("^I search for the patient with name (.+)$") 
	public void searchForPatientByName(String name)
	{
		//Copied from ViewBasicHealthInfoStepDefs.java
		driver.findElement(By.cssSelector("input[name=\"FIRST_NAME\"]")).sendKeys(name);
		driver.findElement(By.cssSelector("input[value=\"User Search\"]")).click();
	}
	
	@When("^click on their link$")
	public void clickPatientLink()
	{
		//Copied from ViewBasicHealthInfoStepDefs.java
		driver.findElement(By.id("selectPatient1")).submit();
	}
	
	@Then("^their fitness calendar comes up$")
	public void checkFitnessCalendar()
	{
		//Find the calendar table by ID.  Assert that such an element exists
		Assert.assertTrue(driver.findElements(By.id("calendarTable")).size() > 0);
	}
	
	@Given("^I have pulled up Patient 1's fitness calendar$")
	public void pullUpFitnessCalendar()
	{
		navigateToPatientFitnessData();
		searchForPatientByName("Random");
		clickPatientLink();
		//We should now be at Patient pid's fitness calendar
	}
	
	@Given("^they have no fitness data for day (\\d+)$")
	public void eraseFitnessData(int iday)
	{
		dayOfTheMonth = iday; //save the parameter in a global variable
		//Date needs to be in the format YYYY-MM-DD for SQL purposes
		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("deprecation")
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), iday);
		date = d.toString();
		
		//Remove any data from the fitness factory that corresponds to this date
		
		try
		{
			fisql.removeFitnessInfo(1, date); //remove anything in database corresponding to PID 1 and the given date
			
			//Make sure there is nothing in that date now.  The return from getFitnessInfo 
			//should be null for a null entry
			Assert.assertNull(fisql.getFitnessInfo(1, date));
		} catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Given("^they have fitness data for day (\\d+)$")
	public void fillWithData(int iday)
	{
		dayOfTheMonth = iday; //save the parameter in a global variable
		//Date needs to be in the format YYYY-MM-DD for SQL purposes
		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("deprecation")
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), iday);
		date = d.toString();
		
		//Now, setup our global variable randominfo to hold the data
		//This bean is for PID 1 and the date generated above.  All the data is just 1's
		randomInfo = new FitnessInfo(1, date, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0);
		
		//Now add this data to the database
		try
		{
			Assert.assertTrue(fisql.add(randomInfo));
		}
		catch (DBException | FormValidationException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//have now populated the databases for the given date and PID 1
	}
	
	@When("^I find that day in their calendar$")
	public void locateDay()
	{
		List<WebElement> addList = driver.findElements(By.linkText("Add"));
		Assert.assertTrue(addList.size() > 0);
		List<WebElement> viewList = driver.findElements(By.linkText("View/Edit"));
		Assert.assertTrue(viewList.size() > 0);
		//find the one with the correct date
		for (int i = 0; i < addList.size(); i++)
		{
			String href = addList.get(i).getAttribute("href");
			if (href.contains(date))
			{
				//found the correct webelement
				dayInCalendar = addList.get(i);
				return;
			}
		}
		for (int i = 0; i < viewList.size(); i++)
		{
			String href = viewList.get(i).getAttribute("href");
			if (href.contains(date))
			{
				//found the correct webelement
				dayInCalendar = viewList.get(i);
				return;
			}
		}
		//If we reach down here, did not locate the link
		Assert.fail("Could not locate the day in the calendar");
	}
	
	@Then("^it has an Add Data link$")
	public void verifyAddLink()
	{
		Assert.assertTrue(dayInCalendar != null);
		Assert.assertTrue(dayInCalendar.getText().equals("Add"));
	}
	
	@Then("^it has a View/Edit Data link$")
	public void verifyViewEditLink()
	{
		Assert.assertTrue(dayInCalendar != null);
		System.out.println("Text: " + dayInCalendar.getText());
		Assert.assertTrue(dayInCalendar.getText().equals("View/Edit"));
	}
	
}
