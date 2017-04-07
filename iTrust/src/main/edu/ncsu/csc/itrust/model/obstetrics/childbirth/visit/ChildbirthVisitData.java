package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface ChildbirthVisitData extends DataBean<ChildbirthVisit>
{
	/**
	 * Method to get the correct childbirth visit
	 * @param officeVisitID the office visit corresponding to the childbirth visit
	 * @return the ChildbirthVisit
	 */
	public ChildbirthVisit getByOfficeVisit(long officeVisitID) throws DBException;
	
	/**
	 * Returns the ID from the childbirth visit
	 * @param ov
	 * @return
	 * @throws DBException
	 */
	public long addReturnGeneratedId(ChildbirthVisit ov) throws DBException;
	
	/**
	 * Method to get the childbirth visits that correspond to an obstetrics init
	 * @param obstetricsInitID the obstetrics init id
	 * @return the List of ChildbirthVisits
	 */
	public List<ChildbirthVisit> getByObstetricsInit(long obstetricsInitID) throws DBException;
}
