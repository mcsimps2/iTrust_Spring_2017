package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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
	
	@Test
	public void testGettersSetters()
	{
		ObstetricsInit oi = new ObstetricsInit(1, new Timestamp(1L), 5L, "2017-03-03", "2017-01-01");
		Assert.assertTrue(oi.getPrettyDate().equals("March 3, 2017"));
		Assert.assertTrue(oi.getPrettyEDD().equals("December 8, 2017"));
		Assert.assertTrue(oi.getPrettyLMP().equals("January 1, 2017"));
		Assert.assertTrue(oi.getPrettyRH().equals("False"));
		oi.setLMP("2013-01-01");
		Assert.assertEquals("2013-01-01", oi.getLMP());
		Date oldDate = oi.getJavaDate();
		oi.setDate(new Date());
		Assert.assertNotEquals(oldDate, oi.getDate());
		Assert.assertNotEquals(oi1.hashCode(), oi.hashCode());
		Assert.assertNotEquals(oi1, oi);
		oi.setRH(true);
		Assert.assertTrue(oi.getRH());
		oi.setId(2);
		Assert.assertEquals(2, oi.getId());
		oi.setID(3);
		Assert.assertEquals(3, oi.getID());
		java.sql.Date sdate = new java.sql.Date(1, 1, 1);
		oi.setDate(sdate);
		Assert.assertEquals(oi.getSQLDate(), sdate);
		oi.setLMP(oldDate);
		Assert.assertEquals(oi.getJavaLMP(), oldDate);
		oi.setLMP(sdate);
		Assert.assertEquals(oi.getSQLLMP(), sdate);
	}
}
