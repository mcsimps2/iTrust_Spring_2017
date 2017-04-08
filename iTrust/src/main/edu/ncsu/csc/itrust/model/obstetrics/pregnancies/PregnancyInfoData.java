package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface PregnancyInfoData extends DataBean<PregnancyInfo> {
	/**
	 * Returns all pregnancy records from all initializations for a given patient
	 * @param pid the patient's ID
	 * @return a list of the found records
	 * @throws DBException 
	 */
	public List<PregnancyInfo> getRecords(long pid) throws DBException;
	
	
	/**
	 * Returns the pregnancy records saved from an obstetrics initialization.  This includes all
	 * prior pregnancies found in the system as well as any added during the initialization.  This
	 * is what should be displayed to HCPs when viewing past obstetrics initializations.
	 * @param oid
	 * @return
	 * @throws DBException
	 */
	public List<PregnancyInfo> getRecordsFromInit(long oid) throws DBException;
	
	/**
	 * Adds a record to the obstetricsInit DB
	 * @param oi the record to add
	 * @return true if the add was successful
	 * @throws DBException 
	 */
	public boolean add(PregnancyInfo pi) throws DBException;
}
