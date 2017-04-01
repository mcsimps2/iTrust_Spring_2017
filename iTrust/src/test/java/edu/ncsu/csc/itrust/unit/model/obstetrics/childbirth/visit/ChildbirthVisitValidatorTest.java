package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.visit;
import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitValidator;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;

public class ChildbirthVisitValidatorTest
{
	@Test
	public void testConstructors()
	{
		try
		{
			new ChildbirthVisitValidator();
			Assert.fail("Invalid constructor call during testing");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		Assert.assertNotNull(new ChildbirthVisitValidator(ConverterDAO.getDataSource()));
	}
	
	@Test
	public void testValidator()
	{
		ChildbirthVisitValidator validator = new ChildbirthVisitValidator(ConverterDAO.getDataSource());
		//Valid values
		ChildbirthVisit[] cvArrValid = {
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 1, 1, 1, 1, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 0, 0, 0, 0, 0)
		};
		for (int i = 0; i < cvArrValid.length; i++)
		{
			try
			{
				validator.validate(cvArrValid[i]);
				Assert.assertTrue(true); //passed if we got to here
			}
			catch (FormValidationException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		//Invaild values
		ChildbirthVisit[] cvArrInvalid = {
				new ChildbirthVisit(0L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, 1), //invalid office visit
				new ChildbirthVisit(null, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, 1), //null office visit
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, -1, 4, 3, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, -1, 3, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, -1, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, -1, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, -1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, null, 4, 3, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, null, 3, 2, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, null, 1),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, null),
				new ChildbirthVisit(1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, null, 2, 1)
		};
		for (int i = 0; i < cvArrInvalid.length; i++)
		{
			try
			{
				validator.validate(cvArrInvalid[i]);
				Assert.fail("Did not catch an invalid object");
			}
			catch (FormValidationException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
}
