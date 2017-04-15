package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.GetUserNameAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class GetUserNameActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen.clearAllTables();
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testCorrectFormat() throws Exception {
		gen.hcp0();
		assertEquals("Kelly Doctor", new GetUserNameAction(factory).getUserName("9000000000"));
	}

	@Test
	public void testWrongFormat() throws Exception {
		gen.hcp0();
		try {
			new GetUserNameAction(factory).getUserName("90000000aaa01");
			fail("Exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("MID not in correct form", e.getMessage());
		}
	}
}
