package edu.ncsu.csc.itrust.unit.model.obstetrics.childbirth.visit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildbirthVisitMySQLTest
{
	ChildbirthVisitMySQL sql;
	ChildbirthVisit[] cvArr;
	
	@Before
	public void setup() throws FileNotFoundException, IOException, SQLException
	{
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		sql = new ChildbirthVisitMySQL(ConverterDAO.getDataSource());
		cvArr = new ChildbirthVisit[2];
		cvArr[0] = new ChildbirthVisit(1L, DeliveryMethod.VAGINAL_DELIVERY, 5, 4, 3, 2, 1);
		cvArr[1] = new ChildbirthVisit(2L, DeliveryMethod.VAGINAL_DELIVERY_VACUUM, 1, 2, 3, 4, 5);
		try
		{
			for (int i = 0; i < cvArr.length; i++)
			{
				Assert.assertTrue(sql.add(cvArr[i]));
			}
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testConstructors()
	{
		try
		{
			new ChildbirthVisitMySQL();
			Assert.fail("Invalid call to constructor");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		Assert.assertNotNull(new ChildbirthVisitMySQL(ConverterDAO.getDataSource()));
	}
	
	@Test
	public void testGetAll()
	{
		try
		{
			List<ChildbirthVisit> list = sql.getAll();
			Assert.assertEquals(cvArr.length, list.size());
			for (int i = 0; i < cvArr.length; i++)
			{
				Assert.assertTrue(list.contains(cvArr[i]));
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
			for (int i = 0; i < cvArr.length; i++)
			{
				Assert.assertEquals(cvArr[i], sql.getByID(i + 1));
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
		//Have already done valid adds in setup
		//Try an invalid add
		ChildbirthVisit cv = new ChildbirthVisit(-1L, DeliveryMethod.CAESAREAN_SECTION, 5, 4, 3, 2, 1); //nonexistent office visit ID
		try
		{
			sql.add(cv);
			Assert.fail("Added and invalid childbirth visit");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testUpdate()
	{
		//Update cvArr[0]
		cvArr[0].setDeliveryType(DeliveryMethod.MISCARRIAGE);
		cvArr[0].setId(1L);
		try
		{
			Assert.assertTrue(sql.update(cvArr[0]));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		
		//Update that should return false
		cvArr[0].setId(-1L);
		try
		{
			Assert.assertFalse(sql.update(cvArr[0]));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
		//Invalid update
		cvArr[0].setId(1L);
		cvArr[0].setEpiduralAnaesthesia(-1);
		try
		{
			sql.update(cvArr[0]);
			Assert.fail("Allowed an invalid update");
		}
		catch (DBException e)
		{
			Assert.assertNotNull(e);
		}
		
	}
	
	@Test
	public void testGetByOfficeVisit()
	{
		try
		{
			Assert.assertEquals(cvArr[0], sql.getByOfficeVisit(1L));
			Assert.assertEquals(cvArr[1], sql.getByOfficeVisit(2L));
			Assert.assertNull(sql.getByOfficeVisit(-1L));
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
}
