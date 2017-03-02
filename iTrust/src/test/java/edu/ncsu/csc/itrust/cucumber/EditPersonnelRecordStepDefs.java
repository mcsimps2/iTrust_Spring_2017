package edu.ncsu.csc.itrust.cucumber;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditPersonnelRecordStepDefs {

	private WebDriver driver = null;
	
	public EditPersonnelRecordStepDefs() {
		this.driver = new HtmlUnitDriver();
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Given("^That I am logged in as HCP 1$")
	public void logging_in() {
		driver.get("http://localhost:8080/iTrust/auth/forwardUser.jsp");
		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Assert.assertTrue("Should have logged in", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp/home.jsp"));
	}
	
	@When("^I open the personal info tab and click on the my demographics link$")
	public void open_demogrpahics() {
		driver.findElement(By.id("pi-menu")).click();
		driver.findElement(By.cssSelector("a[href=\"/iTrust/auth/staff/editMyDemographics.jsp\"]")).click();

		Assert.assertTrue("Should have gotten to the demographics page", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/staff/editMyDemographics.jsp"));
	}
	
	@When("^I edit the last name field$")
	public void edit_last_name() {
		driver.findElement(By.name("lastName")).clear();
		driver.findElement(By.name("lastName")).sendKeys("Flowers");
	}
	
	@When("^I press the edit personnel records button$")
	public void press_button() {
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();		
	}
	
	@When("^I got to the home page$")
	public void return_to_homepage() {
		driver.findElement(By.cssSelector("a[href=\"/iTrust\"]")).click();
		Assert.assertTrue(driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp/home.jsp"));
	}
	
	@Then("^my last name should be properly changed$")
	public void check_changed_name() {
		Assert.assertTrue("Name should be changed", driver.getPageSource().contains("Welcome, Kelly Flowers"));
	}
	
}
