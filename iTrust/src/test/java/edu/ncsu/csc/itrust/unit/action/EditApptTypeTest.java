package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;

import edu.ncsu.csc.itrust.action.EditApptTypeAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EditApptTypeTest  {

	private EditApptTypeAction action;
	private DAOFactory factory;
	private long adminId = 9000000001L;

	@Before
	public void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

		this.factory = TestDAOFactory.getTestInstance();
		this.action = new EditApptTypeAction(this.factory, this.adminId);
	}

	@Test
	public void testGetApptTypes() throws SQLException, DBException {
		assertEquals(8, action.getApptTypes().size());
	}

	@Test
	public void testAddApptType() throws SQLException, FormValidationException, DBException {
		ApptTypeBean a = new ApptTypeBean();
		a.setName("Test");
		a.setDuration(30);

		assertTrue(action.addApptType(a).startsWith("Success"));
		assertEquals(9, action.getApptTypes().size());
	}

	@Test
	public void testAddApptType2() throws SQLException, FormValidationException, DBException {
		ApptTypeBean a = new ApptTypeBean();
		a.setName("General Checkup");
		a.setDuration(30);

		assertTrue(action.addApptType(a).equals("Appointment Type: General Checkup already exists."));
	}

	@Test
	public void testEditApptType() throws SQLException, FormValidationException, DBException {
		ApptTypeBean a = new ApptTypeBean();
		a.setName("General Checkup");
		a.setDuration(30);

		assertTrue(action.editApptType(a).startsWith("Success"));
	}

	@Test
	public void testEditApptType2() throws SQLException, FormValidationException, DBException {
		ApptTypeBean a = new ApptTypeBean("General Checkup", 45);

		assertEquals("Appointment Type: General Checkup already has a duration of 45 minutes.", action.editApptType(a));
	}

	@Test
	public void testAddApptTypeLengthZero() throws SQLException, DBException {
		ApptTypeBean a = new ApptTypeBean();
		a.setName("Test");
		a.setDuration(0);

		try {
			action.addApptType(a);
		} catch (FormValidationException e) {
			// Exception is good.
			return;
		}
		assertTrue(false);
	}

	@Test
	public void testEditNonExistentApptType() throws SQLException, FormValidationException, DBException {
		ApptTypeBean a = new ApptTypeBean();
		a.setName("NonExistent");
		a.setDuration(30);

		assertEquals("Appointment Type: " + a.getName() + " you are trying to update does not exist.",
				action.editApptType(a));
	}
}
