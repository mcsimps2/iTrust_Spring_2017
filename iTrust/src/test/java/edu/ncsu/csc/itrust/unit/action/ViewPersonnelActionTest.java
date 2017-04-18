package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ViewPersonnelAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewPersonnelActionTest  {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ViewPersonnelAction action;
	private TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient4();
		gen.hcp3();
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testViewPersonnel() throws Exception {
		action = new ViewPersonnelAction(factory, 4L);
		PersonnelBean hcp = action.getPersonnel("9000000003");
		assertEquals(9000000003L, hcp.getMID());
	}

	@Test
	public void testNoPersonnel() throws Exception {
		action = new ViewPersonnelAction(factory, 4L);
		try {
			action.getPersonnel("9000000000");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("No personnel record exists for this MID", e.getMessage());
		}
	}

	@Test
	public void testNotANumber() throws Exception {
		action = new ViewPersonnelAction(factory, 4L);
		try {
			action.getPersonnel("a");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("MID not a number", e.getMessage());
		}
	}

}
