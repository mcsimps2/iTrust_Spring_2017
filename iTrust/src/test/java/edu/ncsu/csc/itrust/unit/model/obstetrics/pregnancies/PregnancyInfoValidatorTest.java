package edu.ncsu.csc.itrust.unit.model.obstetrics.pregnancies;


import java.util.Calendar;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoValidator;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class PregnancyInfoValidatorTest {
	PregnancyInfoValidator validator;
	
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		validator = new PregnancyInfoValidator(ConverterDAO.getDataSource());
	}
	
	@Test
	public void testValidPregnancyInfo()
	{
		PregnancyInfo[] piArr = {new PregnancyInfo(1, 1, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1)};
		//Note patient 1 is obstetrics eligible
		for (int i = 0; i < piArr.length; i++)
		{
			try
			{
				validator.validate(piArr[i]);
			}
			catch (FormValidationException e)
			{
				Assert.fail(e.getMessage());
			}
		}
	}
	
	@Test
	public void testInvalidPregnancyInfo()
	{
		PregnancyInfo[] piArr = {
				new PregnancyInfo(1, 2, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //2 is ineligble for obstetrics care
				new PregnancyInfo(1, 1, 1017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //year of conception invalid
				new PregnancyInfo(1, 1, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 0), //invalid multiplicity
				new PregnancyInfo(1, 1, 2017, -1, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //negative value
				new PregnancyInfo(1, 1, 2017, 1, -15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //negative value
				new PregnancyInfo(1, 1, 2017, 1, 15, -25, DeliveryMethod.CAESAREAN_SECTION, 1), //negative value
				new PregnancyInfo(1, 1, 2017, 1, 15, 25, DeliveryMethod.CAESAREAN_SECTION, -1), //negative value
				new PregnancyInfo(1, 9999999, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //nonexistent PID
				new PregnancyInfo(99999999, 1, 2017, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1), //nonexistent obstetricsInitID
				new PregnancyInfo(1, 1, Calendar.getInstance().get(Calendar.YEAR) + 15, 270, 15, 25, DeliveryMethod.CAESAREAN_SECTION, 1) //year in the future	
		};
		for (int i = 0; i < piArr.length; i++)
		{
			try
			{
				validator.validate(piArr[i]);
				System.out.println(piArr[i].toString());
				Assert.fail("Did not catch an invalid PregnancyInfo");
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
			new PregnancyInfoValidator();
			Assert.fail("Should not have been able to call the default constructor");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
	}
}
