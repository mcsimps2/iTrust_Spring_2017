package edu.ncsu.csc.itrust.model.fitness;

public class FitnessInfoFileFormatException extends Exception
{

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public FitnessInfoFileFormatException(String msg)
	{
		super(msg);
	}
	
	public FitnessInfoFileFormatException()
	{
		super("File format exception in the Fitness Info file");
	}
	
}
