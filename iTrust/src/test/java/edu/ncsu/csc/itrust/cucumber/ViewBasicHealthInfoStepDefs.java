package edu.ncsu.csc.itrust.cucumber;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ViewBasicHealthInfoStepDefs {

	private HtmlUnitDriver driver;
	private static final String BASEURL = "http://localhost:8080/iTrust";
	
	public ViewBasicHealthInfoStepDefs(){
		this.driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Given("^user is on the login screen$")
	public void user_is_on_login_screen(){
		driver.get(BASEURL + "/");
	}
	
	@When("^user enters (\\S+) in the MID field and (\\S+) in the Password field and clicks login$")
	public void user_enters_mid_and_pass(String mid, String pass) {
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(mid);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(pass);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
	}
	
	@When("^user clicks Patient Info then Basic Health Information")
	public void user_clicks_patient_info_then_basic_health_information() {
		driver.findElement(By.cssSelector("a[href=\"/iTrust/auth/hcp/viewBasicHealthInfo.xhtml\"]")).click();
	}
	
	@When("^user enters (\\S+) in the search field$")
	public void user_enters_in_search_field(String s) {
		driver.findElement(By.cssSelector("input[name=\"FIRST_NAME\"]")).sendKeys("Random");
		driver.findElement(By.cssSelector("input[value=\"User Search\"]")).click();
	}
	
	@When("^user clicks on the first patient shown")
	public void user_clicks_on_first_patient() {
		driver.findElement(By.id("selectPatient1")).submit();
	}
	
	@Then("^the basic health info page is displayed")
	public void basic_health_info_displayed() {
		Assert.assertTrue(driver.getTitle().equals("iTrust - View Patient Records"));
	}
	
}
