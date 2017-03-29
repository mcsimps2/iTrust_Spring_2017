package edu.ncsu.csc.itrust.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class ObstetricsOfficeVisitStepDefs {
	private iTrustDriver driver;
	
	public ObstetricsOfficeVisitStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@When("^I navigate to Office Visit -> Document Office Visit$")
	public void navigateToDocumentOfficeVisit() {
		try {
			driver.findElement(By.linkText("Document Office Visit")).click();
		} catch (NoSuchElementException e) {
			Assert.fail("Could not click on the Document Office Visit link");
		}

		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@When("^click on the first office visit on the office visits page$")
	public void clickOnFirstOfficeVisit() {
		try {
			driver.findElement(By.cssSelector("#previousVisits tbody tr:first-child td:first-child a")).click();
		} catch (NoSuchElementException e) {
			Assert.fail("Could not click on first office visit link");
		}
		
		Assert.assertTrue(driver.getPageSource().contains("Document Office Visit"));
	}
	
	@When("^the obstetrics tab is there$")
	public void obstetricsTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-obstetrics\"]"));
			driver.findElement(By.cssSelector("input#tab-obstetrics"));
		} catch (NoSuchElementException e) {
			Assert.fail("The obstetrics tab isn't there on the office visit page");
		}
	}
	
	@When("^the ultrasound tab is there$")
	public void ultrasoundTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-ultrasound\"]"));
			driver.findElement(By.cssSelector("input#tab-ultrasound"));
		} catch (NoSuchElementException e) {
			Assert.fail("The ultrasound tab isn't there on the office visit page");
		}
	}
	
	@Then("^this scenario is not implemented yet$")
	public void obstetricsOfficeVisitTestNotImplemented() {
		Assert.assertTrue(true);
	}
}