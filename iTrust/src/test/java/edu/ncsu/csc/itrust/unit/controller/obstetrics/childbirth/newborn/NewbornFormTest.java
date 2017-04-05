package edu.ncsu.csc.itrust.unit.controller.obstetrics.childbirth.newborn;

import java.util.Arrays;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn.NewbornController;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn.NewbornForm;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit.VisitType;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.DeliveryMethod;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Class to test NewbornForm
 * @author jcgonzal
 */
public class NewbornFormTest {

	@Spy private NewbornController nc;
	@Spy private SessionUtils sessionUtils;
	
	@Mock private SessionUtils mockSessionUtils;
	@Mock private PatientDAO mockPatientDAO;
	
	private DataSource ds;
	private NewbornData newbornData;
	private ChildbirthVisitController cvc;
	
	private NewbornForm nf;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockPatientDAO = Mockito.mock(PatientDAO.class);
		
		Mockito.when(mockPatientDAO.addEmptyPatient()).thenReturn(1L);
		
		nc = Mockito.spy(new NewbornController(ds, mockSessionUtils, mockPatientDAO));
		cvc = new ChildbirthVisitController(ds, mockSessionUtils);
		
		nf = new NewbornForm(nc, cvc, mockSessionUtils, 51L);
		
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
	public void testAdd() {
		Newborn n = new Newborn(1L, 1000L, "2012-01-01", "ab", SexType.FEMALE, true);
		nf.setNewborn(n);
		nf.add(51L);
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setOfficeVisitID(51L);
		cvc.add(new ChildbirthVisit(51L, DeliveryMethod.VAGINAL_DELIVERY, VisitType.PRE_SCHEDULED_APPOINTMENT, 1, 1, 1, 1, 1));
		nf.add(51L);
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setTimeOfBirth("8:00 AM");
		nf.add(51L);
		Assert.assertTrue(nf.getNewborn().equals(new Newborn(51L)));
	}

	@Test
	public void testEdit() {
		Newborn n = new Newborn(1L, 51L, "2012-01-01", "ab", SexType.FEMALE, true);
		n.setId(1L);
		nf.setNewborn(n);
		nf.edit(51L);
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setTimeOfBirth("8:00 AM");
		nf.edit(51L);
		Assert.assertTrue(nf.getNewborn().equals(new Newborn(51L)));
	}

	@Test
	public void testDelete() {
		nf.delete(1L);
		try {
			Assert.assertNull(newbornData.getByID(1L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetNewborns() {
		try {
			Assert.assertTrue(nf.getNewborns(51L).equals(nc.getNewbornsByOfficeVisit(51L)));
	
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFillInput() {
		nf.clearFields(51L);
		nf.fillInput(1L, "asdf", ";lkj", SexType.MALE, true, 2L);
		Assert.assertTrue(nf.getNewborn().getId().equals(1L));
		Assert.assertTrue(nf.getNewborn().getDateOfBirth().equals("asdf"));
		Assert.assertTrue(nf.getNewborn().getTimeOfBirth().equals(";lkj"));
		Assert.assertTrue(nf.getNewborn().getSex().equals(SexType.MALE));
		Assert.assertTrue(nf.getNewborn().getTimeEstimated());
		Assert.assertTrue(nf.getNewborn().getPid().equals(2L));
	}

	@Test
	public void testGetSexTypes() {
		Assert.assertTrue(nf.getSexTypes().equals(Arrays.asList(SexType.values())));
	}

	@Test
	public void testClearFields() {
		nf.fillInput(1L, "asdf", ";lkj", SexType.MALE, true, 2L);
		nf.clearFields(51L);
		Assert.assertTrue(nf.getNewborn().equals(new Newborn(51L)));
	}

}
