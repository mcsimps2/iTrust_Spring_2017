package edu.ncsu.csc.itrust.unit.action;

import edu.ncsu.csc.itrust.action.SendMessageAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.MessageBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SendMessageActionTest  {

	private DAOFactory factory;
	private GregorianCalendar gCal;
	private MessageDAO messageDAO;
	private SendMessageAction smAction;
	private TestDataGenerator gen;
	private long patientId;
	private long hcpId;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();

		patientId = 2L;
		hcpId = 9000000000L;
		factory = TestDAOFactory.getTestInstance();
		messageDAO = new MessageDAO(this.factory);
		smAction = new SendMessageAction(this.factory, this.patientId);
		gCal = new GregorianCalendar();
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	@Test
	public void testSendMessage() throws ITrustException, SQLException, FormValidationException {
		String body = "UNIT TEST - SendMessageActionText";
		MessageBean mBean = new MessageBean();
		Timestamp timestamp = new Timestamp(this.gCal.getTimeInMillis());

		mBean.setFrom(this.patientId);
		mBean.setTo(this.hcpId);
		mBean.setSubject(body);
		mBean.setSentDate(timestamp);
		mBean.setBody(body);

		this.smAction.sendMessage(mBean);

		List<MessageBean> mbList = this.messageDAO.getMessagesForMID(this.hcpId);

		assertEquals(15, mbList.size());
		MessageBean mBeanDB = mbList.get(0);
		assertEquals(body, mBeanDB.getBody());
	}

	@Test
	public void testGetPatientName() throws ITrustException {
		assertEquals("Andy Programmer", this.smAction.getPatientName(this.patientId));
	}

	@Test
	public void testGetPersonnelName() throws ITrustException {
		assertEquals("Kelly Doctor", this.smAction.getPersonnelName(this.hcpId));
	}

	@Test
	public void testGetMyRepresentees() throws ITrustException {
		List<PatientBean> pbList = smAction.getMyRepresentees();
		assertEquals(7, pbList.size());
		
		assertEquals("Random Person", pbList.get(0).getFullName());
		assertEquals("05/10/1950", pbList.get(0).getDateOfBirthStr());
		assertEquals("Care Needs", pbList.get(1).getFullName());
		assertEquals("Baby Programmer", pbList.get(2).getFullName());
		assertEquals("Baby A", pbList.get(3).getFullName());
		assertEquals("Baby B", pbList.get(4).getFullName());
		assertEquals("Baby C", pbList.get(5).getFullName());
		assertEquals(7, pbList.size());
		assertEquals("Sandy Sky", pbList.get(6).getFullName());
	}

	@Test
	public void testGetMyDLHCPs() throws ITrustException {
		List<PersonnelBean> pbList = this.smAction.getDLHCPsFor(this.patientId);
		assertEquals(1, pbList.size());
	}

	@Test
	public void testGetMyDLHCPs2() throws ITrustException {
		List<PersonnelBean> pbList = this.smAction.getMyDLHCPs();
		assertEquals(1, pbList.size());
	}

	@Test
	public void testGetDLCHPsFor() throws ITrustException {
		List<PersonnelBean> pbList = this.smAction.getDLHCPsFor(this.patientId);

		assertEquals(1, pbList.size());
	}

}
