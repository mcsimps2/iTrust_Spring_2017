package edu.ncsu.csc.itrust.unit.controller.obstetrics.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.controller.obstetrics.report.ComplicationInfo;
import edu.ncsu.csc.itrust.controller.obstetrics.report.ObstetricsReportController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisData;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoData;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsReportControllerTest {

	@Spy ObstetricsReportController orc;
	
	@Mock private PatientDAO mockPatientDAO;
	@Mock private AllergyDAO mockAllergyDAO;
	
	private DataSource ds;
	private ObstetricsInitData oiData;
	private PregnancyInfoData pregnancyData;
	private ObstetricsVisitData obvData;
	private DiagnosisData dData;
	
	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		
		mockPatientDAO = Mockito.mock(PatientDAO.class);
		mockAllergyDAO = Mockito.mock(AllergyDAO.class);
		
		PatientBean p1 = new PatientBean();
		p1.setBloodType(BloodType.ABNeg);
		p1.setDateOfBirthStr("01/01/1950");
		
		PatientBean p3 = new PatientBean();
		p3.setDateOfBirthStr("01/01/1995");
		
		Mockito.when(mockPatientDAO.getPatient(1L)).thenReturn(p1);
		Mockito.when(mockPatientDAO.getPatient(3L)).thenReturn(p3);
		
		List<AllergyBean> allergies = new ArrayList<AllergyBean>();
		AllergyBean a1 = new AllergyBean();
		a1.setDescription("Penicillin");
		allergies.add(a1);
		AllergyBean a2 = new AllergyBean();
		a2.setDescription("Sulfasalazine");
		allergies.add(a2);
		
		Mockito.when(mockAllergyDAO.getAllergies(3L)).thenReturn(allergies);
		Mockito.when(mockAllergyDAO.getAllergies(1L)).thenReturn(new ArrayList<AllergyBean>());
		
		orc = Mockito.spy(new ObstetricsReportController(ds, mockPatientDAO, mockAllergyDAO));
		
		Mockito.doNothing().when(orc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(orc).navigateToReport();
		
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		obvData = new ObstetricsVisitMySQL(ds);
		dData = new DiagnosisMySQL(ds);
		
		// Reset test data
		DBBuilder.rebuildAll();		
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}
	
	@Test
	public void testGetPrettyBool() {
		Assert.assertTrue(orc.getPrettyBool(true).equals("True"));
		Assert.assertTrue(orc.getPrettyBool(false).equals("False"));
	}

	@Test
	public void testGetPastPregnancies() {
		try {
			long initID = 2L;
			List<PregnancyInfo> pregnancies = this.pregnancyData.getRecordsFromInit(initID);
			Collections.sort(pregnancies,
					(o1, o2) -> o2.getYearOfConception() - o1.getYearOfConception());
			Assert.assertTrue(orc.getPastPregnancies(initID).equals(pregnancies));
	
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetObstetricsInit() {
		try {
			long initID = 2L;
			Assert.assertTrue(oiData.getByID(initID).equals(orc.getObstetricsInit(initID)));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetBloodType() {
		try {
			long pid = 1L;
			Assert.assertTrue(orc.getBloodType(pid).equals(mockPatientDAO.getPatient(pid).getBloodType()));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetObstetricsVisits() {
		try {
			long initID = 1L;
			List<ObstetricsVisit> obVisits = orc.getObstetricsVisits(initID);
			List<ObstetricsVisit> obVisits2 = obvData.getByObstetricsInit(initID);
			for (ObstetricsVisit v : obVisits) {
				Assert.assertTrue(obVisits2.contains(v));
			}
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetHighBloodPressure() {
		Assert.assertFalse(orc.getHighBloodPressure(51L));
		Assert.assertFalse(orc.getHighBloodPressure(3L));
		Assert.assertTrue(orc.getHighBloodPressure(4L));
		Assert.assertTrue(orc.getHighBloodPressure(1L));
	}

	@Test
	public void testGetAdvancedMaternalAge() {
		Assert.assertTrue(orc.getAdvancedMaternalAge(1L));
		Assert.assertFalse(orc.getAdvancedMaternalAge(3L));
	}

	@Test
	public void testGetHasPreexistingCondition() {
		Assert.assertTrue(orc.getHasPreexistingCondition(1L));
		Assert.assertFalse(orc.getHasPreexistingCondition(3L));
		try {
			dData.add(new Diagnosis(10, 57, new ICDCode("E10", "Type 1 diabetes mellitus", true)));
			Assert.assertTrue(orc.getHasPreexistingCondition(4L));
			dData.add(new Diagnosis(10, 57, new ICDCode("E11", "Type 2 diabetes mellitus", true)));
			Assert.assertTrue(orc.getHasPreexistingCondition(4L));
			dData.add(new Diagnosis(10, 57, new ICDCode("E13", "Other specified diabetes mellitus", true)));
			Assert.assertTrue(orc.getHasPreexistingCondition(4L));
			dData.add(new Diagnosis(10, 57, new ICDCode("R971", "Elevated cancer antigen 125 [CA 125]", true)));
			Assert.assertTrue(orc.getHasPreexistingCondition(4L));
			dData.add(new Diagnosis(10, 57, new ICDCode("A94", "Unspecified arthropod-borne vi", true)));
			Assert.assertTrue(orc.getHasPreexistingCondition(4L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetHasAllergies() {
		Assert.assertTrue(orc.getHasAllergies(3L));
		Assert.assertFalse(orc.getHasAllergies(1L));
	}

	@Test
	public void testGetLowLyingPlacenta() {
		Assert.assertFalse(orc.getLowLyingPlacenta(51L));
		Assert.assertTrue(orc.getLowLyingPlacenta(1L));
		Assert.assertFalse(orc.getLowLyingPlacenta(3L));
	}

	@Test
	public void testGetPotentialForMiscarriage() {
		Assert.assertFalse(orc.getPotentialForMiscarriage(1L));
		Assert.assertTrue(orc.getPotentialForMiscarriage(4L));
	}

	@Test
	public void testGetAbnormalFetalHeartRate() {
		try {
			Assert.assertFalse(orc.getAbnormalFetalHeartRate(51L));
			Assert.assertFalse(orc.getAbnormalFetalHeartRate(3L));
			
			ObstetricsVisit v = obvData.getByID(2L);
			v.setFhr(110);
			obvData.update(v);
			Assert.assertTrue(orc.getAbnormalFetalHeartRate(3L));
			
			v.setFhr(170);
			obvData.update(v);
			Assert.assertTrue(orc.getAbnormalFetalHeartRate(3L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetMultiples() {
		Assert.assertFalse(orc.getMultiples(51L));
		Assert.assertTrue(orc.getMultiples(1L));
		Assert.assertFalse(orc.getMultiples(3L));
	}

	@Test
	public void testGetAbnormalWeightGain() {
		Assert.assertFalse(orc.getAbnormalWeightGain(51L));
		Assert.assertTrue(orc.getAbnormalWeightGain(1L));
		Assert.assertFalse(orc.getAbnormalWeightGain(3L));
		Assert.assertTrue(orc.getAbnormalWeightGain(4L));
	}

	@Test
	public void testGetHyperemesisGravidarum() {
		Assert.assertFalse(orc.getHyperemesisGravidarum(51L));
		Assert.assertTrue(orc.getHyperemesisGravidarum(1L));
		Assert.assertTrue(orc.getHyperemesisGravidarum(4L));
		Assert.assertFalse(orc.getHyperemesisGravidarum(3L));
	}

	@Test
	public void testGetHypothyroidism() {
		Assert.assertFalse(orc.getHypothyroidism(51L));
		Assert.assertFalse(orc.getHypothyroidism(3L));
		Assert.assertTrue(orc.getHypothyroidism(1L));
		
		try {
			dData.add(new Diagnosis(10, 57, new ICDCode("E890", "Hypothyroidism, unspecified", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E039", "Hypothyroidism, unspecified", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E038", "Other specified hypothyroidism", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E033", "Postinfectious hypothyroidism", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E032", "Hypothyroidism, due to meds and oth exogenous substances", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E031", "Congenital hypothyroidism without goiter", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E030", "Congenital hypothyroidism with diffuse goiter", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E03", "Other hypothyroidism", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
			dData.update(new Diagnosis(10, 57, new ICDCode("E02", "Subclinical iodine-deficiency hypothyroidism", true)));
			Assert.assertTrue(orc.getHypothyroidism(4L));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		} catch (FormValidationException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetPreexistingConditions() {
		Assert.assertTrue(orc.getPreexistingConditions(51L).isEmpty());
		List<String> conditions = new ArrayList<String>();
		Assert.assertTrue(orc.getPreexistingConditions(3L).equals(conditions));
		conditions.add("Subclinical iodine-deficiency ");
		conditions.add("Type 1 diabetes mellitus");
		Assert.assertTrue(orc.getPreexistingConditions(1L).equals(conditions));
	}

	@Test
	public void testGetDrugAllergies() {
		List<String> allergies = new ArrayList<String>();
		Assert.assertTrue(orc.getDrugAllergies(1L).equals(allergies));
		allergies.add("Penicillin");
		allergies.add("Sulfasalazine");
		Assert.assertTrue(orc.getDrugAllergies(3L).equals(allergies));
	}
	
	@Test
	public void testViewReport() {
		try {
			ObstetricsInit oi = oiData.getByID(1L);
			orc.viewReport(oi);
			Assert.assertTrue(orc.getViewedOI().equals(oi));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetComplications() {
		List<ComplicationInfo> list = orc.getComplications(1L, 3L);
		ComplicationInfo i = new ComplicationInfo();
		i.setFlag(false);
		i.setID("rh");
		i.setMessage("No, RH- flag not present");
		i.setTitle("RH- flag");
		Assert.assertTrue(list.contains(i));
		i.setID("hbp");
		i.setMessage("No");
		i.setTitle("High blood pressure");
		Assert.assertTrue(list.contains(i));		
	}
	
	@Test
	public void testGetDateByObstetricsVisit() {
		try {
			String temp = orc.getDateByObstetricsVisit(obvData.getByID(1L));
			Assert.assertTrue(temp.equals("February 22, 2017"));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetBloodPressureByObstetricsVisit() {
		try {
			String temp = orc.getBloodPressureByObstetricsVisit(obvData.getByID(1L));
			Assert.assertTrue(temp.equals("120/80"));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetWeightByObstetricsVisit() {
		try {
			String temp = orc.getWeightByObstetricsVisit(obvData.getByID(1L));
			Assert.assertTrue(temp.equals("105.1"));
		} catch (DBException e) {
			Assert.fail(e.getMessage());
		}
	}

}
