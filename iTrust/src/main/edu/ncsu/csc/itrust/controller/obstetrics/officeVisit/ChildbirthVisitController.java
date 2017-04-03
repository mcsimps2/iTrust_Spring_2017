package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obstetrics_visit_controller")
@SessionScoped
public class ChildbirthVisitController extends iTrustController {
	
	private ChildbirthVisitData cvData;

	/**
	 * Default constructor.
	 * @throws DBException
	 */
	public ChildbirthVisitController() throws DBException {
		this.cvData = new ChildbirthVisitMySQL();
	}

	/**
	 * For testing purposes.
	 * @param ds DataSource
	 * @param sessionUtils SessionUtils instance
	 */
	public ChildbirthVisitController(DataSource ds, SessionUtils sessionUtils) {
		this.cvData = new ChildbirthVisitMySQL(ds);
	}
	
	
	public ChildbirthVisit getByOfficeVisit(long officeVisitID) {
		try {
			return cvData.getByOfficeVisit(officeVisitID);
		} catch (DBException e) {
			e.printStackTrace();
			return null;
		}
	}
}