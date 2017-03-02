package edu.ncsu.csc.itrust.model.fitness;

/**
 * Holds fitness data associated for a given PID and date
 * @author mcsimps2
 *
 */
public class FitnessInfo
{
	/*The fields used in the database */
	private long pid;
	private int steps;
	private String date;
	private int caloriesBurned;
	private double miles;
	private int floors;
	private int activeCalories;
	private int minutesSedentary;
	private int minutesLightlyActive;
	private int minutesFairlyActive;
	private int minutesVeryActive;
	private int heartRateLow;
	private int heartRateHigh;
	private int heartRateAvg;
	private int activeHours;
	private int minutesUVExposure;
	
	/**
	 * Turns a string in the format "YYYY-MM-DD" to a sql date object
	 * @param date the date to turn into a java.sql.Date object
	 * @return the date in a sql date object
	 */
	@SuppressWarnings("deprecation")
	public static java.sql.Date stringToSQLDate(String date)
	{
		if (!verifyDate(date))
		{
			throw new DateFormatException("Incorrect date format");
		}
		java.sql.Date sDate = new java.sql.Date(Integer.parseInt(date.substring(0,4))-1900, Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
		return sDate;
	}
	
	/**
	 * Turns a string in the format "YYYY-MM-DD" to a java date object
	 * @param date the date to turn into a java.util.Date object
	 * @return the date in a java date object
	 */
	@SuppressWarnings("deprecation")
	public static java.util.Date stringToJavaDate(String date)
	{
		if (!verifyDate(date))
		{
			throw new DateFormatException("Incorrect date format");
		}
		java.util.Date jDate = new java.util.Date(Integer.parseInt(date.substring(0,4))-1900, Integer.parseInt(date.substring(5,7)) - 1, Integer.parseInt(date.substring(8,10)));
		return jDate;
		
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
			//YYYY-MM-DD 0123(4)56(7)89
			String syear = date.substring(0,4);
			String smonth = date.substring(5,7);
			String sday = date.substring(8,10);
			int year = Integer.parseInt(syear);
			int month = Integer.parseInt(smonth);
			int day = Integer.parseInt(sday);
			if (!(month <= 12 && month >= 1))
			{
				return false;
			}
			if (!(day <= 31 && day >= 1))
			{
				return false;
			}
			return true;
		}
		catch (IndexOutOfBoundsException e)
		{
			return false;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
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
	
	
	/* OTHER GETTERS AND SETTERS */
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
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public String getDate() {
		return date;
	}
	
	
	public void setDate(String date) {
		if (!verifyDate(date))
		{
			throw new DateFormatException("Date not in format YYYY-MM-DD");
		}
		this.date = date;
	}
	public int getCaloriesBurned() {
		return caloriesBurned;
	}
	public void setCaloriesBurned(int caloriesBurned) {
		this.caloriesBurned = caloriesBurned;
	}
	public double getMiles() {
		return miles;
	}
	public void setMiles(double miles) {
		this.miles = miles;
	}
	public int getFloors() {
		return floors;
	}
	public void setFloors(int floors) {
		this.floors = floors;
	}
	public int getActiveCalories() {
		return activeCalories;
	}
	public void setActiveCalories(int activeCalories) {
		this.activeCalories = activeCalories;
	}
	public int getMinutesSedentary() {
		return minutesSedentary;
	}
	public void setMinutesSedentary(int minutesSedentary) {
		this.minutesSedentary = minutesSedentary;
	}
	public int getMinutesFairlyActive() {
		return minutesFairlyActive;
	}
	public void setMinutesFairlyActive(int minutesFairlyActive) {
		this.minutesFairlyActive = minutesFairlyActive;
	}
	public int getMinutesLightlyActive() {
		return minutesLightlyActive;
	}
	public void setMinutesLightlyActive(int minutesLightlyActive) {
		this.minutesLightlyActive = minutesLightlyActive;
	}
	public int getMinutesVeryActive() {
		return minutesVeryActive;
	}
	public void setMinutesVeryActive(int minutesVeryActive) {
		this.minutesVeryActive = minutesVeryActive;
	}
	public int getHeartRateLow() {
		return heartRateLow;
	}
	public void setHeartRateLow(int heartRateLow) {
		this.heartRateLow = heartRateLow;
	}
	public int getHeartRateHigh() {
		return heartRateHigh;
	}
	public void setHeartRateHigh(int heartRateHigh) {
		this.heartRateHigh = heartRateHigh;
	}
	public int getHeartRateAvg() {
		return heartRateAvg;
	}
	public void setHeartRateAvg(int heartRateAvg) {
		this.heartRateAvg = heartRateAvg;
	}
	public int getActiveHours() {
		return activeHours;
	}
	public void setActiveHours(int activeHours) {
		this.activeHours = activeHours;
	}
	public int getMinutesUVExposure() {
		return minutesUVExposure;
	}
	public void setMinutesUVExposure(int minutesUVExposure) {
		this.minutesUVExposure = minutesUVExposure;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activeCalories;
		result = prime * result + activeHours;
		result = prime * result + caloriesBurned;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + floors;
		result = prime * result + heartRateAvg;
		result = prime * result + heartRateHigh;
		result = prime * result + heartRateLow;
		long temp;
		temp = Double.doubleToLongBits(miles);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + minutesFairlyActive;
		result = prime * result + minutesLightlyActive;
		result = prime * result + minutesSedentary;
		result = prime * result + minutesUVExposure;
		result = prime * result + minutesVeryActive;
		result = prime * result + (int) (pid ^ (pid >>> 32));
		result = prime * result + steps;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FitnessInfo))
			return false;
		FitnessInfo other = (FitnessInfo) obj;
		if (activeCalories != other.activeCalories)
			return false;
		if (activeHours != other.activeHours)
			return false;
		if (caloriesBurned != other.caloriesBurned)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (floors != other.floors)
			return false;
		if (heartRateAvg != other.heartRateAvg)
			return false;
		if (heartRateHigh != other.heartRateHigh)
			return false;
		if (heartRateLow != other.heartRateLow)
			return false;
		if (Double.doubleToLongBits(miles) != Double.doubleToLongBits(other.miles))
			return false;
		if (minutesFairlyActive != other.minutesFairlyActive)
			return false;
		if (minutesLightlyActive != other.minutesLightlyActive)
			return false;
		if (minutesSedentary != other.minutesSedentary)
			return false;
		if (minutesUVExposure != other.minutesUVExposure)
			return false;
		if (minutesVeryActive != other.minutesVeryActive)
			return false;
		if (pid != other.pid)
			return false;
		if (steps != other.steps)
			return false;
		return true;
	}
	public FitnessInfo(long pid, String date, int steps, int caloriesBurned, double miles, int floors,
			int activeCalories, int minutesSedentary, int minutesFairlyActive, int minutesLightlyActive,
			int minutesVeryActive, int heartRateLow, int heartRateHigh, int heartRateAvg, int activeHours,
			int minutesUVExposure) {
		super();
		this.pid = pid;
		this.steps = steps;
		this.date = date;
		this.caloriesBurned = caloriesBurned;
		this.miles = miles;
		this.floors = floors;
		this.activeCalories = activeCalories;
		this.minutesSedentary = minutesSedentary;
		this.minutesFairlyActive = minutesFairlyActive;
		this.minutesLightlyActive = minutesLightlyActive;
		this.minutesVeryActive = minutesVeryActive;
		this.heartRateLow = heartRateLow;
		this.heartRateHigh = heartRateHigh;
		this.heartRateAvg = heartRateAvg;
		this.activeHours = activeHours;
		this.minutesUVExposure = minutesUVExposure;
	}
	
	public FitnessInfo()
	{
		//do nothing
	}

	@Override
	public String toString()
	{
		return "FitnessInfo [pid=" + pid + ", steps=" + steps + ", date=" + date + ", caloriesBurned=" + caloriesBurned
				+ ", miles=" + miles + ", floors=" + floors + ", activeCalories=" + activeCalories
				+ ", minutesSedentary=" + minutesSedentary + ", minutesLightlyActive=" + minutesLightlyActive
				+ ", minutesFairlyActive=" + minutesFairlyActive + ", minutesVeryActive=" + minutesVeryActive
				+ ", heartRateLow=" + heartRateLow + ", heartRateHigh=" + heartRateHigh + ", heartRateAvg="
				+ heartRateAvg + ", activeHours=" + activeHours + ", minutesUVExposure=" + minutesUVExposure + "]";
	}
	
	
}
