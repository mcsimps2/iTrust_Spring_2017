package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;
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
}
