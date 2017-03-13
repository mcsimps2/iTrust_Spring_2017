package edu.ncsu.csc.itrust.model.fitness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.sql.DataSource;

import edu.ncsu.csc.itrust.CSVParser;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;


/**
 * Imports data from a Fitbit file into the database
 * @author mcsimps2
 *
 */
public class FitbitImportFactory extends FitnessImportFactory
{
	/** Handles the interaction with the database */
	FitnessInfoMySQL fisql;
	
	/** Default constructor
	 * Lets the MySQL class initialize its datasource
	 * @throws DBException 
	 */
	public FitbitImportFactory() throws DBException
	{
		fisql = new FitnessInfoMySQL();
	}
	
	/**
	 * Constructor for testing purposes so we can specify a datasource to be used
	 * for the MySQL object
	 * @param ds the datasource to be used
	 */
	public FitbitImportFactory(DataSource ds)
	{
		fisql = new FitnessInfoMySQL(ds);
	}
	
	/**
	 * Parses the input for Fitbit fitness info and inserts the info for each date into the fitness database,
	 * associating the info with the given PID
	 * @param pid whose fitness data we are looking at
	 * @param input the file of fitness data in a CSV format for FitBit data specifically
	 * @throws CSVFormatException 
	 * @throws IOException 
	 * @see edu.ncsu.csc.itrust.model.fitness.FitnessImportFactory#importFitnessInfo(long, java.io.InputStream)
	 */
	public void importFitnessInfo(long pid, InputStream input) throws CSVFormatException, IOException, FitnessInfoFileFormatException
	{
		//First, get rid of the "Activities" line, this will mess up the CSVParser
		Scanner scan = new Scanner(new InputStreamReader(input));
		if (!scan.hasNextLine())
		{
			scan.close();
			throw new FitnessInfoFileFormatException("The given file did not have an \"Activities\" header at the very top");
		}
		String activitiesLine = scan.nextLine();
		if (!activitiesLine.contains("Activities"))
		{
			scan.close();
			throw new FitnessInfoFileFormatException("The given file did not have an \"Activities\" header at the very top");
		}
		//Now we have gotten rid of that useless line, so open up the CSV Parser
		CSVParser csv = new CSVParser(scan);
		ArrayList<String> header = csv.getHeader();
		ArrayList<ArrayList<String>> data = csv.getData();
		validateHeader(header);
		validateData(data);
		
		//Now we have valid data and headers, put the data into the database
		for (int i = 0; i < data.size(); i++)
		{
			FitnessInfo fi = convertRowToFitnessInfo(data.get(i), pid);
			try
			{
				fisql.add(fi); //will automatically insert/update values, overwriting is default for updates
			} catch (FormValidationException | DBException e)
			{
				throw new FitnessInfoFileFormatException("Invalidly formatted data");
			}
		}
		scan.close();
	}
	
	/**
	 * Imports the fitness info from the given header and data
	 * @param pid whose fitness data the info corresponds to
	 * @param header the header of the file
	 * @param data the data content of the file
	 * @throws FitnessInfoFileFormatException
	 */
	public void importFitnessInfo(long pid, ArrayList<String> header, ArrayList<ArrayList<String>> data) throws FitnessInfoFileFormatException
	{
		if (header == null || data == null)
		{
			throw new FitnessInfoFileFormatException("Empty header or data");
		}
		validateHeader(header);
		validateData(data);
		
		//Now we have valid data and headers, put the data into the database
		for (int i = 0; i < data.size(); i++)
		{
			FitnessInfo fi = convertRowToFitnessInfo(data.get(i), pid);
			try
			{
				fisql.add(fi); //will automatically insert/update values, overwriting is default for updates
			} catch (FormValidationException | DBException e)
			{
				throw new FitnessInfoFileFormatException("Invalidly formatted data");
			}
		}
	}
	
	/**
	 * Converts a row of a CSV file into a FitnessInfo object
	 * Precondition: the row has been validated
	 * @param row the row of the CSV file
	 * @param pid the PID that this information corresponds to
	 * @return a FitnessInfo object representing the row
	 * @throws FitnessInfoFileFormatException if we cannot parse the information
	 */
	private FitnessInfo convertRowToFitnessInfo(ArrayList<String> row, long pid) throws FitnessInfoFileFormatException
	{
		String date = convertDate(row.get(0)); //get the date in YYYY-MM-DD format
		FitnessInfo ret = new FitnessInfo(pid, date, getIntRepresentation(row.get(2)), getIntRepresentation(row.get(1)), getDoubleRepresentation(row.get(3)), getIntRepresentation(row.get(4)), getIntRepresentation(row.get(9)), getIntRepresentation(row.get(5)), getIntRepresentation(row.get(7)), getIntRepresentation(row.get(6)), getIntRepresentation(row.get(8)), 0, 0, 0, 0, 0);
		return ret;
	}
	
	/**
	 * Turns the string into an integer.  If the string is null or empty, turns it into a zero
	 * @param str the string to convert
	 * @return the integer representation
	 * @throws FitnessInfoFileFormatException
	 */
	private int getIntRepresentation(String str) throws FitnessInfoFileFormatException
	{
		if (str == null || str.equals(""))
		{
			return 0;
		}
		else
		{
			return getIntFromString(str);
		}
	}
	
	/**
	 * Turns the string into a double.  If the string is null or empty, turns it into a zero
	 * @param str hte string to convert
	 * @return the integer representation
	 */
	private double getDoubleRepresentation(String str)
	{
		if (str == null || str.equals(""))
		{
			return 0;
		}
		else
		{
			return Double.parseDouble(str);
		}
	}
	
	/**
	 * Verifies the header of the CSV file is in the format
	 * "Date", "Calories Burned", "Steps", "Distance", "Floors", "Minutes Sedentary", "Minutes Lightly Active", "Minutes Fairly Active", "Minutes Very Active", "Activity Calories"
	 * @param header the header of the CSV file
	 * @throws FitnessInfoFileFormatException if not a valid header
	 */
	private void validateHeader(ArrayList<String> header) throws FitnessInfoFileFormatException
	{
		//The correct headers
		String[] correctHeader = {"Date", "Calories Burned", "Steps", "Distance", "Floors", "Minutes Sedentary", "Minutes Lightly Active", "Minutes Fairly Active", "Minutes Very Active", "Activity Calories"};
		
		//First make sure the lengths of the headers are the same
		if (header.size() != correctHeader.length)
		{
			throw new FitnessInfoFileFormatException("The header of the file is of the incorrect length");
		}
		
		//Now make sure the header is using the correctHeaders
		for (int i = 0; i < correctHeader.length; i++)
		{
			if (!header.get(i).equalsIgnoreCase(correctHeader[i]))
			{
				throw new FitnessInfoFileFormatException("Incorrect header field.  It was " + header.get(i) + " when it should have been " + correctHeader[i]);
			}
		}
	}
	
	/**
	 * Data is valid when:
	 * (1) The date is in the format (M)M/(D)D/YY and exists
	 * (2) All fields except Distance are non-negative ints
	 * (3) Distance is a non-negative double
	 * The database will check to make sure CaloriesBurned >= Activity Calories, so this is not checked
	 * in this method
	 * @param data the data to verify
	 * @throws FitnessInfoFileFormatException when the data is invalid
	 */
	private void validateData(ArrayList<ArrayList<String>> data) throws FitnessInfoFileFormatException
	{
		//First, check all dates
		ArrayList<String> dates = getColumn(data, 0);
		for (int i = 0; i < dates.size(); i++)
		{
			if (!dateIsValid(dates.get(i)))
			{
				throw new FitnessInfoFileFormatException("Date in an invalid format");
			}
		}
		
		//Now check all other columns except the fourth (colNum = 3 since 0-indexed, distance) are non-negative ints
		for (int i = 0; i < data.size(); i++) //for every row
		{
			ArrayList<String> row = data.get(i);
			for (int j = 1; j < row.size(); j++) //for every column except the date
			{
				if (j == 3) //skip col=3, the fourth column which is distance
				{
					continue;
				}
				String element = row.get(j);
				if (element == null || element.equals(""))
				{
					row.set(j, "0");
				}
				//System.out.println(j + " value: " + element);
				int val = -1;
				if (element == null || element.equals(""))
				{
					val = 0;
				}
				else
				{
					val = getIntFromString(element);
				}
				if (val < 0)
				{
					throw new FitnessInfoFileFormatException("Encountered a negative value in the file");
				}
			}
		}
		
		//Now check the distances are non-negative
		ArrayList<String> distances = getColumn(data, 3);
		for (int i = 0; i < distances.size(); i++)
		{
			try
			{
				double val = Double.parseDouble(distances.get(i));
				if (val < 0)
				{
					throw new FitnessInfoFileFormatException("Encountered a negative value in the file");
				}
			}
			catch (NumberFormatException e)
			{
				throw new FitnessInfoFileFormatException("Unable to read distances");
			}
		}
		
	}
	
	/**
	 * Converts an integer in a string format I,III,III,... into an int
	 * @param str the integer in a string format
	 * @return the int representation of the string
	 */
	private int getIntFromString(String str) throws FitnessInfoFileFormatException
	{
		str = str.replace(",", "");
		try
		{
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			throw new FitnessInfoFileFormatException("Unable to parse integers in the file");
		}
	}
	
	/**
	 * Converts a date in format (M)M/(D)D/YY to YYYY-MM-DD format
	 * @param date the date in format (M)M/(D)D/YY
	 * @return a date in format YYYY-MM-DD
	 */
	private String convertDate(String date)
	{
		//Precondition: date is valid
		String[] split = date.split("/");
		try
		{
			int month = Integer.parseInt(split[0]);
			int day = Integer.parseInt(split[1]);
			int year = Integer.parseInt(split[2]);
			//Convert YY to YYYY
			DateFormat sdfp = new SimpleDateFormat("yy");
			Date d = sdfp.parse(year + "");
			DateFormat sdff = new SimpleDateFormat("yyyy");
			String syear = sdff.format(d);
			
			String smonth, sday;
			//Convert month to MM
			if (month < 10)
			{
				smonth = "0" + month;
			}
			else
			{
				smonth = month + "";
			}
			//Convert day to DD
			if (day < 10)
			{
				sday = "0" + day;
			}
			else
			{
				sday = day + "";
			}
			
			return syear + "-" + smonth + "-" + sday;
			
		}
		catch (NumberFormatException e)
		{
			throw new DateFormatException("Invalid date");
		} catch (ParseException e)
		{
			throw new DateFormatException("Invalid date");
		}
	}
	/**
	 * Checks to see if the given date is in the format (M)M/(D)D/YY and exists
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean dateIsValid(String date)
	{
		String split[] = date.split("/");
		if (split.length != 3) //should only have 3 parts
		{
			return false;
		}
		try
		{
			int month = Integer.parseInt(split[0]);
			int day = Integer.parseInt(split[1]);
			int year = Integer.parseInt(split[2]);
			if (month > 12 || month < 1 || day < 1 || day > 31 || year < 0 || year > 99)
			{
				return false;
			}
			
			//try to make a date out of this information
			//If this fails, then the given date is not a valid one
			
			try {
				DateFormat sdfp = new SimpleDateFormat("yy");
				Date d = sdfp.parse(year + "");
				DateFormat sdff = new SimpleDateFormat("yyyy");
				year = Integer.parseInt(sdff.format(d));
				//Now we have the year as a four digit number
				
				
				Calendar cal = Calendar.getInstance();
				cal.setLenient(false);  //No false dates allowed
				cal.setTime(new Date(year-1900, month-1, day)); //set the time
			    cal.getTime(); //possibly throw an excepton
			}
			catch (Exception e) {
			  //Date does not exist
				return false;
			}
			
			
			//Passes all the checks
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * Gets the n-th column of a set of data, n>=0
	 * @param data the data in a 2D ArrayList
	 * @param col a 0-indexed column
	 * @return the n-th column, 0-indexed
	 */
	private ArrayList<String> getColumn(ArrayList<ArrayList<String>> data, int col)
	{
		if (data.size() == 0)
		{
			return null;
		}
		if (col < 0 || col > data.get(0).size())
		{
			return null;
		}
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) //go through all rows
		{
			ret.add(data.get(i).get(col));
		}
		return ret;
	}

}
