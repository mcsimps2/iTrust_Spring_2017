package edu.ncsu.csc.itrust.unit.controller.obstetrics.childbirth.visit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

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

import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitForm;
import edu.ncsu.csc.itrust.controller.obstetrics.visit.ObstetricsVisitController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Test for ChildbirthVisitForm class
 * @author jcgonzal
 */
public class ChildbirthVisitFormTest {

	@Spy private ChildbirthVisitController cvc;
	@Spy private OfficeVisitController ofvc;
	@Spy private ObstetricsVisitController obvc;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private DataSource ds;
	private ChildbirthVisitData childbirthVisitData;
	
	private ChildbirthVisitForm cvf;
	
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		
		cvc = Mockito.spy(new ChildbirthVisitController(ds, mockSessionUtils));
		ofvc = Mockito.spy(new OfficeVisitController(ds, mockSessionUtils));
		obvc = Mockito.spy(new ObstetricsVisitController(ds, mockSessionUtils));
		
		cvf = new ChildbirthVisitForm(cvc, ofvc, obvc, ds, 51L);
		
		Mockito.doNothing().when(cvc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.doNothing().when(cvc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		
		childbirthVisitData = new ChildbirthVisitMySQL(ds);
	}
	
	@After
   	public void tearDown() throws FileNotFoundException, SQLException, IOException {
   		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
   	}
	
	@Test
	public void testConstructors() {
		try {
			cvf = new ChildbirthVisitForm();
			Assert.fail("Invalid constructor call when testing");
		} catch (Exception e) {
			Assert.assertNotNull(e);
		}
		
	}
	
	@Test
	public void testSubmitChildbirth() {
		
		cvf.setDeliveryType(DeliveryMethod.MISCARRIAGE);
		cvf.setVisitType(VisitType.EMERGENCY_APPOINTMENT);
		cvf.setPitocin(1);
		cvf.setNitrousOxide(2);
		cvf.setPethidine(3);
		cvf.setEpiduralAnaesthesia(4);
		cvf.setMagnesiumSulfate(5);
		cvf.setRh(51);
		
		Assert.assertTrue(cvf.getDeliveryType().equals(DeliveryMethod.MISCARRIAGE));
		Assert.assertTrue(cvf.getVisitType().equals(VisitType.EMERGENCY_APPOINTMENT));
		Assert.assertTrue(cvf.getPitocin().equals(1));
		Assert.assertTrue(cvf.getNitrousOxide().equals(2));
		Assert.assertTrue(cvf.getPethidine().equals(3));
		Assert.assertTrue(cvf.getEpiduralAnaesthesia().equals(4));
		Assert.assertTrue(cvf.getMagnesiumSulfate().equals(5));
		Assert.assertTrue(cvf.getRh().equals(51));
		
		cvf.submitChildbirth();
		try {
			System.out.println("Value 1: " + childbirthVisitData.getByOfficeVisit(51L).getRH());
			Assert.assertTrue(childbirthVisitData.getByOfficeVisit(51L).getRH().equals(51));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
		cvf.setRh(66);
		cvf.submitChildbirth();
		try {
			System.out.println("Value 2: " + childbirthVisitData.getByOfficeVisit(51L).getRH());
			Assert.assertTrue(childbirthVisitData.getByOfficeVisit(51L).getRH().equals(66));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
		cvf = new ChildbirthVisitForm(cvc, ofvc, obvc, ds, 1000L);
		cvf.setDeliveryType(DeliveryMethod.MISCARRIAGE);
		cvf.setVisitType(VisitType.EMERGENCY_APPOINTMENT);
		cvf.setPitocin(1);
		cvf.setNitrousOxide(2);
		cvf.setPethidine(3);
		cvf.setEpiduralAnaesthesia(4);
		cvf.setMagnesiumSulfate(5);
		cvf.setRh(6);
		
		cvf.submitChildbirth();
		try {
			Assert.assertNull(childbirthVisitData.getByOfficeVisit(1000L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
		
	}

}
