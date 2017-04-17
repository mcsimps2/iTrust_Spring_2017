package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;

import edu.ncsu.csc.itrust.action.EventLoggingAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.TransactionBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class EventLoggingActionTest  {
	private EventLoggingAction action;
	private DAOFactory factory;
	private long mid = 1L;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		factory = TestDAOFactory.getTestInstance();
		action = new EventLoggingAction(factory);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetTransactions() throws FormValidationException, SQLException {
		try {
			action.logEvent(TransactionType.LOGIN_FAILURE, mid, 0, "");

			TransactionDAO dao = factory.getTransactionDAO();
			List<TransactionBean> all = dao.getAllTransactions();

			boolean passes = false;
			for (TransactionBean log : all) {
				if (log.getLoggedInMID() == mid && log.getTransactionType() == TransactionType.LOGIN_FAILURE) {
					passes = true;
					break;
				}
			}
			if (!passes) {
				fail();
			}
		} catch (DBException e) {
			fail();
		}
	}
}
