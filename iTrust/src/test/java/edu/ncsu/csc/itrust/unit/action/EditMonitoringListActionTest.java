package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.EditMonitoringListAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.TelemedicineBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class EditMonitoringListActionTest  {
	EditMonitoringListAction action;
	private TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient1();
		action = new EditMonitoringListAction(TestDAOFactory.getTestInstance(), 9000000000L);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testAddToRemoveFromList() throws Exception {
		TelemedicineBean tBean = new TelemedicineBean();
		assertTrue(action.addToList(1L, tBean));
		assertFalse(action.addToList(1L, tBean));
		assertTrue(action.removeFromList(1L));
		assertFalse(action.removeFromList(1L));
	}

	@Test
	public void testIsPatientInList() throws Exception {
		TelemedicineBean tBean = new TelemedicineBean();
		action.addToList(1L, tBean);
		assertTrue(action.isPatientInList(1));
		assertFalse(action.isPatientInList(2));
	}
}
