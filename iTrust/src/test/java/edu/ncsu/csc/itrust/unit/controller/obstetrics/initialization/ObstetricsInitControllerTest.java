package edu.ncsu.csc.itrust.unit.controller.obstetrics.initialization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.ncsu.csc.itrust.controller.obstetrics.initialization.ObstetricsInitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoData;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test class for the ObstetricsInitController class.
 * 
 * @author Aaron Cheshire, James Gonzales
 */
public class ObstetricsInitControllerTest {
	
	@Spy private ObstetricsInitController oic;
	@Spy private SessionUtils sessionUtils;
	
	@Mock private HttpServletRequest mockHttpServletRequest;
	@Mock private HttpSession mockHttpSession;
	@Mock private SessionUtils mockSessionUtils;
	@Mock private PatientDAO mockPatientDAO;
	@Mock private PersonnelDAO mockPersonnelDAO;

	private ObstetricsInitData oiData;
	private PregnancyInfoData pregnancyData;
	private DataSource ds;
	
	private PatientBean patient1;
	private PatientBean patient2;
	private PersonnelBean personnel1;
	private PersonnelBean personnel2;

	/**
	 * Runs before every test.
	 */
	@Before
	public void setUp() throws Exception {
		// Set up variables
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockPatientDAO = Mockito.mock(PatientDAO.class);
		mockPersonnelDAO = Mockito.mock(PersonnelDAO.class);
		
		patient1 = new PatientBean();
		patient1.setObstetricsCareEligibility(true);
		patient2 = new PatientBean();
		patient2.setObstetricsCareEligibility(false);
		personnel1 = new PersonnelBean();
		personnel1.setSpecialty("OB/GYN");
		personnel2 = new PersonnelBean();
		personnel2.setSpecialty(null);
		
		Mockito.when(mockPatientDAO.getPatient(1)).thenReturn(patient1);
		Mockito.when(mockPatientDAO.getPatient(2)).thenReturn(patient2);
		Mockito.when(mockPersonnelDAO.getPersonnel(9000000012L)).thenReturn(personnel1);
		Mockito.when(mockPersonnelDAO.getPersonnel(9900000000L)).thenReturn(personnel2);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ObstetricsInitControllerTest.this.patient2 = (PatientBean) invocation.getArguments()[0];
				return null;
			}
		}).when(mockPatientDAO).editPatient(patient2, 9000000012L);
		
		oic = Mockito.spy(new ObstetricsInitController(ds, mockSessionUtils, mockPatientDAO, mockPersonnelDAO));
		
		Mockito.doNothing().when(oic).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(oic).navigateToViewAdd();
		Mockito.doNothing().when(oic).redirectToObstetricsRecordsOverview();
		Mockito.when(mockSessionUtils.getCurrentPatientMID()).thenReturn("1");
		Mockito.when(mockSessionUtils.getCurrentPatientMIDLong()).thenReturn(1L);
		Mockito.when(mockSessionUtils.getSessionPID()).thenReturn("1");
		
		oiData = new ObstetricsInitMySQL(ds);	
		pregnancyData = new PregnancyInfoMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

		// Mock HttpServletRequest
		mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

		// Mock HttpSession
		mockHttpSession = Mockito.mock(HttpSession.class);
	}

	/**
	 * Test method for isPatientEligible(String)
	 */
	@Test
	public void testIsPatientEligible() {
		Assert.assertTrue(oic.isPatientEligible("1"));
		Assert.assertFalse(oic.isPatientEligible("2"));
		
		Assert.assertFalse(oic.isPatientEligible("ab"));
		Assert.assertFalse(oic.isPatientEligible("1.2"));
		
		Assert.assertFalse(oic.isPatientEligible("510"));
	}

	/**
	 * Test method for makePatientEligible(String, String)
	 */
	@Test
	public void testMakePatientEligible() {
		Assert.assertFalse(oic.isPatientEligible("2"));
		
		oic.makePatientEligible("2.12", "9000000012");
		Assert.assertFalse(oic.isPatientEligible("2"));
		
		oic.makePatientEligible("2", "902.12");
		Assert.assertFalse(oic.isPatientEligible("2"));
		
		oic.makePatientEligible("510", "9000000012");
		Assert.assertFalse(oic.isPatientEligible("510"));		
		
		oic.makePatientEligible("2", "9000000012");
		Assert.assertFalse(!oic.isPatientEligible("2"));
	}

	/**
	 * Test method for getRecords(String)
	 */
	@Test
	public void testGetRecords() {
		Assert.assertNull(oic.getRecords("a"));

		List<ObstetricsInit> list;
		try {
			list = oiData.getRecords(1);
			Assert.assertEquals(list, oic.getRecords("1"));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
		//TODO: Maybe test sorting
	}
	
	/**
	 * Test method for getPastPregnancies()
	 */
	@Test
	public void testGetPastPregnancies() {
		try {
			List<PregnancyInfo> list = pregnancyData.getRecords(1);
			Assert.assertEquals(list, oic.getPastPregnancies());
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test method for getPastPregnanciesFromInit(id)
	 */
	@Test
	public void testGetPastPregnanciesFromInit() {
		List<PregnancyInfo> list;
		try {
			list = pregnancyData.getRecordsFromInit(1);
			Assert.assertEquals(list, oic.getPastPregnanciesFromInit(1));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test method for isOBGYN(String)
	 */
	@Test
	public void testIsOBGYN() {
		Assert.assertFalse(oic.isOBGYN("a"));
		
		Assert.assertFalse(oic.isOBGYN("123"));
		
		Assert.assertFalse(oic.isOBGYN("9000000003"));
		
		Assert.assertFalse(oic.isOBGYN("9900000000"));
		
		Assert.assertTrue(oic.isOBGYN("9000000012"));
	}

	/**
	 * Test method for viewAddObstetricsInit(ObstetricsInit, String)
	 */
	@Test
	public void testViewAddObstetricsInit() {
		ObstetricsInit oi = new ObstetricsInit();
		oi.setID(1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String lmp = dateFormat.format(c.getTime());
		oi.setLMP(lmp);
		c = Calendar.getInstance();
		oi.setDate(dateFormat.format(c.getTime()));
		
		
		oic.viewAddObstetricsInit(oi, "a");
		Assert.assertTrue(oic.getViewedOI()== null);
		
		oic.viewAddObstetricsInit(null, "9000000012");
		Assert.assertTrue(oic.getViewedOI() == null);
		Assert.assertTrue(oic.getDisplayedPregnancies().equals(oic.getPastPregnancies()));
		
		oic.viewAddObstetricsInit(oi, "9000000012");
		Assert.assertTrue(oic.getViewedOI().equals(oi));
		Assert.assertTrue(oic.getDisplayedPregnancies().equals(oic.getPastPregnanciesFromInit(oi.getId())));
		
	}
	
	/**
	 * Test method for getDeliveryMethods()
	 */
	@Test
	public void testGetDeliveryMethods() {
		Assert.assertTrue(oic.getDeliveryMethods().equals(Arrays.asList(DeliveryMethod.values())));
	}

	/**
	 * Test method for addPregnancyRecord()
	 */
	@Test
	public void testAddPregnancyRecord() {
		
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setMultiplicity("2");
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setWeightGain("12.5");
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setNumHoursInLabor("-1");
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setNumWeeksPregnant("39");
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		
		oic.setYearOfConception("a");
		oic.addPregnancyRecord();
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setYearOfConception("2005");
		oic.setDeliveryType("Vaginal Delivery");
		Assert.assertTrue(oic.getDeliveryType().equals("Vaginal Delivery"));
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());		
		
		oic.setYearOfConception("101");
		Assert.assertTrue(oic.getDisplayedPregnancies().isEmpty());
		
		oic.setYearOfConception("2005");
		oic.setNumHoursInLabor("3");
		oic.addPregnancyRecord();
		Assert.assertFalse(oic.getDisplayedPregnancies().isEmpty());
	}

	/**
	 * Test method for addObstetricsRecord()
	 */
	@Test
	public void testAddObstetricsRecord() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String lmp = dateFormat.format(c.getTime());	
		
		oic.setLmp(lmp);
		
		oic.setMultiplicity("1");
		Assert.assertTrue(oic.getMultiplicity().equals("1"));
		oic.addObstetricsRecord();
		//LMP wasn't cleared, therefore it errored
		Assert.assertTrue(oic.getLmp().equals(lmp));
		
		oic.setWeightGain("1");
		Assert.assertTrue(oic.getWeightGain().equals("1"));
		oic.addObstetricsRecord();
		//LMP wasn't cleared, therefore it errored
		Assert.assertTrue(oic.getLmp().equals(lmp));
		
		oic.setNumHoursInLabor("1");
		Assert.assertTrue(oic.getNumHoursInLabor().equals("1"));
		oic.addObstetricsRecord();
		//LMP wasn't cleared, therefore it errored
		Assert.assertTrue(oic.getLmp().equals(lmp));
		
		oic.setNumWeeksPregnant("39");
		Assert.assertTrue(oic.getNumWeeksPregnant().equals("39"));
		oic.addObstetricsRecord();
		//LMP wasn't cleared, therefore it errored
		Assert.assertTrue(oic.getLmp().equals(lmp));
		
		oic.setYearOfConception("2005");
		Assert.assertTrue(oic.getYearOfConception().equals("2005"));
		oic.addObstetricsRecord();
		//LMP wasn't cleared, therefore it errored
		Assert.assertTrue(oic.getLmp().equals(lmp));
		
		oic.setDeliveryType("Vaginal Delivery");		
		oic.addPregnancyRecord();
		
		oic.setYearOfConception(null);
		oic.setNumWeeksPregnant(null);
		oic.setNumHoursInLabor(null);
		oic.setWeightGain(null);
		oic.setMultiplicity(null);
		oic.setLmp("");
		oic.addObstetricsRecord();
		Assert.assertTrue(oic.getLmp().equals(""));
		
		oic.setLmp(null);
		oic.addObstetricsRecord();
		Assert.assertTrue(oic.getLmp() == null);
		
		oic.setLmp("a");
		oic.addObstetricsRecord();
		Assert.assertTrue(oic.getLmp().equals("a"));
		
		oic.setLmp(lmp);
		oic.addObstetricsRecord();
		Assert.assertTrue(oic.getLmp().equals(""));
	}

	/**
	 * Test method for cancelAddObbstetricsRecord()
	 */
	@Test
	public void testCancelAddObstetricsRecord() {
		oic.setLmp("a");
		oic.cancelAddObstetricsRecord();
		//Make sure LMP was cleared
		Assert.assertTrue(oic.getLmp().equals(""));
	}
	
	/**
	 * Test method for setDisplayedPregnancies(List<PregnancyInfo>)
	 */
	@Test
	public void testSetDisplayedPregnancies() {
		List<PregnancyInfo> list = new ArrayList<PregnancyInfo>();
		oic.setDisplayedPregnancies(list);
		Assert.assertTrue(oic.getDisplayedPregnancies().equals(list));
	}

}
