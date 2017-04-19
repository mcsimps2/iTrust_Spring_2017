package edu.ncsu.csc.itrust.unit.action;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ChangeSessionTimeoutAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ChangeSessionTimeoutActionTest  {
	ChangeSessionTimeoutAction action = new ChangeSessionTimeoutAction(TestDAOFactory.getTestInstance());

	@Test
	public void testNotANumber() throws Exception {
		try {
			action.changeSessionTimeout("a");
			fail("exception should have been thrown");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("That is not a number", e.getErrorList().get(0));
		}
	}

	@Test
	public void testBadNumber() throws Exception {
		try {
			action.changeSessionTimeout("0");
			fail("exception should have been thrown");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Must be a number greater than 0", e.getErrorList().get(0));
		}
	}

	@Test
	public void testFullChange() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.timeout();
		assertEquals(20, action.getSessionTimeout());
		action.changeSessionTimeout("21");
		assertEquals(21, action.getSessionTimeout());
	}
}
