package edu.ncsu.csc.itrust.unit.controller.obstetrics.visit;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitController;
import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitForm;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitData;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test for ObstetricsVisitForm class
 * @author jcgonzal
 */
public class ObstetricsVisitFormTest {
	
	private static final Long TEST_OFFICE_VISIT_ID = 51L;
	private static final Long TEST_PID = 1L;
	private static final Long OBGYN_MID = 9000000012L;
	private static final String EMPTY_CALENDAR = "ncsu.edu_flngq1u6biusd8qhces7tom0b0@group.calendar.google.com";

	@Spy private ObstetricsVisitController obvc;
	@Spy private OfficeVisitController ofvc;
	@Spy private OfficeVisitController mockofvc;
	@Spy private ObstetricsVisitForm obvf;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private OfficeVisitForm ofvf;
	
	private DataSource ds;
	private ObstetricsVisitData obstetricsVisitData;
	private OfficeVisitData officeVisitData;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockofvc = Mockito.mock(OfficeVisitController.class);
		
		obvc = Mockito.spy(new ObstetricsVisitController(ds, mockSessionUtils));
		ofvc = Mockito.spy(new OfficeVisitController(ds, mockSessionUtils));

		obvf = Mockito.spy(new ObstetricsVisitForm(obvc, ofvc, ds, mockSessionUtils, TEST_OFFICE_VISIT_ID));

		Mockito.doNothing().when(obvf).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(obvc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.doNothing().when(obvc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(ofvc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.doNothing().when(ofvc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.when(mockSessionUtils.getCurrentPatientMIDLong()).thenReturn(TEST_PID);
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(OBGYN_MID);
		
		obstetricsVisitData = new ObstetricsVisitMySQL(ds);
		officeVisitData = new OfficeVisitMySQL(ds);
		
		OfficeVisit ofv = officeVisitData.getByID(TEST_OFFICE_VISIT_ID);
		Mockito.when(mockofvc.getSelectedVisit()).thenReturn(ofv);
		
		ofvf = new OfficeVisitForm(mockofvc);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testConstructors() {
		try {
			obvf = new ObstetricsVisitForm();
			Assert.fail("Invalid constructor call when testing");
		} catch (Exception e) {
			Assert.assertNotNull(e);
		}
		
	}
	
	@Test
	public void testSubmitObstetrics() {
		try {
			// Valid submit (update)
			obvf.setWeeksPregnant(20);
			obvf.setFhr(1);
			obvf.setMultiplicity(2);
			obvf.setPlacentaObserved(true);
			obvf.setCalendarID("calendarID");
			
			Assert.assertTrue(obvf.getWeeksPregnant().equals(20));
			Assert.assertTrue(obvf.getFhr().equals(1));
			Assert.assertTrue(obvf.getMultiplicity().equals(2));
			Assert.assertTrue(obvf.getPlacentaObserved().equals(true));
			Assert.assertTrue(obvf.getCalendarID().equals("calendarID"));
			
			Assert.assertEquals(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID), obvf.getOv());
					
			obvf.submitObstetrics(ofvf);
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(1));
			
			// Update
			obvf.setFhr(2);
			obvf.submitObstetrics(ofvf);
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(2));
			
			// Create new office visit with no obstetrics visit
			OfficeVisit ov = officeVisitData.getByID(TEST_OFFICE_VISIT_ID);
			ov.setVisitID(null);
			ov.setWeight(null);
			ov.setBloodPressure(null);
			long newOfficeVisitID = ofvc.addReturnGeneratedId(ov);
			
			// Invalid submit (no health metrics on add)
			obvf = Mockito.spy(new ObstetricsVisitForm(obvc, ofvc, ds, mockSessionUtils, newOfficeVisitID));
			Mockito.doNothing().when(obvf).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(), Mockito.anyString());
			Mockito.when(mockofvc.getSelectedVisit()).thenReturn(ov);
			ofvf = new OfficeVisitForm(mockofvc);
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
			
			ofvf.setWeight(-1f);
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
			
			ofvf.setBloodPressure("");
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
			
			// Invalid submit (invalid health metrics on add)
			ofvf.setBloodPressure("123");
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
			
			// Invalid submit (missing obstetrics data on add)
			ofvf.setWeight(123f);
			ofvf.setBloodPressure("123/123");
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
			
			// Valid add
			obvf.setFhr(1);
			obvf.setMultiplicity(2);
			obvf.setPlacentaObserved(true);
			obvf.setCalendarID(EMPTY_CALENDAR);
			obvf.submitObstetrics(ofvf);
			Assert.assertNotNull(obstetricsVisitData.getByOfficeVisit(newOfficeVisitID));
						
			// Invalid submit (invalid office visit ID)
			obvf = Mockito.spy(new ObstetricsVisitForm(obvc, ofvc, ds, mockSessionUtils, 1000L));
			Mockito.doNothing().when(obvf).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(), Mockito.anyString());
			
			obvf.setFhr(1);
			obvf.setMultiplicity(2);
			obvf.setPlacentaObserved(true);
			obvf.setCalendarID("calendarID");
			
			obvf.submitObstetrics(ofvf);
			Assert.assertNull(obstetricsVisitData.getByOfficeVisit(1000L));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}
	
	@Test
	public void testGetNumDaysToNextAppointment() {
		obvf.setWeeksPregnant(1);
		Assert.assertEquals(28, obvf.getNumDaysToNextAppointment());
		
		obvf.setWeeksPregnant(14);
		Assert.assertEquals(14, obvf.getNumDaysToNextAppointment());

		obvf.setWeeksPregnant(29);
		Assert.assertEquals(7, obvf.getNumDaysToNextAppointment());
		
		obvf.setWeeksPregnant(40);
		int days = obvf.getNumDaysToNextAppointment();
		Assert.assertTrue(days >= 2 && days <= 4);
		
		obvf.setWeeksPregnant(42);
		Assert.assertEquals(-1, obvf.getNumDaysToNextAppointment());
	}
	
	@Test
	public void testUpload() {
		try {
			// No file
			obvf.setMultiplicity(obvf.getMultiplicity() + 1);
			obvf.upload();
			Assert.assertNotEquals(obvf.getMultiplicity(), obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getMultiplicity());
			
			//TODO figure out how set a valid Part and unit test upload
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}
	
	@Test
	public void testDownload() {
		try {
			obvf.download();
			Assert.fail("Invalid call when unit testing");
		} catch (Exception e) {
			Assert.assertNotNull(e);
		}
		//TODO figure out how to unit test this
	}
	
	@Test
	public void testNeedsShot() {
		obvf.setWeeksPregnant(1);
		obvf.setRhFlag(false);
		Assert.assertFalse(obvf.needsShot());
		
		obvf.setWeeksPregnant(28);
		Assert.assertFalse(obvf.needsShot());
		
		obvf.setRhFlag(true);
		Assert.assertTrue(obvf.needsShot());
		
		obvf.setWeeksPregnant(27);
		Assert.assertFalse(obvf.needsShot());
	}

}
