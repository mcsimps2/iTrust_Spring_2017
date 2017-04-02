package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;

import java.util.List;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface NewbornData extends DataBean<Newborn>
{
	/**
	 * Method to get the correct childbirth newborns
	 * @param officeVisitID the office visit corresponding to the childbirth newborns
	 * @return the Newborns
	 */
	public List<Newborn> getByOfficeVisit(long officeVisitID) throws DBException;
	
	/**
	 * Add and returns the ID from the added newborn
	 * @param nb the newborn to add
	 * @return the generated ID
	 * @throws DBException
	 */
	public long addReturnGeneratedId(Newborn nb) throws DBException;
}