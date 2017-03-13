package edu.ncsu.csc.itrust.model.fitness;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc.itrust.CSVParser;
import edu.ncsu.csc.itrust.exception.CSVFormatException;


/**
 * Defines a general method to import fitness info given a CSV file
 * @author mcsimps2
 *
 */
public abstract class FitnessImportFactory 
{
	/**
	 * Import fitness info from a file passed through as an input stream
	 * @param pid whose fitness info we are parsing
	 * @param input the file input stream to parse through for fitness data
	 * @throws CSVFormatException 
	 * @throws IOException 
	 * @throws FitnessInfoFileFormatException 
	 */
	public abstract void importFitnessInfo(long pid, InputStream input) throws CSVFormatException, IOException, FitnessInfoFileFormatException;
	
	/**
	 * Imports the fitness info from the given header and data
	 * @param pid whose fitness data the info corresponds to
	 * @param header the header of the file
	 * @param data the data content of the file
	 * @throws FitnessInfoFileFormatException
	 */
	public abstract void importFitnessInfo(long pid, ArrayList<String> header, ArrayList<ArrayList<String>> data) throws FitnessInfoFileFormatException;
}
