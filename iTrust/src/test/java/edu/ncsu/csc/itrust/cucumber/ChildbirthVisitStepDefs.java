package edu.ncsu.csc.itrust.cucumber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.cucumber.util.iTrustDriver;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;

public class ChildbirthVisitStepDefs {	
	private static final Long TEST_OFFICE_VISIT_ID = 51L;

	private iTrustDriver driver;
	private ChildbirthVisitData cvd;
	private NewbornData nd;


	private int numNewborns;
	private Newborn recentNewborn;
	private List<Newborn> newbornList;
	private ChildbirthVisit cv;
		
	public ChildbirthVisitStepDefs(iTrustDriver driver) {
		this.driver = driver;
		this.cvd = new ChildbirthVisitMySQL(ConverterDAO.getDataSource());
		this.nd = new NewbornMySQL(ConverterDAO.getDataSource());
		
		this.newbornList = new ArrayList<Newborn>();
	}
	
	@Then("^the childbirth tab is there$")
	public void childbirthTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-childbirth\"]"));
			driver.findElement(By.cssSelector("input#tab-childbirth"));
		} catch (NoSuchElementException e) {
			Assert.fail("The childbirth tab isn't there on the office visit page");
		}
	}
	
	@Then("^the newborns tab is there$")
	public void newbornsTabExists() {
		try {
			driver.findElement(By.cssSelector("label[for=\"tab-newborns\"]"));
			driver.findElement(By.cssSelector("input#tab-newborns"));
		} catch (NoSuchElementException e) {
			Assert.fail("The newborns tab isn't there on the office visit page");
		}
	}
	
	@When("^click Save on the childbirth tab$")
	public void clickSaveOnChildbirthTab() {
		driver.findElement(By.id("childbirth_form:submitChildbirthButton")).click();
		newbornList = new ArrayList<Newborn>();
	}
	
	@When("^I check the number of newborns in the table$")
	public void checkNumNewborns() {
		List<WebElement> rows = driver.findElements(By.cssSelector("#newborn_table_form table tbody tr"));
		this.numNewborns = rows.size();
	}
	
	@When("^click Add Newborn on the newborns tab$")
	public void clickAddNewborn() {
		driver.findElement(By.id("newborn_form:addNewbornData")).click();
		newbornList.add(recentNewborn);
	}
	
	@When("^click Update Newborn$")
	public void clickUpdateNewborn() {
		driver.findElement(By.id("newborn_form:updateNewbornData")).click();
	}
	
	@When("^click the first Delete button in the newborns table$")
	public void clickDeleteInNewbornsTable() {
		WebElement table = driver.findElement(By.id("newborn_table_form:newborn_table"));
		table.findElement(By.cssSelector("#newborn_table_form input[value=\"Delete\"]")).click();
	}
	
	@When("^click the first Edit button in the newborns table$")
	public void clickEditInNewbornsTable() {
		WebElement table = driver.findElement(By.id("newborn_table_form:newborn_table"));
		table.findElement(By.cssSelector("#newborn_table_form input[value=\"Edit\"]")).click();
	}
	
	@When("^I check that there is one fewer newborn in the table$")
	public void oneFewerNewborn() {
		List<WebElement> rows = driver.findElements(By.cssSelector("#newborn_table_form table tbody tr"));
		
		Assert.assertEquals("One fewer newborn should exist in the newborns table", rows.size(), this.numNewborns - 1);
		
		numNewborns--;
	}
	
	@Then("^the same number of newborns should be in the table$")
	public void sameNumberOfNewborns() {
		List<WebElement> rows = driver.findElements(By.cssSelector("#newborn_table_form table tbody tr"));
		
		Assert.assertEquals("The same number of newborns should exist in the newborns table", rows.size(), this.numNewborns);
	}
	
	@Then("^a message indicates that only OBGYN HCPs can edit childbirth data$")
	public void obgynMessageAppears() {
		Assert.assertTrue(driver.getPageSource().contains("Only OB/GYN HCPs can edit childbirth information"));
		Assert.assertTrue(driver.getPageSource().contains("Only an OB/GYN can edit newborn information"));
	}
	
	@Then("^(\\d+) more newborns exist in the newborns table$")
	public void moreNewbornsAppear(int numMoreNewborns) {
		List<WebElement> rows = driver.findElements(By.cssSelector("#newborn_table_form table tbody tr"));
		
		Assert.assertEquals(numMoreNewborns + " more newborns should exist in the newborns table", this.numNewborns + numMoreNewborns, rows.size());
		
		numNewborns += numMoreNewborns;
	}
	
	@Then("^the childbirth form fields are disabled$")
	public void obstetricsFormFieldsAreDisabled() {
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:childbirthMethod")).getAttribute("disabled").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:visitType")).getAttribute("disabled").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:pitocin")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:nitrousOxide")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:pethidine")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("childbirth_form:magnesiumSulfate")).getAttribute("readonly").equals("true"));
	}
	
	@Then("^the newborns form fields are disabled$")
	public void ultrasoundFormFieldsAreDisabled() {
		Assert.assertTrue(driver.findElement(By.id("newborn_form:date")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("newborn_form:time")).getAttribute("readonly").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("newborn_form:sex")).getAttribute("disabled").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("newborn_form:estimated-time")).getAttribute("disabled").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("newborn_form:addNewbornData")).getAttribute("disabled").equals("true"));
		Assert.assertTrue(driver.findElement(By.id("newborn_form:updateNewbornData")).getAttribute("disabled").equals("true"));
	}
	
	@Then("^a message says I must add childbirth data first and no newborn data is added$")
	public void ultrasoundDataFailed() {
		Assert.assertTrue(driver.getPageSource().contains("The Childbirth tab must be saved before you can add a newborn"));
		Assert.assertTrue(driver.getPageSource().contains("No Newborns"));
		
		try {
			List<Newborn> results = nd.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			Assert.assertFalse("Newborn was added to database", results.contains(recentNewborn));
		} catch (DBException e) {
			e.printStackTrace();
			Assert.fail("DBException");
		}
	}
	
	@Then("^the patient's obstetrics history is present$")
	public void checkObstetricsHistory() {
		//TODO
	}
	
	@When("^I select (.+) for Childbirth Method$")
	public void selectChildbirthMethod(String birthMethod) {
		cv = new ChildbirthVisit();
		cv.setOfficeVisitID(TEST_OFFICE_VISIT_ID);
		cv.setDeliveryType(DeliveryMethod.matchString(birthMethod));
		
		Select dropdown = new Select(driver.findElement(By.id("childbirth_form:childbirthMethod")));
		dropdown.selectByVisibleText(birthMethod);
	}
	
	@When("^I select (.+) for Visit Type$")
	public void selectVisitType(String visitType) {
		cv.setVisitType(VisitType.matchString(visitType));
		
		Select dropdown = new Select(driver.findElement(By.id("childbirth_form:visitType")));
		dropdown.selectByVisibleText(visitType);
	}
	
	@When("^I enter (.+) for Pitocin, (.+) for Nitrous Oxide, (.+) for Pethidine, (.+) for Epidural Anaesthesia, and (.+) for Magnesium Sulfate$")
	public void enterDosages(String pit, String N2O, String peth, String epi, String MgSO4) {
		cv.setPitocin(Integer.parseInt(pit));
		cv.setNitrousOxide(Integer.parseInt(N2O));
		cv.setPethidine(Integer.parseInt(peth));
		cv.setEpiduralAnaesthesia(Integer.parseInt(epi));
		cv.setMagnesiumSulfate(Integer.parseInt(MgSO4));
		
		driver.findElement(By.id("childbirth_form:pitocin")).clear();
		driver.findElement(By.id("childbirth_form:pitocin")).sendKeys(pit);
		
		driver.findElement(By.id("childbirth_form:nitrousOxide")).clear();
		driver.findElement(By.id("childbirth_form:nitrousOxide")).sendKeys(N2O);
		
		driver.findElement(By.id("childbirth_form:pethidine")).clear();
		driver.findElement(By.id("childbirth_form:pethidine")).sendKeys(peth);
		
		driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).clear();
		driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).sendKeys(epi);
		
		driver.findElement(By.id("childbirth_form:magnesiumSulfate")).clear();
		driver.findElement(By.id("childbirth_form:magnesiumSulfate")).sendKeys(MgSO4);
	}
	
	@When("^I enter (.+) for all childbirth drug fields$")
	public void enterAllDosages(String dosage) {
		cv.setPitocin(Integer.parseInt(dosage));
		cv.setNitrousOxide(Integer.parseInt(dosage));
		cv.setPethidine(Integer.parseInt(dosage));
		cv.setEpiduralAnaesthesia(Integer.parseInt(dosage));
		cv.setMagnesiumSulfate(Integer.parseInt(dosage));
		
		driver.findElement(By.id("childbirth_form:pitocin")).clear();
		driver.findElement(By.id("childbirth_form:pitocin")).sendKeys(dosage);
		
		driver.findElement(By.id("childbirth_form:nitrousOxide")).clear();
		driver.findElement(By.id("childbirth_form:nitrousOxide")).sendKeys(dosage);
		
		driver.findElement(By.id("childbirth_form:pethidine")).clear();
		driver.findElement(By.id("childbirth_form:pethidine")).sendKeys(dosage);
		
		driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).clear();
		driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).sendKeys(dosage);
		
		driver.findElement(By.id("childbirth_form:magnesiumSulfate")).clear();
		driver.findElement(By.id("childbirth_form:magnesiumSulfate")).sendKeys(dosage);
	}
	
	@Then("^the childbirth tab has those fields$")
	public void checkChildbirthFields() {
		Select dropdown = new Select(driver.findElement(By.id("childbirth_form:childbirthMethod")));
		String selectedOption = dropdown.getFirstSelectedOption().getAttribute("value");
		Assert.assertEquals(cv.getDeliveryType(), DeliveryMethod.valueOf(selectedOption));
		
		dropdown = new Select(driver.findElement(By.id("childbirth_form:visitType")));
		selectedOption = dropdown.getFirstSelectedOption().getAttribute("value");
		Assert.assertEquals(cv.getVisitType(), VisitType.valueOf(selectedOption));
		
		String pit = driver.findElement(By.id("childbirth_form:pitocin")).getAttribute("value");
		Assert.assertEquals(cv.getPitocin().toString(), pit);
		
		String N2O = driver.findElement(By.id("childbirth_form:nitrousOxide")).getAttribute("value");
		Assert.assertEquals(cv.getNitrousOxide().toString(), N2O);
		
		String peth = driver.findElement(By.id("childbirth_form:pethidine")).getAttribute("value");
		Assert.assertEquals(cv.getPethidine().toString(), peth);
		
		String epi = driver.findElement(By.id("childbirth_form:epiduralAnaesthesia")).getAttribute("value");
		Assert.assertEquals(cv.getEpiduralAnaesthesia().toString(), epi);
		
		String MgSO4 = driver.findElement(By.id("childbirth_form:magnesiumSulfate")).getAttribute("value");
		Assert.assertEquals(cv.getMagnesiumSulfate().toString(), MgSO4);
	}
	
	@Then("^the childbirth visit is in the database$")
	public void childbirthIsInDatabase() {
		try {
			Assert.assertEquals(cv, cvd.getByOfficeVisit(TEST_OFFICE_VISIT_ID));
		} catch (DBException e) {
			e.printStackTrace();
			Assert.fail("DBException");
		}
	}
	
	@When("^I enter (.+) for Date and (.+) for Time$")
	public void enterNewbornDateAndTime(String date, String time) {
		recentNewborn = new Newborn(TEST_OFFICE_VISIT_ID);
		recentNewborn.setTimeEstimated(false);
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			recentNewborn.setDateOfBirth(sdf.format(sdf.parse(date)));
			sdf = new SimpleDateFormat("h:mm a");
			recentNewborn.setTimeOfBirth(sdf.format(sdf.parse(time)));
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		
		driver.findElement(By.id("newborn_form:date")).clear();
		driver.findElement(By.id("newborn_form:date")).sendKeys(date);
		
		driver.findElement(By.id("newborn_form:time")).clear();
		driver.findElement(By.id("newborn_form:time")).sendKeys(time);
	}
	
	@When("^I select (.+) for Sex$")
	public void selectSex(String sex) {
		recentNewborn.setSex(SexType.matchString(sex));
		
		Select dropdown = new Select(driver.findElement(By.id("newborn_form:sex")));
		dropdown.selectByVisibleText(sex);
	}
	
	@Then("^the newborns are in the database$")
	public void newbornsAreInDatabase() {
		try {
			List<Newborn> results = nd.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			Assert.assertTrue("Missing newborn(s) in the database", results.size() >= newbornList.size());
			for (Newborn n : newbornList) {
				Assert.assertTrue("Missing a newborn in the database", results.contains(n));
			}
		} catch (DBException e) {
			e.printStackTrace();
			Assert.fail("DBException");
		}
	}
	
	@Then("^the first newborn in the table should have those field values$")
	public void checkFirstNewbornInTable() {
		//TODO find fields and compare them to those in recentNewborn (ignore PID)
		
	}
}