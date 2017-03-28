package edu.ncsu.csc.itrust.model.obstetrics.ultrasound;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

/**
 * Interface for UltrasoundMySQL object
 * @author jcgonzal
 */
public interface UltrasoundData extends DataBean<Ultrasound> {
	
	/**
	 * Method to get the ultrasound by the office visit
	 * @param officeVisitID
	 * @return the Ultrasound pojo
	 * @throws DBException
	 */
	public List<Ultrasound> getByOfficeVisit(long officeVisitID) throws DBException;
	
	/**
	 * Method to delete ultrasound
	 * @param id
	 * @return success
	 * @throws DBException
	 */
	public boolean delete(long id) throws DBException;

}
