package edu.ncsu.csc.itrust.unit.action;

import static edu.ncsu.csc.itrust.unit.testutils.JUnitiTrustUtils.assertTransactionsNone;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.EditRepresentativesAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class EditRepresentativesActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private EditRepresentativesAction action;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen.clearAllTables();
		gen.patient1(); // 2 represents 1, but not 4
		gen.patient2();
		gen.patient4();
		gen.multiplePatients_old();
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetRepresentatives() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		List<PatientBean> reps = action.getRepresented(2L);
		assertEquals(1, reps.size());
		assertEquals(1L, reps.get(0).getMID());
	}

	@Test
	public void testAddRepresentee() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		action.addRepresentee("4");
		List<PatientBean> reps = action.getRepresented(2);
		assertEquals(2, reps.size());
		assertEquals(4L, reps.get(1).getMID());
	}

	@Test
	public void testRemoveRepresentee() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		action.removeRepresentee("1");
		List<PatientBean> reps = action.getRepresented(2);
		assertEquals(0, reps.size());
	}

	@Test
	public void testAddNotNumber() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		assertEquals("MID not a number", action.addRepresentee("a"));
		assertEquals("MID not a number", action.removeRepresentee("a"));
	}

	@Test
	public void testRemoveNothing() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		assertEquals("No change made", action.removeRepresentee("2"));
		assertTransactionsNone();
		assertEquals(1, action.getRepresented(2).size());
	}

	@Test
	public void testCannotRepresentSelf() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		try {
			action.addRepresentee("2");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("This user cannot represent themselves.", e.getMessage());
		}
		assertTransactionsNone();
		assertEquals(1, action.getRepresented(2).size());
	}

	@Test
	public void testNotAPatient() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		assertEquals("No change made", action.removeRepresentee("9000000000"));
		assertTransactionsNone();
		assertEquals(1, action.getRepresented(2).size());
	}

	@Test
	public void testCheckIfPatientIsActive() throws ITrustException {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");

		assertFalse(action.checkIfPatientIsActive(9000000000L));
		assertFalse(action.checkIfPatientIsActive(0L));

	}

	@Test
	public void testGetRepresentativeName() throws Exception {
		action = new EditRepresentativesAction(factory, 9000000000L, "2");
		assertEquals("Andy Programmer", action.getRepresentativeName());
	}

	/**
	 * Tests that non patients cannot add representatives
	 */
	@Test
	public void testNonPatientAddRepresentative() {
		try {
			action = new EditRepresentativesAction(factory, 9000000000L, "2");
			action.addRepresentee("9000000071");
			fail("Representee is not a patient");
		} catch (ITrustException e) {
			assertTrue(e.getMessage().contains("This user is not a patient"));
		}
	}

}
