package edu.ncsu.csc.itrust.webutils;

import java.text.SimpleDateFormat;
import java.time.ZoneId;

/**
 * This class contains methods to convert display otherwise not-user-friendly objects.
 * 
 * @author tags are dumb
 */
public class PrettyUtils {
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
	public static String getPrettyDate(java.util.Date date) {
		return (new SimpleDateFormat("MMMMMMMMM d, yyyy")).format(date);
	}
	
	public static String getPrettyDate(java.time.LocalDateTime date) {
		return getPrettyDate(java.util.Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
	}
}