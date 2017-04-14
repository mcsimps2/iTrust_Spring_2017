/**
 * Tests for AddUAPAction
 */

package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.AddUAPAction;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddUAPActionTest  {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	private AddUAPAction action;

	/**
	 * Sets up defaults
	 */
	@Before
	public void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		action = new AddUAPAction(factory, 9000000000L);
	}

	/**
	 * Tests adding a new UAP
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddUAP() throws Exception {
		PersonnelBean p = new PersonnelBean();
		p.setFirstName("Cosmo");
		p.setLastName("Kramer");
		p.setEmail("cosmo@kramer.com");
		long newMID = action.add(p);
		assertEquals(p.getMID(), newMID);
	}
}
