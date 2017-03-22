package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;


public interface ObstetricsInitData extends DataBean<ObstetricsInit>
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
	
	/**
	 * Adds a record to the obstetricsInit DB and returns the ID of the newly inserted record
	 * @param oi the record to add
	 * @return the ID of the new row, -1 if unsuccessful
	 * @throws DBException 
	 */
	public int addAndReturnID(ObstetricsInit oi) throws DBException;
}
