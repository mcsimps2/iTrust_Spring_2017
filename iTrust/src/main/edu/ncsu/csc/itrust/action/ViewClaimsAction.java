package edu.ncsu.csc.itrust.action;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.BillingBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BillingDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;

/**
 * ViewClaimsAction handles interaction between user input and the billing DAO. 
 */
public class ViewClaimsAction {
	
	/**billingAccess provides access to the Billing table*/
	private BillingDAO billingAccess;
	/**patientRetriever provides access to the patients table*/
	private PatientDAO patientRetriever;
	
	/**
	 * ViewClaimsAction simply initializes the DAOs.
	 * @param fact The dao factory that will create the DAOs
	 */
	public ViewClaimsAction(DAOFactory fact){
		billingAccess = fact.getBillingDAO();
		patientRetriever = fact.getPatientDAO();
	}
	
	/**
	 * getClaims returns a list of all the insurance claims.
	 * @return A list of all the insurance claims.
	 */
	public List<BillingBean> getClaims(){
		try {
			return billingAccess.getInsuranceBills();
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * getSubmitter returns the person who submitted the claim.
	 * @param b The bill we are curious about.
	 * @return The name of the submitter.
	 */
	public String getSubmitter(BillingBean b){
		try {
			return patientRetriever.getName(b.getPatient());
		} catch (ITrustException e) {
			return null;
		}
	}
	
	/**
	 * getDate returns the date the bill was submitted.
	 * @param b The bill we are talking 
	 * @return
	 */
	public String getDate(BillingBean b){
		return new SimpleDateFormat("MM/dd/YYYY").format(b.getSubTime());
	}
}
