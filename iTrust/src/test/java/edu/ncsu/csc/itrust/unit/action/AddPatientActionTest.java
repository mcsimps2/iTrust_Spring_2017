/**
 * Tests for AddPatientAction
 */

package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.AddPatientAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddPatientActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	private AddPatientAction action;

	/**
	 * Sets up defaults
	 */
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();

		gen.transactionLog();
		gen.hcp0();
		action = new AddPatientAction(factory, 9000000000L);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	/**
	 * Tests adding a new patient
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddPatient() throws Exception {
		AuthDAO authDAO = factory.getAuthDAO();

		// Add a dependent
		PatientBean p = new PatientBean();
		p.setFirstName("Jiminy");
		p.setLastName("Cricket");
		p.setEmail("make.awish@gmail.com");
		long newMID = action.addDependentPatient(p, 102, 9000000000L);
		assertEquals(p.getMID(), newMID);
		assertTrue(authDAO.isDependent(newMID));

		// Add a non-dependent
		p.setFirstName("Chuck");
		p.setLastName("Cheese");
		p.setEmail("admin@chuckecheese.com");
		newMID = action.addPatient(p, 9000000000L);
		assertEquals(p.getMID(), newMID);
		assertFalse(authDAO.isDependent(newMID));
	}
}
