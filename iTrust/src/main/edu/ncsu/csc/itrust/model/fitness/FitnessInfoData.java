package edu.ncsu.csc.itrust.model.fitness;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

/**
 * Interface for interacting with the database to get fitness information for
 * specified PIDs and dates
 * @author mcsimps2
 *
 */
public interface FitnessInfoData extends DataBean<FitnessInfo>
{
	//public void addFitnessInfo(FitnessInfo info); already in DataBean<FitnessInfo> interface
	/**
	 * Gets the fitness info for a given PID and date from the database
	 * @param pid the pid of the fitness info to search for
	 * @param date the date of the fitness info in the format YYYY-MM-DD
	 * @return a FitnessInfo object containing all the relevant data in the database for the given pid and date
	 * @throws DBException
	 */
	public FitnessInfo getFitnessInfo(long pid, String date) throws DBException;
	
	/**
	 * Gets the fitness info for a given PID and span of dates
	 * @param pid whose information we are looking for
	 * @param startDate the earliest date of info that will be returned in the search results
	 * @param endDate the latest date of info that will be returned in the search results
	 * @return a list of FitnessInfo objects containing the info relevant to the given PID and whose dates occur within the given span of dates
	 * @throws DBException
	 */
	public FitnessInfo[] getFitnessInfo(long pid, String startDate, String endDate) throws DBException;
	
	/**
	 * Removes the fitness data entry in the database corresponding to the given PID and date
	 * @param pid whose fitness data we are looking for
	 * @param date the date of the fitness data
	 * @return true if the data was successfully removed
	 * @throws DBException
	 */
	public boolean removeFitnessInfo(long pid, String date) throws DBException;
	/**
	 * Removes the fitness data entries in the database corresponding to the given PID and having
	 * a date falling within a certain span.
	 * @param pid whose fitness data we are looking for
	 * @param startDate the earliest date of info that will be removed
	 * @param endDate the latest date of info that will be removed
	 * @return true if the data was successfully removed
	 * @throws DBException
	 */
	public boolean removeFitnessInfo(long pid, String startDate, String endDate) throws DBException;
}
