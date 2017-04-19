package edu.ncsu.csc.itrust.controller.obstetrics.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
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
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.PrettyUtils;

@ManagedBean(name = "obstetrics_report_controller")
@SessionScoped
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
	private static final String ERROR_LOADING_GENETIC_POTENTIAL_FOR_MISCARRIAGE = "Error loading genetic potential for miscarriage";
	private static final String ERROR_VIEWING_REPORT = "Error viewing report";
	
	private ObstetricsInitData oiData;
	private PregnancyInfoData pregnancyData;
	private ObstetricsVisitData obvData;
	private OfficeVisitData ofvData;
	private DiagnosisData dData;
	private PatientDAO patientDAO;
	private AllergyDAO allergyDAO;

	private ObstetricsInit viewedOI;
	
	public ObstetricsReportController() {
		super();
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
	
	public ObstetricsReportController(DataSource ds, PatientDAO patientDAO, AllergyDAO allergyDAO) {
		super();
		this.patientDAO = patientDAO;
		this.allergyDAO = allergyDAO;
		oiData = new ObstetricsInitMySQL(ds);
		pregnancyData = new PregnancyInfoMySQL(ds);
		obvData = new ObstetricsVisitMySQL(ds);
		ofvData = new OfficeVisitMySQL(ds);
		dData = new DiagnosisMySQL(ds);
	}
	
	public void viewReport(ObstetricsInit oi) {
		this.setViewedOI(oi);
		try {
			this.navigateToReport();
			logTransaction(TransactionType.LABOR_DELIVERY_REPORT, this.sessionUtils.getSessionLoggedInMIDLong(), sessionUtils.getCurrentPatientMIDLong(), null);
		} catch (IOException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_VIEWING_REPORT, e.getMessage(), null);			
		}
	}
	
	public void navigateToReport() throws IOException {
		NavigationController.viewObstetricsReport();
	}
	
	public String getPrettyBool(boolean bool) {
		return bool ? "True" : "False";
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
			return obvData.getByObstetricsInit(initID);
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_OBSTETRICS_VISITS, e.getMessage(), null);
			return null;
		}
	}
	
	public boolean getHighBloodPressure(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// If there are no office visits associated with this obstetrics record, then no blood
				// pressure has been recorded yet, so no high blood pressure should be reported.
				return false;
			}
			
			ObstetricsVisit obv = obstetricsVisits.get(0);
			
			String bloodPressure = ofvData.getByID(obv.getOfficeVisitID()).getBloodPressure();
			int i = bloodPressure.indexOf("/");
			int top = Integer.parseInt(bloodPressure.substring(0, i));
			int bottom = Integer.parseInt(bloodPressure.substring(i + 1));
			return top >= 140 || bottom >= 90;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_BLOOD_PRESSURE, e.getMessage(), null);
			return false;
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
	
	public boolean getHasPreexistingCondition(long initID) {
		return !this.getPreexistingConditions(initID).isEmpty();
	}
	
	public boolean getHasAllergies(long pid) {
		return !this.getDrugAllergies(pid).isEmpty();
	}
	
	public boolean getLowLyingPlacenta(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// No low-lying placenta has been observed yet because no office visits have
				// occurred yet.
				return false;
			}
			ObstetricsVisit obv = obstetricsVisits.get(0);
			return obv.isLowLyingPlacentaObserved();
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_LOW_LYING_PLACENTA, e.getMessage(), null);
			return false;
		}
	}
	
	public boolean getPotentialForMiscarriage(long initID) {
		try {
			return oiData.getByID(initID).getGeneticPotentialForMiscarriage();
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_GENETIC_POTENTIAL_FOR_MISCARRIAGE, e.getMessage(), null);
			return false;
		}
	}
	
	public boolean getAbnormalFetalHeartRate(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// No fetal heart rate has been recorded yet, so it cannot be abnormal.
				return false;
			}
			ObstetricsVisit obv = obstetricsVisits.get(0);
			int fhr = obv.getFhr();
			return fhr < 120 || fhr > 160;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_ABNORMAL_FETAL_HEART_RATE, e.getMessage(), null);
			return false;
		}
	}
	
	public boolean getMultiples(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// If no office visits have occurred, then no babies have been born,
				// so there haven't been multiples yet.
				return false;
			}
			ObstetricsVisit obv = obstetricsVisits.get(0);
			int multiplicity = obv.getMultiplicity();
			return multiplicity > 1;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_MULTIPLES, e.getMessage(), null);
			return false;
		}
	}
	
	public boolean getAbnormalWeightGain(long initID) {
		try {
			List<ObstetricsVisit> list = obvData.getByObstetricsInit(initID);
			if (list.size() < 2) {
				// If fewer than two office visits have occurred, we don't know any weight
				// change, so we report that no abnormal weight gain has occurred.
				return false;
			}
			
			OfficeVisit lastOfv = ofvData.getByID(list.get(0).getOfficeVisitID());
			OfficeVisit firstOfv = ofvData.getByID(list.get(list.size() - 1).getOfficeVisitID());
			float weightGain = lastOfv.getWeight() - firstOfv.getWeight();
			return weightGain < 15 || weightGain > 35;
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_LOADING_ABNORMAL_WEIGHT_GAIN, e.getMessage(), null);
			return false;
		}
	}
	
	public boolean getHyperemesisGravidarum(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// No office visit has occurred, so no hyperemesis gravidarum has been recorded.
				return false;
			}
			long officeVisitID = obstetricsVisits.get(0).getOfficeVisitID();
			List<Diagnosis> diagnoses = dData.getAllDiagnosisByOfficeVisit(officeVisitID);
			for (Diagnosis d : diagnoses) {
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
	
	public boolean getHypothyroidism(long initID) {
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// No office visit has occurred, so no hypothyroidism has been recorded.
				return false;
			}
			
			long officeVisitID = obstetricsVisits.get(0).getOfficeVisitID();
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

	public List<String> getPreexistingConditions(long initID) {
		List<String> conditions = new ArrayList<String>();
		try {
			List<ObstetricsVisit> obstetricsVisits = obvData.getByObstetricsInit(initID);
			if (obstetricsVisits.isEmpty()) {
				// If there are no office visits associated with this obstetrics record, then no preexisting
				// conditions have been recorded yet, so no none should be reported.
				return conditions;
			}
			long officeVisitID = obstetricsVisits.get(0).getOfficeVisitID();
			List<Diagnosis> list = dData.getAllDiagnosisByOfficeVisit(officeVisitID);
			for (Diagnosis d: list) {
				String code = d.getIcdCode().getCode();
				if (code.equals("E10") ||
						code.equals("E11") ||
						code.equals("E13") ||
						code.equals("R971") ||
						code.equals("A94") ||
						d.getIcdCode().isChronic())
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

	public ObstetricsInit getViewedOI() {
		return viewedOI;
	}

	public void setViewedOI(ObstetricsInit viewedOI) {
		this.viewedOI = viewedOI;
	}
	
	public List<ComplicationInfo> getComplications(long pid, long initID) {
		List<ComplicationInfo> list = new ArrayList<ComplicationInfo>();

		try {
			// RH- flag
			ComplicationInfo rh = new ComplicationInfo();
			boolean rhFlag = this.getObstetricsInit(initID).getRH();
			rh.setFlag(rhFlag);
			rh.setTitle("RH- flag");
			rh.setMessage(rhFlag ? "Yes, RH- flag present" : "No, RH- flag not present");
			rh.setID("rh");
			list.add(rh);
			
			// High blood pressure
			ComplicationInfo hbp = new ComplicationInfo();
			boolean hbpFlag = this.getHighBloodPressure(initID);
			hbp.setFlag(hbpFlag);
			hbp.setTitle("High blood pressure");
			if (hbpFlag) {
				String bloodPressure = ofvData.getByID(obvData.getByObstetricsInit(initID).get(0).getOfficeVisitID()).getBloodPressure();
				hbp.setMessage("Yes, " + bloodPressure);
			} else {
				hbp.setMessage("No");
			}
			hbp.setID("hbp");
			list.add(hbp);
			
			// Advanced maternal age
			ComplicationInfo ama = new ComplicationInfo();
			boolean amaFlag = this.getAdvancedMaternalAge(pid);
			ama.setFlag(amaFlag);
			ama.setTitle("Advanced maternal age");
			String age = Integer.toString(patientDAO.getPatient(pid).getAge());
			ama.setMessage(amaFlag ? "Yes, " + age : "No, " + age);
			ama.setID("ama");
			list.add(ama);
			
			// Pre-existing conditions
			ComplicationInfo pec = new ComplicationInfo();
			List<String> pecs = this.getPreexistingConditions(initID);
			boolean pecFlag = !pecs.isEmpty();
			pec.setFlag(pecFlag);
			pec.setTitle("Pre-existing conditions");
			String listOfConditions = String.join(", ", pecs);
			pec.setMessage(pecFlag ? "Yes: " + listOfConditions : "None");
			pec.setID("pec");
			list.add(pec);
			
			// Drug allergies
			ComplicationInfo da = new ComplicationInfo();
			List<String> das = this.getDrugAllergies(pid);
			boolean daFlag = !das.isEmpty();
			da.setFlag(daFlag);
			da.setTitle("Drug allergies");
			String listOfDrugAllergies = String.join(", ", das);
			da.setMessage(daFlag ? "Yes: " + listOfDrugAllergies : "None");
			da.setID("da");
			list.add(da);
			
			// Low-lying placenta
			ComplicationInfo llp = new ComplicationInfo();
			boolean llpFlag = this.getLowLyingPlacenta(initID);
			llp.setFlag(llpFlag);
			llp.setTitle("Low-lying placenta");
			llp.setMessage(llpFlag ? "Low-lying placenta observed" : "None observed");
			llp.setID("llp");
			list.add(llp);
			
			// High genetic potential for miscarriages
			ComplicationInfo gpm = new ComplicationInfo();
			boolean gpmFlag = this.getPotentialForMiscarriage(initID);
			gpm.setFlag(gpmFlag);
			gpm.setTitle("High genetic potential for miscarriages");
			gpm.setMessage(gpmFlag ? "Yes" : "No");
			gpm.setID("gpm");
			list.add(gpm);
			
			// Abnormal fetal heart rate (FHR)
			ComplicationInfo afhr = new ComplicationInfo();
			boolean afhrFlag = this.getAbnormalFetalHeartRate(initID);
			afhr.setFlag(afhrFlag);
			afhr.setTitle("Abnormal fetal heart rate (FHR)");
			if (afhrFlag) {
				String fhr = Integer.toString(obvData.getByObstetricsInit(initID).get(0).getFhr());
				afhr.setMessage("Yes, " + fhr);
			} else {
				afhr.setMessage("No");
			}
			afhr.setID("afhr");
			list.add(afhr);
			
			// Multiplicity > 1
			ComplicationInfo mp = new ComplicationInfo();
			boolean mpFlag = this.getMultiples(initID);
			mp.setFlag(mpFlag);
			mp.setTitle("Multiplicity > 1");
			if (mpFlag) {
				String multiplicity = Integer.toString(obvData.getByObstetricsInit(initID).get(0).getMultiplicity());
				mp.setMessage("Yes, " + multiplicity);
			} else {
				mp.setMessage("No");
			}
			mp.setID("mp");
			list.add(mp);
			
			// Atypical weight change
			ComplicationInfo awc = new ComplicationInfo();
			boolean awcFlag = this.getAbnormalWeightGain(initID);
			awc.setFlag(awcFlag);
			awc.setTitle("Atypical weight change");
			if (awcFlag) {
				List<ObstetricsVisit> ovs = obvData.getByObstetricsInit(initID);
				OfficeVisit last = ofvData.getByID(ovs.get(0).getOfficeVisitID());
				OfficeVisit first = ofvData.getByID(ovs.get(ovs.size() - 1).getOfficeVisitID());
				String weightChange = Float.toString(last.getWeight() - first.getWeight());
				awc.setMessage("Yes, " + weightChange);
			} else {
				awc.setMessage("No");
			}
			awc.setID("awc");
			list.add(awc);
			
			// Hyperemesis gravidarum
			ComplicationInfo hg = new ComplicationInfo();
			boolean hgFlag = this.getHyperemesisGravidarum(initID);
			hg.setFlag(hgFlag);
			hg.setTitle("Hyperemesis gravidarum");
			hg.setMessage(hgFlag ? "Yes" : "No");
			hg.setID("hg");
			list.add(hg);
			
			// Hypothyroidism
			ComplicationInfo ht = new ComplicationInfo();
			boolean htFlag = this.getHypothyroidism(initID);
			ht.setFlag(htFlag);
			ht.setTitle("Hypothyroidism");
			ht.setMessage(htFlag ? "Yes" : "No");
			ht.setID("ht");
			list.add(ht);
		} catch (DBException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public String getDateByObstetricsVisit(ObstetricsVisit ov) {
		try {
			return PrettyUtils.getPrettyDate(ofvData.getByID(ov.getOfficeVisitID()).getDate());
		} catch (DBException e) {
			return "Error";
		}
	}
	
	public String getBloodPressureByObstetricsVisit(ObstetricsVisit ov) {
		try {
			return ofvData.getByID(ov.getOfficeVisitID()).getBloodPressure();
		} catch (DBException e) {
			return "Error";
		}
	}
	
	public String getWeightByObstetricsVisit(ObstetricsVisit ov) {
		try {
			return Float.toString(ofvData.getByID(ov.getOfficeVisitID()).getWeight());
		} catch (DBException e) {
			return "Error";
		}
	}
}