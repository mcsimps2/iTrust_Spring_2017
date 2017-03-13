package edu.ncsu.csc.itrust.cucumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.controller.fitness.FitnessInfoController;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitbitImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoFileFormatException;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class UploadFitnessDataStepDefs {
	
	/** Selenium WebDriver used to navigate for tests */
	iTrustDriver driver;
	/** Fitness data file path shared between steps */
	private String filePath;
	/** Whether or not the last file upload attempt was successful */
	private boolean fileUploaded;
	
	
	/**
	 * Constructor that takes a Selenium WebDriver to do the navigation for the tests
	 * @param driver a WebDriver to do the navigation for the tests
	 */
	public UploadFitnessDataStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
	}
	
	@Given("^I'm at the iTrust login screen$")
	public void atLogin() {
		//log out of any potential logged in user and go to login screen
		driver.loadPage("/iTrust/logout.jsp");
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/forwardUser.jsp")); //make sure we are at the login screen
	}
	
	@Given("^I have logged in with MID: (.+) and password: (.+)$")
	public void login(String mid, String pwd) {
		// Make sure we are at the login screen
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/forwardUser.jsp"));
				
		//login with the given mid and password
		driver.findElement(By.name("j_username")).sendKeys(mid);;
		driver.findElement(By.name("j_password")).sendKeys(pwd);;
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

		//Check if we logged in correctly
		if (driver.getTitle().equals("iTrust - Login"))
			Assert.fail("Error logging in, user not in database?");
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
	}
	
	@Given("^I have pulled up the patient calendar for Patient 1 with name (.+)$")
	public void toFitnessCalendar(String name) {		
		// Make sure we're at the home page
		Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/home.jsp"));
		
		try {
			// Navigate to Patient Info -> Patient Fitness Data
			driver.findElement(By.linkText("Patient Fitness Data")).click();
			
			// Make sure we are at the select a patient screen
			Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
			
			// Enter patient MID and search
			driver.findElement(By.cssSelector("input[name=\"FIRST_NAME\"]")).sendKeys(name);
			driver.findElement(By.cssSelector("input[value=\"User Search\"]")).click();
			
			// Select patient
			driver.findElement(By.id("selectPatient1")).submit();
			
			// Make sure that the calendar appears
			Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/fitnessCalendar.xhtml"));
			Assert.assertTrue(driver.findElements(By.id("calendarTable")).size() > 0);
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I click the upload fitness data button$")
	public void toUploadFitnessData() {
		try {
			// Click upload fitness data button
			driver.findElement(By.linkText("Upload Fitness File")).click();
			
			// Make sure we're on the right page
			Assert.assertTrue(driver.verifyLocation("/iTrust/auth/hcp/uploadFitnessInfo.xhtml"));
		} catch (NoSuchElementException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@When("^I upload the file (.+)$")
	public void uploadFitnessData(String filePath) throws IOException {
		// Save file path for future steps
		this.filePath = filePath;
		
		// Ditch the web driver, let's go below the surface to upload the data
		InputStream input = null;
		try {
			input = new FileInputStream(filePath);
			FitbitImportFactory fact = new FitbitImportFactory(ConverterDAO.getDataSource());
			fact.importFitnessInfo(1, input);
			fileUploaded = true;
		} catch (FileNotFoundException e) {
			Assert.fail(e.getMessage());
		} catch (CSVFormatException e) {
			Assert.fail(e.getMessage());
		} catch (FitnessInfoFileFormatException e) {
			fileUploaded = false;
		} finally {
			if (input != null)
				input.close();
		}
	}
	
	@Then("^the data in the file is added to the patient’s fitness data$")
	public void checkFitnessDataAdded() {
		// Check that file upload was successful
		Assert.assertTrue(fileUploaded);
		
		// Set up Scanner
		Scanner input = null;
		try {
			input = new Scanner(new File(filePath));
			
			// Read through first two lines
			input.nextLine();
			input.nextLine();
			
			// Process one line at a time
			while (input.hasNextLine()) {
				// Start by building a FitnessInfo object - we will fill this with the information in the next line
				FitnessInfo csvInfo = new FitnessInfo();
				csvInfo.setPid(1);
				String line = input.nextLine();
				
				// Split by commas
				String[] tokens = line.split(",");
				
				// We may need to merge a few of these tokens, so let's first switch to an ArrayList to make it easier
				ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(tokens));
				
				/* Now let's look for tokens that need merging
				   We know that a token starting with a double quote needs to be merged with the next token,
				   because that means it would have had a comma in between double quotes in the csv file,
				   which should be registered as one token. For example:
				   		...,"1,234",...
			   	   "1,234" should be read as one token, but the split() method above would have split it
			   	   into two tokens: ("1) and (234")
			    */
				for (int i = 0; i < tokenList.size(); i++) {
					String token = tokenList.get(i);
					if (token.startsWith("\"")) { // this token needs to be merged with the next
						// Merge the tokens
						String newToken = token + tokenList.get(i + 1); //should contain: "someText" (with quotes)
						newToken = newToken.substring(1, newToken.length() - 1); //remove beginning and ending quotes
						tokenList.set(i, newToken); // replace the old token with the new merged token
						tokenList.remove(i + 1); // remove the token that was merged with
					}
				}
				
				// Now we just need to fix the format of the date to match YYYY-MM-DD
				try {
					// Separate date token by '/'
					String[] split = tokenList.get(0).split("/");
					int month = Integer.parseInt(split[0]);
					int day = Integer.parseInt(split[1]);
					int year = Integer.parseInt(split[2]);
					
					//Convert YY to YYYY
					DateFormat formatYY = new SimpleDateFormat("yy");
					Date yearShort = formatYY.parse(year + "");
					DateFormat formatYYYY = new SimpleDateFormat("yyyy");
					String yearLong = formatYYYY.format(yearShort);
					
					// Create date string in format: YYYY-MM-DD
					String date = String.format("%s-%02d-%02d", yearLong, month, day);
					
					// Update date token
					tokenList.set(0, date);
				} catch (Exception e) {
					Assert.fail("Failed to format date from file: " + e.getMessage());
				}
				
				// Tokens should now be correct, so let's fill in the fitness bean
				csvInfo.setDate(tokenList.get(0));
				csvInfo.setCaloriesBurned(Integer.parseInt(tokenList.get(1)));
				csvInfo.setSteps(Integer.parseInt(tokenList.get(2)));
				csvInfo.setMiles(Double.parseDouble(tokenList.get(3)));
				csvInfo.setFloors(Integer.parseInt(tokenList.get(4)));
				csvInfo.setMinutesSedentary(Integer.parseInt(tokenList.get(5)));
				csvInfo.setMinutesLightlyActive(Integer.parseInt(tokenList.get(6)));
				csvInfo.setMinutesFairlyActive(Integer.parseInt(tokenList.get(7)));
				csvInfo.setMinutesVeryActive(Integer.parseInt(tokenList.get(8)));
				csvInfo.setActiveCalories(Integer.parseInt(tokenList.get(9)));
				
				// Now let's check that the data in the bean is in the database
				FitnessInfoController controller = new FitnessInfoController(ConverterDAO.getDataSource());
				FitnessInfo dbInfo = controller.getFitnessInfo(1, csvInfo.getDate());
				Assert.assertTrue("FitnessInfo does not match", csvInfo.equals(dbInfo));
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			if (input != null)
				input.close();
		}
	}
	
	@Then("^the file is not uploaded$")
	public void checkFileNotUploaded() {
		Assert.assertFalse(fileUploaded);
	}
	
}