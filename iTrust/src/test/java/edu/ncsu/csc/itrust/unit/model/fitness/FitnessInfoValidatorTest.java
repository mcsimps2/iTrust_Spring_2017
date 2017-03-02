package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoValidator;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
/**
 * 
 * @author mcsimps2
 * Tests the validation of FitnessInfo objects
 *
 */
public class FitnessInfoValidatorTest {
	/* the dates of today, yesterday, and tomorrow in YYYY-MM-DD format */
	String currDate, yesterday, tmrw;
	/* The validator */
	FitnessInfoValidator validator;
	
	/**
	 * Setup the tests, include getting the dates of today, yesterday,
	 * and tomorrow.
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Before
	public void setup() throws IOException, SQLException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dcurrDate = new Date();
		Date dyesterday = DateUtils.addDays(new Date(), -1);
		Date dtmrw = DateUtils.addDays(new Date(), 1);
		currDate = formatter.format(dcurrDate);
		yesterday = formatter.format(dyesterday);
		tmrw = formatter.format(dtmrw);
		
		validator = new FitnessInfoValidator(ConverterDAO.getDataSource());
		TestDataGenerator.main(null);
	}
	
	/**
	 * Feed in valid FitnessInfo objects and make sure they pass
	 * validation
	 */
	@Test
	public void testValidFitnessInfo()
	{
		FitnessInfo[] fiArr = new FitnessInfo[4];
		fiArr[0] = new FitnessInfo(1, tmrw, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fiArr[1] = new FitnessInfo(1, yesterday, 500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		fiArr[2] = new FitnessInfo(1, tmrw, 500, 8, 18231.2316, 2, 5, 20, 15, 16, 18, 2000, 300, 4000, 50, 2);
		fiArr[3] = new FitnessInfo(2, "2004-01-01", 18, 500, 500, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);
		//Note patient 2 died in 2005-03-10
		try
		{
			for (int i = 0; i < fiArr.length; i++)
			{
				validator.validate(fiArr[i]);
				System.out.println("Validated " + i);
			}
		}
		catch (FormValidationException e)
		{
			e.printStackTrace();
			Assert.fail("Did not correctly validate valid objects: " + e.getMessage());
		}
	}
	
	/**
	 * Pass in invalid FitnessInfo objects and make sure they don't
	 * pass validation
	 */
	@Test
	public void testInvalidFitnessInfo()
	{
		FitnessInfo[] fiArr = new FitnessInfo[7];
		//Before date of birth
		fiArr[0] = new FitnessInfo(1, "1500-01-02", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//Invalid pid
		fiArr[1] = new FitnessInfo(-152, tmrw, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//invalid pid
		fiArr[2] = new FitnessInfo(0, tmrw, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//Nonexistent pid
		fiArr[3] = new FitnessInfo(55555555, tmrw, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//Negative steps
		fiArr[4] = new FitnessInfo(1, currDate, -1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);
		//Calories active > calories burned
		fiArr[5] = new FitnessInfo(1, currDate, 1, 5, 6, 7, 80, 9, 10, 11, 12, 13, 14, 15, 16, 17); 
		//After date of death
		fiArr[6] = new FitnessInfo(2, tmrw, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		for (int i = 0; i < fiArr.length; i++)
		{
			try
			{
				validator.validate(fiArr[i]);
				Assert.fail("Validated an invalid object");
			}
			catch (FormValidationException e)
			{
				//Good! Caught the invalid object
				//Dummy test to pass
				Assert.assertNotNull(fiArr[i]);
			}
		}
	}
	
}
