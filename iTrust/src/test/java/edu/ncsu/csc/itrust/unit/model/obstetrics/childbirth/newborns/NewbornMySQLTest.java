package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.newborns;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class NewbornMySQLTest
{
	NewbornMySQL sql;
	Newborn[] newborns;
	
	@Before
	public void setup() throws FileNotFoundException, IOException, SQLException
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		sql = new NewbornMySQL(ConverterDAO.getDataSource());
		newborns = new Newborn[2];
		newborns[0] = new Newborn(999L, 1L, "2017-05-05", "3:15 PM", SexType.OTHER, false);
		newborns[1] = new Newborn(1999L, 1L, "2017-01-01", "01:12 AM", SexType.FEMALE, true);
		for (int i = 0; i < newborns.length; i++)
		{
			try
			{
				Assert.assertTrue(sql.add(newborns[i]));
			}
			catch (DBException e)
			{
				Assert.fail(e.getMessage());
			}
		}
	}
	
	@Test
	public void testConstructors()
	{
		try
		{
			new NewbornMySQL();
			Assert.fail("Invalid call to constructor during testing");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		Assert.assertNotNull(new NewbornMySQL(ConverterDAO.getDataSource()));
	}
	
	@Test
	public void testGetAll()
	{
		try
		{
			List<Newborn> list = sql.getAll();
			Assert.assertEquals(newborns.length, list.size());
			for (int i = 0; i < list.size(); i++)
			{
				Assert.assertTrue(list.contains(newborns[i]));
			}
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetByID()
	{
		try
		{
			for (int i = 0; i < newborns.length; i++)
			{
				Assert.assertEquals(newborns[i], sql.getByID(i + 1));
			}
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testAdd()
	{
		//Have already added valid ones in setup, try invalid ones
		Newborn nb = new Newborn(999L, 0L, "2015-blah", "hithere", SexType.OTHER, null);
		try
		{
			sql.add(nb);
			Assert.fail("Allowed an invalid object");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testAddReturnID()
	{
		Newborn nb = new Newborn(1515L, 1L, "2015-11-15", "5:05 PM", SexType.OTHER, false);
		try
		{
			Assert.assertEquals(3, sql.addReturnGeneratedId(nb));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpdate()
	{
		newborns[0].setDateOfBirth("1999-1-1");
		newborns[0].setId(1L);
		try
		{
			Assert.assertTrue(sql.update(newborns[0]));
			newborns[1].setId(-1L);
			Assert.assertFalse(sql.update(newborns[1]));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		
		try
		{
			newborns[0].setOfficeVisitID(-1L);
			sql.update(newborns[0]);
			Assert.fail("Allowed invalid object");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
		//Valid data
		Newborn[] newborns = {
				new Newborn(999L, 1L, "2017-08-19", "01:45 PM", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00 PM", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00 PM", null, true),
				new Newborn(999L, 2L, "2015-12-31", "09:00 PM", SexType.MALE, null),
				new Newborn(999L, 2L, null, "09:00 PM", SexType.MALE, true),
				new Newborn(999L, 2L, "2015-12-31", null, SexType.MALE, true),
		};
		for (int i = 0; i < newborns.length; i++)
		{
			try
			{
				newborns[i].setId(1L);
				sql.update(newborns[i]);
				Assert.assertEquals(newborns[i], sql.getByID(1L));
			}
			catch (DBException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		//Invalid data
		Newborn[] newbornsInv = {
				new Newborn(999L, 1L, "2017-12-32", "1:50 PM", SexType.MALE, true), //invalid date
				new Newborn(999L, 1L, "2017-8-19", "25:5", SexType.MALE, true), //invalid time
				new Newborn(999L, 0L, "2017-8-19", "1:50 PM", SexType.MALE, true) //invalid office visit
		};
		for (int i = 0; i < newbornsInv.length; i++)
		{
			try
			{
				newbornsInv[i].setId(1L);
				sql.update(newbornsInv[i]);
				Assert.fail("Did not catch invalid object " + i);
			}
			catch (DBException e)
			{
				Assert.assertNotNull(e);
			}
		}
	}
	
	@Test
	public void testGetByOfficeVisit()
	{
		try
		{
			List<Newborn> list = sql.getByOfficeVisit(1L);
			Assert.assertEquals(newborns.length, list.size());
			for (int i = 0; i < list.size(); i++)
			{
				Assert.assertTrue(list.contains(newborns[i]));
			}
			list = sql.getByOfficeVisit(2L);
			Assert.assertEquals(0, list.size());
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testDelete()
	{
		try
		{
			sql.delete(2L);
			List<Newborn> list = sql.getAll();
			Assert.assertEquals(1, list.size());
			Assert.assertEquals(newborns[0], list.get(0));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
}
