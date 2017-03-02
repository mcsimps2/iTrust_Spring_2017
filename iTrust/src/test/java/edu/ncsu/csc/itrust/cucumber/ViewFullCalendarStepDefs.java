package edu.ncsu.csc.itrust.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ViewFullCalendarStepDefs {
	private final static String BASEURL = "http://localhost:8080/iTrust";
	private HtmlUnitDriver driver;
	
	public ViewFullCalendarStepDefs() {
		this.driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Given("^user logs in with username (\\S+) and password (\\S+)$")
	public void login(String username, String password) {
		driver.get(BASEURL + "/");
		
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(username);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(password);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Assert.assertTrue(driver.getPageSource().contains("Welcome, Random Person"));
	}
	
	@When("^user goes to the full calendar page$")
	public void goToCalendarPage() {
		driver.findElementByXPath("//*[contains(text(), 'Full Calendar')]").click();
	}
	
	@Then("^the page appears normally$")
	public void calendarPageLoaded() {
		Assert.assertFalse(driver.getPageSource().contains("HTTP Status 500"));
	}
}