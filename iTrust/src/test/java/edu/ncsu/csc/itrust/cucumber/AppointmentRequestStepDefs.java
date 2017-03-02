package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

public class AppointmentRequestStepDefs {
	private final static String BASEURL = "http://localhost:8080/iTrust";
	private HtmlUnitDriver driver;
	
	public AppointmentRequestStepDefs() {
		this.driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Given("^patient with username (\\d+) and password (\\S+) has requested an appointment with HCP ID (\\S+) for (\\d+)/(\\d+)/(\\d+) with keyword (\\S+) in the comment$")
	public void appointmentHasBeenRequested(String username, String password,
			String hcpid, String month, String day, String year, String keyword) {

		driver.get(BASEURL + "/");
		
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(username);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(password);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Assert.assertTrue(driver.getPageSource().contains("Welcome,"));
		
		driver.findElementByXPath("//*[contains(text(), 'Appointment Requests')]").click();

		// Select the specified HCP (health care provider)
		Select select = new Select(driver.findElement(By.cssSelector("select[name='hcpid']")));
		select.selectByValue(hcpid);
		
		String startDate = month + "/" + day + "/" + year;
		driver.findElement(By.cssSelector("input[name=startDate]")).clear();
		driver.findElement(By.cssSelector("input[name=startDate]")).sendKeys(startDate);
		driver.findElement(By.cssSelector("textarea[name=comment]")).clear();
		driver.findElement(By.cssSelector("textarea[name=comment]")).sendKeys(keyword);
		driver.findElement(By.cssSelector("input[type=submit]")).click();
		
		driver.findElement(By.cssSelector("a[href=\"/iTrust/logout.jsp\"]")).click();
	}
	
	@When("^I log in as user (\\S+) with password (\\S+)$")
	public void login(String username, String password) {
		driver.get(BASEURL + "/");
		
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(username);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(password);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Assert.assertTrue(driver.getPageSource().contains("Welcome,"));
	}
	
	@When("^I check my appointment requests$")
	public void checkAppointmentRequests() {
		driver.findElementByXPath("//*[contains(text(), 'Appointment Requests')]").click();
	}
	
	@Then("^I do not have an appointment request for (\\S+) with keyword (\\S+)")
	public void noAppointmentRequest(String date, String keyword) {
		Assert.assertFalse(driver.getPageSource().contains("At time: " + date));
		Assert.assertFalse(driver.getPageSource().contains("Comment: " + keyword));
	}
	
	@Then("^I have an appointment request for (\\S+) with keyword (\\S+)")
	public void appointmentRequest(String date, String keyword) {
		Assert.assertTrue(driver.getPageSource().contains("At time: " + date));
		Assert.assertTrue(driver.getPageSource().contains("Comment: " + keyword));
	}
}