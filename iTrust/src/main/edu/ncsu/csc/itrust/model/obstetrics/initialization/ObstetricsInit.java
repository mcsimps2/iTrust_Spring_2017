package edu.ncsu.csc.itrust.model.obstetrics.initialization;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import edu.ncsu.csc.itrust.model.fitness.DateFormatException;

public class ObstetricsInit implements Comparable<ObstetricsInit> {
	/**ID of this record */
	int id = -1;
	/** PID of patient */
	long pid = 0;
	/** Date of the initialization Record */
	private String date;
	/** Timestamp */
	private Timestamp timestamp;
	/** Date of the last menstrual period */
	private String lmp;
	/** EDD - LMP in days*/
	private final int EDD_LMP_DIFF = 280;
	
	/**
	 * Constructor to be used when adding a new record to the database
	 * In this case, recordID is irrelevant and not needed
	 * @param pid the PID of the patient associated with the record
	 * @param date the date of the record
	 * @param lmp the lmp recorded in the record
	 */
	public ObstetricsInit(long pid, String date, String lmp) {
		super();
		setPid(pid);
		setDate(date);
		setLMP(lmp);
	}
	
	/**
	 * Constructor to be used when populating this POJO with information pulled from the DB
	 * @param recordID the ID of the record in the DB. Generated by MySQL
	 * @param pid the PID of the patient associated with the record
	 * @param date the date of the record
	 * @param lmp the lmp recorded in the record
	 */
	public ObstetricsInit(int id, Timestamp timestamp, long pid, String date, String lmp) {
		super();
		setID(id);
		setTimestamp(timestamp);
		setPid(pid);
		setDate(date);
		setLMP(lmp);
	}
	
	/** 
	 * Calculates the EDD and returns the date as a String
	 * @return the EDD
	 */
	public String getEDD()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(getJavaDate());
		cal.add(Calendar.DATE, EDD_LMP_DIFF);
		return dateToString(cal.getTime());
	}
	
	public String getPrettyEDD() {
		return generatePrettyDate(getEDD());
	}
	
	/**
	 * Returns the number of days pregnant divided by 7, and then floored
	 * @param _currentDate the date of the record
	 * @return the number of weeks between the date of the record and the LMP
	 */
	public int getNumWeeksPregnant()
	{
		java.util.Date date = getJavaDate();
		java.util.Date lmpDate = stringToJavaDate(getLMP());
		
		//Find the difference in days
		long diff = date.getTime() - lmpDate.getTime();
		return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/7);
	}
	
	/**
	 * Turns a string in the format "YYYY-MM-DD" to a sql date object
	 * @param date the date to turn into a java.sql.Date object
	 * @return the date in a sql date object
	 */
	public static java.sql.Date stringToSQLDate(String date)
	{
		java.util.Date javaDate = stringToJavaDate(date);
		return new java.sql.Date(javaDate.getTime());
	}
	
	/**
	 * Turns a string in the format "YYYY-MM-DD" to a java date object
	 * @param date the date to turn into a java.util.Date object
	 * @return the date in a java date object
	 */
	public static java.util.Date stringToJavaDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Turns a SQL Date object into its string format YYYY-MM-DD
	 * @param date the sql date to turn into a string
	 * @return the string representation in format YYYY-MM-DD of the date
	 */
	public static String dateToString(java.sql.Date date)
	{
		return date.toString();
	}
	
	/**
	 * Turns a java date object into its string format YYYY-MM-DD
	 * @param date the java date object to turn into a string
	 * @return the string representation of the date in the format YYYY-MM-DD
	 */
	@SuppressWarnings("deprecation")
	public static String dateToString(java.util.Date date)
	{
		int day = date.getDate();
		int month = date.getMonth() + 1; //Month is 0-11, we convert it to 1-12
		int year = date.getYear() + 1900; //Year is the year-1900, so add 1900 to it
		return year + "-" + month + "-" + day;
	}
	
	/**
	 * Verifies the date is in the correct format YYYY-MM-DD with month between 1-12 and day between 1-31
	 * @param date the date to verify the format of
	 * @return true if the date is in the format YYYY-MM-DD with month between 1-12 and day between 1-31
	 */
	public static boolean verifyDate(String date)
	{
		try
		{
			
			
			//Check that this forms a valid date
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false);
			cal.setTime(stringToJavaDate(date));
			cal.getTime();  //If we have something like 2-31, this will be caught here
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Generate and return a pretty version of the specified date string.
	 * Format:
	 *     Month day, year
	 * Example:
	 *     March 4, 2017
	 *
	 * @param dateString a string representing a date
	 * @return a pretty version of the date
	 */
	private String generatePrettyDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMMMMMMM d, yyyy");
		java.util.Date date = stringToJavaDate(dateString);
		return formatter.format(date);
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getPrettyDate() {
		return generatePrettyDate(this.date);
	}
	
	public void setDate(String date) {
		if(!verifyDate(date))
		{
			throw new DateFormatException("Invalid date");
		}
		this.date = date;
	}
	
	public String getLMP() {
		return lmp;
	}
	
	public String getPrettyLMP() {
		return generatePrettyDate(getLMP());
	}
	
	public void setLMP(String lmp) {
		this.lmp = lmp;
	}
	
	public void setLMP(java.util.Date date)
	{
		setLMP(dateToString(date));
	}
	
	public void setLMP(java.sql.Date date)
	{
		setLMP(dateToString(date));
	}
	
	public java.sql.Date getSQLLMP()
	{
		return stringToSQLDate(getLMP());
	}
	
	public java.util.Date getJavaLMP()
	{
		return stringToJavaDate(getLMP());
	}
	
	/**
	 * Returns the date in a Java Date Object
	 * @return a java date object of the date
	 */
	public java.util.Date getJavaDate() {
		return stringToJavaDate(getDate());
	}
	
	/**
	 * Returns the date in a SQL date object
	 * @return a sql date object of the date
	 */
	public java.sql.Date getSQLDate() {
		return stringToSQLDate(getDate());
	}
	
	/**
	 * Sets the date field to the given date
	 * @param date the SQL date 
	 */
	public void setDate(java.sql.Date date)
	{
		String sDate = dateToString(date);
		setDate(sDate);
	}
	
	/**
	 * Sets the date field to the given date
	 * @param date the Java date
	 */
	public void setDate(java.util.Date date)
	{
		String sDate = dateToString(date);
		setDate(sDate);
	}

	
	
	public ObstetricsInit()
	{
		super();
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lmp == null) ? 0 : lmp.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (pid ^ (pid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObstetricsInit other = (ObstetricsInit) obj;
		if (lmp == null) {
			if (other.lmp != null)
				return false;
		} else if (!lmp.equals(other.lmp))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (pid != other.pid)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Compares this instance of ObstetricsInit to another instance of ObstetricsInit.
	 * Returns less than 0 or greater than 0 if this instance should come before or after the given instance,
	 * based on descending order by timestamp (so the instance with the most recent timestamp would come first).
	 * If the timestamps are equal, returns 0.
	 */
	@Override
	public int compareTo(ObstetricsInit other) {
		if (other == null)
			throw new NullPointerException();
		return this.timestamp.compareTo(other.timestamp) * -1;
	}
}

