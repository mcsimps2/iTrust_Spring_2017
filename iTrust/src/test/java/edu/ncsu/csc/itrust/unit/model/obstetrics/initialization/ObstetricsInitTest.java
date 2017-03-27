package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;

import java.util.Calendar;

import org.junit.*;

import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;

public class ObstetricsInitTest {
	ObstetricsInit oi1;
	
	@Before
	public void setup()
	{
		oi1 = new ObstetricsInit(1, "2017-03-16", "2017-01-01");
	}
	
	@Test
	public void testGetNumWeeksPregnant()
	{
		Assert.assertEquals(74/7, oi1.getNumWeeksPregnant());
	}
	
	@Test
	public void testGetEDD()
	{
		Assert.assertEquals("2017-12-21", oi1.getEDD());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testStringToSQLDate()
	{
		java.sql.Date date = ObstetricsInit.stringToSQLDate("2017-03-21");
		
		Assert.assertEquals(Calendar.MARCH, date.getMonth());
		Assert.assertEquals(21, date.getDate());
		Assert.assertEquals(2017 - 1900, date.getYear());
	}
}
