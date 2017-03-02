package edu.ncsu.csc.itrust.cucumber;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;

import com.gargoylesoftware.htmlunit.Page;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.MyDriver;

/**
 * Tests the messaging inbox to see if it properly 
 * marks read messages as read.
 * @author Nicholas Anthony (nranthon)
 */
public class MessagingInboxStepDefs {

	private MyDriver driver = null;
	
	private Page cached = null;
	
	private boolean cachingEnabled;
	
	public MessagingInboxStepDefs() throws Exception {
		this.driver = new MyDriver();
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Given("^I am logged in as HCP 1$")
	public void logged_In() {
		driver.get("http://localhost:8080/iTrust/auth/forwardUser.jsp");
		driver.findElement(By.id("j_username")).sendKeys("9000000000");
		driver.findElement(By.id("j_password")).sendKeys("pw");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
		Assert.assertTrue("Should have logged in", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp/home.jsp"));
	}
	
	@When("^I open the messaging tab and click on the messaging inbox$")
	public void message_tab() {
		driver.findElement(By.id("msg-menu")).click();
		driver.findElement(By.cssSelector("a[href=\"/iTrust/auth/hcp-patient/messageInbox.jsp\"]")).click();

		Assert.assertTrue("Should have gotten to the messaging inbox page", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp-patient/messageInbox.jsp"));
	}
	
	@When("^I click on an unread message$")
	public void unread_message() {
		cachingEnabled = driver.cachingEnabled();
		if (cachingEnabled)
			cached = driver.savePage();
		driver.findElement(By.cssSelector("tr[style=\"font-weight: bold;\"]:first-child a")).click();
		Assert.assertFalse("Should have gotten to the messaging inbox page", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp-patient/messageInbox.jsp"));

	}
	
	@When("^I press the back button on the web browser$")
	public void press_back() {
		if (cachingEnabled)
			driver.getSavedPage(cached);
		else
			driver.get("http://localhost:8080/iTrust/auth/hcp-patient/messageInbox.jsp");
		Assert.assertTrue("Should have gotten to the messaging inbox page", driver.getCurrentUrl().equals("http://localhost:8080/iTrust/auth/hcp-patient/messageInbox.jsp"));
	}
	
	@Then("^the previously opened message will be marked as read$")
	public void marked_as_read() {
		Assert.assertTrue(driver.findElement(By.cssSelector("tbody tr:first-child")).getAttribute("style") == null);
	}
}
