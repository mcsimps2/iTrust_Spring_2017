package edu.ncsu.csc.itrust.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class ViewObstetricsRecordsStepDefs {

	private iTrustDriver driver;
	
	public ViewObstetricsRecordsStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@Given("^I have navigated to Patient Info -> Obstetrics Records$")
	public void navigateToObstetricsRecords() {
		//Find the link to patient fitness data and click it
		System.out.println(driver.getCurrentUrl());
		try
		{
			driver.findElement(By.linkText("Obstetrics Records")).click();
		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not click on Obstetrics Records link");
		}
		//Make sure we are at the select a patient screen
		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@Then("^an obstetrics record appears with date (.+)$")
	public void anObstetricsRecordAppears(String date)
	{
		Assert.assertTrue(driver.findElement(By.cssSelector("#previousRecords tr:first-child td:first-child")).getText().equals(date));
	}
	
	@When("^I click the button to make the patient eligable for obstetrics care$")
	public void makeEligableObstetrics()
	{
		driver.findElement(By.cssSelector("input[value=\"Make Eligible\"]")).click();
	}
	
	@Then("^no obstetrics records appear$")
	public void noObstetricsRecordsAppear(String date)
	{
		Assert.assertTrue(driver.findElement(By.cssSelector("#previousRecords tr:first-child td:first-child")).getText().equals(date));
	}
	
	@Then("^the add record button will not be displayed$")
	public void noAddButton()
	{
		Assert.assertFalse(driver.getPageSource().contains("Add New Record"));
	}
	
	@When("^I click on the first obstetrics record$")
	public void clickFirstObstetricsRecord()
	{
		driver.findElement(By.cssSelector("tbody tr:first-child form")).submit();
	}
	
	@When("^I decide to select another patient$")
	public void selectAnotherPatient()
	{
		driver.findElement(By.linkText("Select a Different Patient")).click();
	}
	
	@Then("^the following data will be displayed: (.+), (.+), (.+), (.+)$")
	public void obstetricsRecordDataDisplayed(String initDate, String lmp, String edd, String weeksPreg)
	{
		Assert.assertTrue(driver.findElement(By.cssSelector(".obstetrics-record-container tbody tr:first-child td:nth-child(2)")).getText().equals(initDate));
		Assert.assertTrue(driver.findElement(By.cssSelector(".obstetrics-record-container tbody tr:nth-child(2) td:nth-child(2)")).getText().equals(lmp));
		Assert.assertTrue(driver.findElement(By.cssSelector(".obstetrics-record-container tbody tr:nth-child(3) td:nth-child(2)")).getText().equals(edd));
		Assert.assertTrue(driver.findElement(By.cssSelector(".obstetrics-record-container tbody tr:nth-child(4) td:nth-child(2)")).getText().equals(weeksPreg));
	}
	
	@Then("^there will be (\\d+) prior pregnancies$")
	public void checkPriorPregnancies(int numPreg) {
		try {
			driver.findElement(By.cssSelector(".prior-pregnancies-wrapper tbody tr:nth-child(" + numPreg + ")"));
		} catch (NoSuchElementException e) {
			Assert.fail("Can't find prior pregancy at row " + numPreg);
		}
		
		try {
			driver.findElement(By.cssSelector(".prior-pregnancies-wrapper tbody tr:nth-child(" + (numPreg + 1) + ")"));
			Assert.fail("Too many prior pregnancies shown");
		} catch (NoSuchElementException e) {
			//Good. There shouldn't be that element
		}
	}
}
