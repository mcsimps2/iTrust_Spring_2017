package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.newborns;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornValidator;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class NewbornValidatorTest
{
	
	NewbornValidator validator;
	
	@Before
	public void setup() throws Exception
	{
		validator = new NewbornValidator(ConverterDAO.getDataSource());
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testConstructors()
	{
		try
		{
			new NewbornValidator();
			Assert.fail("Called invalid constructor during testing");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		Assert.assertNotNull(new NewbornValidator(ConverterDAO.getDataSource()));
	}
	
	@Test
	public void testValidate()
	{		
		//Valid data
		Newborn[] newborns = {
				new Newborn(999L, 51L, "2017-8-19", "1:05 PM", SexType.MALE, true),
				new Newborn(999L, 51L, "2015-12-31", "9:00 AM", SexType.MALE, true),
		};
		for (int i = 0; i < newborns.length; i++)
		{
			try
			{
				validator.validate(newborns[i]);
				Assert.assertTrue(true); //passed validation
			}
			catch (FormValidationException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		//Invalid data
		Newborn[] newbornsInv = {
				new Newborn(999L, 1L, "2017-12-32", "1:05 PM", SexType.MALE, true), //invalid date
				new Newborn(999L, 1L, "2017-12-32", "1:05", SexType.MALE, true), //invalid time
				new Newborn(999L, 1L, "2017-8-19", "13:05 PM", SexType.MALE, true), //invalid time
				new Newborn(999L, 0L, "2017-8-19", "1:50 PM", SexType.MALE, true), //invalid office visit
				new Newborn(999L, null, "2017-8-19", "1:50 PM", SexType.MALE, true),
				new Newborn(999L, 1L, null, "1:50 PM", SexType.MALE, true),
				new Newborn(999L, 1L, "2017-8-19", null, SexType.MALE, true),
				new Newborn(999L, 1L, "2017-8-19", "1:50 PM", SexType.MALE, null)
		};
		for (int i = 0; i < newbornsInv.length; i++)
		{
			try
			{
				validator.validate(newbornsInv[i]);
				Assert.fail("Did not catch invalid object");
			}
			catch (FormValidationException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
	
	@Test
	public void testValidateUpdate()
	{
		//Valid data
		Newborn[] newborns = {
				new Newborn(999L, 51L, "2017-8-19", "1:50 PM", SexType.MALE, true),
				new Newborn(999L, 51L, "2015-12-31", "9:00 PM", SexType.MALE, true),
				new Newborn(999L, 51L, "", "9:00 PM", SexType.MALE, true),
				new Newborn(999L, 51L, "2015-12-31", "", SexType.MALE, true),
				new Newborn(999L, 51L, "2015-12-31", "9:00 PM", null, true),
				new Newborn(999L, 51L, "2015-12-31", "9:00 PM", SexType.MALE, null),
				new Newborn(999L, 51L, null, "9:00 AM", SexType.MALE, true),
				new Newborn(999L, 51L, "2015-12-31", null, SexType.MALE, true)
		};
		for (int i = 0; i < newborns.length; i++)
		{
			try
			{
				validator.validateUpdate(newborns[i]);
				Assert.assertTrue(true); //passed validation
			}
			catch (FormValidationException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		//Invalid data
		Newborn[] newbornsInv = {
				new Newborn(999L, 1L, "2017-12-32", "1:05 AM", SexType.MALE, true), //invalid date
				new Newborn(999L, 1L, "2017-8-19", "25:5", SexType.MALE, true), //invalid time
				new Newborn(999L, 1L, "2017-8-19", "-1:05 AM", SexType.MALE, true), //invalid time
				new Newborn(999L, 0L, "2017-8-19", "1:05 PM", SexType.MALE, true) //invalid office visit
		};
		for (int i = 0; i < newbornsInv.length; i++)
		{
			try
			{
				validator.validateUpdate(newbornsInv[i]);
				Assert.fail("Did not catch invalid object " + i);
			}
			catch (FormValidationException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
}
