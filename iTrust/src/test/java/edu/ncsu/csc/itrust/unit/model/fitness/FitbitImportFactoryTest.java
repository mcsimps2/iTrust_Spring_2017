package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitbitImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoFileFormatException;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class FitbitImportFactoryTest
{
	FitbitImportFactory importer;
	FitnessInfoMySQL fisql;
	File f1, f2;
	
	@Before
	public void setup() throws DBException, IOException, SQLException
	{
		//Clear the tables	
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		DataSource ds = ConverterDAO.getDataSource();
		importer = new FitbitImportFactory(ds);
		fisql = new FitnessInfoMySQL(ds);
		f1 = new File("testing-files/sample_fitnessData/fitbit_export_valid1.csv");
		f2 = new File("testing-files/sample_fitnessData/fitbit_export_valid2.csv");
		//System.out.println("Absolute path is " + f1.getAbsolutePath());
	}
	
	@Test
	public void testValidFile()
	{
		//First, create a whole bunch of FitnessInfo objects that represent what they data should be
		//These will be used for comparisons
		FitnessInfo[] fiArr = new FitnessInfo[3];
		fiArr[0] = new FitnessInfo(1, "2016-10-01", 11017, 2455, 4.89, 15, 1448, 970, 2, 463, 5, 0, 0, 0, 0, 0);
		fiArr[1] = new FitnessInfo(1, "2016-10-21", 6339, 2014, 3.06, 11, 724, 1232, 3, 187, 18, 0, 0, 0, 0, 0);
		fiArr[2] = new FitnessInfo(1, "1999-10-31", 7137, 2212, 3.17, 16, 1015, 1143, 5, 285, 7, 0, 0, 0, 0, 0);
		/*
		* public FitnessInfo(long pid, String date, int steps, int caloriesBurned, double miles, int floors,
				int activeCalories, int minutesSedentary, int minutesFairlyActive, int minutesLightlyActive,
				int minutesVeryActive, int heartRateLow, int heartRateHigh, int heartRateAvg, int activeHours,
				int minutesUVExposure) 
			 */
		try
		{
			InputStream is = new FileInputStream(f1);
			importer.importFitnessInfo(1, is);
			FitnessInfo fi0 = fisql.getFitnessInfo(1, "2016-10-1");
			Assert.assertTrue(fi0.equals(fiArr[0]));
			
			FitnessInfo fi1 = fisql.getFitnessInfo(1, "2016-10-21");
			Assert.assertTrue(fi1.equals(fiArr[1]));
			
			
			FitnessInfo fi2 = fisql.getFitnessInfo(1, "1999-10-31");
			Assert.assertTrue(fi2.equals(fiArr[2]));
			
			is = new FileInputStream(f2);
			importer.importFitnessInfo(1, is);
			fi0 = fisql.getFitnessInfo(1, "2016-10-1");
			Assert.assertTrue(fi0.equals(fiArr[0]));
			
			fi1 = fisql.getFitnessInfo(1, "2016-10-21");
			Assert.assertTrue(fi1.equals(fiArr[1]));
			
			
			fi2 = fisql.getFitnessInfo(1, "1999-10-31");
			Assert.assertTrue(fi2.equals(fiArr[2]));
			
		}
		catch (FileNotFoundException e)
		{
			//System.out.println("Could not find the file " + f1.getAbsolutePath());
			Assert.fail("Couldn't find the specified file: " + e.getMessage());
		}
		catch (IOException e)
		{
			Assert.fail("Got an IOException: " + e.getMessage());
		}
		catch (FitnessInfoFileFormatException e)
		{
			Assert.fail("Got a FitnessInfoFileFormatException: " + e.getMessage());
		}
		catch (CSVFormatException e)
		{
			Assert.fail("Got a CSVFormatException: " + e.getMessage());
		}
		catch (DBException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvalidFile()
	{
		String[] invalidFileNames = {"testing-files/sample_fitnessData/fitbit_export_invalid_HW3.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid1.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid2.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid3.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid4.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid5.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid6.csv",
				"testing-files/sample_fitnessData/fitbit_export_invalid7.csv"};
		for (int i = 0; i < invalidFileNames.length; i++)
		{
			try
			{
				File file = new File(invalidFileNames[i]);
				InputStream is = new FileInputStream(file);
				importer.importFitnessInfo(1, is);
				Assert.fail("imported invalid info!");
			}
			catch (FitnessInfoFileFormatException e)
			{
				//passed!
				Assert.assertTrue(true);
			} catch (CSVFormatException e)
			{
				Assert.fail(e.getMessage());
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
