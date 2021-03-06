package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ViewApptRequestsAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean;
import edu.ncsu.csc.itrust.model.old.beans.MessageBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewApptRequestsActionTest  {

	private ViewApptRequestsAction action;
	private TestDataGenerator gen = new TestDataGenerator();
	private MessageDAO mDAO;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen.clearAllTables();
		gen.standardData();
		gen.apptRequestConflicts();
		action = new ViewApptRequestsAction(9000000000L, TestDAOFactory.getTestInstance());
		mDAO = TestDAOFactory.getTestInstance().getMessageDAO();
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetApptRequests() throws Exception {
		List<ApptRequestBean> list = action.getApptRequests();
		assertEquals(1, list.size());
		assertEquals(2L, list.get(0).getRequestedAppt().getPatient());
	}

	@Test
	public void testAcceptApptRequest() throws Exception {
		List<ApptRequestBean> list = action.getApptRequests();
		assertEquals(1, list.size());
		assertEquals(2L, list.get(0).getRequestedAppt().getPatient());
		String res = action.acceptApptRequest(list.get(0).getRequestedAppt().getApptID());
		assertEquals("The appointment request you selected has been accepted and scheduled.", res);
		list = action.getApptRequests();
		assertEquals(1, list.size());
		assertEquals(2L, list.get(0).getRequestedAppt().getPatient());
		assertTrue(list.get(0).isAccepted());
		List<MessageBean> msgs = mDAO.getMessagesForMID(list.get(0).getRequestedAppt().getPatient());
		assertEquals(list.get(0).getRequestedAppt().getHcp(), msgs.get(0).getFrom());
		assertTrue(msgs.get(0).getBody().contains("has been accepted."));
	}

	@Test
	public void testRejectApptRequest() throws Exception {
		List<ApptRequestBean> list = action.getApptRequests();
		assertEquals(1, list.size());
		assertEquals(2L, list.get(0).getRequestedAppt().getPatient());
		String res = action.rejectApptRequest(list.get(0).getRequestedAppt().getApptID());
		assertEquals("The appointment request you selected has been rejected.", res);
		list = action.getApptRequests();
		assertEquals(1, list.size());
		assertEquals(2L, list.get(0).getRequestedAppt().getPatient());
		assertFalse(list.get(0).isAccepted());
		List<MessageBean> msgs = mDAO.getMessagesForMID(list.get(0).getRequestedAppt().getPatient());
		assertEquals(list.get(0).getRequestedAppt().getHcp(), msgs.get(0).getFrom());
		assertTrue(msgs.get(0).getBody().contains("has been rejected."));
	}

}
