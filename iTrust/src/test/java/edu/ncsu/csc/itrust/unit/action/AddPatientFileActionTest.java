package edu.ncsu.csc.itrust.unit.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import edu.ncsu.csc.itrust.action.AddPatientFileAction;
import edu.ncsu.csc.itrust.exception.AddPatientFileException;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests adding a patient file
 *
 */
@SuppressWarnings("unused")
public class AddPatientFileActionTest  {

	String fileDirectory = "testing-files/sample_patientupload/";

	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test when you have valid data
	 * 
	 * @throws CSVFormatException
	 * @throws AddPatientFileException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testValidData() throws CSVFormatException, AddPatientFileException, FileNotFoundException {
		DAOFactory prodDAO = DAOFactory.getProductionInstance();
		AuthDAO authDAO = prodDAO.getAuthDAO();
		InputStream testFile = new FileInputStream(fileDirectory + "HCPPatientUploadValidData.csv");
		AddPatientFileAction apfa = new AddPatientFileAction(testFile, null, 0);
		assertEquals(3, apfa.getPatients().size());
		assertFalse(apfa.getErrors().hasErrors());
	}

	/**
	 * Tests with invalid data
	 * 
	 * @throws CSVFormatException
	 * @throws AddPatientFileException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testInvalidData() throws CSVFormatException, AddPatientFileException, FileNotFoundException {
		DAOFactory prodDAO = DAOFactory.getProductionInstance();
		AuthDAO authDAO = prodDAO.getAuthDAO();
		InputStream testFile = new FileInputStream(fileDirectory + "HCPPatientUploadInvalidData.csv");
		AddPatientFileAction apfa = new AddPatientFileAction(testFile, null, 0);
		assertEquals(1, apfa.getPatients().size());
		assertTrue(apfa.getErrors().hasErrors());
	}

	/**
	 * Test adding a duplicate field
	 * 
	 * @throws CSVFormatException
	 * @throws AddPatientFileException
	 * @throws FileNotFoundException
	 */
	@SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE")
	@Test
	public void testDuplicateField() throws CSVFormatException, AddPatientFileException, FileNotFoundException {
		DAOFactory prodDAO = DAOFactory.getProductionInstance();
		AuthDAO authDAO = prodDAO.getAuthDAO();
		InputStream testFile = new FileInputStream(fileDirectory + "HCPPatientUploadDuplicateField.csv");
		AddPatientFileAction apfa = null;
		try {
			apfa = new AddPatientFileAction(testFile, null, 0);
			fail();
		} catch (AddPatientFileException e) {
			assertNull(apfa);
			assertTrue(e instanceof AddPatientFileException);
		}
	}

	/**
	 * Tests with an invalid header
	 * 
	 * @throws CSVFormatException
	 * @throws AddPatientFileException
	 * @throws FileNotFoundException
	 */
	@SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE")
	@Test
	public void testInvalidHeader() throws CSVFormatException, AddPatientFileException, FileNotFoundException {
		DAOFactory prodDAO = DAOFactory.getProductionInstance();
		AuthDAO authDAO = prodDAO.getAuthDAO();
		InputStream testFile = new FileInputStream(fileDirectory + "HCPPatientUploadInvalidField.csv");
		AddPatientFileAction apfa = null;
		try {
			apfa = new AddPatientFileAction(testFile, null, 0);
			fail();
		} catch (AddPatientFileException e) {
			assertNull(apfa);
		}
	}

	/**
	 * Tests when you have a required field missing
	 * 
	 * @throws CSVFormatException
	 * @throws AddPatientFileException
	 * @throws FileNotFoundException
	 */
	@SuppressFBWarnings(value = "DLS_DEAD_LOCAL_STORE")
	@Test
	public void testRequiredFieldMissing() throws CSVFormatException, AddPatientFileException, FileNotFoundException {
		DAOFactory prodDAO = DAOFactory.getProductionInstance();
		AuthDAO authDAO = prodDAO.getAuthDAO();
		InputStream testFile = new FileInputStream(fileDirectory + "HCPPatientUploadRequiredFieldMissing.csv");
		AddPatientFileAction apfa = null;
		try {
			apfa = new AddPatientFileAction(testFile, null, 0);
			fail();
		} catch (AddPatientFileException e) {
			assertNull(apfa);
		}
	}
}
