package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ViewReportAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class ViewReportActionTest  {

	private DAOFactory factory;
	private ViewReportAction action;
	private TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient2();
		factory = TestDAOFactory.getTestInstance();
		action = new ViewReportAction(factory, 2L);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testGetDeclaredHCPs() throws Exception {
		gen.hcp3();
		List<PersonnelBean> list = action.getDeclaredHCPs(2L);
		assertEquals(1, list.size());
	}

	@Test
	public void testGetPersonnel() throws ITrustException {
		PersonnelBean bean = action.getPersonnel(9000000000L);
		assertEquals("Kelly Doctor", bean.getFullName());
		assertEquals("surgeon", bean.getSpecialty());
	}

	@Test
	public void testGetPatientl() throws ITrustException {
		PatientBean bean = action.getPatient(2L);
		assertEquals("Andy Programmer", bean.getFullName());
	}

}
