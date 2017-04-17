package edu.ncsu.csc.itrust.unit.action;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * ViewMyRemoteMonitoringListActionTest
 */
public class ViewMyRemoteMonitoringListActionTest  {
	ViewMyRemoteMonitoringListAction action;
	private TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		TransactionLogger.getInstance().setTransactionDAO(TestDAOFactory.getTestInstance().getTransactionDAO());
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient1();
		action = new ViewMyRemoteMonitoringListAction(TestDAOFactory.getTestInstance(), 9000000000L);
	}
	
	@After
	public void tearDown()
	{
		TransactionLogger.getInstance().setTransactionDAO(DAOFactory.getProductionInstance().getTransactionDAO());
	}

	/**
	 * testGetPatientData
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPatientData() throws Exception {
		gen.remoteMonitoring3();
		List<RemoteMonitoringDataBean> data = action.getPatientsData();

		assertEquals(1L, data.get(0).getPatientMID());
		assertEquals(160, data.get(0).getSystolicBloodPressure());
		assertEquals(110, data.get(0).getDiastolicBloodPressure());
		assertEquals(60, data.get(0).getGlucoseLevel());
		assertTrue(data.get(0).getTime().toString().contains("08:00:00"));

		assertEquals(1L, data.get(1).getPatientMID());
		assertEquals(100, data.get(1).getSystolicBloodPressure());
		assertEquals(70, data.get(1).getDiastolicBloodPressure());
		assertEquals(90, data.get(1).getGlucoseLevel());
		assertTrue(data.get(1).getTime().toString().contains("07:15:00"));

		assertEquals(1L, data.get(2).getPatientMID());
		assertEquals(90, data.get(2).getSystolicBloodPressure());
		assertEquals(60, data.get(2).getDiastolicBloodPressure());
		assertEquals(80, data.get(2).getGlucoseLevel());
		assertTrue(data.get(2).getTime().toString().contains("05:30:00"));

		assertEquals(5L, data.get(3).getPatientMID());
		assertEquals(0, data.get(3).getSystolicBloodPressure());
		assertEquals(0, data.get(3).getDiastolicBloodPressure());
		assertEquals(0, data.get(3).getGlucoseLevel());
		assertNull(data.get(3).getTime());

		gen.remoteMonitoring3();
		data = action.getPatientDataWithoutLogging();

		assertEquals(1L, data.get(0).getPatientMID());
		assertEquals(160, data.get(0).getSystolicBloodPressure());
		assertEquals(110, data.get(0).getDiastolicBloodPressure());
		assertEquals(60, data.get(0).getGlucoseLevel());
		assertTrue(data.get(0).getTime().toString().contains("08:00:00"));

		assertEquals(1L, data.get(1).getPatientMID());
		assertEquals(100, data.get(1).getSystolicBloodPressure());
		assertEquals(70, data.get(1).getDiastolicBloodPressure());
		assertEquals(90, data.get(1).getGlucoseLevel());
		assertTrue(data.get(1).getTime().toString().contains("07:15:00"));

		assertEquals(1L, data.get(2).getPatientMID());
		assertEquals(90, data.get(2).getSystolicBloodPressure());
		assertEquals(60, data.get(2).getDiastolicBloodPressure());
		assertEquals(80, data.get(2).getGlucoseLevel());
		assertTrue(data.get(2).getTime().toString().contains("05:30:00"));

		assertEquals(5L, data.get(3).getPatientMID());
		assertEquals(0, data.get(3).getSystolicBloodPressure());
		assertEquals(0, data.get(3).getDiastolicBloodPressure());
		assertEquals(0, data.get(3).getGlucoseLevel());
		assertNull(data.get(3).getTime());

	}

	/**
	 * testGetPatientDataByDate
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPatientDataByDate() throws Exception {
		gen.remoteMonitoring3();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		String currentDate = dateFormat.format(date);
		List<RemoteMonitoringDataBean> data = action.getPatientDataByDate(1L, currentDate, currentDate);

		assertEquals(1L, data.get(0).getPatientMID());
		assertEquals(160, data.get(0).getSystolicBloodPressure());
		assertEquals(110, data.get(0).getDiastolicBloodPressure());
		assertEquals(60, data.get(0).getGlucoseLevel());
		assertTrue(data.get(0).getTime().toString().contains("08:00:00"));
		assertEquals(2L, data.get(0).getReporterMID());

		assertEquals(1L, data.get(1).getPatientMID());
		assertEquals(100, data.get(1).getSystolicBloodPressure());
		assertEquals(70, data.get(1).getDiastolicBloodPressure());
		assertEquals(90, data.get(1).getGlucoseLevel());
		assertTrue(data.get(1).getTime().toString().contains("07:15:00"));
		assertEquals(8000000009L, data.get(1).getReporterMID());

		assertEquals(1L, data.get(2).getPatientMID());
		assertEquals(90, data.get(2).getSystolicBloodPressure());
		assertEquals(60, data.get(2).getDiastolicBloodPressure());
		assertEquals(80, data.get(2).getGlucoseLevel());
		assertTrue(data.get(2).getTime().toString().contains("05:30:00"));
		assertEquals(1L, data.get(2).getReporterMID());
	}

	/**
	 * testGetPatientDataByType
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPatientDataByType() throws Exception {
		gen.remoteMonitoring5();
		List<RemoteMonitoringDataBean> data = action.getPatientDataByType(1L, "weight");

		assertEquals(1L, data.get(0).getPatientMID());
		assertEquals(180.0f, data.get(0).getWeight(), 0.01);
		assertTrue(data.get(0).getTime().toString().contains("08:19:00"));
		assertEquals(1L, data.get(0).getReporterMID());

		assertEquals(1L, data.get(1).getPatientMID());
		assertEquals(177.0f, data.get(1).getWeight(), 0.01);
		assertTrue(data.get(1).getTime().toString().contains("07:48:00"));
		assertEquals(2L, data.get(1).getReporterMID());

		assertEquals(1L, data.get(2).getPatientMID());
		assertEquals(186.5f, data.get(2).getWeight(), 0.01);
		assertTrue(data.get(2).getTime().toString().contains("07:17:00"));
		assertEquals(1L, data.get(2).getReporterMID());
	}

	/**
	 * testIllegalGetPatientDataByDate1
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIllegalGetPatientDataByDate1() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		String currentDate = dateFormat.format(date);
		try {
			List<RemoteMonitoringDataBean> data = action.getPatientDataByDate(1L, currentDate, "01/01/2009");
			fail("Start Date is After End Date, Illegal!");
			// Here to remove warning about data not being read
			data.get(0).getPatientMID();
		} catch (Exception e) {
			// TODO
		}
	}

	/**
	 * testGetPatientName
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPatientName() throws Exception {
		assertEquals("Random Person", action.getPatientName(1L));
	}

}
