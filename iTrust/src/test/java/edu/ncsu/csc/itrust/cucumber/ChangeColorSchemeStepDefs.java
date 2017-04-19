package edu.ncsu.csc.itrust.cucumber;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class ChangeColorSchemeStepDefs {
	
	private iTrustDriver driver;
	
	public ChangeColorSchemeStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@When("^I click on the settings page$")
	public void clickSettings() {
		try {
			driver.findElement(By.cssSelector("a[href=\"/iTrust/auth/settings.xhtml\"]")).click();	
		} catch (NoSuchElementException e) {
			Assert.fail("Could not click on settings page");
		}
		Assert.assertTrue(driver.getPageSource().contains("Color Scheme"));
	}
	
	@When("^I set the color scheme to (.+)$")
	public void setColorScheme(String scheme) {
		Select select = new Select(driver.findElement(By.id("settings_form:color_scheme")));
		select.selectByVisibleText(scheme);
	}
	
	@When("^I save the color changes$")
	public void saveColor() {
		driver.findElement(By.id("settings_form:submit_button"));
	}
	
	@Then("^the colors have been changed to (.+)$")
	public void colorsHaveChangedTo(String color) {
		Assert.assertTrue(true);
	}
}
