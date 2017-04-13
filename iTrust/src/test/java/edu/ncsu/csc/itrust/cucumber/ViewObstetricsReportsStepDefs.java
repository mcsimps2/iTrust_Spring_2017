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
	
	@Then("^the report displays correctly$")
	public void reportDisplaysCorrectly() {
		String pageSource = driver.getPageSource();
		
		Assert.assertTrue(pageSource.contains("Labor and Delivery Report"));
		Assert.assertTrue(pageSource.contains("ABPos"));
		Assert.assertTrue(pageSource.contains("42w 6d"));
		Assert.assertTrue(pageSource.contains("28w 4d"));
		Assert.assertTrue(pageSource.contains("No, RH- flag not present"));
		Assert.assertTrue(pageSource.contains("Yes, 66"));
		Assert.assertTrue(pageSource.contains("None observed"));
	}
}