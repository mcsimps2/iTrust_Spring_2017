package edu.ncsu.csc.itrust.unit.model.fitness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.*;

import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoFileFormatException;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.model.fitness.MicrosoftBandImportFactory;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class MicrosoftBandImportFactoryTest {
	FitnessInfoMySQL fisql;
	MicrosoftBandImportFactory importer;
	final String TEST_DIR = "testing-files/sample_fitnessData/";
	
	@Before
	public void setup() throws IOException, SQLException
	{
		// Reset test data
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		DataSource ds = ConverterDAO.getDataSource();
		fisql = new FitnessInfoMySQL(ds);
		importer = new MicrosoftBandImportFactory(ds);
	}
	
	@Test
	public void testValidFiles()
	{
		File[] validFiles = {new File(TEST_DIR + "MS_Band_Data.csv")};
		/*
		 * public FitnessInfo(long pid, String date, int steps, int caloriesBurned, double miles, int floors,
			int activeCalories, int minutesSedentary, int minutesFairlyActive, int minutesLightlyActive,
			int minutesVeryActive, int heartRateLow, int heartRateHigh, int heartRateAvg, int activeHours,
			int minutesUVExposure)
		 */
		FitnessInfo[] expected = {new FitnessInfo(1, "2016-02-16", 11967, 2893, 7.012789062, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0),
				new FitnessInfo(1, "2016-02-17", 4367, 2310, 2.151858148, 0, 0, 0, 0, 0, 0, 64, 83, 71, 3, 0),
				new FitnessInfo(1, "2016-02-18", 7030, 2309, 3.471973674, 8, 0, 0, 0, 0, 0, 47, 116, 71, 4, 0),
				new FitnessInfo(1, "2016-02-19", 996, 2182, 0.490075459, 1, 0, 0, 0, 0, 0, 58, 103, 70, 0, 0),
				new FitnessInfo(1, "2016-02-20", 0, 2198, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
				new FitnessInfo(1, "2016-02-21", 548, 2094, 0.269811799, 0, 0, 0, 0, 0, 0, 55, 94, 69, 0, 0),
				new FitnessInfo(1, "2016-02-22", 1048, 1947, 0.515042154, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
				
						};
		FitnessInfo fi = null;
		for (int i = 0; i < validFiles.length; i++)
		{
			for (int j = 0; j < expected.length; j++)
			{
				try {
					importer.importFitnessInfo(1, new FileInputStream(validFiles[i]));
					fi = fisql.getFitnessInfo(1, expected[j].getDate());
					//System.out.println(fi.toString());
					//System.out.println(expected[j].toString());
					Assert.assertTrue(expected[j].equals(fi));
				} catch (FileNotFoundException e) {
					System.out.println(fi.toString());
					System.out.println(expected[j].toString());
					Assert.fail(e.getMessage());
				} catch (CSVFormatException e) {
					System.out.println(fi.toString());
					System.out.println(expected[j].toString());
					Assert.fail(e.getMessage());
				} catch (IOException e) {
					System.out.println(fi.toString());
					System.out.println(expected[j].toString());
					Assert.fail(e.getMessage());
				} catch (FitnessInfoFileFormatException e) {
					System.out.println(fi.toString());
					System.out.println(expected[j].toString());
					Assert.fail(e.getMessage());
				} catch (DBException e) {
					System.out.println(fi.toString());
					System.out.println(expected[j].toString());
					Assert.fail(e.getMessage());
				}
			}
		}
	}
	
	@Test
	public void testInvalidFiles()
	{
		File[] invalidFiles = {new File(TEST_DIR + "MS_export_invalid1.csv"),
				new File(TEST_DIR + "MS_export_invalid2.csv"),
				new File(TEST_DIR + "MS_export_invalid3.csv"),
				new File(TEST_DIR + "MS_export_invalid4.csv"),
				new File(TEST_DIR + "MS_export_invalid5.csv"),
				new File(TEST_DIR + "MS_export_invalid6.csv"),
				new File(TEST_DIR + "MS_export_invalid7.csv"),
		};
		
		for (int i = 0; i < invalidFiles.length; i++)
		{
			try {
				importer.importFitnessInfo(1, new FileInputStream(invalidFiles[i]));
				//Should throw an exception. If not, fail the test
				Assert.fail("Did not catch an invalid file");
			} catch (CSVFormatException | IOException | FitnessInfoFileFormatException e) {
				//good! Passed the test if we caught an exception
				Assert.assertNotNull(e);
			}
			
		}
	}
}
