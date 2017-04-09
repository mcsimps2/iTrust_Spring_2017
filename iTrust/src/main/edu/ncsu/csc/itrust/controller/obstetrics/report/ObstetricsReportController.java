package edu.ncsu.csc.itrust.controller.obstetrics.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.diagnosis.Diagnosis;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisData;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInit;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitData;
import edu.ncsu.csc.itrust.model.obstetrics.initialization.ObstetricsInitMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfo;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoData;
import edu.ncsu.csc.itrust.model.obstetrics.pregnancies.PregnancyInfoMySQL;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisit;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitData;
import edu.ncsu.csc.itrust.model.obstetrics.visit.ObstetricsVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitData;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

public class ObstetricsReportController extends iTrustController {
	
	private static final String ERROR_LOADING_PREGNANCIES = "Error loading pregnancy data.";
	private static final String ERROR_LOADING_INIT = "Error loading obstetrics initialization data.";
	private static final String ERROR_LOADING_BLOOD_TYPE = "Error loading blood type.";	
	private static final String ERROR_LOADING_OBSTETRICS_VISITS = "Error loading obstetrics visit data.";
	private static final String ERROR_LOADING_BLOOD_PRESSURE = "Error loading blood pressure.";
	private static final String ERROR_LOADING_MATERNAL_AGE = "Error loading maternal age.";
	private static final String ERROR_LOADING_LOW_LYING_PLACENTA = "Error loading low-lying placenta";
	private static final String ERROR_LOADING_ABNORMAL_FETAL_HEART_RATE = "Error loading fetal heart rate";
	private static final String ERROR_LOADING_MULTIPLES = "Error loading pregnancy multiplicity";
	private static final String ERROR_LOADING_ABNORMAL_WEIGHT_GAIN = "Error loading weight change";
	private static final String ERROR_LOADING_HYPEREMESIS_GRAVIDARUM = "Error loading hyperemesis gravidarum";
	private static final String ERROR_LOADING_PREEXISTING_CONDITIONS = "Error loading preexisting conditions";
	private static final String ERROR_LOADING_ALLERGIES = "Error loading allergies";
	
	ObstetricsInitData oiData;
	PregnancyInfoData pregnancyData;
	ObstetricsVisitData obvData;
	OfficeVisitData ofvData;
	DiagnosisData dData;
	PatientDAO patientDAO;
	AllergyDAO allergyDAO;
	
	SessionUtils sessionUtils;
	
	public ObstetricsReportController() {
		super();
		sessionUtils = SessionUtils.getInstance();
		patientDAO = new PatientDAO(DAOFactory.getProductionInstance());
		allergyDAO = new AllergyDAO(DAOFactory.getProductionInstance());
		try {
			oiData = new ObstetricsInitMySQL();
			pregnancyData = new PregnancyInfoMySQL();
			obvData = new ObstetricsVisitMySQL();
			ofvData = new OfficeVisitMySQL();
			dData = new DiagnosisMySQL();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	public ObstetricsReportController(DataSource ds, SessionUtils sessionUtils, PatientDAO patientDAO, AllergyDAO allergyDAO) {
		super();
		this.sessionUtils = sessionUtils;
		this.patientDAO = patientDAO;
		this.allergyDAO = allergyDAO;
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		obvData = new ObstetricsVisitMySQL(ds);
		ofvData = new OfficeVisitMySQL(ds);
		dData = new DiagnosisMySQL(ds);
	}
	
	public List<PregnancyInfo> getPastPregnancies(long initID) {
		try {
			List<PregnancyInfo> pregnancies = this.pregnancyData.getRecordsFromInit(initID);
			Collections.sort(pregnancies,
					(o1, o2) -> o2.getYearOfConception() - o1.getYearOfConception());
			return pregnancies;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PREGNANCIES, e.getMessage(), null);
			return null;
		}
	}
	
	public ObstetricsInit getObstetricsInit(long initID) {
		try {
			return oiData.getByID(initID);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_INIT, e.getMessage(), null);
			return null;
		}
	}
	
	public BloodType getBloodType(long pid) {
		try {
			return patientDAO.getPatient(pid).getBloodType();
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_BLOOD_TYPE, e.getMessage(), null);
			return null;
		}
	}
	
	public List<ObstetricsVisit> getObstetricsVisits(long initID) {
		try {
			List<ObstetricsVisit> visits = obvData.getByObstetricsInit(initID);
			Collections.sort(visits, new Comparator<ObstetricsVisit>() {
				@Override
				public int compare(ObstetricsVisit arg0, ObstetricsVisit arg1) {
					try {
						LocalDateTime d0 = ofvData.getByID(arg0.getOfficeVisitID()).getDate();
						LocalDateTime d1 = ofvData.getByID(arg1.getOfficeVisitID()).getDate();
						//This might need to be flipped
						if (d0.isBefore(d1)) return -1;
						else if (d1.isBefore(d0)) return 1;
						else return 0;
					} catch (DBException e) {
						ObstetricsReportController.this.printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_OBSTETRICS_VISITS, e.getMessage(), null);
						return 0;
					}
				}
			});
			return visits;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_OBSTETRICS_VISITS, e.getMessage(), null);
			return null;
		}
	}
	
	public boolean getHighBloodPressure(long initID) {
		try {
			ObstetricsVisit obv = obvData.getByObstetricsInit(initID).get(0);
			String bloodPressure = ofvData.getByID(obv.getOfficeVisitID()).getBloodPressure();
			int i = bloodPressure.indexOf("/");
			int top = Integer.parseInt(bloodPressure.substring(0, i));
			int bottom = Integer.parseInt(bloodPressure.substring(i));
			return top >= 140 || bottom >= 90;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_BLOOD_PRESSURE, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyHighBloodPressure(long initID) {
		if (getHighBloodPressure(initID)) {
			return "True";
		} else {
			return "False";
		}
	}
	
	public boolean getAdvancedMaternalAge(long pid) {
		try {
			int age = patientDAO.getPatient(pid).getAge();
			return age >= 35;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_MATERNAL_AGE, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyAdvancedMaternalAge(long pid) {
		if (getAdvancedMaternalAge(pid))
			return "True";
		else
			return "False";
	}
	
	public boolean getHasPreexistingCondition(long initID) {
		return !this.getPreexistingConditions(initID).isEmpty();
	}
	
	public String getPrettyHasPreexistingCondition(long initID) {
		if (getHasPreexistingCondition(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getHasAllergies(long pid) {
		return !this.getDrugAllergies(pid).isEmpty();
	}
	
	public String getPrettyHasAllergies(long pid) {
		if (getHasAllergies(pid))
			return "True";
		else
			return "False";
	}
	
	public boolean getLowLyingPlacenta(long initID) {
		try {
			ObstetricsVisit obv = obvData.getByObstetricsInit(initID).get(0);
			return obv.isLowLyingPlacentaObserved();
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_LOW_LYING_PLACENTA, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyLowLyingPlacenta(long initID) {
		if (getLowLyingPlacenta(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getPotentialForMiscarriage(long pid) {
		return false;
	}
	
	public String getPrettyPotentialForMiscarriage(long pid) {
		if (getPotentialForMiscarriage(pid))
			return "True";
		else
			return "False";
	}
	
	public boolean getAbnormalFetalHeartRate(long initID) {
		try {
			ObstetricsVisit obv = obvData.getByObstetricsInit(initID).get(0);
			int fhr = obv.getFhr();
			return fhr < 120 || fhr > 160;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_ABNORMAL_FETAL_HEART_RATE, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyAbnormalFetalHeartRate(long initID) {
		if (getAbnormalFetalHeartRate(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getMultiples(long initID) {
		try {
			ObstetricsVisit obv = obvData.getByObstetricsInit(initID).get(0);
			int multiplicity = obv.getMultiplicity();
			return multiplicity > 1;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_MULTIPLES, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyMultiples(long initID) {
		if (getMultiples(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getAbnormalWeightGain(long initID) {
		try {
			//This might not be correct;
			List<ObstetricsVisit> list = obvData.getByObstetricsInit(initID);
			OfficeVisit lastOfv = ofvData.getByID(list.get(0).getOfficeVisitID());
			OfficeVisit firstOfv = ofvData.getByID(list.get(list.size() - 1).getOfficeVisitID());
			float weightGain = lastOfv.getWeight() - firstOfv.getWeight();
			return weightGain < 15 || weightGain > 35;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_ABNORMAL_WEIGHT_GAIN, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyAbnormalWeightGain(long initID) {
		if (getAbnormalWeightGain(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getHyperemesisGravidarum(long initID) {
		try {
			long officeVisitID = obvData.getByObstetricsInit(initID).get(0).getOfficeVisitID();
			List<Diagnosis> list = dData.getAllDiagnosisByOfficeVisit(officeVisitID);
			for (Diagnosis d : list) {
				String code = d.getIcdCode().getCode();
				if (code.equals("O210") || code.equals("O211"))
					return true;
			}
			return false;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HYPEREMESIS_GRAVIDARUM, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyHyperemesisGravidarum(long initID) {
		if (getHyperemesisGravidarum(initID))
			return "True";
		else
			return "False";
	}
	
	public boolean getHypothyroidism(long initID) {
		try {
			long officeVisitID = obvData.getByObstetricsInit(initID).get(0).getOfficeVisitID();
			List<Diagnosis> list = dData.getAllDiagnosisByOfficeVisit(officeVisitID);
			for (Diagnosis d : list) {
				String code = d.getIcdCode().getCode();
				if (code.equals("E02") ||
						code.equals("E03") ||
						code.equals("E030") ||
						code.equals("E031") ||
						code.equals("E032") ||
						code.equals("E033") ||
						code.equals("E038") ||
						code.equals("E039") ||
						code.equals("E890"))
					return true;
			}
			return false;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_HYPEREMESIS_GRAVIDARUM, e.getMessage(), null);
			return false;
		}
	}
	
	public String getPrettyHypothyroidism(long initID) {
		if (getHypothyroidism(initID))
			return "True";
		else
			return "False";
	}

	public List<String> getPreexistingConditions(long initID) {
		List<String> conditions = new ArrayList<String>();
		try {
			long officeVisitID = obvData.getByObstetricsInit(initID).get(0).getOfficeVisitID();
			List<Diagnosis> list = dData.getAllDiagnosisByOfficeVisit(officeVisitID);
			for (Diagnosis d: list) {
				String code = d.getIcdCode().getCode();
				if (d.getIcdCode().isChronic() ||
						code.equals("E10") ||
						code.equals("E11") ||
						code.equals("E12") ||
						code.equals("R971") ||
						code.equals("A94"))
					//This might need to be d.getIcdCode().getName();
					conditions.add(d.getName());
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_PREEXISTING_CONDITIONS, e.getMessage(), null);
		}
		return conditions;
	}
	
	public List<String> getDrugAllergies(long pid) {
		List<String> allergies = new ArrayList<String>();
		try {
			List<AllergyBean> list = allergyDAO.getAllergies(pid);
			for (AllergyBean a : list) {
				allergies.add(a.getDescription());
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_ALLERGIES, e.getMessage(), null);
		}
		return allergies;
	}
}
