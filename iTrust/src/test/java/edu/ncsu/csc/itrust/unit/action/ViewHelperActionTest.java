package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;

import edu.ncsu.csc.itrust.action.ViewHelperAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ViewHelperActionTest  {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUpdateUserPrefs() throws FormValidationException, SQLException, ITrustException {
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 0).equals("000000"));
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 1.0).equals("FFFFFF"));
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 0.5).equals("7F7F7F"));
	}

}
