package edu.ncsu.itrust.model.obstetrics.visit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface ObstetricsVisitData extends DataBean<ObstetricsVisit> {
	
	/**
	 * Method to get the correct obstetrics visit
	 * @param officeVisitID the office visit corresponding to the obstetrics visit
	 * @return the ObstetricsVisit
	 */
	public ObstetricsVisit getObstetricsVisitByOfficeVisit(long officeVisitID) throws DBException;
}
