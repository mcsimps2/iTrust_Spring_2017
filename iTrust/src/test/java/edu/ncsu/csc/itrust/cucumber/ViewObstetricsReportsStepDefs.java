package edu.ncsu.csc.itrust.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class ViewObstetricsReportsStepDefs {
	private iTrustDriver driver;
	
	public ViewObstetricsReportsStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@When("^I click Generate Report on the first entry$")
	public void clickGenerateReport() {
		driver.findElement(By.xpath("//*[@id=\"previousRecords:0:j_idt42\"]/input[2]")).click();
	}
	
	@When("^I click Done$")
	public void clickDone() {
		driver.findElement(By.id("doneForm:doneButton")).click();
	}
	
	@Then("^the report for the new record displays correctly$")
	public void reportNewRecordDisplaysCorrectly() {
		String pageSource = driver.getPageSource();
		
		Assert.assertTrue(pageSource.contains("Labor and Delivery Report"));
		Assert.assertTrue(pageSource.contains("ABPos"));
		Assert.assertTrue(pageSource.contains("42w 6d"));
		Assert.assertTrue(pageSource.contains("28w 4d"));
		Assert.assertTrue(pageSource.contains("No, RH- flag not present"));
		Assert.assertTrue(pageSource.contains("Yes, 66"));
		Assert.assertTrue(pageSource.contains("None observed"));
	}
	
	@Then("^the report for the existing record displays correctly$")
	public void reportExistingRecordDisplaysCorrectly() {
		String pageSource = driver.getPageSource();
		Assert.assertTrue(pageSource.contains(""));
		
		// Sanity check: title of report should be there
		Assert.assertTrue(pageSource.contains("Labor and Delivery Report"));
		
		// Pregnancy info section
		Assert.assertTrue(pageSource.contains("ABPos")); // Blood type
		Assert.assertTrue(pageSource.contains("December 21, 2017")); // EDD
		
		// Obstetrics office visits section - line 1
		Assert.assertTrue(pageSource.contains("February 22, 2017")); // Date
		Assert.assertTrue(pageSource.contains("22")); // Weeks pregnant
		Assert.assertTrue(pageSource.contains("105.1")); // Weight
		Assert.assertTrue(pageSource.contains("120/80")); // Blood pressure
		Assert.assertTrue(pageSource.contains("130")); // FHR
		
		// Obstetrics office visits section - line 2
		Assert.assertTrue(pageSource.contains("April 22, 2017")); // Date
		Assert.assertTrue(pageSource.contains("30")); // Weeks pregnant
		Assert.assertTrue(pageSource.contains("125.1")); // Weight
		Assert.assertTrue(pageSource.contains("120/80")); // Blood pressure
		Assert.assertTrue(pageSource.contains("135")); // FHR
		
		// Past pregnancies
		Assert.assertTrue(pageSource.contains("2016"));
		Assert.assertTrue(pageSource.contains("2014"));
		Assert.assertTrue(pageSource.contains("42w 6d"));
		Assert.assertTrue(pageSource.contains("28w 4d"));
		Assert.assertTrue(pageSource.contains("Vaginal Delivery"));
		
		// Complications
		// We test all complications later - this is just a sanity check that things are there
		Assert.assertTrue(pageSource.contains("No, RH- flag not present")); // RH flag
		Assert.assertTrue(pageSource.contains("Yes, 66")); // Advanced maternal age
		Assert.assertTrue(pageSource.contains("None observed")); // Low-lying placenta
	}
}