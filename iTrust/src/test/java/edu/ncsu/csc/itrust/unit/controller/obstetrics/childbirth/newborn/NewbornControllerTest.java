package edu.ncsu.csc.itrust.unit.controller.obstetrics.childbirth.newborn;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn.NewbornController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test class for the NewbornController class
 * @author jcgonzal
 */
public class NewbornControllerTest {

	@Spy private NewbornController nc;
	@Spy private SessionUtils sessionUtils;
	
	@Mock private HttpServletRequest mockHttpServletRequest;
	@Mock private HttpSession mockHttpSession;
	@Mock private SessionUtils mockSessionUtils;
	@Mock private PatientDAO mockPatientDAO;
	
	private DataSource ds;
	private NewbornData newbornData;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockPatientDAO = Mockito.mock(PatientDAO.class);
		
		Mockito.when(mockPatientDAO.addEmptyPatient()).thenReturn(1L);
		
		nc = Mockito.spy(new NewbornController(ds, mockSessionUtils, mockPatientDAO));
		
		Mockito.doNothing().when(nc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(9000000012L);
		
		newbornData = new NewbornMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	@Test
	public void testNewbornController() {
		//fail("Not yet implemented");
	}

	@Test
	public void testNewbornControllerDataSourcePatientDAO() {
		//fail("Not yet implemented");
	}

	@Test
	public void testAdd() {
		try {
			List<Newborn> before = newbornData.getByOfficeVisit(51L);
			Newborn invalid = new Newborn(1500L, 51L, "2014-01-01", "a", SexType.MALE, false);
			nc.add(invalid);
			Assert.assertTrue(before.equals(newbornData.getByOfficeVisit(51L)));
			Newborn valid = new Newborn(1500L, 51L, "2014-01-01", "9:00 AM", SexType.MALE, false);
			nc.add(valid);
			Assert.assertTrue(before.size() + 1 == newbornData.getByOfficeVisit(51L).size());
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testEdit() {
		try {
			List<Newborn> l = newbornData.getByOfficeVisit(51L);
			if (l.isEmpty()) Assert.fail("No test data in the db");
			Newborn n1 = l.get(0);
			Newborn n2 = new Newborn();
			n2.setId(n1.getId());
			n2.setPid(n1.getPid());
			n2.setOfficeVisitID(n1.getOfficeVisitID());
			n2.setDateOfBirth(n1.getDateOfBirth());
			n2.setTimeOfBirth("abc");
			n2.setSex(n1.getSex());
			n2.setTimeEstimated(!n1.getTimeEstimated());
			
			nc.edit(n2);
			Newborn afterEdit = newbornData.getByID(n1.getId());
			Assert.assertTrue(afterEdit.equals(n1));
			
			n2.setTimeOfBirth(n1.getTimeOfBirth());
			nc.edit(n2);
			afterEdit = newbornData.getByID(n1.getId());
			Assert.assertFalse(afterEdit.equals(n1));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			List<Newborn> l = newbornData.getAll();
			if (l.isEmpty()) Assert.fail("No test data in the db");
			
			nc.delete(5000000L);
			Assert.assertTrue(newbornData.getAll().size() == l.size());
			
			Newborn n1 = l.get(0);
			nc.delete(n1.getId());
			Assert.assertTrue(newbornData.getAll().size() == l.size() - 1);
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testGetNewbornsByOfficeVisit() {
		try {
			Assert.assertTrue(nc.getNewbornsByOfficeVisit(51L).equals(newbornData.getByOfficeVisit(51L)));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

}
