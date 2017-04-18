package edu.ncsu.csc.itrust.unit.controller.obstetrics.ultrasound;

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
import edu.ncsu.csc.itrust.controller.obstetrics.ultrasound.UltrasoundForm;
import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitController;
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
 * Class to test UltrasoundForm
 * @author amcheshi
 */
public class UltrasoundFormTest {
	
	private static final Long TEST_OFFICE_VISIT_ID = 51L;
	private static final Long OBGYN_MID = 9000000012L;

	@Spy private UltrasoundController uc;
	@Spy private ObstetricsVisitController ovc;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private DataSource ds;
	private UltrasoundData ultrasoundData;
	
	private UltrasoundForm uf;
	
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		
		uc = Mockito.spy(new UltrasoundController(ds, mockSessionUtils));
		ovc = Mockito.spy(new ObstetricsVisitController(ds, mockSessionUtils));
		
		uf = new UltrasoundForm(uc, ovc, mockSessionUtils, TEST_OFFICE_VISIT_ID);
		
		Mockito.doNothing().when(uc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(uc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
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
			new UltrasoundForm().delete(1L);
			Assert.fail("Invalid constructor call while unit testing");
		} catch (Exception e) {
			Assert.assertNotNull(e);
		}
	}

	@Test
	public void testAdd() {
		Ultrasound u = new Ultrasound(TEST_OFFICE_VISIT_ID, -1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
		uf.setUltrasound(u);
		uf.add();
		Assert.assertTrue(uf.getUltrasound().equals(u));
		
		u.setCrl(1.0f);
		uf.add();
		Assert.assertFalse(uf.getUltrasound().equals(u));
		Assert.assertTrue(uf.getUltrasound().equals(new Ultrasound(TEST_OFFICE_VISIT_ID)));
	}

	@Test
	public void testEdit() {
		Ultrasound u = new Ultrasound(TEST_OFFICE_VISIT_ID, -1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
		u.setId(1L);
		uf.setUltrasound(u);
		uf.edit();
		Assert.assertTrue(uf.getUltrasound().equals(u));
		
		u.setCrl(1.0f);
		uf.edit();
		Assert.assertFalse(uf.getUltrasound().equals(u));
		Assert.assertTrue(uf.getUltrasound().equals(new Ultrasound(TEST_OFFICE_VISIT_ID)));
	}

	@Test
	public void testDelete() {
		uf.delete(1L);
		try {
			Assert.assertNull(ultrasoundData.getByID(1L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetUltrasounds() {
		try {
			Assert.assertTrue(uf.getUltrasounds().equals(uc.getUltrasoundsByOfficeVisit(TEST_OFFICE_VISIT_ID)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFillInput() {
		uf.clearFields();
		uf.fillInput(1L, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
		Assert.assertTrue(uf.getUltrasound().getId().equals(1L));
		Assert.assertTrue(uf.getUltrasound().getCrl().equals(1.0f));
		Assert.assertTrue(uf.getUltrasound().getBpd().equals(2.0f));
		Assert.assertTrue(uf.getUltrasound().getHc().equals(3.0f));
		Assert.assertTrue(uf.getUltrasound().getFl().equals(4.0f));
		Assert.assertTrue(uf.getUltrasound().getOfd().equals(5.0f));
		Assert.assertTrue(uf.getUltrasound().getAc().equals(6.0f));
		Assert.assertTrue(uf.getUltrasound().getHl().equals(7.0f));
		Assert.assertTrue(uf.getUltrasound().getEfw().equals(8.0f));
	}

	@Test
	public void testClearFields() {
		uf.fillInput(1L, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f);
		uf.clearFields();
		Assert.assertTrue(uf.getUltrasound().equals(new Ultrasound(TEST_OFFICE_VISIT_ID)));
	}
	
	@Test
	public void testIsObstetricsTabSaved() {
		Assert.assertTrue(uf.isObstetricsTabSaved());
		uf = new UltrasoundForm(uc, ovc, mockSessionUtils, 0L);
		Assert.assertFalse(uf.isObstetricsTabSaved());
	}
}
