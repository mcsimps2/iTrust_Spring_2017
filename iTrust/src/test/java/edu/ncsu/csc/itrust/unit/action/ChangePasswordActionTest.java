package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ChangePasswordAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ChangePasswordActionTest  {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private ChangePasswordAction action;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen.clearAllTables();
		gen.standardData();
		action = new ChangePasswordAction(factory);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testChangePassword() throws Exception {
		String response1 = action.changePassword(1L, "pw", "pass1", "pass1");
		String response2 = action.changePassword(1L, "pass1", "pw1", "pw1");
		String response3 = action.changePassword(1L, "pass1", "password", "password");
		String response4 = action.changePassword(1L, "shallnotpass", "pass1", "pass1");

		assertTrue(response1.contains("Password Changed"));
		assertTrue(response2.contains("Invalid password"));
		assertTrue(response3.contains("Invalid password"));
		assertTrue(response4.contains("Invalid password"));
	}

}
