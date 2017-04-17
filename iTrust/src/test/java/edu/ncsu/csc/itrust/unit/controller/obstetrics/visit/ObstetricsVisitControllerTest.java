package edu.ncsu.csc.itrust.unit.controller.obstetrics.visit;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test for the ObstetricsVisitController class
 * @author amcheshi
 *
 */
public class ObstetricsVisitControllerTest {
	
	private static final Long TEST_OFFICE_VISIT_ID = 51L;
	private static final Long TEST_INIT_ID = 3L;

	@Spy private ObstetricsVisitController ovc;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private DataSource ds;
	private ObstetricsVisitData obstetricsVisitData;
	
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		
		ovc = Mockito.spy(new ObstetricsVisitController(ds, mockSessionUtils));
		
		Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(ovc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		
		obstetricsVisitData = new ObstetricsVisitMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@After
   	public void tearDown() {
   		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
   	}
	
	@Test
	public void testConstructors() {
		try {
			ovc = new ObstetricsVisitController();
			Assert.fail("DBException not thrown");
		} catch (DBException e) {
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testGetByOfficeVisit() {
		try {
			Assert.assertTrue(ovc.getByOfficeVisit(TEST_OFFICE_VISIT_ID).equals(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetByObstetricsInit() {
		try {
			Assert.assertTrue(ovc.getByObstetricsInit(TEST_INIT_ID).equals(obstetricsVisitData.getByObstetricsInit(TEST_INIT_ID)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetMostRecentOI() {
		Assert.assertNull(ovc.getMostRecentOI(null));
		
		try {
			OfficeVisit officeVisit = new OfficeVisitController(ds, mockSessionUtils).getVisitByID(TEST_OFFICE_VISIT_ID.toString());
			ObstetricsInit expected = new ObstetricsInitMySQL(ds).getByID(TEST_INIT_ID);
			ObstetricsInit actual = ovc.getMostRecentOI(officeVisit);
			Assert.assertEquals(expected, actual);
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}
	
	@Test
	public void testCalculateWeeksPregnant() {
		Assert.assertNull(ovc.calculateWeeksPregnant(null, null));
		
		try {
			OfficeVisit officeVisit = new OfficeVisitController(ds, mockSessionUtils).getVisitByID(TEST_OFFICE_VISIT_ID.toString());
			ObstetricsInit oi = new ObstetricsInitMySQL(ds).getByID(1L);
			Assert.assertNull(ovc.calculateWeeksPregnant(officeVisit, oi));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}
	
	@Test
	public void testAdd() {
		try {
			int numberBefore = obstetricsVisitData.getAll().size();
			OfficeVisit officeVisit = new OfficeVisitController(ds, mockSessionUtils).getVisitByID(TEST_OFFICE_VISIT_ID.toString());
			ObstetricsInit oi = ovc.getMostRecentOI(officeVisit);
			Integer weeksPregnant = ovc.calculateWeeksPregnant(officeVisit, oi);
			ObstetricsVisit ov = new ObstetricsVisit(TEST_OFFICE_VISIT_ID, TEST_INIT_ID, -weeksPregnant, 1, 2, true, null, "image.png");
			try {
				ovc.add(ov);
				Assert.fail("Invalid ObstetricsVisit added");
			} catch (DBException | FormValidationException e) {
				Assert.assertNotNull(e);
			}
			Assert.assertTrue(numberBefore == obstetricsVisitData.getAll().size());
			
			ov.setWeeksPregnant(weeksPregnant);
			try {
				ovc.add(ov);
			} catch (DBException | FormValidationException e) {
				Assert.fail("Exception thrown when adding valid ObstetricsVisit");
			}
			Assert.assertTrue(numberBefore == obstetricsVisitData.getAll().size() - 1);
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		try {
			ObstetricsVisit ov = obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			Assert.assertNotNull(ov);
			
			Integer oldFhr = ov.getFhr();
			Assert.assertTrue(oldFhr != 50);
			
			ov.setFhr(-50);
			Assert.assertFalse(ovc.update(ov));
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(oldFhr));
			
			ov.setFhr(50);
			Assert.assertTrue(ovc.update(ov));
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(50));
			
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testUpload() {
		try {
			ObstetricsVisit ov = obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			Assert.assertNotNull(ov);
			
			Integer oldFhr = ov.getFhr();
			Assert.assertTrue(oldFhr != 50);
			
			ov.setFhr(-50);
			Assert.assertFalse(ovc.upload(ov));
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(oldFhr));
			
			ov.setFhr(50);
			Assert.assertTrue(ovc.upload(ov));
			Assert.assertTrue(obstetricsVisitData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).getFhr().equals(50));
			
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
