package edu.ncsu.csc.itrust.model.obstetrics.childbirth.visit;

public enum VisitType {
	EMERGENCY_APPOINTMENT("Emergency appointment"),
	PRE_SCHEDULED_APPOINTMENT("Pre-scheduled appointment");
	
	private final String text;

	private VisitType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	/**
	 * Converts a string to the Enum type it matches
	 * 
	 * @param text the string to convert
	 * @return a VisitType enum type representing the string
	 */
	public static VisitType matchString(String text) {
		for (VisitType i : VisitType.values()) {
			if (i.text.equalsIgnoreCase(text)) {
				return i;
			}
		}
		throw new IllegalArgumentException("No such enum exists");
	}
}