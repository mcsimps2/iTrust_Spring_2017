package edu.ncsu.csc.itrust.model.obstetrics.childbirth.newborns;


public enum SexType
{
	MALE("Male"),
	FEMALE("Female"),
	OTHER("Other"),
	UNSPECIFIED("Unspecified");

	private final String text;

	private SexType(final String text) {
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
	 * @return a SexType enum type representing the string
	 */
	public static SexType matchString(String text) {
		for (SexType i : SexType.values()) {
			if (i.text.equalsIgnoreCase(text)) {
				return i;
			}
		}
		throw new IllegalArgumentException("No such enum exists");
	}
}
