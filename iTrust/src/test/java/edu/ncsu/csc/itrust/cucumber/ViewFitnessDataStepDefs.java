package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;

import java.util.Calendar;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class ViewFitnessDataStepDefs {
	private iTrustDriver driver;
	FitnessInfo randomInfo;
	FitnessInfo info;
	FitnessInfoMySQL fisql;
	
	public ViewFitnessDataStepDefs(iTrustDriver driver)
	{
		this.driver = driver;
		randomInfo = new FitnessInfo();
		info = new FitnessInfo();
		fisql = new FitnessInfoMySQL(ConverterDAO.getDataSource());
	}
	
	@Given("^the patient has populated fitness data for the current month day (\\d+)$")
	public void populateDataCurrentMonth(int day)
	{
		//Date needs to be in the format YYYY-MM-DD for SQL purposes
		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("deprecation")
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), day);
		
		//Now, setup our global variable randominfo to hold the data
		//This bean is for PID 1 and the date generated above.  All the data is just 1's
		randomInfo = new FitnessInfo();
		randomInfo.setDate(d.toString());
		randomInfo.setPid(1);
		randomInfo.setActiveCalories(500);
		randomInfo.setActiveHours(3);
		randomInfo.setCaloriesBurned(1000);
		randomInfo.setFloors(5);
		randomInfo.setHeartRateAvg(60);
		randomInfo.setHeartRateLow(50);
		randomInfo.setHeartRateHigh(100);
		randomInfo.setMiles(6);
		randomInfo.setMinutesFairlyActive(120);
		randomInfo.setMinutesLightlyActive(100);
		randomInfo.setMinutesSedentary(200);
		randomInfo.setMinutesUVExposure(150);
		randomInfo.setMinutesVeryActive(200);
		
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
	
	@Given("^the patient has populated fitness data for last month during the day (\\d+)$")
	public void populateDataLastMonth(int day)
	{
		//First, get the date corresponding to the day of the last month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1); //go to last month
		@SuppressWarnings("deprecation")
		java.sql.Date d = new java.sql.Date(cal.get(Calendar.YEAR)-1900, cal.get(Calendar.MONTH), day);
		String date = d.toString();
		//System.out.println("Date is " + date);
		//Now begin inserting values
		//Now, setup our global variable randominfo to hold the data
		randomInfo = new FitnessInfo();
		randomInfo.setDate(date);
		randomInfo.setPid(1);
		randomInfo.setActiveCalories(500);
		randomInfo.setActiveHours(3);
		randomInfo.setCaloriesBurned(1000);
		randomInfo.setFloors(5);
		randomInfo.setHeartRateAvg(60);
		randomInfo.setHeartRateLow(50);
		randomInfo.setHeartRateHigh(100);
		randomInfo.setMiles(6);
		randomInfo.setMinutesFairlyActive(120);
		randomInfo.setMinutesLightlyActive(100);
		randomInfo.setMinutesSedentary(200);
		randomInfo.setMinutesUVExposure(150);
		randomInfo.setMinutesVeryActive(200);
		
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
	
	@When("^I click on the previous button to go to the previous month$")
	public void clickPreviousMonth()
	{
		try
		{
			driver.findElement(By.name("j_idt16:j_idt17")).click();
		}
		catch (NoSuchElementException Exception)
		{
			Assert.fail("Could not go to previous month");
		}
	}
	
	@SuppressWarnings("deprecation")
	@When("^I click on the previous month day (\\d+)")
	public void clickOnPrevMonthDay(int day)
	{
		try
		{
			//The naming convention for these "Add" or "View/Edit" links is fitnessData-Day
			//driver.findElement(By.name("fitnessData-" + day)).click();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
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
	
	@Then("^I can see the patient’s fitness data for that date$")
	public void verifyFitnessData()
	{
		//First, get the fitness data from the screen
		WebElement calsBBox = driver.findElement(By.id("j_idt21:fiCalsBurned"));
		WebElement stepsBox = driver.findElement(By.id("j_idt21:fiSteps"));
		WebElement milesBox = driver.findElement(By.id("j_idt21:fiMiles"));
		WebElement floorsBox = driver.findElement(By.id("j_idt21:fiFloors"));
		WebElement minSedBox = driver.findElement(By.id("j_idt21:fiSedentary"));
		WebElement minLightBox = driver.findElement(By.id("j_idt21:fiLightlyActive"));
		WebElement minFairBox = driver.findElement(By.id("j_idt21:fiFairlyActive"));
		WebElement minHighBox = driver.findElement(By.id("j_idt21:fiVeryActive"));
		WebElement calsABox = driver.findElement(By.id("j_idt21:fiActiveCals"));
		//Now check various values
		Assert.assertEquals(randomInfo.getCaloriesBurned(), Integer.parseInt(calsBBox.getAttribute("value"))); //if this doesn't work, try getText()
		Assert.assertEquals(randomInfo.getSteps(), Integer.parseInt(stepsBox.getAttribute("value")));
		Assert.assertTrue(Math.abs(randomInfo.getMiles() - Double.parseDouble(milesBox.getAttribute("value"))) < 0.01);
		Assert.assertEquals(randomInfo.getFloors(), Integer.parseInt(floorsBox.getAttribute("value")));
		Assert.assertEquals(randomInfo.getMinutesSedentary(), Integer.parseInt(minSedBox.getAttribute("value")));
		Assert.assertEquals(randomInfo.getMinutesLightlyActive(), Integer.parseInt(minLightBox.getAttribute("value")));
		Assert.assertEquals(randomInfo.getMinutesFairlyActive(), Integer.parseInt(minFairBox.getAttribute("value")));
		Assert.assertEquals(randomInfo.getMinutesVeryActive(), Integer.parseInt(minHighBox.getAttribute("value")));
		Assert.assertEquals(randomInfo.getActiveCalories(), Integer.parseInt(calsABox.getAttribute("value")));
		
	}
	
}
