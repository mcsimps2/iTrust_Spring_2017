package edu.ncsu.csc.itrust.unit.action;

import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApptBeanTest  {

	@Test
	public void testApptEquals() {
		ApptBean b = new ApptBean();
		b.setApptID(3);

		ApptBean a = new ApptBean();
		a.setApptID(3);

		assertTrue(a.equals(b));
	}
}