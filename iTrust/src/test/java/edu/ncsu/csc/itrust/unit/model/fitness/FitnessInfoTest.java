package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.*;

import edu.ncsu.csc.itrust.model.fitness.DateFormatException;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class FitnessInfoTest
{
	FitnessInfo fi;
	
	@Before
	public void setup() throws IOException, SQLException
	{
		TestDataGenerator.main(null);
		fi = new FitnessInfo();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testStringToSQLDate()
	{
		String date = "1996-01-01";
		String invalidDate = "abcd";
		java.sql.Date d = FitnessInfo.stringToSQLDate(date);
		Assert.assertEquals(d.getYear()+1900, 1996);
		Assert.assertEquals(d.getMonth() + 1, 1);
		Assert.assertEquals(d.getDate(), 1);
		
		java.sql.Date ret = null;
		try
		{
			ret = FitnessInfo.stringToSQLDate(invalidDate);
			Assert.fail("Did not catch an invalid date");
		}
		catch (DateFormatException e)
		{
			Assert.assertNull(ret);
		}
	}
	
	@Test
	public void testStringToJavaDate()
	{
		String invalidDate = "abcd";
		java.util.Date ret = null;
		try
		{
			ret = FitnessInfo.stringToJavaDate(invalidDate);
			Assert.fail("Did not catch an invalid date");
		}
		catch (DateFormatException e)
		{
			Assert.assertNull(ret);
		}
	}
	
	@Test
	public void testVerifyDate()
	{
		String d1 = "13";
		Assert.assertFalse(FitnessInfo.verifyDate(d1));
		
		String d2 = "abcd-ef-gh";
		Assert.assertFalse(FitnessInfo.verifyDate(d2));
	}
	
	@Test
	public void testGettersAndSetters()
	{
		try
		{
			fi.setDate("abcd-ef-gg");
			Assert.fail("Allowed an invalid date");
		}
		catch (DateFormatException e)
		{
			Assert.assertEquals("Date not in format YYYY-MM-DD", e.getMessage());
		}
	}
	
	@Test
	public void testHashCode()
	{
		FitnessInfo f = new FitnessInfo();
		f.setPid(100);
		f.setDate("1996-01-02");
		f.setSteps(5);
		int hash1 = f.hashCode();
		FitnessInfo f2 = new FitnessInfo();
		f2.setPid(100);
		f2.setDate("1996-01-02");
		f2.setSteps(5);
		int hash2 = f2.hashCode();
		Assert.assertEquals(hash1, hash2);
	}
	
	@Test
	public void testToString()
	{
		FitnessInfo f = new FitnessInfo();
		f.setPid(100);
		f.setDate("1996-01-02");
		f.setSteps(5);
		Assert.assertEquals("FitnessInfo [pid=100, steps=5, date=1996-01-02, caloriesBurned=0, miles=0.0, floors=0, activeCalories=0, minutesSedentary=0, minutesLightlyActive=0, minutesFairlyActive=0, minutesVeryActive=0, heartRateLow=0, heartRateHigh=0, heartRateAvg=0, activeHours=0, minutesUVExposure=0]", f.toString());
	}
}
