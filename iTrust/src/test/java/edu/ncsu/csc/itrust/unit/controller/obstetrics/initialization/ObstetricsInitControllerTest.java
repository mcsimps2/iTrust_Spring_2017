/**
 * 
 */
package edu.ncsu.csc.itrust.unit.controller.obstetrics.initialization;

import static org.junit.Assert.fail;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.initialization.ObstetricsInitController;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.model.user.patient.PatientMySQLConverter;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test class for the ObstetricsInitController class.
 * 
 * @author Aaron Cheshire
 */
public class ObstetricsInitControllerTest {
	
	@Spy private ObstetricsInitController oic;
	@Spy private ObstetricsInitController oicWithNullDataSource;
	@Spy private SessionUtils sessionUtils;
	
	@Mock private HttpServletRequest mockHttpServletRequest;
	@Mock private HttpSession mockHttpSession;
	@Mock private SessionUtils mockSessionUtils;

	private ObstetricsInitData oiData;
	private DataBean<Patient> patientData;
	private DataSource ds;

	/**
	 * Runs before every test.
	 */
	@Before
	public void setUp() throws Exception {
		// Set up variables
		ds = ConverterDAO.getDataSource();
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		oic = Mockito.spy(new ObstetricsInitController(ds, mockSessionUtils));
		Mockito.doNothing().when(oic).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(oic).navigateToViewAdd();
		Mockito.doNothing().when(oic).redirectToObstetricsRecordsOverview();
		oiData = new ObstetricsInitMySQL(ds);		
		patientData = new PatientMySQLConverter(ds);
		// TODO make a PersonnelMySQLConverter class that can take ds as a constructor parameter
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

		// Initialize a office visit controller with null data source
		oicWithNullDataSource = new ObstetricsInitController(null, sessionUtils);

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
		fail("Not yet implemented");
	}

	/**
	 * Test method for makePatientEligible(String, String)
	 */
	@Test
	public void testMakePatientEligible() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for getRecords(String)
	 */
	@Test
	public void testGetRecords() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for getPastPregnanciesFromInit(int)
	 */
	@Test
	public void testGetPastPregnanciesFromInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for isOBGYN(String)
	 */
	@Test
	public void testIsOBGYN() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for viewAddObstetricsInit(ObstetricsInit, String)
	 */
	@Test
	public void testViewAddObstetricsInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for addPregnancyRecord()
	 */
	@Test
	public void testAddPregnancyRecord() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for addObstetricsRecord()
	 */
	@Test
	public void testAddObstetricsRecord() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for cancelAddObbstetricsRecord()
	 */
	@Test
	public void testCancelAddObstetricsRecord() {
		fail("Not yet implemented");
	}

}
