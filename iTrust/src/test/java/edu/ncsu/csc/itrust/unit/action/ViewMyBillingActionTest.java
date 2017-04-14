package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;

import edu.ncsu.csc.itrust.action.ViewMyBillingAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ViewMyBillingActionTest  {
	private ViewMyBillingAction action;
	private long mid = 311L; // Sean Ford

	@Before
	public void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		gen.uc60();

		action = new ViewMyBillingAction(TestDAOFactory.getTestInstance(), this.mid);
	}

	@Test
	public void testGetMyUnpaidBills() throws DBException, SQLException {
		assertEquals(1, action.getMyUnpaidBills().size());
	}

	@Test
	public void testGetAllMyBills() throws DBException, SQLException {
		assertEquals(2, action.getAllMyBills().size());
	}

}
