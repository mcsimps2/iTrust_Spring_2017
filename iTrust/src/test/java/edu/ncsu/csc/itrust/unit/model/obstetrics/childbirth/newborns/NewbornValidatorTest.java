package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.newborns;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.*;

public class NewbornValidatorTest
{
	@Test
	public void test() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		sdf.setLenient(false);
		System.out.println("Test 1: " + sdf.parse("1:1:1").toString());
		try
		{
			System.out.println("Test 2: " + sdf.parse("30:1:1").toString());
			Assert.fail("passed");
		}
		catch (ParseException e)
		{
			Assert.assertNotNull(e);
		}
		System.out.println("Test 3: " + java.sql.Time.valueOf("1:1:1"));
		System.out.println("Test 4: " + java.sql.Time.valueOf("30:1:1"));
		System.out.println("Test 5: " + java.sql.Time.valueOf("abc"));
	}
}
