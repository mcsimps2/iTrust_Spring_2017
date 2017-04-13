package edu.ncsu.csc.itrust.cucumber;

import org.openqa.selenium.By;

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
}