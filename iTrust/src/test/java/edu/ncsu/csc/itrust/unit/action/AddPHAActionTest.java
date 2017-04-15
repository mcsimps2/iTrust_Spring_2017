/**
 * Tests AddHCPAction in that the validator is checked and the proper DAOs are hit.
 * 
 * This tests is an example of using Mock Objects. If you haven't covered this in class (yet, or at all), then
 * disregard this class. If you are trying to learn about unit testing with Mock Objects, this is a great
 * class to start with.
 */

package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;

import edu.ncsu.csc.itrust.action.AddPHAAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.ActionTestWithMocks;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddPHAActionTest extends ActionTestWithMocks {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private PersonnelBean personnel;
	private TestDataGenerator gen;

	/**
	 * Sets up defaults
	 */
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();

		personnel = new PersonnelBean();
		personnel.setFirstName("Bob");
		personnel.setLastName("Blah");
		personnel.setEmail("bobblah@blarg.com");
		personnel.setRole(Role.PHA);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	/**
	 * Tests adding a new PHA
	 * 
	 * @throws Exception
	 * 
	 */
	public void testAddPHA() throws Exception {
		AddPHAAction action = new AddPHAAction(factory, 7000000000L);
		long newMID = action.add(personnel);
		assertEquals(7000000000L, newMID);
	}
}
