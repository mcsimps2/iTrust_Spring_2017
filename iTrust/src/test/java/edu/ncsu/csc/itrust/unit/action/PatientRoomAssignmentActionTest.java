package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.PatientRoomAssignmentAction;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.WardRoomBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.WardDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 */
public class PatientRoomAssignmentActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private PatientRoomAssignmentAction action;
	private WardDAO wardDAO;
	private PatientBean patient = new PatientBean();

	@Before
	public void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		wardDAO = new WardDAO(factory);
	}

	/**
	 * testassignPatient
	 * 
	 * @throws Exception
	 */
	@Test
	public void testassignPatient() throws Exception {
		WardRoomBean rm = new WardRoomBean(1, 1, 1, "test", "clean");
		wardDAO.removeWardRoom(rm.getRoomID());
		action = new PatientRoomAssignmentAction(factory);
		action.assignPatientToRoom(rm, 1);
		assertEquals(0, wardDAO.getAllWardRoomsByWardID(0).size());
		wardDAO.removeWardRoom(rm.getRoomID());
		patient.setMID(1L);
		action.assignPatientToRoom(rm, patient);
	}

}
