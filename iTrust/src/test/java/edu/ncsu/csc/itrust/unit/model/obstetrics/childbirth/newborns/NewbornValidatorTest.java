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
		NewbornValidator validator = new NewbornValidator(ConverterDAO.getDataSource());
		
		//Valid data
		Newborn[] newborns = {
				new Newborn(1L, "2017-8-19", "1:5:15", SexType.MALE, true),
				new Newborn(2L, "2015-12-31", "09:00:09", SexType.MALE, true),
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
				new Newborn(1L, "2017-12-32", "1:5:15", SexType.MALE, true), //invalid date
				new Newborn(1L, "2017-8-19", "25:5:15", SexType.MALE, true), //invalid time
				new Newborn(0L, "2017-8-19", "1:5:15", SexType.MALE, true), //invalid office visit
				new Newborn(null, "2017-8-19", "1:5:15", SexType.MALE, true),
				new Newborn(1L, null, "1:5:15", SexType.MALE, true),
				new Newborn(1L, "2017-8-19", null, SexType.MALE, true),
				new Newborn(1L, "2017-8-19", "1:5:15", SexType.MALE, null)
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
		NewbornValidator validator = new NewbornValidator(ConverterDAO.getDataSource());
		
		//Valid data
		Newborn[] newborns = {
				new Newborn(1L, "2017-8-19", "1:5:15", SexType.MALE, true),
				new Newborn(2L, "2015-12-31", "09:00:09", SexType.MALE, true),
				new Newborn(2L, "", "09:00:09", SexType.MALE, true),
				new Newborn(2L, "2015-12-31", "", SexType.MALE, true),
				new Newborn(2L, "2015-12-31", "09:00:09", null, true),
				new Newborn(2L, "2015-12-31", "09:00:09", SexType.MALE, null),
				new Newborn(2L, null, "09:00:09", SexType.MALE, true),
				new Newborn(2L, "2015-12-31", null, SexType.MALE, true),
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
				new Newborn(1L, "2017-12-32", "1:5:15", SexType.MALE, true), //invalid date
				new Newborn(1L, "2017-8-19", "25:5:15", SexType.MALE, true), //invalid time
				new Newborn(0L, "2017-8-19", "1:5:15", SexType.MALE, true) //invalid office visit
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
}
