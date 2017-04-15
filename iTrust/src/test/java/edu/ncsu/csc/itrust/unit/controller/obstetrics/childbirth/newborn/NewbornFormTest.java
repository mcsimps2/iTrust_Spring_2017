package edu.ncsu.csc.itrust.unit.controller.obstetrics.childbirth.newborn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

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

import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn.NewbornController;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.newborn.NewbornForm;
import edu.ncsu.csc.itrust.controller.obstetrics.childbirth.visit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.Newborn;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornData;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.NewbornMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns.SexType;
import edu.ncsu.csc.itrust.model.old.beans.Email;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Class to test NewbornForm
 * @author jcgonzal
 */
public class NewbornFormTest {

	@Spy private NewbornController nc;
	@Spy private ChildbirthVisitController cvc;
	
	@Mock private SessionUtils mockSessionUtils;
	@Mock private PatientDAO mockPatientDAO;
	@Mock private AuthDAO mockAuthDAO;
	@Mock private PersonnelDAO mockPersonnelDAO;
	@Mock private FakeEmailDAO mockFakeEmailDAO;
	@Mock private DAOFactory mockDaoFactory;
	
	private DataSource ds;
	private NewbornData newbornData;
	
	private NewbornForm nf;
	
	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		ds = ConverterDAO.getDataSource();
		
		mockSessionUtils = Mockito.mock(SessionUtils.class);
		mockPatientDAO = Mockito.mock(PatientDAO.class);
		mockAuthDAO = Mockito.mock(AuthDAO.class);
		mockPersonnelDAO = Mockito.mock(PersonnelDAO.class);
		mockFakeEmailDAO = Mockito.mock(FakeEmailDAO.class);
		mockDaoFactory = Mockito.mock(DAOFactory.class);
		
		PatientBean p = new PatientBean();
		p.setFirstName("Michael");
		p.setLastName("Jackson");
		p.setEmail("michael.jackson@gmail.com");
		
		Mockito.when(mockPatientDAO.addEmptyPatient()).thenReturn(6135L);
		Mockito.doNothing().when(mockPatientDAO).editPatient(Mockito.any(PatientBean.class), Mockito.anyLong());
		Mockito.when(mockPatientDAO.checkPatientExists(1L)).thenReturn(true);
		Mockito.when(mockPatientDAO.getPatient(Mockito.anyLong())).thenReturn(p);
		Mockito.when(mockAuthDAO.addUser(Mockito.anyLong(), Mockito.any(Role.class), Mockito.anyString())).thenReturn("pw");	
		Mockito.doNothing().when(mockFakeEmailDAO).sendEmailRecord(Mockito.any(Email.class));
		
		Mockito.when(mockDaoFactory.getPatientDAO()).thenReturn(mockPatientDAO);
		Mockito.when(mockDaoFactory.getAuthDAO()).thenReturn(mockAuthDAO);
		Mockito.when(mockDaoFactory.getPersonnelDAO()).thenReturn(mockPersonnelDAO);
		Mockito.when(mockDaoFactory.getFakeEmailDAO()).thenReturn(mockFakeEmailDAO);
		
		
		nc = Mockito.spy(new NewbornController(ds, mockSessionUtils, mockDaoFactory));
		cvc = Mockito.spy(new ChildbirthVisitController(ds, mockSessionUtils));
		
		nf = new NewbornForm(nc, cvc, mockSessionUtils, 51L, mockPatientDAO);
		
		Mockito.doNothing().when(nc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(nc).logTransaction(Matchers.any(TransactionType.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString());
		Mockito.doNothing().when(cvc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.when(mockSessionUtils.getSessionLoggedInMIDLong()).thenReturn(9000000012L);
		
		newbornData = new NewbornMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@After
   	public void tearDown() throws FileNotFoundException, SQLException, IOException {
   		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
   	}

	@Test
	public void testAdd() {
		Newborn n = new Newborn(1L, 1000L, "2012-01-01", "ab", SexType.FEMALE, true);
		PatientBean p = new PatientBean();
		p.setFirstName("Michael");
		p.setLastName("Person");
		p.setEmail("random.person@gmail.com");
		nf.setNewborn(n);
		nf.setPatient(p);
		nf.add(51L, "Person", "random.person@gmail.com");
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setOfficeVisitID(51L);
		nf.add(51L, "Person", "random.person@gmail.com");
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setTimeOfBirth("8:00 AM");
		nf.add(51L, "Person", "random.person@gmail.com");
		Assert.assertTrue(nf.getNewborn().equals(new Newborn(51L)));
	}

	@Test
	public void testEdit() {
		Newborn n = new Newborn(1L, 51L, "2012-01-01", "ab", SexType.FEMALE, true);
		n.setId(1L);
		PatientBean p = new PatientBean();
		p.setMID(1L);
		p.setFirstName("Michael");
		p.setLastName("Jackson");
		p.setEmail("Michael.Jackson@gmail.com");
		nf.setNewborn(n);
		nf.setPatient(p);
		nf.edit(51L, "Jackson", "Michael.Jackson@gmail.com");
		Assert.assertTrue(nf.getNewborn().equals(n));
		
		n.setTimeOfBirth("8:00 AM");
		nf.edit(51L, "Jackson", "Michael.Jackson@gmail.com");
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
		nf.clearFields(51L, "Jackson", "Michael.Jackson@gmail.com");
		try {
			nf.fillInput(1L, "asdf", ";lkj", SexType.MALE, true, 2L);
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
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
		try {
			nf.fillInput(1L, "asdf", ";lkj", SexType.MALE, true, 2L);
			nf.clearFields(51L, "Jackson", "Michael.Jackson@gmail.com");
			Assert.assertTrue(nf.getNewborn().equals(new Newborn(51L)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
