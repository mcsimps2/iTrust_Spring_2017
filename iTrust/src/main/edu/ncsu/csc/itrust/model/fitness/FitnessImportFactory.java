package edu.ncsu.csc.itrust.model.fitness;

import java.io.IOException;
import java.io.InputStream;

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
	
}
