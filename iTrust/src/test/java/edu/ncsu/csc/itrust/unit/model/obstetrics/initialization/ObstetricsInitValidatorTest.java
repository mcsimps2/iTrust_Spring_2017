package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;


import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitValidator;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitValidatorTest 
{
	ObstetricsInitValidator validator;
	
	@Before
	public void setup() throws Exception
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
				
		validator = new ObstetricsInitValidator(ConverterDAO.getDataSource());
	}
	
	@Test
	public void testValidObstetricInit()
	{
		ObstetricsInit[] oiArr = {new ObstetricsInit(1, "2014-05-01", "2014-01-01")};
		//Note that patient 1 is eligible for obstetrics care
		for (int i = 0; i < oiArr.length; i++)
		{
			try
			{
				validator.validate(oiArr[i]);
			}
			catch (FormValidationException e)
			{
				Assert.fail(e.getMessage());
			}
		}
	}
	
	@Test
	public void testInvalidObstetricInit()
	{
		ObstetricsInit badDate = new ObstetricsInit();
		badDate.setPid(1);
		ObstetricsInit[] oiArr = {
				new ObstetricsInit(2, "2014-05-01", "2014-01-01"), //2 is ineligible for obstetrics care
				new ObstetricsInit(1, "2014-01-01", "2014-01-05"), //Date occurs < to LMP
				new ObstetricsInit(0, "2014-05-01", "2014-01-01"), //non-existent patient
				new ObstetricsInit(1, "2015-05-05", "2015-05-06"), //Check for off by 1 errors
				badDate //null date and LMP
		};
		for (int i = 0; i < oiArr.length; i++)
		{
			try
			{
				validator.validate(oiArr[i]);
				System.out.println(i);
				Assert.fail("Did not catch an invalid ObstetricInit");
			}
			catch (FormValidationException e)
			{
				//Good
				Assert.assertTrue(true);
			}
		}
	}
	
	@Test
	public void testConstructor()
	{
		try
		{
			new ObstetricsInitValidator();
			Assert.fail("Should not have been able to call the default constructor");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
	}
}
