package edu.ncsu.csc.itrust.model.obstetrics.pregnancies;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;

public interface PregnancyInfoData {
	/**
	 * Returns all pregnancy records from all initializations for a given patient
	 * @param pid the patient's ID
	 * @return a list of the found records
	 * @throws DBException 
	 */
	public List<PregnancyInfo> getRecords(long pid) throws DBException;
	
	/**
	 * You probably don't want to use this method. Look at getRecordsFromInit instead.
	 * Returns the pregnancy records ADDED during a specific obstetric initialization
	 * @param obstetricsInitID the ID of the initialization where the pregnancy records were added
	 * @return a list of the found records
	 * @throws DBException
	 */
	public List<PregnancyInfo> getNewRecordsAddedDuringInit(int obstetricsInitID) throws DBException;
	
	/**
	 * Returns the pregnancy records saved from an obstetrics initialization.  This includes all
	 * prior pregnancies found in the system as well as any added during the initialization.  This
	 * is what should be displayed to HCPs when viewing past obstetrics initializations.
	 * @param obstetricsInitID
	 * @return
	 * @throws DBException
	 */
	public List<PregnancyInfo> getRecordsFromInit(int obstetricsInitID) throws DBException;
	
	/**
	 * Adds a record to the obstetricsInit DB
	 * @param oi the record to add
	 * @return true if the add was successful
	 * @throws DBException 
	 */
	public boolean add(PregnancyInfo pi) throws DBException;
	
	/**
	 * Returns the unique record corresponding to the given recordID
	 * @param recordID the unique ID of the record in the DB, set by MySQL
	 * @return the record corresponding to the recordID
	 * @throws DBException
	 */
	public PregnancyInfo getRecordByID(int recordID) throws DBException;
}
