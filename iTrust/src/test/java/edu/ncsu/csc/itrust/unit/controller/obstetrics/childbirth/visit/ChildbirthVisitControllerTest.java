package edu.ncsu.csc.itrust.unit.controller.obstetrics.childbirth.visit;

import static org.junit.Assert.*;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import cucumber.api.java.Before;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class ChildbirthVisitControllerTest {

	@Spy private ChildbirthVisitController cvc;
	
	@Mock private SessionUtils mockSessionUtils;
	
	private DataSource ds;
	private ChildbirthVisitData childbirthVisitData;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		
		cvc = Mockito.spy(new ChildbirthVisitController(ds, mockSessionUtils));
		
		Mockito.doNothing().when(cvc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		
		childbirthVisitData = new ChildbirthVisitMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testGetByOfficeVisit() {
		try {
			Assert.assertTrue(cvc.getByOfficeVisit(51L).equals(childbirthVisitData.getByOfficeVisit(51L)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testAdd() {
		ChildbirthVisit cv = new ChildbirthVisit(51L, DeliveryMethod.VAGINAL_DELIVERY, VisitType.PRE_SCHEDULED_APPOINTMENT, 1, 2, 3, 4, 5);
		
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintFacesMessageSeverityStringString() {
		fail("Not yet implemented");
	}

}
