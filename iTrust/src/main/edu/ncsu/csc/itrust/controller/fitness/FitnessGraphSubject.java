package edu.ncsu.csc.itrust.controller.fitness;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.observer.fitness.DailyGraphObserver;
import edu.ncsu.csc.itrust.observer.fitness.DeltaGraphObserver;
import edu.ncsu.csc.itrust.observer.fitness.GraphObserver;
import edu.ncsu.csc.itrust.observer.fitness.WeeklyAvgGraphObserver;

/**
 * The subject file that serves to notify the observers when the data should be changed
 * @author jcgonzal
 *
 */
@ManagedBean(name = "fitness_graph_subject")
@SessionScoped
public class FitnessGraphSubject {

	/**
	 * Start date corresponding to the input field
	 */
	private String startDate;
	
	/**
	 * End date corresponding to the input field
	 */
	private String endDate;
	
	/**
	 * Observer for the daily graph
	 */
	private GraphObserver dailyGraphObserver;
	
	/**
	 * Observer for the delta graph
	 */
	private GraphObserver deltaGraphObserver;
	
	/**
	 * Observer for the weekly graph
	 */
	private GraphObserver weeklyAvgGraphObserver;
	
	/**
	 * Error message for input of an invalid date format.
	 */
	private static final String INVALID_DATE_FORMAT = "Invalid date format. Use YYYY-MM-DD.";
	
	/**
	 * Constructor. Instantiates observer objects.
	 */
	public FitnessGraphSubject()
	{
		dailyGraphObserver = new DailyGraphObserver();
		deltaGraphObserver = new DeltaGraphObserver();
		weeklyAvgGraphObserver = new WeeklyAvgGraphObserver();
	}
	
	/**
	 * Constructor with data source
	 */
	public FitnessGraphSubject(DataSource ds) {
		dailyGraphObserver = new DailyGraphObserver(ds);
		deltaGraphObserver = new DeltaGraphObserver(ds);
		weeklyAvgGraphObserver = new WeeklyAvgGraphObserver(ds);
	}
	
	/**
	 * Notifies all the observers that they need to update.
	 * @throws IOException
	 */
	public void updateAndNotify() throws IOException
	{
		if (!FitnessInfo.verifyDate(startDate) || !FitnessInfo.verifyDate(endDate)) {
			dailyGraphObserver.getFitnessController().printFacesMessage(FacesMessage.SEVERITY_INFO, INVALID_DATE_FORMAT, INVALID_DATE_FORMAT, null);
			return;
		}
		dailyGraphObserver.updateData(null, startDate, endDate);
		deltaGraphObserver.updateData(null, startDate, endDate);
		weeklyAvgGraphObserver.updateData(null, startDate, endDate);
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp/fitnessGraphs.xhtml");
		}
	}
	
	/**
	 * Notifies all the observers that they need to update. Used only for testing, so doesn't refresh the page
	 * @throws IOException
	 */
	public void updateAndNotify(Long pid) throws IOException
	{
		if (!FitnessInfo.verifyDate(startDate) || !FitnessInfo.verifyDate(endDate)) {
			dailyGraphObserver.getFitnessController().printFacesMessage(FacesMessage.SEVERITY_INFO, INVALID_DATE_FORMAT, INVALID_DATE_FORMAT, null);
			return;
		}
		dailyGraphObserver.updateData(pid, startDate, endDate);
		deltaGraphObserver.updateData(pid, startDate, endDate);
		weeklyAvgGraphObserver.updateData(pid, startDate, endDate);
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the dailyGraphObserver
	 */
	public GraphObserver getDailyGraphObserver() {
		return dailyGraphObserver;
	}

	/**
	 * @param dailyGraphObserver the dailyGraphObserver to set
	 */
	public void setDailyGraphObserver(GraphObserver dailyGraphObserver) {
		this.dailyGraphObserver = dailyGraphObserver;
	}

	/**
	 * @return the deltaGraphObserver
	 */
	public GraphObserver getDeltaGraphObserver() {
		return deltaGraphObserver;
	}

	/**
	 * @param deltaGraphObserver the deltaGraphObserver to set
	 */
	public void setDeltaGraphObserver(GraphObserver deltaGraphObserver) {
		this.deltaGraphObserver = deltaGraphObserver;
	}

	/**
	 * @return the weeklyAvgGraphObserver
	 */
	public GraphObserver getWeeklyAvgGraphObserver() {
		return weeklyAvgGraphObserver;
	}

	/**
	 * @param weeklyAvgGraphObserver the weeklyAvgGraphObserver to set
	 */
	public void setWeeklyAvgGraphObserver(GraphObserver weeklyAvgGraphObserver) {
		this.weeklyAvgGraphObserver = weeklyAvgGraphObserver;
	}		
	
}
