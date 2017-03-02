package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class EnterAndEditFitnessDataStepDefs {
	private iTrustDriver driver;
	private FitnessInfoMySQL fisql;
	private FitnessInfo info;
	private FitnessInfo randomInfo;
	
	public EnterAndEditFitnessDataStepDefs(iTrustDriver driver)
	{
		this.driver = driver;
		fisql = new FitnessInfoMySQL(ConverterDAO.getDataSource());
		info = new FitnessInfo();
		randomInfo = new FitnessInfo();
	}
	@After
	public void resetDB() throws IOException, SQLException
	{
		TestDataGenerator.main(null);
	}
	
	@Given("^the day (\\d+) has no fitness data$")
	public void removeFitnessData(int iday)
	{
		//Date needs to be in the format YYYY-MM-DD for SQL purposes
		Calendar cal = Calendar.getInstance();
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), iday);
		String date = d.toString();
		
		//Now, setup our global variable info to hold the data in the form the SQL database does
		info = new FitnessInfo();
		info.setDate(date);
		info.setPid(1);
		
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
	
	@Given("^the patient has populated fitness data for the day (.+)$")
	public void makeFitnessData(int iday)
	{
		//Date needs to be in the format YYYY-MM-DD for SQL purposes
		Calendar cal = Calendar.getInstance();
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), iday);
		
		//Now, setup our global variable randominfo to hold the data
		//This bean is for PID 1 and the date generated above.  All the data is just 1's
		randomInfo = new FitnessInfo(1, d.toString(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0);
		
		info = new FitnessInfo();
		info.setDate(d.toString());
		info.setPid(1);
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
	
	@SuppressWarnings("deprecation")
	@When("^I click on the day (\\d+)$")
	public void clickOnDay(int day)
	{
		try
		{
			//The naming convention for these "Add" or "View/Edit" links is fitnessData-Day
			//driver.findElement(By.name("fitnessData-" + day)).click();
			Calendar cal = Calendar.getInstance();
			java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR)-1900, cal.get(Calendar.MONTH), day);
			
			//http://localhost:8080/iTrust/auth/hcp/editFitnessInfo.xhtml?date=2017-02-08&day=8
			
			driver.loadPage("/iTrust/auth/hcp/editFitnessInfo.xhtml?date=" + d.toString() + "&day=" + day);
			//System.out.println("At the page: " + driver.getCurrentUrl());
		}
		catch (NoSuchElementException e)
		{
			Assert.fail("Could not click on the day");
		}
	}
	
	
	@When("^I enter the following values: (\\d+) Calories Burned, (\\d+) Steps, (\\d+) Distance, (\\d+) Floors, (\\d+) Minutes Sedentary, (\\d+) Minutes Lightly Active, (\\d+) Minutes Fairly Active, (\\d+) Minutes Very Active, (\\d+) Activity Calories$")
	public void enterFitnessValues(int calsB, int steps, int miles, int floors, int minSed, int minLight, int minFair, int minHigh, int calsA)
	{
		//Store the variables  to use later on
		info.setCaloriesBurned(calsB);
		info.setSteps(steps);
		info.setMiles(miles);
		info.setFloors(floors);
		info.setMinutesSedentary(minSed);
		info.setMinutesLightlyActive(minLight);
		info.setMinutesFairlyActive(minFair);
		info.setMinutesVeryActive(minHigh);
		info.setActiveCalories(calsA);
		
		//Find the textboxes for entry in the page
		try {
			WebElement calsBBox = driver.findElement(By.id("j_idt21:fiCalsBurned"));
			WebElement stepsBox = driver.findElement(By.id("j_idt21:fiSteps"));
			WebElement milesBox = driver.findElement(By.id("j_idt21:fiMiles"));
			WebElement floorsBox = driver.findElement(By.id("j_idt21:fiFloors"));
			WebElement minSedBox = driver.findElement(By.id("j_idt21:fiSedentary"));
			WebElement minLightBox = driver.findElement(By.id("j_idt21:fiLightlyActive"));
			WebElement minFairBox = driver.findElement(By.id("j_idt21:fiFairlyActive"));
			WebElement minHighBox = driver.findElement(By.id("j_idt21:fiVeryActive"));
			WebElement calsABox = driver.findElement(By.id("j_idt21:fiActiveCals"));
			
			
			//Clear all the boxes
			calsBBox.clear();
			stepsBox.clear();
			milesBox.clear();
			floorsBox.clear();
			minSedBox.clear();
			minLightBox.clear();
			minFairBox.clear();
			minHighBox.clear();
			calsABox.clear();
			
			//Fill in the boxes
			calsBBox.sendKeys(calsB + "");
			stepsBox.sendKeys(steps + "");
			milesBox.sendKeys(miles + "");
			floorsBox.sendKeys(floors + "");
			minSedBox.sendKeys(minSed + "");
			minLightBox.sendKeys(minLight + "");
			minFairBox.sendKeys(minFair + "");
			minHighBox.sendKeys(minHigh + "");
			calsABox.sendKeys(calsA + "");

		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not find the textboxes for entry of fitness values");
		}
		
	}
	
	@When("^I enter the following invalid values: (.+) Calories Burned, (.+) Steps, (.+) Distance, (.+) Floors, (.+) Minutes Sedentary, (.+) Minutes Lightly Active, (.+) Minutes Fairly Active, (.+) Minutes Very Active, (.+) Activity Calories$")
	public void enterInvalidFitnessValues(String calsB, String steps, String miles, String floors, String minSed, String minLight, String minFair, String minHigh, String calsA)
	{
		
		//Find the textboxes for entry in the page
		try {
			WebElement calsBBox = driver.findElement(By.id("j_idt21:fiCalsBurned"));
			WebElement stepsBox = driver.findElement(By.id("j_idt21:fiSteps"));
			WebElement milesBox = driver.findElement(By.id("j_idt21:fiMiles"));
			WebElement floorsBox = driver.findElement(By.id("j_idt21:fiFloors"));
			WebElement minSedBox = driver.findElement(By.id("j_idt21:fiSedentary"));
			WebElement minLightBox = driver.findElement(By.id("j_idt21:fiLightlyActive"));
			WebElement minFairBox = driver.findElement(By.id("j_idt21:fiFairlyActive"));
			WebElement minHighBox = driver.findElement(By.id("j_idt21:fiVeryActive"));
			WebElement calsABox = driver.findElement(By.id("j_idt21:fiActiveCals"));
			
			
			//Clear all the boxes
			calsBBox.clear();
			stepsBox.clear();
			milesBox.clear();
			floorsBox.clear();
			minSedBox.clear();
			minLightBox.clear();
			minFairBox.clear();
			minHighBox.clear();
			calsABox.clear();
			
			//Fill in the boxes
			calsBBox.sendKeys(calsB + "");
			stepsBox.sendKeys(steps + "");
			milesBox.sendKeys(miles + "");
			floorsBox.sendKeys(floors + "");
			minSedBox.sendKeys(minSed + "");
			minLightBox.sendKeys(minLight + "");
			minFairBox.sendKeys(minFair + "");
			minHighBox.sendKeys(minHigh + "");
			calsABox.sendKeys(calsA + "");

		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not find the textboxes for entry of fitness values");
		}
		
	}

	@When("^I click Save$")
	public void submitFitnessForm()
	{
		try
		{
			driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		}
		catch (Exception e)
		{
			Assert.fail("Unable to submit form");
		}
	}
	
	@Then("^a success message appears$")
	public void checkForSuccess()
	{
		Assert.assertTrue(driver.getPageSource().contains("Success"));
	}
	
	@Then("^the information is saved$")
	public void checkInformationSaved()
	{
		//Get what is in the database for patient 1 and the given date
		try
		{
			FitnessInfo savedInfo = fisql.getFitnessInfo(info.getPid(), info.getDate());
			
			//Compare it to what it should have been
			Assert.assertTrue(savedInfo.equals(info));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Then("^the information is not saved$")
	public void checkNotSaved()
	{
		try
		{
			FitnessInfo savedInfo = fisql.getFitnessInfo(info.getPid(), info.getDate());
			Assert.assertNull(savedInfo);
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
}
