package edu.ncsu.csc.itrust.unit.controller.obstetrics.ultrasound;

import java.util.List;

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

import edu.ncsu.csc.itrust.controller.obstetrics.ultrasound.UltrasoundController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.Ultrasound;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundData;
import edu.ncsu.csc.itrust.model.obstetrics.ultrasound.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test class for the UltrasoundController class
 * @author amcheshi
 */
public class UltrasoundControllerTest {
	
	private static final long TEST_OFFICE_VISIT_ID = 51L;
	private static final long OBGYN_MID = 9000000012L;

	@Spy private UltrasoundController uc;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private DataSource ds;
	private UltrasoundData ultrasoundData;
	
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		
		uc = Mockito.spy(new UltrasoundController(ds, mockSessionUtils));
		
		Mockito.doNothing().when(uc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(uc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(OBGYN_MID);
		
		ultrasoundData = new UltrasoundMySQL(ds);
		
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
	public void testConstructor() {
		try {
			new UltrasoundController();
			Assert.fail("Invalid constructor call while unit testing");
		} catch (DBException e) {
			Assert.assertNotNull(e);
		}
	}

	@Test
	public void testAdd() {
		try {
			List<Ultrasound> before = ultrasoundData.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			Ultrasound invalid = new Ultrasound(TEST_OFFICE_VISIT_ID, -1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
			Assert.assertFalse(uc.add(invalid));
			Assert.assertTrue(before.equals(ultrasoundData.getByOfficeVisit(TEST_OFFICE_VISIT_ID)));
			Ultrasound valid = new Ultrasound(TEST_OFFICE_VISIT_ID, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
			Assert.assertTrue(uc.add(valid));
			Assert.assertTrue(before.size() + 1 == ultrasoundData.getByOfficeVisit(TEST_OFFICE_VISIT_ID).size());
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testEdit() {
		try {
			List<Ultrasound> l = ultrasoundData.getByOfficeVisit(TEST_OFFICE_VISIT_ID);
			if (l.isEmpty()) Assert.fail("No test data in the db");
			Ultrasound u1 = l.get(0);
			Ultrasound u2 = new Ultrasound(u1.getOfficeVisitID());
			u2.setId(u1.getId());
			u2.setCrl(-u1.getCrl());
			u2.setBpd(2.5f);
			u2.setHc(u1.getHc());
			u2.setFl(u1.getFl());
			u2.setOfd(u1.getOfd());
			u2.setAc(u1.getAc());
			u2.setHl(u1.getHl());
			u2.setEfw(u1.getEfw());
			
			Assert.assertFalse(uc.edit(u2));
			Ultrasound afterEdit = ultrasoundData.getByID(u1.getId());
			Assert.assertTrue(afterEdit.equals(u1));
			
			u2.setCrl(u1.getCrl());
			Assert.assertTrue(uc.edit(u2));
			afterEdit = ultrasoundData.getByID(u1.getId());
			Assert.assertFalse(afterEdit.equals(u1));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			List<Ultrasound> l = ultrasoundData.getAll();
			if (l.isEmpty()) Assert.fail("No test data in the db");
			
			uc.delete(0L);
			Assert.assertTrue(ultrasoundData.getAll().size() == l.size());
			
			Ultrasound u1 = l.get(0);
			uc.delete(u1.getId());
			Assert.assertTrue(ultrasoundData.getAll().size() == l.size() - 1);
			Assert.assertFalse(ultrasoundData.getAll().contains(u1));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

	@Test
	public void testGetUltrasoundsByOfficeVisit() {
		try {
			Assert.assertTrue(uc.getUltrasoundsByOfficeVisit(TEST_OFFICE_VISIT_ID).equals(ultrasoundData.getByOfficeVisit(TEST_OFFICE_VISIT_ID)));
		} catch (DBException e) {
			Assert.fail(e.getExtendedMessage());
		}
	}

}
