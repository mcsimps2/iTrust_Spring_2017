package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.newborns;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornValidator;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;

public class NewbornValidatorTest
{
	
	NewbornValidator validator;
	
	@Before
	public void setup()
	{
		validator = new NewbornValidator(ConverterDAO.getDataSource());
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
				new Newborn(999L, 1L, "2017-8-19", "1:5", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00", SexType.MALE, true),
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
				new Newborn(999L, 1L, "2017-12-32", "1:5", SexType.MALE, true), //invalid date
				new Newborn(999L, 1L, "2017-8-19", "25:5", SexType.MALE, true), //invalid time
				new Newborn(999L, 0L, "2017-8-19", "1:5", SexType.MALE, true), //invalid office visit
				new Newborn(999L, null, "2017-8-19", "1:5", SexType.MALE, true),
				new Newborn(999L, 1L, null, "1:5", SexType.MALE, true),
				new Newborn(999L, 1L, "2017-8-19", null, SexType.MALE, true),
				new Newborn(999L, 1L, "2017-8-19", "1:5", SexType.MALE, null)
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
				new Newborn(999L, 1L, "2017-8-19", "1:5", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00", SexType.MALE, true),
				new Newborn(999L, 2L, "", "09:00", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00", null, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00", SexType.MALE, null),
				new Newborn(999L, 2L, null, "09:00", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", null, SexType.MALE, true),
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
				new Newborn(999L, 1L, "2017-12-32", "1:5", SexType.MALE, true), //invalid date
				new Newborn(999L, 1L, "2017-8-19", "25:5", SexType.MALE, true), //invalid time
				new Newborn(999L, 0L, "2017-8-19", "1:5", SexType.MALE, true) //invalid office visit
		};
		for (int i = 0; i < newbornsInv.length; i++)
		{
			try
			{
				validator.validateUpdate(newbornsInv[i]);
				Assert.fail("Did not catch invalid object");
			}
			catch (FormValidationException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
	
	@Test
	public void testTimes()
	{
		NewbornValidator validator = new NewbornValidator(ConverterDAO.getDataSource());
		Newborn nb1 = new Newborn(999L, 1L, "2017-8-19", "1:5:10:15:20:25:30", SexType.FEMALE, false);
		Newborn nb2 = new Newborn(999L, 1L, "2017-8-19", "1:5", SexType.FEMALE, false);
		try
		{
			validator.validate(nb1);
			validator.validate(nb2);
			Assert.assertEquals("1:5", nb1.getTimeOfBirth());
			Assert.assertEquals("1:5", nb2.getTimeOfBirth());
		}
		catch (FormValidationException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
