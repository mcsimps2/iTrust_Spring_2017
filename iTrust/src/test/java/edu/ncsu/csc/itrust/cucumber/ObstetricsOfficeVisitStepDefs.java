package edu.ncsu.csc.itrust.cucumber;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;

public class ObstetricsOfficeVisitStepDefs {
	private iTrustDriver driver;
	
	private int numUltrasounds = 0;
	
	public ObstetricsOfficeVisitStepDefs(iTrustDriver driver) {
		this.driver = driver;
	}
	
	@When("^I navigate to Office Visit -> Document Office Visit$")
	public void navigateToDocumentOfficeVisit() {
		try {
			driver.findElement(By.linkText("Document Office Visit")).click();
		} catch (NoSuchElementException e) {
			Assert.fail("Could not click on the Document Office Visit link");
		}

		Assert.assertTrue(driver.getPageSource().contains("Select a Patient"));
	}
	
	@When("^click on the first office visit on the office visits page$")
	public void clickOnFirstOfficeVisit() {
		try {
			driver.findElement(By.cssSelector("#previousVisits tbody tr:first-child td:first-child a")).click();
		} catch (NoSuchElementException e) {
			Assert.fail("Could not click on first office visit link");
		}
		
		Assert.assertTrue(driver.getPageSource().contains("Document Office Visit"));
	}
	
	@When("^the obstetrics tab is there$")
	public void obstetricsTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-obstetrics\"]"));
			driver.findElement(By.cssSelector("input#tab-obstetrics"));
		} catch (NoSuchElementException e) {
			Assert.fail("The obstetrics tab isn't there on the office visit page");
		}
	}
	
	@When("^the ultrasound tab is there$")
	public void ultrasoundTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-ultrasound\"]"));
			driver.findElement(By.cssSelector("input#tab-ultrasound"));
		} catch (NoSuchElementException e) {
			Assert.fail("The ultrasound tab isn't there on the office visit page");
		}
	}
	
	@When("^enter (.+) for FHR, (.+) for multiplicity, and (.+) for low-lying placenta$")
	public void enterDataToObstetricsOfficeVisitTab(String fhr, String mult, String llp) {
		driver.findElement(By.id("obstetrics_form:fhr")).clear();
		driver.findElement(By.id("obstetrics_form:fhr")).sendKeys(fhr);

		driver.findElement(By.id("obstetrics_form:multiplicity")).clear();
		driver.findElement(By.id("obstetrics_form:multiplicity")).sendKeys(mult);
		
		boolean llpSelected = driver.findElement(By.id("obstetrics_form:placenta")).isSelected();
		
		if (!llp.equals("yes") && !llp.equals("no")) {
			Assert.fail("The llp argument must be \"yes\" or \"no\", but it is \"" + llp + "\"");
		}
		
		if ((llp.equals("yes") && !llpSelected) ||
		    (llp.equals("no")  &&  llpSelected)) {
			driver.findElement(By.id("obstetrics_form:placenta")).click();
		}
	}
	
	@When("^click Save on the obstetrics office visit tab$")
	public void clickSaveOnObstetricsOfficeVisitTab() {
		driver.findElement(By.id("obstetrics_form:submitObstetricsButton")).click();
	}
	
	@When("^the obstetrics tab should have a FHR of (.+), a multiplicity of (.+), and (.+) for low-lying placenta$")
	public void checkObstetricsForm(String expectedFHR, String expectedMult, String expectedLLP) {
		String fhr = driver.findElement(By.id("obstetrics_form:fhr")).getAttribute("value");
		String mult = driver.findElement(By.id("obstetrics_form:multiplicity")).getAttribute("value");
		
		boolean llpBoolean = driver.findElement(By.id("obstetrics_form:placenta")).isSelected();
		String llp = llpBoolean ? "yes" : "no";
		
		Assert.assertTrue("fhr \"" + fhr + "\" should match expectedFHR \"" + expectedFHR + "\"", fhr.equals(expectedFHR));
		Assert.assertTrue("mult \"" + mult + "\" should match expectedMult \"" + expectedMult + "\"", mult.equals(expectedMult));
		Assert.assertTrue("llp \"" + llp + "\" should match expectedLLP \"" + expectedLLP + "\"", llp.equals(expectedLLP));
	}
	
	@When("^I check the number of ultrasounds already in the table$")
	public void checkNumUltrasounds() {
		List<WebElement> rows = driver.findElements(By.cssSelector("#ultrasound_table_form table tbody tr"));
		this.numUltrasounds = rows.size();
	}
	
	@When("^I enter (.+) for CRL, (.+) for BPD, (.+) for HC, (.+) for FL, (.+) for OFD, (.+) for AC, (.+) for HL, and (.+) for EFW$")
	public void enterUltrasoundData(String crl, String bpd, String hc, String fl, String ofd, String ac, String hl, String efw) {
		driver.findElement(By.id("ultrasound_form:crl")).sendKeys(crl);
		driver.findElement(By.id("ultrasound_form:bpd")).sendKeys(bpd);
		driver.findElement(By.id("ultrasound_form:hc" )).sendKeys(hc);
		driver.findElement(By.id("ultrasound_form:fl" )).sendKeys(fl);
		driver.findElement(By.id("ultrasound_form:ofd")).sendKeys(ofd);
		driver.findElement(By.id("ultrasound_form:ac" )).sendKeys(ac);
		driver.findElement(By.id("ultrasound_form:hl" )).sendKeys(hl);
		driver.findElement(By.id("ultrasound_form:efw")).sendKeys(efw);
	}
	
	@When("^click Add Fetus Data on the ultrasound office visit tab$")
	public void clickAddFetusData() {
		driver.findElement(By.id("ultrasound_form:addFetusData")).click();
	}
	
	@When("^upload the file (.+)$")
	public void uploadUltrasoundFile(String filename) {
		String cwd = System.getProperty("user.dir");
		String dir = "testing-files/sample_obstetrics/";
		String filepath = cwd + dir + filename;
		
		driver.findElement(By.id("ultrasound_image_form:file")).sendKeys(filepath);
		driver.findElement(By.id("ultrasound_image_form:uploadButton")).click();
		
		Assert.assertTrue(driver.getPageSource().contains("File Successfully Uploaded"));
	}
	
	@Then("^the ultrasound images were uploaded successfully$")
	public void ultrasoundImagesUploadedSuccessfully() {
		Assert.assertTrue(driver.getPageSource().contains("Download Ultrasound Image"));
	}
	
	@Then("^two more ultrasounds exist in the ultrasound table than before$")
	public void twoUltrasoundsAppear() {
		List<WebElement> rows = driver.findElements(By.cssSelector("#ultrasound_table_form table tbody tr"));
		
		Assert.assertEquals("Two more ultrasounds should exist in the ultrasound table", rows.size(), this.numUltrasounds + 2);
	}
	
	@Then("^this scenario is not implemented yet$")
	public void obstetricsOfficeVisitTestNotImplemented() {
		Assert.assertTrue(true);
	}
}