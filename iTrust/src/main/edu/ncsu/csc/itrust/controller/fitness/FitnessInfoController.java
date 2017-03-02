package edu.ncsu.csc.itrust.controller.fitness;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.CSVFormatException;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.fitness.FitbitImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessImportFactory;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfo;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoData;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoFileFormatException;
import edu.ncsu.csc.itrust.model.fitness.FitnessInfoMySQL;
import edu.ncsu.csc.itrust.model.fitness.MicrosoftBandImportFactory;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Handles a lot of the logic and communication between the view and the model.
 * 
 * @author jcgonzal
 * @author amcheshi
 */
@ManagedBean(name = "fitness_info_controller")
@SessionScoped
public class FitnessInfoController extends iTrustController
{
	/** Constant for the message to be displayed if the fitness info was unsuccessfully added/updated */
	private static final String FITNESS_INFO_CANNOT_BE_ADDED = "Fitness Info Cannot Be Updated";
	/** Constant for the message to be displayed if the fitness info was successfully added/updated */
	private static final String FITNESS_INFO_SUCCESSFULLY_ADDED = "Fitness Info Successfully Updated";
	private static final String FITBIT_INFO_SUCCESSFULLY_ADDED = "Fitbit Fitness Info Successfully Updated";
	private static final String MICROSOFT_BAND_INFO_SUCCESSFULLY_ADDED = "Microsoft Band Fitness Info Successfully Updated";
	private static final String FITNESS_UPLOAD_SUCCESSFUL = "Fitness data uploaded successfully";
	private static final String FITNESS_UPLOAD_UNSUCCESSFUL = "Failed to upload fitness data";
	
	/** Grants access to the database */
	FitnessInfoData fitnessInfoData;
	/** Used to obtain session variables and request parameters */
	SessionUtils sessionUtils;
	/** TODO */
	//FitnessGraphSubject fitnessGraphSubject;
	/** Calendar object used to create the fitness calendar */
	Calendar cal;
	/** Used to import files */
	FitnessImportFactory fitbitFactory;
	MicrosoftBandImportFactory microsoftBandFactory;
	
	/**
	 * Default constructor.
	 */
	public FitnessInfoController() {
		super();
		sessionUtils = SessionUtils.getInstance();
		cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		try {
			fitnessInfoData = new FitnessInfoMySQL();
		} catch (DBException e) {
			e.printStackTrace();
		}
		try {
			fitbitFactory = new FitbitImportFactory();
			microsoftBandFactory = new MicrosoftBandImportFactory();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor injection, intended only for unit testing purposes.
	 * 
	 * @param ds
	 *            The injected DataSource dependency
	 */
	public FitnessInfoController(DataSource ds) {
		super();
		sessionUtils = SessionUtils.getInstance();
		cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		fitnessInfoData = new FitnessInfoMySQL(ds);
		fitbitFactory = new FitbitImportFactory(ds);
		microsoftBandFactory = new MicrosoftBandImportFactory(ds);
	}
	
	/**
	 * Returns the FitnessInfo corresponding to the currently selected patient and date.
	 * This is intended to be used after having selected a date on the fitness calendar.
	 * 
	 * @return the FitnessInfo corresponding to the currently selected patient and date
	 */
	public FitnessInfo getSelectedFitnessInfo() {
		// Obtain the date from the request parameter
		String date = sessionUtils.getRequestParameter("date");
		if (date == null || date.isEmpty()) {
			return null;
		}
		
		// Obtain the pid from session variables
		long pid = getSessionPID();
		if (pid == -1) {
			return null;
		}
		
		// Get fitness info using the pid and date
		return getFitnessInfo(pid, date);
	}
	
	/**
	 * Obtain the pid of the currently selected patient and return it as a long.
	 * If the session variable does not exist or is invalid, return -1
	 * @return the pid of the currently selected patient as a long or -1 if not found or invalid
	 */
	private long getSessionPID() {
		// Get pid String from session variables
		String pid = sessionUtils.getSessionPID();
		if (pid == null || pid.isEmpty()) {
			return -1;
		}
		
		// Parse the String
		long pidLong;
		try {
			pidLong = Long.parseLong(pid);
			return pidLong;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Add the given FitnessInfo into the database.
	 * Overwrites any pre-existing data with the same pid and date
	 * 
	 * @param fitnessInfo the given FitnessInfo to add
	 */
	public void addFitnessInfo(FitnessInfo fitnessInfo) {
		try {
			if (fitnessInfoData.add(fitnessInfo)) {
				printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_INFO_SUCCESSFULLY_ADDED,
						FITNESS_INFO_SUCCESSFULLY_ADDED, null);
			} else {
				printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_INFO_CANNOT_BE_ADDED,
						FITNESS_INFO_CANNOT_BE_ADDED, null);
			}
		} catch (DBException e) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_INFO_CANNOT_BE_ADDED, e.getExtendedMessage(), null);
		} catch (FormValidationException e) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_INFO_CANNOT_BE_ADDED, e.getMessage(), null);
		}		
	}
	
	/**
	 * Sends a FacesMessage for FacesContext to display.
	 * 
	 * @param severity
	 *            severity of the message
	 * @param summary
	 *            localized summary message text
	 * @param detail
	 *            localized detail message text
	 * @param clientId
	 *            The client identifier with which this message is associated
	 *            (if any)
	 */
	public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx == null) {
			return;
		}
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.addMessage(clientId, new FacesMessage(severity, summary, detail));
	}
	
	/**
	 * Returns the FitnessInfo object corresponding to the fitness data entry with the given pid and date.
	 * If such a data entry does not exist, then return null.
	 * 
	 * @param pid the given pid
	 * @param date the given date in the format: YYYY-MM-DD
	 * @return the data entry with the given pid and date, or null if such an entry does not exist 
	 */
	public FitnessInfo getFitnessInfo(long pid, String date)
	{
		try {
			FitnessInfo fi = fitnessInfoData.getFitnessInfo(pid, date);
			if (fi == null) {
				return null;
			}
			return fi;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * Returns a list of the FitnessInfo objects corresponding to the fitness data entries that 
	 * have the given pid and are within the given span of dates (inclusive).
	 * If there are not any data entries that match, then return null.
	 * 
	 * @param pid the given pid
	 * @param startDate the start date of the date span in the format: YYYY-MM-DD
	 * @param endDate the end date of the date span in the format: YYYY-MM-DD
	 * @return the list of data entries with the given pid within the date span, or null if no such entries exist 
	 */
	public FitnessInfo[] getFitnessInfo(long pid, String startDate, String endDate)
	{
		try {
			FitnessInfo[] fiList = fitnessInfoData.getFitnessInfo(pid, startDate, endDate);
			if (fiList == null) {
				return null;
			}
			return fiList;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * Returns a list of the FitnessInfo objects corresponding to the fitness data entries that 
	 * have the given session pid and are within the given span of dates (inclusive).
	 * If there are not any data entries that match, then return null.
	 * 
	 * @param startDate the start date of the date span in the format: YYYY-MM-DD
	 * @param endDate the end date of the date span in the format: YYYY-MM-DD
	 * @return the list of data entries with the given pid within the date span, or null if no such entries exist 
	 */
	public FitnessInfo[] getFitnessInfo(String startDate, String endDate)
	{
		try {
			FitnessInfo[] fiList = fitnessInfoData.getFitnessInfo(getSessionPID(), startDate, endDate);
			if (fiList == null) {
				return null;
			}
			return fiList;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * TODO
	 * @param month
	 * @param year
	 * @return
	 */
	public List<Boolean> getCalendarData(int month, int year) {
		//TODO: Actually make this method work
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		ret.add(true);
		ret.add(false);
		ret.add(true);
		return ret;
	}
	
	/**
	 * Returns cal's current month as a zero-based int
	 * (meaning that Jan. = 0, Feb. = 1, ..., Dec. = 11).
	 * 
	 * @return cal's current month as a zero-based int
	 */
	public int getMonth() {
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * Returns cal's current year.
	 * @return cal's current year
	 */
	public int getYear() {
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * Returns the name of cal's current month as a String
	 * @return the name of cal's current month as a String
	 */
	public String getMonthName() {
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
	}
	
	/**
	 * Returns the day of the month selected on the fitness calendar as a String.
	 * @return the day of the month selected on the fitness calendar
	 */
	public String getSelectedDay() {
		// Try to obtain the day from the request parameter
		String day = sessionUtils.getRequestParameter("day");
		if (day == null || day.isEmpty()) { //param doesn't exist, so look in session variables
			return (String) sessionUtils.getSessionVariable("fitnessDate");
		} else { //set session variable for future reference
			sessionUtils.setSessionVariable("fitnessDate", day);
			return day;
		}
	}
	
	/**
	 * Navigate cal back one month and reload the fitnessCalendar page.
	 * @throws IOException
	 */
	public void goToPrevious() throws IOException {
		cal.add(Calendar.MONTH, -1);
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp/fitnessCalendar.xhtml");
		}
	}
	
	/**
	 * Navigate cal forward one month and reload the fitnessCalendar page.
	 * @throws IOException
	 */
	public void goToNext() throws IOException {
		cal.add(Calendar.MONTH, 1);
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		if (ctx != null) {
			ctx.redirect("/iTrust/auth/hcp/fitnessCalendar.xhtml");
		}
	}
	
	/**
	 * Returns an array of Strings containing the names of the days of the week,
	 * starting from Sunday and ending on Saturday.
	 * 
	 * @return the names of the days of the week (Sun-Sat)
	 */
	public String[] getDaysOfWeek() {
		return new String[]{"Sunday", "Monday", "Tuesday", "Wedensday", "Thursday", "Friday", "Saturday"};
	}
	
	/**
	 * Create a date string using the given day and whatever month and year cal is currently on.
	 * The string should be in the format: YYYY-MM-DD
	 * 
	 * @param day the day of the month
	 * @return a date string in the format: YYYY-MM-DD
	 */
	private String makeDateString(int day) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		return String.format("%04d-%02d-%02d", year, month, day);
	}
	
	/**
	 * Return a 2D array of CalDay objects representing a visual month on a calendar.
	 * The calendar month to be represented is the current month (and year) that cal is set to.
	 * 
	 * @return a calendar representation as a 2D array of CalDay objects
	 */
	public ArrayList<ArrayList<CalDay>> getFitnessCalendar() {
		// Get pid from session variables
		long pid = getSessionPID();
		
		// Start building calendar
		ArrayList<ArrayList<CalDay>> ret = new ArrayList<ArrayList<CalDay>>();
		ArrayList<CalDay> week = new ArrayList<CalDay>(7);
		int k = 1;
		
		//Blank days first
		for (int i = 1; i < cal.get(Calendar.DAY_OF_WEEK); i++) {
			week.add(null);
			k++;
		}
		
		//Then days in the month
		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {	
			if (k > 7) {
				k = 1;
				ret.add(week);
				week = new ArrayList<CalDay>(7);
			}
			
			// Check if data exists for this day
			boolean dataExists = false;
			String date = makeDateString(i);
			try {
				if (fitnessInfoData.getFitnessInfo(pid, date) != null)
					dataExists = true;
			} catch (DBException e) {
				dataExists = false;
			}
			
			// Create new CalDay and add to the week
			week.add(new CalDay(dataExists, i));
			k++;
		}
		
		//Then blank days after month
		while (k <= 7) {
			week.add(null);
			k++;
		}
		ret.add(week);
		return ret;
	}
	
	/**
	 * Uploads fitness data to the database from the given InputStream.
	 * The given InputStream should be set to the start of a FitBit csv file.
	 * 
	 * @param inputStream the given InputStream
	 */
	public void upload(String fileType, InputStream inputStream) {
		long pid = getSessionPID();
		String message;
		boolean success = false;
		
		if (fileType.equals("FitBit")) {
			try {
				fitbitFactory.importFitnessInfo(pid, inputStream);
				success = true;
				message = FITBIT_INFO_SUCCESSFULLY_ADDED;
			} catch (CSVFormatException e) {
				message = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				message = e.getMessage();
				e.printStackTrace();
			} catch (FitnessInfoFileFormatException e) {
				message = e.getMessage();
				e.printStackTrace();
			}
		} else {
			try {
				microsoftBandFactory.importFitnessInfo(pid, inputStream);
				success = true;
				message = MICROSOFT_BAND_INFO_SUCCESSFULLY_ADDED;
			} catch (CSVFormatException e) {
				message = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				message = e.getMessage();
				e.printStackTrace();
			} catch (FitnessInfoFileFormatException e) {
				message = e.getMessage();
				e.printStackTrace();
			}
		}
		
		if (success) {
			printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_UPLOAD_SUCCESSFUL,
					message, null);
		} else {
			printFacesMessage(FacesMessage.SEVERITY_INFO, FITNESS_UPLOAD_UNSUCCESSFUL,
					message, null);
		}
		
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Represents a day on a visual calendar.
	 * 
	 * @author jcgonzal
	 */
	public class CalDay {
		/** Whether or not there exists fitness data for the date represented by this object */
		boolean containsData;
		/** The day of the month */
		int day;
		/** The date in the format: YYYY-MM-DD */
		String date;
		
		/**
		 * Sets the fields to the given values and creates the date string.
		 * 
		 * @param containsData whether or not this calendar day contains data
		 * @param day the day of the month
		 */
		public CalDay(boolean containsData, int day) {
			this.containsData = containsData;
			this.day = day;
			this.date = makeDateString(day);
		}

		/**
		 * Returns containsData.
		 * @return containsData
		 */
		public boolean isContainsData() {
			return containsData;
		}

		/**
		 * Sets containsData
		 * @param containsData true or false
		 */
		public void setContainsData(boolean containsData) {
			this.containsData = containsData;
		}

		/**
		 * Returns day.
		 * @return day
		 */
		public int getDay() {
			return day;
		}

		/**
		 * Sets day.
		 * @param day the day of the month
		 */
		public void setDay(int day) {
			this.day = day;
		}
		
		/**
		 * Returns date.
		 * @return date
		 */
		public String getDate() {
			return date;
		}

		/**
		 * Sets date.
		 * @param date the date in the format: YYYY-MM-DD
		 */
		public void setDate(String date) {
			this.date = date;
		}
		
	}
	
	/**
	 * Class used to create the graphs. Each object is a data point.
	 * @author jcgonzal
	 *
	 */
	public static class GraphKeyValue {
		
		/**
		 * String that will be the label on the x axis.
		 */
		String key;
		
		/**
		 * Integer that will be the value on the y axis. Potentially null.
		 */
		Integer value;
		
		/**
		 * Constructor
		 * @param key for x axis
		 * @param value for y axis
		 */
		public GraphKeyValue(String key, Integer value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(Integer value) {
			this.value = value;
		}
		
		public boolean equals(GraphKeyValue o) {
			return this.key.equals(o.key) && this.value.equals(o.value);
		}
	}
}
