package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.DeclareHCPAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;

public class DeclareHCPActionExceptionTest  {
	private DeclareHCPAction action;

	@Before
	public void setUp() throws Exception {
		action = new DeclareHCPAction(EvilDAOFactory.getEvilInstance(), 2L);
	}

	@Test
	public void testDeclareMalformed() throws Exception {
		try {
			action.declareHCP("not a number");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("HCP's MID not a number", e.getMessage());
		}
	}

	@Test
	public void testUnDeclareMalformed() throws Exception {
		try {
			action.undeclareHCP("not a number");
			fail("exception should have been thrown");
		} catch (ITrustException e) {
			assertEquals("HCP's MID not a number", e.getMessage());
		}
	}

}
