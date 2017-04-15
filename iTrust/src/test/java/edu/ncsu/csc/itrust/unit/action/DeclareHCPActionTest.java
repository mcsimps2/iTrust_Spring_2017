package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import edu.ncsu.csc.itrust.action.DeclareHCPAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.TransactionBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeclareHCPActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private DeclareHCPAction action;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen.clearAllTables();
		gen.patient2();
		gen.hcp0();
		gen.hcp3();
		action = new DeclareHCPAction(factory, 2L);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetDeclared() throws Exception {
		List<PersonnelBean> decs = action.getDeclaredHCPS();
		assertEquals(1, decs.size());
		assertEquals(9000000003L, decs.get(0).getMID());
	}

	@Test
	public void testDeclareNormal() throws Exception {
		assertEquals("HCP successfully declared", action.declareHCP("9000000000"));
		List<PersonnelBean> decs = action.getDeclaredHCPS();
		assertEquals(2, decs.size());
		assertEquals(9000000000L, decs.get(0).getMID());

	}

	@Test
	public void testUnDeclareNormal() throws Exception {
		assertEquals("HCP successfully undeclared", action.undeclareHCP("9000000003"));
		List<PersonnelBean> decs = action.getDeclaredHCPS();
		assertEquals(0, decs.size());
	}

	@Test
	public void testDeclareAlreadyDeclared() throws Exception {
		try {
			action.declareHCP("9000000003");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("HCP 9000000003 has already been declared for patient 2", e.getMessage());
		}
		List<PersonnelBean> decs = action.getDeclaredHCPS();
		assertEquals(1, decs.size());
		assertEquals(9000000003L, decs.get(0).getMID());
		// Assert the transaction
		List<TransactionBean> transList = factory.getTransactionDAO().getAllTransactions();
		assertEquals(0, transList.size());
	}

	@Test
	public void testUnDeclareNotDeclared() throws Exception {
		assertEquals("HCP not undeclared", action.undeclareHCP("9000000000"));
		List<PersonnelBean> decs = action.getDeclaredHCPS();
		assertEquals(1, decs.size());
		// Assert the transaction
		List<TransactionBean> transList = factory.getTransactionDAO().getAllTransactions();
		assertEquals(0, transList.size());
	}

	@Test
	public void testDeclareAdmin() throws Exception {
		gen.admin1();
		try {
			action.declareHCP("9000000001");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("This user is not a licensed healthcare professional!", e.getMessage());
		}
	}
}
