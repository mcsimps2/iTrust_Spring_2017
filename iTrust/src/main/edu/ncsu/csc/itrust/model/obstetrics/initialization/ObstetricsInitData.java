package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;


public interface ObstetricsInitData
{	
	/**
	 * Returns a list of records for a given patient
	 * @param pid the patient's ID
	 * @return a list of the found records
	 * @throws DBException 
	 */
	public List<ObstetricsInit> getRecords(long pid) throws DBException;
	
	/**
	 * Adds a record to the obstetricsInit DB
	 * @param oi the record to add
	 * @return true if the add was successful
	 * @throws DBException 
	 */
	public boolean add(ObstetricsInit oi) throws DBException;
}
