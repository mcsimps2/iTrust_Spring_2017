package edu.ncsu.csc.itrust.controller.obstetrics.officeVisit;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.util.DateTime;

import edu.ncsu.csc.itrust.JSONReader;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO;

public class GoogleScheduler
{
	/** Factory for databases */
	private static DAOFactory factory = DAOFactory.getProductionInstance();
	/** API key for Google calendar */
	private static final String API_KEY = "AIzaSyBRUdxP2Gs0_eWuNrcYRNXMh2TMIn2nUbQ";
	/** Base URL for accessing Google calendars via the API */
	private static final String BASE_URL = "https://www.googleapis.com/calendar/v3/calendars/";
	/** 1 Day in Milliseconds */
	private static final long DAY_TO_MILLI = 86400000L;
	/** The ID of the federal Holiday Google calendar */
	private static final String HOLIDAY_CALENDAR_ID = "l2o03lmus0c4gu4rftse4fejqg@group.calendar.google.com";
	/** Don't schedule appointments before 9 AM */
	private static final int LOWER_BOUND = 9;
	/** Don't schedule appointments after 4 PM */
	private static final int UPPER_BOUND = 16;
	/** The type of appointment to schedule */
	private static final String APPT_TYPE = "Obstetrics";
	/** Maximum attempts of scheduling before giving up */
	private static final int MAX_ATTEMPTS = 75;
	/** Minutes to Milliseconds */
	private static final long MINUTE_TO_MILLI = 60000L;
	
	/**
	 * Uses the given factory instead of the default one
	 * @param _factory the factory to use
	 */
	public static void useFactory(DAOFactory _factory)
	{
		factory = _factory;
	}
	
	/**
	 * Gets a list of google events from the specified calendar inbetween the given dates
	 * @param calendarID the ID of the public google calendar
	 * @param startDate events will have a date >= to this (inclusive)
	 * @param endDate events will have a date < to this (exclusive)
	 * @return a list of events given as arrays of {startTimeOfEvent, endTimeOfEvent}
	 * @throws JSONException if the calendar is not public or unaccessible
	 * @throws IOException if there is trouble parsing
	 */
	public static List<DateTime[]> getGoogleEvents(String calendarID, DateTime startDate, DateTime endDate) throws JSONException, IOException
	{
		JSONArray ja = JSONReader.readFromURL(getURL(calendarID, startDate, endDate)).getJSONArray("items");
		List<DateTime[]> list = new ArrayList<DateTime[]>();
		for (int i = 0; i < ja.length(); i++)
		{
			JSONObject jo = ja.getJSONObject(i);
			String start, end;
			try
			{
				start = jo.getJSONObject("start").getString("dateTime");
			}
			catch (JSONException e)
			{
				start = jo.getJSONObject("start").getString("date");
			}
			try
			{
				end = jo.getJSONObject("end").getString("dateTime");
			}
			catch (JSONException e)
			{
				end = jo.getJSONObject("end").getString("date");
			}
			DateTime[] toAdd = {new DateTime(start), new DateTime(end)};
			list.add(toAdd);
		}
		return list;
	}
	
	/**
	 * Gets the google calendar link to the given calendar, returning only events within the specified timeframe
	 * @param calendarID the ID of the public Google calendar
	 * @param startDate events returned will be after this date (inclusive)
	 * @param endDate events returned will be before this date (exclusive)
	 * @return the URL to access the pulic Google calendar
	 */
	private static String getURL(String calendarID, DateTime startDate, DateTime endDate)
	{
		String ret = BASE_URL + calendarID + 
				"/events?timeMin=" + startDate.toStringRfc3339() + 
				"&timeMax=" + endDate.toStringRfc3339() + "&singleEvents=true&key=" + API_KEY;
		return ret;
	}
	
	/**
	 * Gets all appointments for an HCP between the two (future) dates.
	 * NOTE: Only dates after the current time are given
	 * @param mid the MID of the HCP
	 * @param startDate lower bound on the events returned, inclusive
	 * @param endDate upper bound on the events returned, exclusive
	 * @return List of appointments in the format {startOfAppointment, endOfAppointment}
	 * @throws DBException
	 * @throws SQLException
	 */
	 private static List<DateTime[]> getAppts(long mid, DateTime startDate, DateTime endDate) throws DBException, SQLException
	 {
		 ApptDAO apptDAO = factory.getApptDAO();
		 ApptTypeDAO apptTypeDAO = factory.getApptTypeDAO();
		 List<ApptBean> apptBeans = apptDAO.getAllApptsFor(mid);
		 List<DateTime[]> list = new ArrayList<DateTime[]>();
		 for (int i = 0; i < apptBeans.size(); i++)
		 {
			 ApptBean appt = apptBeans.get(i);
			 String type = appt.getApptType();
			 int duration = apptTypeDAO.getApptType(type).getDuration();
			 Timestamp date = appt.getDate();
			 DateTime dt_start = new DateTime(date.getTime());
			 DateTime dt_end = new DateTime(date.getTime() + duration*MINUTE_TO_MILLI);
			 if (dt_start.getValue() >= startDate.getValue() && dt_end.getValue() < endDate.getValue())
			 {
				 DateTime[] toAdd = {dt_start, dt_end};
				 list.add(toAdd);
			 }
		 }
		 return list;
	 }
	 
	 /**
	  * Determines whether or not an appointment with a given duration can be scheduled at the given date
	  * @param cal a calendar object set to the preferred datetime of the appointment (year, month, day, hour, minute, seconds)
	  * @param conflicts a list of conflicts to avoid
	  * @param duration how long the appointment should last in minutes
	  * @return true if the appointment can be scheduled, false otherwise
	  */
	 private static boolean isValidTime(Calendar cal, List<DateTime[]> conflicts, int duration)
	 {
		 int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		 if (dayOfWeek == Calendar.SATURDAY)
		 {
			 return false;
		 }
		 if (dayOfWeek == Calendar.SUNDAY)
		 {
			 return false;
		 }
		 long timeOfAppt = cal.getTimeInMillis();
		 //get the datetime of this object
		 for (int i = 0; i < conflicts.size(); i++)
		 {
			 if (timeOfAppt >= conflicts.get(i)[0].getValue() && timeOfAppt < conflicts.get(i)[1].getValue()) //can't  start at an invalid time
			 {
				 return false;
			 }
			 if ((timeOfAppt + duration*MINUTE_TO_MILLI) >= conflicts.get(i)[0].getValue() && (timeOfAppt + duration*MINUTE_TO_MILLI) < conflicts.get(i)[1].getValue()) //can't end at invalid time
			 {
				 return false;
			 }
		 }
		 //No conflict if we got to this point!
		 return true;
	 }
	 
	 /**
	  * Schedules an obstetrics appointment at least numDays from today at the same time of the current office visit,
	  * taking into account the availability of the HCP (via appointments) and the patient (via their calendar).
	  * Passing in a null or empty string calendarID when ignore the patient's calendar.
	  * To use the patient's calendar, it must be public.
	  * @param hcpid the ID of the HCP
	  * @param pid the ID of the patient
	  * @param calendarID the ID of the patient's public Google calendar.  Null or empty string to ignore.
	  * @param numDays Minimum number of days from now to schedule the appointment
	  * @param ov the current office visit
	  * @return an ApptBean containing information about the scheduled appointment
	  * @throws GoogleSchedulerException if unable to automatically schedule an appointment
	  */
	 public static ApptBean scheduleObstetricsAppointment(long hcpid, long pid, String calendarID, int numDays, OfficeVisit ov) throws GoogleSchedulerException
	 {
		 try
		 {
			 int duration = factory.getApptTypeDAO().getApptType(APPT_TYPE).getDuration();
			 return scheduleAppointment(hcpid, pid, calendarID, numDays, duration, ov);
		 }
		 catch (DBException | SQLException e)
		 {
			 throw new GoogleSchedulerException("Unable to determine the duration of the new appointment");
		 }
	 }
	 
	 /**
	  * Schedules an appointment at least numDays from today at the same time of the current office visit.
	  * Pass in a null calendarID or an empty string calendarID to ignore the patient's public calendar.
	  * The algorithm takes into account the availability of both the patient and the HCP.
	  * The appointment will occur at the same time T of the current office visit (hour and minute), unless this is outside the bounds 9 AM - 4 PM.
	  * In this case, the visit will occur at 9 AM if the time of the current visit is before 9 AM, or at 4 PM if the time of the current visit if after 4 PM.
	  * The appointment will be scheduled at least numDays from the current date. The algorithm
	  * will first try to schedule it numDays from today, but will incrementally check successive dates at time T if it fails.
	  * A date can fail if 
	  * (1) there is a conflict that day, either in the patients calendar (if he/she has one) or in the HCP's list of appointments, 
	  * during time T.
	  * (2) it is a federal holiday
	  * (3) it is Saturday or Sunday
	  * If a patient gives a private calendar ID or nonexistent calendar ID, it is ignored.
	  * @param hcpid the HCP's ID
	  * @param pid the patient's ID
	  * @param calendarID the ID of the patient's publicly available calendar. If this is not public, it is ignored.  If it is null or the empty string, it is ignored
	  * @param numDays the number of days the appointment should be made from now
	  * @param duration length of the new appointment in minutes
	  * @param ov the current office visit
	  * @return the ApptBean that was populated to the database
	  * @throws GoogleSchedulerException if unable to schedule an appointment, especially if the max iterations has been exceeded
	  */
	 public static ApptBean scheduleAppointment(long hcpid, long pid, String calendarID, int numDays, int duration, OfficeVisit ov) throws GoogleSchedulerException
	 {
		 try
		 {
			 //First, find the preferred timing of the appointment
			 LocalDateTime officeVisitDate = ov.getDate();
			 int hour = officeVisitDate.getHour();
			 int minute = officeVisitDate.getMinute();
			 int seconds = officeVisitDate.getSecond();
			 
			 
			 //Make sure time is between 9 AM and 4 PM.  If not, round it to the nearest bound (9 AM or 4 PM)
			 double timeOfAppt = hour + ((double) minute)/60;
			 if (timeOfAppt < LOWER_BOUND)
			 {
				 hour = 9;
				 minute = 0;
				 seconds = 0;
			 }
			 else if (timeOfAppt > UPPER_BOUND)
			 {
				 hour = 16;
				 minute = 0;
				 seconds = 0;
			 }
			 
			 //Now see if an appointment at this time, numDays from now, is available
			 Calendar cal = Calendar.getInstance();
			 cal.set(Calendar.YEAR, officeVisitDate.getYear());
			 cal.set(Calendar.MONTH, officeVisitDate.getMonthValue() - 1);
			 cal.set(Calendar.DAY_OF_MONTH, officeVisitDate.getDayOfMonth());
			 cal.set(Calendar.HOUR_OF_DAY, hour);
			 cal.set(Calendar.MINUTE, minute);
			 cal.set(Calendar.SECOND, seconds);
			 cal.set(Calendar.MILLISECOND, 0);
			 cal.add(Calendar.DATE, numDays);
			 
			 //Get the list of conflicts for the HCP for that day
			 DateTime dt_start = new DateTime(cal.getTimeInMillis());
			 DateTime dt_end = new DateTime(cal.getTimeInMillis() + DAY_TO_MILLI*(MAX_ATTEMPTS + 1));
			 List<DateTime[]> conflicts = getAppts(hcpid, dt_start, dt_end);
			 List<DateTime[]> conflictsPatient;
			 //Check if we were passed in a calendarID
			 if (calendarID == null || calendarID.equals(""))
			 {
				 conflictsPatient = new ArrayList<DateTime[]>(); //empty list
			 }
			 else
			 {
				 try
				 {
					 conflictsPatient = getGoogleEvents(calendarID, dt_start, dt_end);
				 }
				 catch (JSONException | IOException e) //If the patient doesn't have a public Google calendar, or an invalid ID
				 {
					 conflictsPatient = new ArrayList<DateTime[]>(); //empty list
				 }
			 }
			 List<DateTime[]> conflictsHoliday = getGoogleEvents(HOLIDAY_CALENDAR_ID, dt_start, dt_end);
			 conflicts.addAll(conflictsPatient);
			 conflicts.addAll(conflictsHoliday);
			 
			 //Iterate until we have found a valid time, but don't exceed a threshold of iterations
			 int attempts = 0;
			 while (!isValidTime(cal, conflicts, duration))
			 {
				 if (attempts >= MAX_ATTEMPTS)
				 {
					 throw new GoogleSchedulerException("Unable to schedule an appointment: Too many attempts");
				 }
				 //Try the next available day
				 cal.add(Calendar.DATE, 1);
				 attempts++;
			 }
			 
			 //Now we have a valid date
			 
			 ApptDAO apptDAO = factory.getApptDAO();
			 ApptBean newAppt = new ApptBean();
			 newAppt.setApptType(APPT_TYPE);
			 newAppt.setDate(new Timestamp(cal.getTimeInMillis()));
			 newAppt.setPatient(pid);
			 newAppt.setHcp(hcpid);
			 newAppt.setComment("Automatically generated appointment");
			 apptDAO.scheduleAppt(newAppt);
			 return newAppt;
		 }
		 catch (DBException | SQLException | JSONException | IOException e)
		 {
			 throw new GoogleSchedulerException("Unable to schedule an appointment");
		 }
	 }
}
