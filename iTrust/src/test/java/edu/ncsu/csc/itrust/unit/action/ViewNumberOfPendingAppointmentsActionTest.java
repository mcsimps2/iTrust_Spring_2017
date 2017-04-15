
package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.action.ViewApptRequestsAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class ViewNumberOfPendingAppointmentsActionTest  {
	private ViewApptRequestsAction action;
	private DAOFactory factory;
	private long mid = 1L;
	private long hcpId = 9000000000L;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

		gen.pendingAppointmentAlert();
		this.factory = TestDAOFactory.getTestInstance();
		this.action = new ViewApptRequestsAction(this.hcpId, this.factory);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetNumRequest() throws SQLException, DBException {
		List<ApptRequestBean> reqs = action.getApptRequests();
		assertEquals(1, action.getNumRequests(reqs));
	}

}
