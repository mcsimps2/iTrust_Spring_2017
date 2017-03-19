package edu.ncsu.csc.itrust.unit.model.obstetrics.initialization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitSQLLoader;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsInitMySQLTest {
	ObstetricsInitMySQL oisql;
	ObstetricsInitSQLLoader loader;
	DataSource ds;
	ObstetricsInit[] oiArr;
	
	@Before
	public void setup() throws Exception
	{
		DBBuilder.main(null);
		TestDataGenerator.main(null);
		ds = ConverterDAO.getDataSource();
		oisql = new ObstetricsInitMySQL(ds);
		loader = new ObstetricsInitSQLLoader();
		
		//Pickup some values already in the DB
		oiArr = new ObstetricsInit[2];
		oiArr[0] = new ObstetricsInit(1, "2017-03-16", "2017-01-01");
		oiArr[1] = new ObstetricsInit(1, "2016-02-03", "2015-11-21");
	}
	
	@Test
	public void testGetRecords()
	{
		try {
			List<ObstetricsInit> results = oisql.getRecords(1);
			for (int i = 0; i < oiArr.length; i++)
			{
				Assert.assertTrue(results.contains(oiArr[i]));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetRecordByID()
	{
		try
		{
			List<ObstetricsInit> results = oisql.getRecords(1);
			ObstetricsInit result = results.get(0);
			Assert.assertTrue(oisql.getByID(result.getID()).equals(result));
		}
		catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testAdd()
	{
		ObstetricsInit[] toAdd = {
				new ObstetricsInit(1, "2000-01-01", "1999-11-11"),
				new ObstetricsInit(1, "2017-01-01", "2016-05-05")
		};
		for (int i = 0; i < toAdd.length; i++)
		{
			try
			{
				Assert.assertTrue(oisql.add(toAdd[i]));
			}
			catch (DBException e)
			{
				Assert.fail(e.getMessage());
			}
		}
		
		try {
			List<ObstetricsInit> results = oisql.getRecords(1);
			for (int i = 0; i < toAdd.length; i++)
			{
				Assert.assertTrue(results.contains(toAdd[i]));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
}
