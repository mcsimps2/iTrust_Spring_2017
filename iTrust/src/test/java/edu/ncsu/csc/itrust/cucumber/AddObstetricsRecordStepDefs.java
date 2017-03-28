package edu.ncsu.csc.itrust.cucumber;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class AddObstetricsRecordStepDefs {

	private iTrustDriver driver;
	
	public AddObstetricsRecordStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@Given("^I have logged in as OBGYN with MID 9000000012 and password pw")
	public void loggedInOBGYN() {
		// Make sure we are at the login screen
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/forwardUser.jsp"));
				
		//login with the given mid and password
		driver.findElement(By.name("j_username")).sendKeys("9000000012");
		driver.findElement(By.name("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		//Check if we logged in correctly
		if (driver.getTitle().equals("iTrust - Login"))
			Assert.fail("Error logging in, user not in database?");
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
	}
	
	@When("^I click Add New Record$")
	public void clickAddNewRecord() {
		driver.findElement(By.cssSelector("input[value=\"Add New Record\"]")).submit();
	}
	
	@When("^I enter an lmp in for the LMP field$")
	public void enterLMP() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String date = ft.format(c.getTime());
		driver.findElement(By.cssSelector(".record-info-table input")).sendKeys(date);
	}
	
	@When("^I enter a prior pregnancy with values: conception year (.+), weeks pregnant (.+), hours in labor (.+), weight gain (.+), delivery type (.+), multiplicity (.+)$")
	public void enterPriorPregnancy(String cYear, String weeksPreg, String hrsLabor, String weightGain, String deliveryType, String mult) {
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt47\"]")).sendKeys(cYear);
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt49\"]")).sendKeys(weeksPreg);
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt51\"]")).sendKeys(hrsLabor);
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt53\"]")).sendKeys(weightGain);
		Select s = new Select(driver.findElement(By.cssSelector("select[name=\"j_idt20:j_idt55\"]")));
		s.selectByVisibleText(deliveryType);
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt58\"]")).sendKeys(mult);
	}
	
	@When("^I click add pregnancy$")
	public void addPregnancy() {
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt60\"]")).submit();
	}
	
	@When("^I click Save Record")
	public void saveRecord() {
		driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt67\"]")).click();
	}
	
	@Then("^I am redirected to obstetrics records page$")
	public void redirectedToObstetricsRecordsPage() {
		driver.verifyLocation("asdf");
	}
	
	@Then("^a success message appears indicating save successful$")
	public void successMessageAppears() {
		Assert.assertTrue(driver.getPageSource().contains("success"));
	}
	
	@Then("^there is an error message appears about invalid input$")
	public void errorMessageInvalidInput() {
		Assert.assertTrue(driver.getPageSource().contains("Error"));
	}
	
	@When("^I click cancel$")
	public void clickCancel() {
		driver.findElement(By.cssSelector("input[value=\"Cancel\"]")).click(); //TODO: This will probably change, they both shouldn't be submit inputs
	}
	
	@Then("^all the fields are empty")
	public void allFieldsEmpty() {
		Assert.assertTrue(driver.findElement(By.cssSelector(".record-info-table input")).getText().length() == 0);
		Assert.assertTrue(driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt47\"]")).getText().length() == 0);
		Assert.assertTrue(driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt49\"]")).getText().length() == 0);
		Assert.assertTrue(driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt51\"]")).getText().length() == 0);
		Assert.assertTrue(driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt53\"]")).getText().length() == 0);
		Assert.assertTrue(driver.findElement(By.cssSelector("input[name=\"j_idt20:j_idt58\"]")).getText().length() == 0);
	}
}
