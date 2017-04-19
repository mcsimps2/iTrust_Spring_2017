package edu.ncsu.csc.itrust.cucumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;


import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;


public class PatientProfilePictureStepDefs
{
	private iTrustDriver driver;
	
	public PatientProfilePictureStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@When("^I navigate to Patient Information$")
	public void i_navigate_to_Patient_Information() {
		try
		{
			driver.findElement(By.linkText("Patient Information")).click();
		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not click on Patient Information link");
		}
		//Make sure we are at the select a patient screen
		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@When("^I navigate to Basic Health Information$")
	public void i_navigate_to_Basic_Health_Information() {
		try
		{
			driver.findElement(By.linkText("Basic Health Information")).click();
		} catch (NoSuchElementException e)
		{
			Assert.fail("Could not click on Basic Health Information link");
		}
		//Make sure we are at the select a patient screen
		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@Then("^a profile picture appears$")
	public void a_default_profile_picture_appears() {
		//Assert an image is displaying, which is true if there is a hyperlink to the image servlet
		Assert.assertTrue(driver.getPageSource().contains("/iTrust/ImageServlet"));
	}
	
	@Then("^no profile picture appears$")
	public void no_profile_picture() {
		Assert.assertFalse(driver.getPageSource().contains("/iTrust/ImageServlet"));
	}
	
	@When("^I upload a profile picture (.+)$")
	public void i_upload_a_profile_picture_testing_files_sample_profilepictures_profile_jpg(String filePath) {
		driver.findElement(By.name("file")).sendKeys("/Users/msimpson/Documents/development/csc326-203-Project-05/iTrust/" + filePath);
		driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/table/tbody/tr/td[3]/table[3]/tbody/tr[2]/td/form/input[3]")).click();
	}

	@Then("^the profile picture is saved in the database$")
	public void the_profile_picture_is_saved_in_the_database() {
		//Assert there is a new image besides just "Default" in the database
		//It should be called patient2
		try (Connection conn = TestDAOFactory.getTestInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM image");
				ResultSet rs = ps.executeQuery();)
		{
			boolean found = false;
			while (rs.next())
			{
				String name = rs.getString("name");
				if (name.equals("patient2"))
				{
					found = true;
					break;
				}
			}
			Assert.assertTrue(found);
		}
		catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I upload a picture (.+) but don't submit anything$")
	public void i_click_the_upload_button_for_profile_pictures_but_don_t_submit_anything(String filePath) {
		driver.findElement(By.name("file")).sendKeys("/Users/msimpson/Documents/development/csc326-203-Project-05/iTrust/" + filePath);
		//Don't click the submit button! This is an error path
	}

	@Then("^the database is unchanged$")
	public void the_database_is_unchanged() {
		try (Connection conn = TestDAOFactory.getTestInstance().getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM image");
				ResultSet rs = ps.executeQuery();)
		{
			boolean found = false;
			while (rs.next())
			{
				String name = rs.getString("name");
				if (name.equals("patient2"))
				{
					found = true;
					break;
				}
			}
			Assert.assertFalse(found);
		}
		catch (SQLException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
